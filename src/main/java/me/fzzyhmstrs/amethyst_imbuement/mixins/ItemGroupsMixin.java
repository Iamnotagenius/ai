package me.fzzyhmstrs.amethyst_imbuement.mixins;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.ScepterAugment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemGroups.class)
public class ItemGroupsMixin {

    @WrapWithCondition(method = "addMaxLevelEnchantedBooks",at = @At(value = "INVOKE", target = "net/minecraft/item/ItemGroup$Entries.add (Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemGroup$StackVisibility;)V"))
    private static boolean amethyst_imbuement_checkMaxForScepterAugmentAndIgnore(ItemGroup.Entries instance, ItemStack stack, ItemGroup.StackVisibility visibility, @Local Enchantment enchantment){
        return !(enchantment instanceof ScepterAugment);
    }

    @WrapWithCondition(method = "addAllLevelEnchantedBooks",at = @At(value = "INVOKE", target = "net/minecraft/item/ItemGroup$Entries.add (Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemGroup$StackVisibility;)V"))
    private static boolean amethyst_imbuement_checkAllForScepterAugmentAndIgnore(ItemGroup.Entries instance, ItemStack stack, ItemGroup.StackVisibility visibility, @Local Enchantment enchantment){
        return !(enchantment instanceof ScepterAugment);
    }

}
