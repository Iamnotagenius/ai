package me.fzzyhmstrs.amethyst_imbuement.scepter

import me.fzzyhmstrs.amethyst_core.modifier_util.AugmentEffect
import me.fzzyhmstrs.amethyst_core.scepter_util.LoreTier
import me.fzzyhmstrs.amethyst_core.scepter_util.SpellType
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.AugmentDatapoint
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.ElementalAugment
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.SummonEntityAugment
import me.fzzyhmstrs.amethyst_imbuement.entity.totem.TotemOfGraceEntity
import me.fzzyhmstrs.amethyst_imbuement.entity.totem.TotemOfWitEntity
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEntity
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterItem
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World

@Suppress("SpellCheckingInspection")
class SummonWitTotemAugment(tier: Int, maxLvl: Int, vararg slot: EquipmentSlot): SummonEntityAugment(tier, maxLvl, *slot), ElementalAugment {

    override val baseEffect: AugmentEffect
        get() = super.baseEffect
            .withDuration(1075,125)
            .withRange(13.75,1.25)
            .withAmplifier(0,1)

    override fun placeEntity(
        world: World,
        user: PlayerEntity,
        hit: HitResult,
        level: Int,
        effects: AugmentEffect
    ): Boolean {
        var successes = 0
        val xrnd: Double = (hit as BlockHitResult).blockPos.x + 0.5
        val zrnd: Double = hit.blockPos.z + 0.5
        val yrnd = hit.blockPos.y + 1.0
        val graceEntity = TotemOfWitEntity(RegisterEntity.TOTEM_OF_WIT_ENTITY, world,user,effects.duration(level))
        graceEntity.passEffects(effects,level)
        graceEntity.setPos(xrnd, yrnd, zrnd)
        if (world.spawnEntity(graceEntity)){
            successes++
        }
        if (successes > 0) {
            return super.placeEntity(world, user, hit, level, effects)
        }
        return false
    }

    override fun soundEvent(): SoundEvent {
        return SoundEvents.BLOCK_DEEPSLATE_BRICKS_PLACE
    }

    override fun augmentStat(imbueLevel: Int): AugmentDatapoint {
        return AugmentDatapoint(SpellType.FURY,2400,100,14,imbueLevel,LoreTier.LOW_TIER, RegisterItem.ENERGETIC_OPAL)
    }
}