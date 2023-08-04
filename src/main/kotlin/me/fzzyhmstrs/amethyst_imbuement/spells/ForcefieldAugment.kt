package me.fzzyhmstrs.amethyst_imbuement.spells

import me.fzzyhmstrs.amethyst_core.augments.ScepterAugment
import me.fzzyhmstrs.amethyst_core.augments.SpellActionResult
import me.fzzyhmstrs.amethyst_core.augments.data.AugmentDatapoint
import me.fzzyhmstrs.amethyst_core.augments.paired.AugmentType
import me.fzzyhmstrs.amethyst_core.augments.paired.PairedAugments
import me.fzzyhmstrs.amethyst_core.augments.paired.ProcessContext
import me.fzzyhmstrs.amethyst_core.interfaces.SpellCastingEntity
import me.fzzyhmstrs.amethyst_core.modifier.AugmentConsumer
import me.fzzyhmstrs.amethyst_core.modifier.AugmentEffect
import me.fzzyhmstrs.amethyst_core.scepter.LoreTier
import me.fzzyhmstrs.amethyst_core.scepter.ScepterTier
import me.fzzyhmstrs.amethyst_core.scepter.SpellType
import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterBlock
import me.fzzyhmstrs.amethyst_imbuement.spells.pieces.SpellAdvancementChecks
import me.fzzyhmstrs.fzzy_core.coding_util.PerLvlI
import net.minecraft.block.Blocks
import net.minecraft.block.PlantBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World

class ForcefieldAugment: ScepterAugment(ScepterTier.TWO, AugmentType.BLOCK_AREA){
    //ml 6

    private val offset = intArrayOf(-2,2)
    
    override val augmentData: AugmentDatapoint =
        AugmentDatapoint(AI.identity("forcefield"),SpellType.WIT, PerLvlI(620,-20),135,
            13,6,1,15, LoreTier.LOW_TIER, Items.SHIELD)
        
    override val baseEffect: AugmentEffect
        get() = super.baseEffect.withDuration(1800,200)

    override fun appendDescription(description: MutableList<Text>, other: ScepterAugment, othersType: AugmentType) {
        TODO("Not yet implemented")
    }

    override fun provideArgs(pairedSpell: ScepterAugment): Array<Text> {
        return arrayOf(pairedSpell.provideNoun(this))
    }

    override fun onPaired(player: ServerPlayerEntity, pair: PairedAugments) {
        SpellAdvancementChecks.uniqueOrDouble(player, pair)
    }
    override fun <T> applyTasks(
        world: World,
        context: ProcessContext,
        user: T,
        hand: Hand,
        level: Int,
        effects: AugmentEffect,
        spells: PairedAugments
    )
            :
            SpellActionResult
            where
            T : SpellCastingEntity,
            T : LivingEntity
    {
        TODO("Not yet implemented")
    }

    override fun effect(
        world: World,
        target: Entity?,
        user: LivingEntity,
        level: Int,
        hit: HitResult?,
        effect: AugmentEffect
    ): Boolean {
        val pos = user.blockPos.add(0,1,0)
        var successes = 0
        for (i in offset){
            val posX = pos.add(i,0,0)
            val posY = pos.add(0,i,0)
            val posZ = pos.add(0,0,i)
            baseAge = effect.duration(level)
            for (j in -1..1){
                for (k in -1..1){
                    val bsX = world.getBlockState(posX.add(0,j,k))
                    val bsY = world.getBlockState(posY.add(j,0,k))
                    val bsZ = world.getBlockState(posZ.add(j,k,0))
                    if (bsX.isAir || bsX.block is PlantBlock){
                        if (!bsX.isAir) world.breakBlock(posX.add(0,j,k),true)
                        world.setBlockState(posX.add(0,j,k),RegisterBlock.FORCEFIELD_BLOCK.getWaterState(false))
                        world.scheduleBlockTick(posX.add(0,j,k),RegisterBlock.FORCEFIELD_BLOCK, getAge(world, baseAge))
                        successes++
                    } else if (bsX.isOf(Blocks.WATER)){
                        world.setBlockState(posX.add(0,j,k),RegisterBlock.FORCEFIELD_BLOCK.getWaterState(true))
                        world.scheduleBlockTick(posX.add(0,j,k),RegisterBlock.FORCEFIELD_BLOCK, getAge(world, baseAge))
                        successes++
                    }
                    if (bsY.isAir || bsY.block is PlantBlock ){
                        if (!bsY.isAir) world.breakBlock(posY.add(j,0,k),true)
                        world.setBlockState(posY.add(j,0,k),RegisterBlock.FORCEFIELD_BLOCK.getWaterState(false))
                        world.scheduleBlockTick(posY.add(j,0,k),RegisterBlock.FORCEFIELD_BLOCK, getAge(world, baseAge))
                        successes++
                    } else if (bsY.isOf(Blocks.WATER)){
                        world.setBlockState(posY.add(j,0,k),RegisterBlock.FORCEFIELD_BLOCK.getWaterState(true))
                        world.scheduleBlockTick(posY.add(j,0,k),RegisterBlock.FORCEFIELD_BLOCK, getAge(world, baseAge))
                        successes++
                    }
                    if (bsZ.isAir || bsZ.block is PlantBlock){
                        if (!bsZ.isAir) world.breakBlock(posZ.add(j,k,0),true)
                        world.setBlockState(posZ.add(j,k,0),RegisterBlock.FORCEFIELD_BLOCK.getWaterState(false))
                        world.scheduleBlockTick(posZ.add(j,k,0),RegisterBlock.FORCEFIELD_BLOCK, getAge(world, baseAge))
                        successes++
                    } else if (bsZ.isOf(Blocks.WATER)){
                        world.setBlockState(posZ.add(j,k,0),RegisterBlock.FORCEFIELD_BLOCK.getWaterState(true))
                        world.scheduleBlockTick(posZ.add(j,k,0),RegisterBlock.FORCEFIELD_BLOCK, getAge(world, baseAge))
                        successes++
                    }
                }
            }
            baseAge = actualBaseAge
        }
        val bl = successes > 0
        if (bl) {
            world.playSound(null,user.blockPos,soundEvent(),SoundCategory.NEUTRAL,1.0f,1.0f)
            effect.accept(user, AugmentConsumer.Type.BENEFICIAL)
        }
        return bl
    }

    private fun getAge(world: World, baseAge: Int): Int{
        val rnd0 = world.random.nextInt(4)
        return if (rnd0 != 0) {
            baseAge
        } else {
            val rnd1 = world.random.nextInt(10)
            val rnd2 = world.random.nextInt(10-rnd1)
            baseAge - 20 * rnd2
        }
    }

    override fun soundEvent(): SoundEvent {
        return SoundEvents.BLOCK_BEACON_POWER_SELECT
    }

    companion object{
        private const val actualBaseAge = 2000
        var baseAge = 2000
    }
}
