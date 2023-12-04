package me.fzzyhmstrs.amethyst_imbuement.augment

import me.fzzyhmstrs.amethyst_core.registry.RegisterAttribute
import me.fzzyhmstrs.amethyst_imbuement.augment.base_augments.PassiveAugment
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.item.ItemStack
import java.util.*

class RebindingAugment(weight: Rarity, mxLvl: Int = 1, vararg slot: EquipmentSlot): PassiveAugment(weight,mxLvl, *slot) {

    override fun attributeModifier(stack: ItemStack, level: Int, uuid: UUID): Pair<EntityAttribute, EntityAttributeModifier> {
        return Pair(
            RegisterAttribute.MANA_REGENERATION,
            EntityAttributeModifier(uuid, "amethyst_imbuement:mana_regen", 0.25 * level, EntityAttributeModifier.Operation.ADDITION)
        )
    }

    /*override fun tickEffect(user: LivingEntity, level: Int, stack: ItemStack) {
        EffectQueue.addStatusToQueue(user,StatusEffects.LUCK,260,0)
    }*/

}