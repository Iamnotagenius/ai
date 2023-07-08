package me.fzzyhmstrs.amethyst_imbuement.entity

import me.fzzyhmstrs.amethyst_core.entity.ModifiableEffectEntity
import me.fzzyhmstrs.amethyst_core.interfaces.SpellCastingEntity
import me.fzzyhmstrs.amethyst_core.modifier.AugmentEffect
import me.fzzyhmstrs.amethyst_core.augments.paired.PairedAugments
import me.fzzyhmstrs.amethyst_core.entity.TickEffect
import me.fzzyhmstrs.amethyst_imbuement.config.AiConfig
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

open class BasicShardEntity(entityType: EntityType<out BasicShardEntity?>, world: World): PersistentProjectileEntity(entityType, world), ModifiableEffectEntity<BasicShardEntity> {

    override var entityEffects: AugmentEffect = AugmentEffect().withDamage(6F).withAmplifier(0)
    override var level: Int = 1
    override var spells: PairedAugments = PairedAugments()
    override val tickEffects: ConcurrentLinkedQueue<TickEffect> = ConcurrentLinkedQueue()
    private val struckEntities: MutableList<UUID> = mutableListOf()
    private val particle
        get() = spells.getCastParticleType()

    override fun tickingEntity(): BasicShardEntity {
        return this
    }

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)
        if (!world.isClient) {
            discard()
        }
    }
    override fun onEntityHit(entityHitResult: EntityHitResult) {
        if (world.isClient) {
            return
        }
        val entity = owner
        if (entity is LivingEntity) {
            val entity2 = entityHitResult.entity
            val aug = spells.primary()?:return
            if (!(entity2 is SpellCastingEntity && AiConfig.entities.isEntityPvpTeammate(entity, entity2, aug))){
                if (!struckEntities.contains(entity2.uuid)){
                    struckEntities.add(entity2.uuid)
                    onShardEntityHit(entityHitResult)
                }
            }
        }
        if (struckEntities.size > entityEffects.amplifier(0)){
            discard()
        }
    }

    open fun onShardEntityHit(entityHitResult: EntityHitResult){
        val entity = owner
        if (entity is LivingEntity && entity is SpellCastingEntity) {
            spells.processSingleEntityHit(entityHitResult,world,this,entity, Hand.MAIN_HAND,level,entityEffects)
            if (!entityHitResult.entity.isAlive){
                spells.processOnKill(entityHitResult,world,this,entity, Hand.MAIN_HAND,level,entityEffects)
            }
        }
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        onShardBlockHit(blockHitResult)
        discard()
    }

    open fun onShardBlockHit(blockHitResult: BlockHitResult){
        val entity = owner
        if (entity is LivingEntity && entity is SpellCastingEntity) {
            spells.processSingleBlockHit(blockHitResult,world,this,entity, Hand.MAIN_HAND,level,entityEffects)
        }
    }

    override fun asItemStack(): ItemStack {
        return ItemStack.EMPTY
    }

    override fun tryPickup(player: PlayerEntity): Boolean {
        return false
    }

    override fun tick() {
        super.tick()
        if (this.age > 1200){
            discard()
        }
        tickTickEffects()
        if (!inGround)
            addParticles(velocity.x, velocity.y, velocity.z)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        readModifiableNbt(nbt)
        super.readCustomDataFromNbt(nbt)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        writeModifiableNbt(nbt)
        super.writeCustomDataToNbt(nbt)
    }

    open fun addParticles(x2: Double, y2: Double, z2: Double){
        val particleWorld = world
        if (particleWorld !is ServerWorld) return
        if (this.isTouchingWater) {
            particleWorld.spawnParticles(ParticleTypes.BUBBLE,this.x,this.y,this.z,3,1.0,1.0,1.0,0.0)
        } else {
            particleWorld.spawnParticles(particle,this.x,this.y,this.z,3,1.0,1.0,1.0,0.0)
        }
    }

}
