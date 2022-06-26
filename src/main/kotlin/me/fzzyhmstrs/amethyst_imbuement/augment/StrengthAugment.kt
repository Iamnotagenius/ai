package me.fzzyhmstrs.amethyst_imbuement.augment
import me.fzzyhmstrs.amethyst_core.trinket_util.EffectQueue
import me.fzzyhmstrs.amethyst_imbuement.augment.base_augments.ActiveAugment
import me.fzzyhmstrs.amethyst_imbuement.item.TotemItem
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEnchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.TranslatableText

class StrengthAugment(weight: Rarity, mxLvl: Int = 1, vararg slot: EquipmentSlot): ActiveAugment(weight,mxLvl,*slot) {

    override fun activateEffect(user: LivingEntity, level: Int, stack: ItemStack) {
        val lvl = EnchantmentHelper.getLevel(RegisterEnchantment.STRENGTH,stack)
        EffectQueue.addStatusToQueue(user,StatusEffects.STRENGTH,100,lvl-1)
        val rnd = user.world.random.nextFloat()
        if (rnd <= 0.5) {
            if (TotemItem.damageHandler(stack, user.world, user as PlayerEntity, 1)) {
                TotemItem.burnOutHandler(stack, RegisterEnchantment.STRENGTH,user, TranslatableText("augment_damage.strength.burnout"))
            }
        }
    }

}