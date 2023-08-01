package me.fzzyhmstrs.amethyst_imbuement.entity

import com.google.common.collect.Sets
import me.fzzyhmstrs.amethyst_core.augments.paired.PairedAugments
import me.fzzyhmstrs.amethyst_core.augments.paired.ProcessContext
import me.fzzyhmstrs.amethyst_core.entity.ModifiableEffectContainer
import me.fzzyhmstrs.amethyst_core.entity.ModifiableEffectEntity
import me.fzzyhmstrs.amethyst_core.interfaces.SpellCastingEntity
import me.fzzyhmstrs.amethyst_core.modifier.AugmentEffect
import me.fzzyhmstrs.amethyst_imbuement.config.AiConfig
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEntity
import net.minecraft.advancement.criterion.Criteria
import net.minecraft.block.*
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LightningEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.world.Difficulty
import net.minecraft.world.GameRules
import net.minecraft.world.World
import net.minecraft.world.WorldEvents
import net.minecraft.world.event.GameEvent
import java.util.*

class PlayerLightningEntity(entityType: EntityType<out PlayerLightningEntity?>, world: World): LightningEntity(entityType, world),
    ModifiableEffectEntity {

    constructor(world: World, attacker: LivingEntity): this(RegisterEntity.PLAYER_LIGHTNING, world){
        owner = attacker
    }

    private var owner: LivingEntity? = null
    private var ownerUuid: UUID? = null
    private var ambientTick = 2
    private var remainingActions = this.random.nextInt(3 + 1)
    private val struckEntities: MutableSet<Entity> = Sets.newHashSet()
    override var entityEffects: AugmentEffect = AugmentEffect().withDamage(5.0F).withAmplifier(8)
    override var level: Int = 1
    override var spells: PairedAugments = PairedAugments()
    override var modifiableEffects = ModifiableEffectContainer()
    override var processContext: ProcessContext = ProcessContext.EMPTY_CONTEXT

    fun getOwner(): LivingEntity? {
        if (owner != null && !owner!!.isRemoved) {
            return owner
        }
        if (this.ownerUuid != null && world is ServerWorld) {
            val o = (world as ServerWorld).getEntity(this.ownerUuid)
            if (o is LivingEntity) {
                owner = o
                return o
            }
        }
        return null
    }

    override fun tick() {
        var list: List<Entity>
        super.tick()
        tickTickEffects(this,owner,processContext)
        val livingEntity = getOwner()
        if (livingEntity !is LivingEntity || livingEntity !is SpellCastingEntity) return
        val augment = spells.primary() ?: return
        if (ambientTick == 2) {
            if (world.isClient) {
                world.playSound(
                    this.x,
                    this.y,
                    this.z,
                    SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
                    SoundCategory.WEATHER,
                    10000.0f,
                    0.8f + random.nextFloat() * 0.2f,
                    false
                )
                world.playSound(
                    this.x,
                    this.y,
                    this.z,
                    SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT,
                    SoundCategory.WEATHER,
                    2.0f,
                    0.5f + random.nextFloat() * 0.2f,
                    false
                )
            } else {
                //may move into the spells, not sure
                val difficulty = world.difficulty
                if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
                    spawnFire(4)
                }
                powerLightningRod()
                cleanOxidation(world, getAffectedBlockPos())
                spells.processSingleBlockHit(BlockHitResult(this.pos,Direction.UP,getAffectedBlockPos(),false),world,this,livingEntity,Hand.MAIN_HAND,level,entityEffects)
                this.emitGameEvent(GameEvent.LIGHTNING_STRIKE)
            }
        }
        --ambientTick
        if (ambientTick < 0) {
            if (remainingActions == 0) {
                if (!world.isClient) {
                    list = world.getOtherEntities(
                        this,
                        Box(
                            this.x - 15.0,
                            this.y - 15.0,
                            this.z - 15.0,
                            this.x + 15.0,
                            this.y + 6.0 + 15.0,
                            this.z + 15.0
                        )
                    ) { entity: Entity ->
                        entity.isAlive && !struckEntities.contains(
                            entity
                        )
                    }
                    for (serverPlayerEntity2 in (world as ServerWorld).getPlayers { serverPlayerEntity: ServerPlayerEntity ->
                        serverPlayerEntity.distanceTo(
                            this
                        ) < 256.0f
                    }) {
                        Criteria.LIGHTNING_STRIKE.trigger(serverPlayerEntity2, this, list)
                    }
                }
                discard()
            } else if (ambientTick < -random.nextInt(10)) {
                --remainingActions
                ambientTick = 1
                seed = random.nextLong()
                spawnFire(0)
            }
        }
        if (ambientTick >= 0) {
            if (world.isClient) {
                world.setLightningTicksLeft(2)
            } else {
                list = world.getOtherEntities(
                    this, Box(this.x - 3.0, this.y - 3.0, this.z - 3.0, this.x + 3.0, this.y + 6.0 + 3.0, this.z + 3.0)
                ) { obj: Entity -> obj.isAlive && obj is LivingEntity && !(obj is SpellCastingEntity && AiConfig.entities.isEntityPvpTeammate(owner, obj, augment)) }
                val list2: MutableList<EntityHitResult> = mutableListOf()
                for (target in list){
                    list2.add(EntityHitResult(target))
                }

                runEffect(ModifiableEffectEntity.DAMAGE,this,owner,processContext)
                spells.processMultipleEntityHits(list2,world,this,livingEntity,Hand.MAIN_HAND,level,entityEffects)
                for (hit in list2){
                    if (!hit.entity.isAlive){
                        runEffect(ModifiableEffectEntity.KILL,this,owner,processContext)
                        break
                    }
                }
                spells.processMultipleOnKill(list2,world,this,livingEntity,Hand.MAIN_HAND,level,entityEffects)
                struckEntities.addAll(list)
                if (channeler != null) {
                    Criteria.CHANNELED_LIGHTNING.trigger(channeler, list)
                }
            }
        }
    }

    private fun powerLightningRod() {
        val blockPos = getAffectedBlockPos()
        val blockState = world.getBlockState(blockPos)
        if (blockState.isOf(Blocks.LIGHTNING_ROD)) {
            (blockState.block as LightningRodBlock).setPowered(blockState, world, blockPos)
        }
    }

    private fun getAffectedBlockPos(): BlockPos {
        val vec3d = pos
        return BlockPos.ofFloored(vec3d.x, vec3d.y - 1.0E-6, vec3d.z)
    }

    private fun spawnFire(spreadAttempts: Int) {
        if (world.isClient || !world.gameRules.getBoolean(GameRules.DO_FIRE_TICK)) {
            return
        }
        val blockPos = blockPos
        var blockState = AbstractFireBlock.getState(world, blockPos)
        if (world.getBlockState(blockPos).isAir && blockState.canPlaceAt(world, blockPos)) {
            world.setBlockState(blockPos, blockState)
        }
        for (i in 0 until spreadAttempts) {
            val blockPos2 = blockPos.add(random.nextInt(3) - 1, random.nextInt(3) - 1, random.nextInt(3) - 1)
            blockState = AbstractFireBlock.getState(world, blockPos2)
            if (!world.getBlockState(blockPos2).isAir || !blockState.canPlaceAt(world, blockPos2)) continue
            world.setBlockState(blockPos2, blockState)
        }
    }

    private fun cleanOxidation(world: World, pos: BlockPos) {
        val blockState2: BlockState
        val blockPos: BlockPos
        val blockState = world.getBlockState(pos)
        if (blockState.isOf(Blocks.LIGHTNING_ROD)) {
            blockPos = pos.offset(blockState.get(LightningRodBlock.FACING).opposite)
            blockState2 = world.getBlockState(blockPos)
        } else {
            blockPos = pos
            blockState2 = blockState
        }
        if (blockState2.block !is Oxidizable) {
            return
        }
        world.setBlockState(blockPos, Oxidizable.getUnaffectedOxidationState(world.getBlockState(blockPos)))
        val mutable = pos.mutableCopy()
        val i = world.random.nextInt(3) + 3
        for (j in 0 until i) {
            val k = world.random.nextInt(8) + 1
            cleanOxidationAround(world, blockPos, mutable, k)
        }
    }

    private fun cleanOxidationAround(world: World, pos: BlockPos, mutablePos: BlockPos.Mutable, count: Int) {
        mutablePos.set(pos)
        var optional: Optional<BlockPos> = cleanOxidationAround(world, mutablePos)
        var i = 1
        while (i < count && cleanOxidationAround(world, mutablePos).also { optional = it }.isPresent) {
            mutablePos.set(optional.get())
            ++i
        }
    }

    private fun cleanOxidationAround(world: World, pos: BlockPos): Optional<BlockPos> {
        for (blockPos in BlockPos.iterateRandomly(world.random, 10, pos, 1)) {
            val blockState = world.getBlockState(blockPos)
            if (blockState.block !is Oxidizable) continue
            Oxidizable.getDecreasedOxidationState(blockState).ifPresent { state: BlockState? ->
                world.setBlockState(
                    blockPos,
                    state
                )
            }
            world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, blockPos, -1)
            return Optional.of(blockPos)
        }
        return Optional.empty()
    }

    override fun remove(reason: RemovalReason?) {
        processContext.beforeRemoval()
        runEffect(ModifiableEffectEntity.ON_REMOVED,this,owner,processContext)
        super.remove(reason)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        if (ownerUuid != null) {
            nbt.putUuid("Owner", ownerUuid)
        }
        writeModifiableNbt(nbt)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        if (nbt.containsUuid("Owner")) {
            ownerUuid = nbt.getUuid("Owner")
            owner = null
        }
        readModifiableNbt(nbt)
    }
}
