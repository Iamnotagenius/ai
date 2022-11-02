package me.fzzyhmstrs.amethyst_imbuement.mixins;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.PiercingEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiercingEnchantment.class)
public abstract class EnchantPiercingMixin {

    @ModifyReturnValue(method = "getMaxLevel", at = @At("RETURN"))
    private int amethyst_imbuement_updateMaxLevelToSix(int original){
        return 6;
    }

    @ModifyReturnValue(method = "getMaxPower", at = @At("RETURN"))
    private int amethyst_imbuement_updateMaxPower(int original, int level){
        int i = 0;
        if (level == 5) i = 10;
        if (level == 6) i = 20;
        return 50+i;
    }
}
