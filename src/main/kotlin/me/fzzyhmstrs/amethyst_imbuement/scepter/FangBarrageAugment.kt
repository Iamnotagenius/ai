package me.fzzyhmstrs.amethyst_imbuement.scepter

import me.fzzyhmstrs.amethyst_imbuement.scepter.base_augments.MiscAugment
import me.fzzyhmstrs.amethyst_imbuement.util.SpellType
import net.minecraft.block.BlockState
import net.minecraft.entity.*
import net.minecraft.entity.mob.EvokerFangsEntity
import net.minecraft.item.Items
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.World
import kotlin.math.max
import kotlin.math.min

class FangBarrageAugment(tier: Int, maxLvl: Int, vararg slot: EquipmentSlot): MiscAugment(tier, maxLvl, *slot) {

    override fun effect(world: World, target: Entity?, user: LivingEntity, level: Int, hit: HitResult?): Boolean {
        val entityList: MutableList<Entity> = mutableListOf()
        val d: Double
        val e: Double
        if (target != null){
            d = min(target.y, user.y)
            e = max(target.y, user.y) + 1.0
            entityList.add(target)
        } else {
            d = user.y
            e = user.y + 1.0
        }
        val f = (user.yaw + 90) * MathHelper.PI / 180
        val successes = conjureBarrage(user,world,d,e,f)
        ScepterObject.setPersistentTickerNeed(world,user,entityList,level, BlockPos.ORIGIN,this,14,28)
        return successes > 0
    }

    override fun persistentEffect(
        world: World,
        user: LivingEntity,
        blockPos: BlockPos,
        entityList: MutableList<Entity>,
        level: Int
    ) {
        val d: Double
        val e: Double
        if (entityList.isNotEmpty()){
            val target = entityList[0]
            d = min(target.y, user.y)
            e = max(target.y, user.y) + 1.0
            entityList.add(target)
        } else {
            d = user.y
            e = user.y + 1.0
        }
        val f = (user.yaw + 90) * MathHelper.PI / 180
        conjureBarrage(user,world,d,e,f)
    }

    override fun rangeOfEffect(): Double {
        return 12.0
    }

    override fun augmentStat(imbueLevel: Int): ScepterObject.AugmentDatapoint {
        return ScepterObject.AugmentDatapoint(SpellType.FURY,100,40,22,imbueLevel,2, Items.EMERALD_BLOCK)
    }

    override fun soundEvent(): SoundEvent {
        return SoundEvents.ENTITY_EVOKER_FANGS_ATTACK
    }

    private fun conjureBarrage(user: LivingEntity, world: World, d: Double, e: Double, f: Float): Int{
        var successes = 0
        for (i in 0..12) {
            val g = 1.25 * (i + 1).toDouble()
            for (k in -1..1){
                val success = conjureFangs(
                    world,
                    user,
                    user.x + MathHelper.cos(f + (11.0F * MathHelper.PI / 180 * k)).toDouble() * g,
                    user.z + MathHelper.sin(f + (11.0F * MathHelper.PI / 180 * k)).toDouble() * g,
                    d,
                    e,
                    f,
                    i
                )
                if (success) successes++
            }
        }
        return successes
    }

    private fun conjureFangs(world: World,user: LivingEntity,x: Double, z: Double, maxY: Double, y: Double, yaw: Float, warmup: Int): Boolean {
        var blockPos = BlockPos(x, y, z)
        var bl = false
        var d = 0.0
        do {

            val blockPos2: BlockPos = blockPos.down()
            val blockState: BlockState = world.getBlockState(blockPos2)
            val blockState2: BlockState = world.getBlockState(blockPos)
            val voxelShape: VoxelShape = blockState2.getCollisionShape(world,blockPos)
            if (!blockState.isSideSolidFullSquare(world,blockPos2, Direction.UP)) {
                blockPos = blockPos.down()
                continue
            }
            if (!world.isAir(blockPos) && !voxelShape.isEmpty) {
                d = voxelShape.getMax(Direction.Axis.Y)
            }
            bl = true
            break
        } while (blockPos.y >= MathHelper.floor(maxY) - 1)
        if (bl) {
            world.spawnEntity(
                EvokerFangsEntity(
                    world,
                    x,
                    blockPos.y.toDouble() + d,
                    z,
                    yaw,
                    warmup,
                    user
                )
            )
            return true
        }
        return false
    }

}