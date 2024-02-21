package me.fzzyhmstrs.amethyst_imbuement.augment

import me.fzzyhmstrs.amethyst_imbuement.augment.base_augments.PassiveAugment
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterTag
import me.fzzyhmstrs.fzzy_core.coding_util.FzzyPort
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import java.util.*

class HealthyAugment(weight: Rarity, mxLvl: Int, vararg slot: EquipmentSlot): PassiveAugment(weight, mxLvl, *slot) {

    override fun attributeModifier(stack: ItemStack, level: Int, uuid: UUID): Pair<EntityAttribute, EntityAttributeModifier> {
        return Pair(
            EntityAttributes.GENERIC_MAX_HEALTH,
            EntityAttributeModifier(uuid, "amethyst_imbuement:max_health", 0.1 * level, EntityAttributeModifier.Operation.MULTIPLY_BASE))
    }

    override fun isAcceptableItem(stack: ItemStack): Boolean {
        return stack.isIn(RegisterTag.AMULETS_TAG)
    }

    override fun acceptableItemStacks(): MutableList<ItemStack> {
        return FzzyPort.ITEM.iterateEntries(RegisterTag.AMULETS_TAG).map { it.value().defaultStack }.toMutableList()
    }
}