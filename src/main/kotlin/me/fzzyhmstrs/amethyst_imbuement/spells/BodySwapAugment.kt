package me.fzzyhmstrs.amethyst_imbuement.spells

import me.fzzyhmstrs.amethyst_core.augments.ScepterAugment
import me.fzzyhmstrs.amethyst_core.augments.base.SingleTargetAugment
import me.fzzyhmstrs.amethyst_core.augments.data.AugmentDatapoint
import me.fzzyhmstrs.amethyst_core.augments.paired.AugmentType
import me.fzzyhmstrs.amethyst_core.augments.paired.PairedAugments
import me.fzzyhmstrs.amethyst_core.modifier.AugmentEffect
import me.fzzyhmstrs.amethyst_core.scepter.LoreTier
import me.fzzyhmstrs.amethyst_core.scepter.ScepterTier
import me.fzzyhmstrs.amethyst_core.scepter.SpellType
import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.amethyst_imbuement.spells.pieces.SpellAdvancementChecks
import me.fzzyhmstrs.fzzy_core.raycaster_util.RaycasterUtil
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*

class BodySwapAugment: SingleTargetAugment(ScepterTier.THREE){
    override val augmentData: AugmentDatapoint =
        AugmentDatapoint(AI.identity("body_swap"),SpellType.FURY,600,125,
            19,11,1,40, LoreTier.HIGH_TIER, Items.ENDER_EYE)

    //ml 11
    override val baseEffect: AugmentEffect
        get() = super.baseEffect.withRange(15.75,0.75)

    override fun appendDescription(description: MutableList<Text>, other: ScepterAugment, othersType: AugmentType) {
        TODO("Not yet implemented")
    }

    override fun provideArgs(pairedSpell: ScepterAugment): Array<Text> {
        TODO()
    }

    override fun onPaired(player: ServerPlayerEntity, pair: PairedAugments) {
        if (pair.spellsAreEqual()){
            SpellAdvancementChecks.grant(player, SpellAdvancementChecks.DOUBLE_TRIGGER)
        }
        if (pair.spellsAreUnique()){
            SpellAdvancementChecks.grant(player, SpellAdvancementChecks.UNIQUE_TRIGGER)
        }
    }

    override fun effect(
        world: World,
        target: Entity?,
        user: LivingEntity,
        level: Int,
        hit: HitResult?,
        effect: AugmentEffect
    ): Boolean {
        if (world !is ServerWorld) return false
        if (user !is PlayerEntity) return false
        val rotation = user.getRotationVec(1.0F)
        val perpendicularVector = RaycasterUtil.perpendicularVector(rotation, RaycasterUtil.InPlane.XZ)
        val raycasterPos = user.pos.add(rotation.multiply(effect.range(level)/2)).add(Vec3d(0.0,user.height/2.0,0.0))
        val entityList: MutableList<Entity> =
            RaycasterUtil.raycastEntityRotatedArea(
                world.iterateEntities(),
                user,
                raycasterPos,
                rotation,
                perpendicularVector,
                effect.range(level),
                2.5,
                3.0)
        if (entityList.isEmpty()) return false
        val map: SortedMap<Double,Entity> = sortedMapOf()
        entityList.forEach {
            map[it.squaredDistanceTo(user)] = it
        }
        val entity = map[map.lastKey()]?:return false
        val pos1 = Vec3d(entity.pos.toVector3f())
        val pos2 = Vec3d(user.pos.toVector3f())
        entity.teleport(pos2.x,pos2.y,pos2.z)
        user.teleport(pos1.x,pos1.y,pos1.z)
        world.playSound(null, user.blockPos, soundEvent(), SoundCategory.PLAYERS, 0.7F, 1.1F)
        return true
    }

    override fun soundEvent(): SoundEvent {
        return SoundEvents.BLOCK_PORTAL_TRIGGER
    }
}
