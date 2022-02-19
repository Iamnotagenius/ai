package me.fzzyhmstrs.amethyst_imbuement.mixins;

import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEnchantment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(MiningToolItem.class)
public class MiningToolItemMixin {

    @Final
    @Shadow private Tag<Block> effectiveBlocks;


    @Inject(method = "postMine", at = @At(value = "TAIL"), cancellable = true)
    private void postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cir){
        Block block = state.getBlock();
        if (miner instanceof PlayerEntity && effectiveBlocks.contains(block)) {
            int lvl = EnchantmentHelper.getLevel(RegisterEnchantment.INSTANCE.getVEIN_MINER(), stack);
            if (lvl > 0) {
                System.out.println(block.getName());
                int rng = 4 * lvl - 1;
                List<BlockPos> list = new ArrayList<>();
                for (int x = 0; x < 6; x++) {
                    BlockPos bp = newBlockPos(x,world,pos);
                    BlockState bs = world.getBlockState(bp);
                    if (bs.isAir()) continue;
                    if (bs.isOf(block)) {
                        world.breakBlock(bp,true);
                        list.add(bp);
                        rng--;
                        if (rng <= 0) return;
                        //try a random block surrounding the mined block
                        BlockPos bp2 = newBlockPos(-1,world,bp);
                        BlockState bs2 = world.getBlockState(bp);
                        if (bs2.isAir()) continue;
                        if (bs2.isOf(block)) {
                            world.breakBlock(bp2, true);
                            list.add(bp2);
                            rng--;
                            if (rng <= 0) return;
                        }
                    }
                }
                if (!list.isEmpty()) {
                    int triesLeft = list.size();
                    int remaining = rng;
                    while (triesLeft > 0) {
                        int rndList = world.random.nextInt(list.size());
                        remaining = veinMine(remaining, list.get(rndList), world, block, stack, miner);
                        list.remove(rndList);
                        if (remaining <= 0) break;
                        triesLeft--;
                    }
                }
            }
        }
    }

    private int veinMine(int amountLeft, BlockPos pos, World world, Block block, ItemStack stack, LivingEntity miner){
        int amount = amountLeft;
        if (world.getBlockState(pos).isOf(block)) {
            world.breakBlock(pos, true);
            stack.damage(1, miner, value -> value.sendToolBreakStatus(miner.getActiveHand()));
            amount--;
        }
        if (amount <= 0) return 0;
        int[] arr1 = shuffleArray(new int[]{-1, 1}, world.random);
        int[] arr2 = shuffleArray(new int[]{-1, 1}, world.random);
        int[] arr3 = shuffleArray(new int[]{-1, 1}, world.random);
        int rndCase = world.random.nextInt(3);
        switch (rndCase) {
            case 0:
            for (int x : arr1) {
                BlockPos bp = pos.add(x, 0, 0);
                BlockState bs = world.getBlockState(bp);
                if (bs.isAir()) continue;
                if (bs.isOf(block)) {
                    float rnd = world.random.nextFloat();
                    if (rnd < 0.70) {
                        amount = veinMine(amount, bp, world, bs.getBlock(), stack, miner);
                    } else {
                        world.breakBlock(pos, true);
                        stack.damage(1, miner, value -> value.sendToolBreakStatus(miner.getActiveHand()));
                        amount--;
                    }
                    if (amount <= 0) return 0;
                }
            }
            break;
            case 1:
            for (int y : arr2) {
                BlockPos bp = pos.add(0, y, 0);
                BlockState bs = world.getBlockState(bp);
                if (bs.isAir()) continue;
                if (bs.isOf(block)) {
                    float rnd = world.random.nextFloat();
                    if (rnd < 0.70) {
                        amount = veinMine(amount, bp, world, bs.getBlock(), stack, miner);
                    } else {
                        world.breakBlock(pos, true);
                        stack.damage(1, miner, value -> value.sendToolBreakStatus(miner.getActiveHand()));
                        amount--;
                    }
                    if (amount <= 0) return 0;
                }
            }
            break;
            case 2:
            for (int z : arr3) {
                BlockPos bp = pos.add(0, 0, z);
                BlockState bs = world.getBlockState(bp);
                if (bs.isAir()) continue;
                if (bs.isOf(block)) {
                    float rnd = world.random.nextFloat();
                    if (rnd < 0.70) {
                        amount = veinMine(amount, bp, world, bs.getBlock(), stack, miner);
                    } else {
                        world.breakBlock(pos, true);
                        stack.damage(1, miner, value -> value.sendToolBreakStatus(miner.getActiveHand()));
                        amount--;
                    }
                    if (amount <= 0) return 0;
                }
            }
            break;
        }
        return amount;
    }

    private int veinMine2(int amountLeft, BlockPos pos, World world, Block block, ItemStack stack, LivingEntity miner){
        int amount = amountLeft;
        world.breakBlock(pos,true);
        stack.damage(1,miner,value -> value.sendToolBreakStatus(miner.getActiveHand()));
        amount --;
        if (amount <= 0) return 0;
        int[] arr1 = shuffleArray(new int[]{-1, 1}, world.random);
        int[] arr2 = shuffleArray(new int[]{-1, 1}, world.random);
        int[] arr3 = shuffleArray(new int[]{-1, 1}, world.random);
        int rndCase = world.random.nextInt(3);
        switch (rndCase) {
            case 0:
                for (int x : arr1) {
                    BlockPos bp = pos.add(x, 0, 0);
                    BlockState bs = world.getBlockState(bp);
                    if (bs.isAir()) continue;
                    if (bs.isOf(block)) {
                        float rnd = world.random.nextFloat();
                        if (rnd < 0.70) {
                            amount = veinMine(amount, bp, world, bs.getBlock(), stack, miner);
                        } else {
                            world.breakBlock(pos, true);
                            stack.damage(1, miner, value -> value.sendToolBreakStatus(miner.getActiveHand()));
                            amount--;
                        }
                        if (amount <= 0) return 0;
                    }
                }
                break;
            case 1:
                for (int y : arr2) {
                    BlockPos bp = pos.add(0, y, 0);
                    BlockState bs = world.getBlockState(bp);
                    if (bs.isAir()) continue;
                    if (bs.isOf(block)) {
                        float rnd = world.random.nextFloat();
                        if (rnd < 0.70) {
                            amount = veinMine(amount, bp, world, bs.getBlock(), stack, miner);
                        } else {
                            world.breakBlock(pos, true);
                            stack.damage(1, miner, value -> value.sendToolBreakStatus(miner.getActiveHand()));
                            amount--;
                        }
                        if (amount <= 0) return 0;
                    }
                }
                break;
            case 2:
                for (int z : arr3) {
                    BlockPos bp = pos.add(0, 0, z);
                    BlockState bs = world.getBlockState(bp);
                    if (bs.isAir()) continue;
                    if (bs.isOf(block)) {
                        float rnd = world.random.nextFloat();
                        if (rnd < 0.70) {
                            amount = veinMine(amount, bp, world, bs.getBlock(), stack, miner);
                        } else {
                            world.breakBlock(pos, true);
                            stack.damage(1, miner, value -> value.sendToolBreakStatus(miner.getActiveHand()));
                            amount--;
                        }
                        if (amount <= 0) return 0;
                    }
                }
                break;
        }
        return amount;
    }

    private BlockPos newBlockPos(int checkDef, World world, BlockPos pos){
        int check;
        if (checkDef < 0 || checkDef > 5) {
            check = world.random.nextInt(6);
        } else {
            check = checkDef;
        }
        return switch (check) {
            case 0 -> pos.add(1, 0, 0);
            case 1 -> pos.add(-1, 0, 0);
            case 2 -> pos.add(0, 1, 0);
            case 3 -> pos.add(0, -1, 0);
            case 4 -> pos.add(0, 0, 1);
            case 5 -> pos.add(0, 0, -1);
            default -> pos;
        };
    }

    private int[] shuffleArray(int[] arrIn, Random rand){
        int[] arr = arrIn;
        for (int i = 0; i < arr.length; ++i) {
            int index = rand.nextInt(arr.length - i);
            int tmp = arr[arr.length - 1 - i];
            arr[arr.length - 1 - i] = arr[index];
            arr[index] = tmp;
        }
        return arr;
    }
}
