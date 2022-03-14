package me.fzzyhmstrs.amethyst_imbuement.augment

import me.fzzyhmstrs.amethyst_imbuement.augment.base_augments.BaseAugment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.ItemStack
import net.minecraft.item.ShieldItem
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.registry.Registry

class SpikedAugment(weight: Rarity,mxLvl: Int = 1, vararg slot: EquipmentSlot): BaseAugment(weight, mxLvl,EnchantmentTarget.CROSSBOW,*slot) {

    /*override fun onUserDamaged(user: LivingEntity, attacker: Entity, level: Int) {
        if (user.world !is ServerWorld) return
        attacker.damage(DamageSource.GENERIC,1.0F)
    }*/

    override fun isAcceptableItem(stack: ItemStack): Boolean {
        return (stack.item is ShieldItem)
    }

    override fun acceptableItemStacks(): MutableList<ItemStack> {
        val list = mutableListOf<ItemStack>()
        val entries = Registry.ITEM.entries
        for (entry in entries){
            val item = entry.value
            if (item is ShieldItem){
                list.add(ItemStack(item,1))
            }
        }
        return list
    }

    override fun specialEffect(user: LivingEntity, level: Int, stack: ItemStack): Boolean {
        //user is actually the attacker in this case
        if (user.world !is ServerWorld) return false
        user.damage(DamageSource.SWEET_BERRY_BUSH,0.5F * level)
        return true
    }
}