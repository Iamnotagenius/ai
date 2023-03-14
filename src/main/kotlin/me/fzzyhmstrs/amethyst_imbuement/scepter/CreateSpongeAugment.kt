package me.fzzyhmstrs.amethyst_imbuement.scepter

import me.fzzyhmstrs.amethyst_core.scepter_util.LoreTier
import me.fzzyhmstrs.amethyst_core.scepter_util.ScepterTier
import me.fzzyhmstrs.amethyst_core.scepter_util.SpellType
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.AugmentDatapoint
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.PlaceItemAugment
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents

class CreateSpongeAugment: PlaceItemAugment(ScepterTier.ONE,1, Items.SPONGE){
    override fun augmentStat(imbueLevel: Int): AugmentDatapoint {
        return AugmentDatapoint(SpellType.WIT,30,8,
            1,imbueLevel,1, LoreTier.LOW_TIER, Items.SPONGE)
    }

    override fun soundEvent(): SoundEvent {
        return SoundEvents.BLOCK_GRASS_PLACE
    }

}