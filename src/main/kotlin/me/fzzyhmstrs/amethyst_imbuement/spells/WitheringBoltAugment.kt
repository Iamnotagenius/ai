package me.fzzyhmstrs.amethyst_imbuement.spells

import me.fzzyhmstrs.amethyst_core.augments.ScepterAugment
import me.fzzyhmstrs.amethyst_core.augments.base.ProjectileAugment
import me.fzzyhmstrs.amethyst_core.augments.data.AugmentDatapoint
import me.fzzyhmstrs.amethyst_core.augments.paired.AugmentType
import me.fzzyhmstrs.amethyst_core.augments.paired.PairedAugments
import me.fzzyhmstrs.amethyst_core.augments.paired.ProcessContext
import me.fzzyhmstrs.amethyst_core.interfaces.SpellCastingEntity
import me.fzzyhmstrs.amethyst_core.modifier.AugmentEffect
import me.fzzyhmstrs.amethyst_core.scepter.LoreTier
import me.fzzyhmstrs.amethyst_core.scepter.ScepterTier
import me.fzzyhmstrs.amethyst_core.scepter.SpellType
import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.amethyst_imbuement.entity.PlayerWitherSkullEntity
import me.fzzyhmstrs.amethyst_imbuement.spells.pieces.SpellAdvancementChecks
import me.fzzyhmstrs.fzzy_core.coding_util.PerLvlI
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World

class WitheringBoltAugment: ProjectileAugment(ScepterTier.TWO, AugmentType.BALL){
    override val augmentData: AugmentDatapoint =
         AugmentDatapoint(AI.identity("withering_bolt"),SpellType.FURY, PerLvlI(30,-2),13,
            11,5,1,2,LoreTier.LOW_TIER, Items.WITHER_SKELETON_SKULL)

    //ml 5
    override val baseEffect: AugmentEffect
        get() = super.baseEffect.withDamage(7.5f,0.5f)

    override fun appendDescription(description: MutableList<Text>, other: ScepterAugment, othersType: AugmentType) {
        TODO("Not yet implemented")
    }

    override fun provideArgs(pairedSpell: ScepterAugment): Array<Text> {
        return arrayOf(pairedSpell.provideAdjective(this))
    }

    override fun onPaired(player: ServerPlayerEntity, pair: PairedAugments) {
        SpellAdvancementChecks.uniqueOrDouble(player, pair)
        SpellAdvancementChecks.grant(player, SpellAdvancementChecks.EXPLODES_TRIGGER)
    }

    override fun <T> createProjectileEntities(
        world: World,
        context: ProcessContext,
        user: T,
        level: Int,
        effects: AugmentEffect,
        spells: PairedAugments,
        count: Int
    ): List<ProjectileEntity> where T : SpellCastingEntity,T : LivingEntity {
        val list: MutableList<ProjectileEntity> = mutableListOf()
        val direction = user.rotationVec3d
        for (i in 1..count){
            val wse = PlayerWitherSkullEntity(world,user,direction.x,direction.y,direction.z)
            wse.setVelocity()
        }
        return list
    }

    override fun entityClass(world: World, user: LivingEntity, level: Int, effects: AugmentEffect): ProjectileEntity {
        val yaw = user.yaw
        val pitch = user.pitch
        val roll = user.roll
        val speed = 4.0F
        val f = (-MathHelper.sin(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180)) * speed) + user.velocity.x
        val g = (-MathHelper.sin((pitch + roll) * (Math.PI.toFloat() / 180)) * speed) + user.velocity.y
        val h = (MathHelper.cos(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180)) * speed) + user.velocity.z
        val wse = PlayerWitherSkullEntity(world,user,f,g,h)
        wse.passEffects(effects, level)
        wse.setPos(user.x,user.eyeY-0.2,user.z)
        wse.isCharged = false
        wse.setAugment(this)
        return wse
    }

    override fun soundEvent(): SoundEvent {
        return SoundEvents.ENTITY_WITHER_SHOOT
    }
}
