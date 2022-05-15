package me.fzzyhmstrs.amethyst_imbuement.mixins;

import me.fzzyhmstrs.amethyst_imbuement.config.AiConfig;
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.structure.Structure;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Structure.class)
public class StructureMixin {

    @Redirect(method = "loadPalettedBlockInfo", at = @At(value = "INVOKE", target = "net/minecraft/nbt/NbtList.getCompound (I)Lnet/minecraft/nbt/NbtCompound;"))
    private NbtCompound checkForEnchantingTable(NbtList instance, int index){
        NbtCompound nbtCompound = instance.getCompound(index);
        BlockState state = NbtHelper.toBlockState(nbtCompound);
        if (state.isOf(Blocks.ENCHANTING_TABLE) && AiConfig.INSTANCE.getAltars().getImbuingTableReplaceEnchantingTable()){
            Item table = RegisterBlock.INSTANCE.getIMBUING_TABLE().asItem();
            nbtCompound.putString("Name", Registry.ITEM.getId(table).toString());
        }
        return nbtCompound;
    }

}
