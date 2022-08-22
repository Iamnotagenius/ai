package me.fzzyhmstrs.amethyst_imbuement.scepter

import me.fzzyhmstrs.amethyst_core.modifier_util.AugmentEffect
import me.fzzyhmstrs.amethyst_core.scepter_util.LoreTier
import me.fzzyhmstrs.amethyst_core.scepter_util.SpellType
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.AugmentDatapoint
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.SlashAugment
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.SoulAugment
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterItem
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.Items
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents

@Suppress("SameParameterValue")
open class EmpoweredSlashAugment(tier: Int, maxLvl: Int, vararg slot: EquipmentSlot): SlashAugment(tier, maxLvl, *slot) {

    override val baseEffect: AugmentEffect
        get() = super.baseEffect.withDamage(6.0F,3.0F,0.0F).withAmplifier(0,1,0)


    override fun particleType(): DefaultParticleType{
        return ParticleTypes.CRIT
    }

    override fun soundEvent(): SoundEvent {
        return SoundEvents.ENTITY_PLAYER_ATTACK_CRIT
    }

    override fun augmentStat(imbueLevel: Int): AugmentDatapoint {
        return AugmentDatapoint(SpellType.FURY,18,15,13,imbueLevel,LoreTier.LOW_TIER, RegisterItem.GARNET_SWORD)
    }
}