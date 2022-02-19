package me.fzzyhmstrs.amethyst_imbuement.block

import me.fzzyhmstrs.amethyst_imbuement.entity.ForcefieldBlockEntity
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEntity
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

@Suppress("PrivatePropertyName")
class ForcefieldBlock(settings: Settings) : BlockWithEntity(settings) {

    fun getWaterState(waterState: Boolean): BlockState{
        return if (waterState) {
            defaultState.with(WATER_SPAWN, true)
        } else {
            defaultState.with(WATER_SPAWN, false)
        }
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val bs =  super.getPlacementState(ctx)
        val pos = ctx.blockPos
        val bs2: BlockState = ctx.world.getBlockState(pos)
        return if (bs != null) {
            if (bs2.isOf(Blocks.WATER)) {
                bs.with(WATER_SPAWN, true)
            } else {
                bs.with(WATER_SPAWN, false)
            }
        } else {
            if (bs2.isOf(Blocks.WATER)) {
                defaultState.with(WATER_SPAWN, true)
            } else {
                defaultState.with(WATER_SPAWN, false)
            }
        }

    }

    override fun isTranslucent(state: BlockState?, world: BlockView?, pos: BlockPos?): Boolean {
        return true
    }

    override fun isSideInvisible(state: BlockState?, stateFrom: BlockState, direction: Direction?): Boolean {
        return if (stateFrom.isOf(this)) {
            true
        } else super.isSideInvisible(state, stateFrom, direction)
    }

    override fun canMobSpawnInside(): Boolean {
        return false
    }

    override fun canPathfindThrough(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        type: NavigationType
    ): Boolean {
        return false
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        val bl = state.contains(WATER_SPAWN)
        return if (bl){
            ForcefieldBlockEntity(pos, state, state.get(WATER_SPAWN))
        } else {
            ForcefieldBlockEntity(pos, state)
        }
    }

    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return if (!world.isClient)
            checkType(
            type, RegisterEntity.FORCEFIELD_BLOCK_ENTITY
        ) { world2: World, pos: BlockPos, state2: BlockState, blockEntity: ForcefieldBlockEntity ->
            ForcefieldBlockEntity.tick(
                world2,
                pos,
                state2,
                blockEntity
            )
        } else null
    }

    override fun getRenderType(state: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        if (context is EntityShapeContext){
            val entity = context.entity
            if (entity is PlayerEntity){
                return VoxelShapes.empty()
            }
            if (entity is ProjectileEntity){
                if (entity.owner is PlayerEntity) {
                    return VoxelShapes.empty()
                }
            }
        }
        return if (collidable) state.getOutlineShape(world, pos) else VoxelShapes.empty()
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(WATER_SPAWN)
    }

    companion object{
        private val WATER_SPAWN: BooleanProperty = Properties.WATERLOGGED
    }

}