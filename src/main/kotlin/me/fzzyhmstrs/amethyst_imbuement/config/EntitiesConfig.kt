package me.fzzyhmstrs.amethyst_imbuement.config

import fzzyhmstrs.should_i_hit_that.api.MobCheckerBuilder
import fzzyhmstrs.should_i_hit_that.api.MobCheckers
import fzzyhmstrs.should_i_hit_that.api.ShouldHitResult
import fzzyhmstrs.should_i_hit_that.api.ShouldItHitPredicate
import fzzyhmstrs.should_i_hit_that.checkers.*
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.ScepterAugment
import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.amethyst_imbuement.entity.living.PlayerCreatedConstructEntity
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterTag
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom
import me.fzzyhmstrs.fzzy_config.annotations.RequiresRestart
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.config.ConfigSection
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.Tameable
import net.minecraft.entity.mob.Angerable
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.passive.GolemEntity
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.entity.player.PlayerEntity

@ConvertFrom("entities_v6.json",AI.MOD_ID)
class EntitiesConfig: Config(AI.identity("entities_config")) {

    private val IS_PVP_NOT_FRIEND: MobChecker = PredicatedPassChecker(
        {_,_,args -> forcePvpOnAllSpells.get() || args.isNotEmpty() && (args[0] as? ScepterAugment)?.getPvpMode() == true},
        MobCheckers.NOT_FRIEND
    )
    private val IS_PVP_FRIEND: MobChecker = PredicatedPassChecker(
        {attacker,victim,args -> if (TogglePvpMobChecker.togglePvpInstalledButNotPvp(attacker, victim) || victim !is PlayerEntity) false else forcePvpOnAllSpells.get() || args.isNotEmpty() && (args[0] as? ScepterAugment)?.getPvpMode() == true},
        MobCheckers.FRIEND
    )
    private val NOT_PVP_NOT_PLAYER: MobChecker = PredicatedPassChecker(
        {_,_,args -> !(forcePvpOnAllSpells.get() || args.isNotEmpty() && (args[0] as? ScepterAugment)?.getPvpMode() == true)},
        MobCheckers.NOT_PLAYER
    )
    private val TOGGLE_PVP_FRIEND: MobChecker = PredicatedPassChecker(
        {attacker,victim,_ -> TogglePvpMobChecker.areBothPvp(attacker, victim)},
        MobCheckers.FRIEND
    )
    private val TOGGLE_PVP_PET: MobChecker = IfElseMobChecker(
        {attacker,victim,_ -> TogglePvpMobChecker.togglePvpInstalledButNotPvp(attacker, victim)},
        MobCheckers.FAIL,
        MobCheckers.NOT_PET
    )
    private val NULL_NOT_MONSTER: MobChecker = PredicatedPassChecker(
        {attacker,_,_ -> attacker == null},
        {_,victim,_ -> if (victim is Monster) ShouldHitResult.FAIL else ShouldHitResult.PASS}
    )
    private val NULL_MONSTER: MobChecker = PredicatedPassChecker(
        {attacker,_,_ -> attacker == null},
        {_,victim,_ -> if (victim is Monster) ShouldHitResult.PASS else ShouldHitResult.FAIL}
    )

    private val HIT_CHECKER = MobCheckerBuilder.sequence(
        NULL_MONSTER,
        MobCheckers.NOT_SELF,
        MobCheckers.NOT_MONSTER_FRIEND,
        MobCheckers.NOT_CLAIMED,
        TOGGLE_PVP_PET,
        TogglePvpMobChecker,
        IS_PVP_NOT_FRIEND,
        NOT_PVP_NOT_PLAYER
    )

    private val FRIEND_CHECKER = MobCheckerBuilder.and(
        NULL_NOT_MONSTER,
        MobCheckers.MONSTER_FRIEND,
        //MobCheckers.CLAIMED,
        MobCheckers.PET,
        TOGGLE_PVP_FRIEND,
        OrMobChecker(IS_PVP_FRIEND, MobCheckers.SELF)
    )

    fun isEntityPvpTeammate(user: LivingEntity?, entity: Entity, spell: ScepterAugment): Boolean{
        if (entity is Monster)
            return user is Monster
        if (user == null) return false
        if (entity === user) return true
        if ((entity as? Tameable)?.owner == user) return true
        if (forcePvpOnAllSpells.get() || spell.getPvpMode()){
            return user.isTeammate(entity)
        }
        return entity is PlayerEntity
    }

    interface ShouldItHit{
        fun shouldItHit(attacker: LivingEntity?, victim: Entity, vararg args: Any?): Boolean
    }

    enum class Options: ShouldItHit {
        NONE {
            override fun shouldItHit(attacker: LivingEntity?, victim: Entity, vararg args: Any?): Boolean {
                return true
            }
        },
        NON_BOSS {
            override fun shouldItHit(attacker: LivingEntity?, victim: Entity, vararg args: Any?): Boolean {
                return NON_BOSS_HIT_CHECKER.shouldItHit(attacker, victim, args)
            }
        },
        NON_VILLAGER {
            override fun shouldItHit(attacker: LivingEntity?, victim: Entity, vararg args: Any?): Boolean {
                return NON_VILLAGER_HIT_CHECKER.shouldItHit(attacker, victim, args)
            }
        },
        NON_GOLEM{
            override fun shouldItHit(attacker: LivingEntity?, victim: Entity, vararg args: Any?): Boolean {
                if (victim is PlayerCreatedConstructEntity) return true // let other logic handle summons
                if (victim !is GolemEntity || victim !is Angerable) return true // other logic will handle if it's not a golem
                return victim.isUniversallyAngry(victim.world) || (attacker?.uuid != null && attacker.uuid == victim.angryAt)
            }
        },
        NON_FRIENDLY{
            override fun shouldItHit(attacker: LivingEntity?, victim: Entity, vararg args: Any?): Boolean {
                return if(victim is Angerable) victim.angryAt != null && victim.angryAt == attacker?.uuid else victim !is PassiveEntity
            }
        },
        NON_FRIENDLY_NON_GOLEM {
            override fun shouldItHit(attacker: LivingEntity?, victim: Entity, vararg args: Any?): Boolean {
                return NON_FRIENDLY.shouldItHit(attacker, victim, args) && NON_GOLEM.shouldItHit(attacker, victim, args)
            }
        },
        HOSTILE_ONLY {
            override fun shouldItHit(attacker: LivingEntity?, victim: Entity, vararg args: Any?): Boolean {
                return victim is Monster
            }
        },
        NON_BOSS_NON_FRIENDLY {
            override fun shouldItHit(attacker: LivingEntity?, victim: Entity, vararg args: Any?): Boolean {
                return NON_BOSS_HIT_CHECKER.shouldItHit(attacker, victim, args) && NON_FRIENDLY.shouldItHit(attacker, victim, args)
            }
        };

        protected val NON_VILLAGER_HIT_CHECKER: ShouldItHitPredicate = MobCheckerBuilder.single(MobCheckers.NOT_VILLAGER)

        protected val NON_BOSS_HIT_CHECKER: ShouldItHitPredicate = MobCheckerBuilder.single(
            ExcludeTagChecker(
                RegisterTag.POULTRYMORPH_IGNORES)
        )
    }

    fun shouldItHitBase(attacker: LivingEntity?, victim: Entity, vararg args: Any?): Boolean{
        return shouldItHit(attacker,victim, defaultSecondaryHitCheckerOption.get(), args)
    }

    fun shouldItHit(attacker: LivingEntity?, victim: Entity, options: Options, vararg args: Any?): Boolean{
        return options.shouldItHit(attacker, victim, args,*ignoredGuilds.get().toTypedArray()) && HIT_CHECKER.shouldItHit(attacker, victim, *ignoredGuilds.get().toTypedArray())
    }

    fun shouldItHitFriend(attacker: LivingEntity?, victim: Entity, vararg args: Any?): Boolean{
        return FRIEND_CHECKER.shouldItHit(attacker, victim, args,*ignoredGuilds.get().toTypedArray())
    }

    //MonsterShouldHit:
    //NOT_MONSTER_FRIEND &&
    //

    //@ReadMeText("readme.entities.forcePvpOnAllSpells")
    var forcePvpOnAllSpells = ValidatedBoolean(false)
    var defaultSecondaryHitCheckerOption = ValidatedEnum(Options.NON_VILLAGER)

    var ignoredGuilds = ValidatedList.ofString("Streamers")

    var unhallowed = Unhallowed()

    @RequiresRestart
    class Unhallowed: ConfigSection(){
        var baseLifespan = ValidatedInt(2400,180000,20)
        var baseHealth = ValidatedDouble(20.0,100.0,1.0)
        var baseDamage = ValidatedFloat(3.0f,20.0f,0.0f)
    }

    var crystalGolem = CrystalGolem()
    @RequiresRestart
    class CrystalGolem: ConfigSection(){
        //@ReadMeText("readme.entities.crystalGolem.spellBaseLifespan")
        var spellBaseLifespan = ValidatedInt(5500, Int.MAX_VALUE-120000,20)
        //@ReadMeText("readme.entities.crystalGolem.spellPerLvlLifespan")
        var spellPerLvlLifespan = ValidatedInt(500,5000,0)
        //@ReadMeText("readme.entities.crystalGolem.guardianLifespan")
        var guardianLifespan = ValidatedInt(900, Int.MAX_VALUE,20)
        var baseHealth = ValidatedDouble(180.0,1024.0,1.0)
        var baseDamage = ValidatedFloat(20.0f,1000f,0f)
    }

    var hamster = Hamster()
    @RequiresRestart
    class Hamster: ConfigSection(){
        //@ReadMeText("readme.entities.hamster.baseLifespan")
        var baseLifespan = ValidatedInt(3600,180000,-1)
        var baseHealth = ValidatedDouble(8.0,40.0,1.0)
        //@ReadMeText("readme.entities.hamster.baseDamage")
        var baseSummonDamage = ValidatedFloat(1.0f,10.0f,0.0f)
        var baseHamptertimeDamage = ValidatedFloat(2.0f,10.0f,0.0f)
        var perLvlDamage = ValidatedFloat(0.1f,1.0f,0.0f)
        var hamptertimeBaseSpawnCount = ValidatedDouble(10.0,100.0,1.0)
        var hamptertimePerLvlSpawnCount = ValidatedDouble(0.5,5.0,0.0)
    }

    var bonestorm = Bonestorm()
    @RequiresRestart
    class Bonestorm: ConfigSection(){
        //@ReadMeText("readme.entities.bonestorm.baseLifespan")
        var baseLifespan = ValidatedInt(2160,Int.MAX_VALUE-1000000,20)
        //@ReadMeText("readme.entities.bonestorm.perLvlLifespan")
        var perLvlLifespan = ValidatedInt(240,2400,0)
        var baseHealth = ValidatedDouble(24.0,240.0,1.0)
        var baseDamage = ValidatedFloat(4.5f,10.0f,0.0f)
        var perLvlDamage = ValidatedFloat(0.25f,1.0f,0.0f)
    }

    var cholem = Cholem()
    @RequiresRestart
    class Cholem: ConfigSection(){
        var baseLifespan = ValidatedInt(3600,Int.MAX_VALUE-1000000,20)
        var baseHealth = ValidatedDouble(80.0,800.0,1.0)
        var baseArmor = ValidatedDouble(4.0,20.0,1.0)
        var baseDamage = ValidatedFloat(10f,100f,0.0f)
        var enragedDamage = ValidatedFloat(4f,40f,0.0f)
    }

    var chorse = Chorse()
    @RequiresRestart
    class Chorse: ConfigSection(){
        var baseHealth = ValidatedDouble(80.0,800.0,1.0)
        var baseJumpStrength = ValidatedDouble(0.93,2.0,0.0)
        var baseMoveSpeed = ValidatedDouble(0.275,1.0,0.01)
    }

    var sardonyxFragment = SardonyxFragment()
    @RequiresRestart
    class SardonyxFragment: ConfigSection(){
        var baseHealth = ValidatedDouble(60.0,600.0,1.0)
        var baseArmor = ValidatedDouble(8.0,20.0,1.0)
        var baseDamage = ValidatedDouble(9.0,90.0,0.0)
        var enragedDamage = ValidatedDouble(6.0,60.0,0.0)
    }

    var sardonyxElemental = SardonyxElemental()
    @RequiresRestart
    class SardonyxElemental: ConfigSection(){
        var baseHealth = ValidatedDouble(512.0,1024.0,1.0)
        var baseArmor = ValidatedDouble(14.0,50.0,1.0)
        var baseDamage = ValidatedDouble(28.0,280.0,0.0)
        var projectileDamage = ValidatedFloat(20f,200f,0f)
        var fragmentsSpawned = ValidatedInt(3,25,1)
        var devastationBeamDmg = ValidatedFloat(50f,Float.MAX_VALUE,0f)
        var spellActivationCooldown = ValidatedInt(600,6000,100)
        var amountHealedPerSecond = ValidatedFloat(0.2f,5f, 0f)
    }

}