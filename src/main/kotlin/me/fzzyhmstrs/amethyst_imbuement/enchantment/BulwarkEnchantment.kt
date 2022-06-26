package me.fzzyhmstrs.amethyst_imbuement.enchantment

import me.fzzyhmstrs.amethyst_core.trinket_util.BaseAugment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.*
import net.minecraft.server.world.ServerWorld

class BulwarkEnchantment(weight: Rarity, mxLvl: Int = 1, vararg slot: EquipmentSlot): BaseAugment(weight, mxLvl,EnchantmentTarget.CROSSBOW,*slot) {

    override fun getMinPower(level: Int): Int {
        return 25
    }

    override fun getMaxPower(level: Int): Int {
        return 45
    }

    override fun getMaxLevel(): Int {
        return 1
    }

    override fun isAcceptableItem(stack: ItemStack): Boolean {
        return (stack.item is ShieldItem)
    }

    override fun specialEffect(user: LivingEntity, level: Int, stack: ItemStack): Boolean {
        //user is the user
        if (user.world !is ServerWorld) return false
        user.heal(1.0f)
        return true
    }

}