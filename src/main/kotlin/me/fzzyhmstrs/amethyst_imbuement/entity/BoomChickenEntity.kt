package me.fzzyhmstrs.amethyst_imbuement.entity

import me.fzzyhmstrs.amethyst_core.interfaces.SpellCastingEntity
import me.fzzyhmstrs.amethyst_imbuement.config.AiConfig
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEnchantment
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LightningEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.passive.ChickenEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.registry.tag.ItemTags
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import java.util.*

class BoomChickenEntity(entityType:EntityType<BoomChickenEntity>,world: World): ChickenEntity(entityType, world) {

    companion object{

        private val FUSE_SPEED = DataTracker.registerData(BoomChickenEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        private val IGNITED = DataTracker.registerData(BoomChickenEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)

        fun createBoomChickenAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30)
        }
    }
    private var lastFuseTime = 0
    private var currentFuseTime = 0
    private val fuseTime = 30
    private var owner: LivingEntity? = null

    fun setOwner(owner:LivingEntity?){
        this.owner = owner
    }

    override fun initGoals() {
        goalSelector.add(0, MeleeAttackGoal(this, 1.0, false))
        goalSelector.add(1, SwimGoal(this))
        goalSelector.add(2, EscapeDangerGoal(this, 1.4))
        goalSelector.add(3, WanderAroundFarGoal(this, 1.0))
        goalSelector.add(4, LookAtEntityGoal(this, PlayerEntity::class.java, 6.0f))
        goalSelector.add(5, LookAroundGoal(this))
        targetSelector.add(1, ActiveTargetGoal(this, LivingEntity::class.java, true) {
            it is Monster || (it is SpellCastingEntity && it !== owner && !AiConfig.entities.isEntityPvpTeammate(owner, it, RegisterEnchantment.TORRENT_OF_BEAKS))
        })
        targetSelector.add(2, RevengeGoal(this, *arrayOfNulls(0)))
    }

    override fun tick() {
        if (this.isAlive) {
            lastFuseTime = currentFuseTime
            if (isIgnited()) {
                setFuseSpeed(1)
            }
            val i: Int = getFuseSpeed()
            if (i > 0 && currentFuseTime == 0) {
                playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 0.5f)
                this.emitGameEvent(GameEvent.PRIME_FUSE)
            }
            currentFuseTime += i
            if (currentFuseTime < 0) {
                currentFuseTime = 0
            }
            if (currentFuseTime >= fuseTime) {
                currentFuseTime = fuseTime
                this.explode()
            }
        }
        super.tick()
    }

    private fun explode() {
        if (!world.isClient) {
            dead = true
            world.createExplosion(this, DamageSource.mob(this),
                BoomChickenExplosionBehavior,this.pos,1.0f,false,World.ExplosionSourceType.NONE)
            discard()
        }
    }

    override fun interactMob(player2: PlayerEntity, hand: Hand?): ActionResult? {
        val itemStack = player2.getStackInHand(hand)
        if (itemStack.isIn(ItemTags.CREEPER_IGNITERS)) {
            val soundEvent =
                if (itemStack.isOf(Items.FIRE_CHARGE)) SoundEvents.ITEM_FIRECHARGE_USE else SoundEvents.ITEM_FLINTANDSTEEL_USE
            world.playSound(
                player2,
                this.x,
                this.y,
                this.z,
                soundEvent,
                this.soundCategory,
                1.0f,
                random.nextFloat() * 0.4f + 0.8f
            )
            if (!world.isClient) {
                ignite()
                itemStack.damage(1, player2) { player: PlayerEntity ->
                    player.sendToolBreakStatus(
                        hand
                    )
                }
            }
            return ActionResult.success(world.isClient)
        }
        return super.interactMob(player2, hand)
    }

    override fun tryAttack(target: Entity?): Boolean {
        return true
    }

    fun getFuseSpeed(): Int {
        return dataTracker.get(FUSE_SPEED)
    }

    fun setFuseSpeed(fuseSpeed: Int) {
        dataTracker.set(FUSE_SPEED, fuseSpeed)
    }

    override fun onStruckByLightning(world: ServerWorld?, lightning: LightningEntity?) {
        super.onStruckByLightning(world, lightning)
        ignite()
    }

    fun isIgnited(): Boolean {
        return dataTracker.get(IGNITED)
    }

    fun ignite() {
        dataTracker.set(IGNITED, true)
    }

    private object BoomChickenExplosionBehavior: ExplosionBehavior(){
        override fun canDestroyBlock(
            explosion: Explosion?,
            world: BlockView?,
            pos: BlockPos?,
            state: BlockState?,
            power: Float
        ): Boolean {
            return false
        }
    }

    class CreeperIgniteGoal(private val chicken: BoomChickenEntity) : Goal() {
        private var target: LivingEntity? = null

        override fun canStart(): Boolean {
            val livingEntity = chicken.target
            return chicken.getFuseSpeed() > 0 || livingEntity != null && chicken.squaredDistanceTo(livingEntity) < 6.0
        }

        override fun start() {
            target = chicken.target
        }

        override fun stop() {
            target = null
        }

        override fun shouldRunEveryTick(): Boolean {
            return true
        }

        override fun tick() {
            if (target == null) {
                chicken.setFuseSpeed(-1)
                return
            }
            if (chicken.squaredDistanceTo(target) > 49.0) {
                chicken.setFuseSpeed(-1)
                return
            }
            if (!chicken.visibilityCache.canSee(target)) {
                chicken.setFuseSpeed(-1)
                return
            }
            chicken.setFuseSpeed(1)
        }

        init {
            controls = EnumSet.of(Control.MOVE)
        }
    }

}