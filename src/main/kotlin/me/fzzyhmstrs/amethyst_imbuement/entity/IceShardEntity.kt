package me.fzzyhmstrs.amethyst_imbuement.entity

import me.fzzyhmstrs.amethyst_core.entity_util.ModifiableEffectEntity
import me.fzzyhmstrs.amethyst_core.interfaces.SpellCastingEntity
import me.fzzyhmstrs.amethyst_core.modifier_util.AugmentConsumer
import me.fzzyhmstrs.amethyst_core.modifier_util.AugmentEffect
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.ScepterAugment
import me.fzzyhmstrs.amethyst_imbuement.config.AiConfig
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEnchantment
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*
import kotlin.math.max

class IceShardEntity(entityType: EntityType<out IceShardEntity?>, world: World): PersistentProjectileEntity(entityType, world), ModifiableEffectEntity {

    constructor(world: World, owner: LivingEntity, speed: Float, divergence: Float, pos: Vec3d, rot: Vec3d): this(
        RegisterEntity.ICE_SHARD_ENTITY,world){
        this.owner = owner
        this.setVelocity(rot.x,rot.y,rot.z,speed,divergence)
        this.setPosition(pos)
    }

    override var entityEffects: AugmentEffect = AugmentEffect().withDamage(6F).withDuration(180).withAmplifier(1)
    private val struckEntities: MutableList<UUID> = mutableListOf()
    private var augment: ScepterAugment = RegisterEnchantment.ICE_SHARD
    
    fun setAugment(aug: ScepterAugment){
        this.augment = aug
    }

    override fun passEffects(ae: AugmentEffect, level: Int) {
        super.passEffects(ae, level)
        entityEffects.setDamage(ae.damage(level))
        entityEffects.setAmplifier(ae.amplifier(level))
        entityEffects.setDuration(ae.duration(level))
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        if (world.isClient) {
            return
        }
        val entity = owner
        if (entity is LivingEntity) {
            val entity2 = entityHitResult.entity
            if (!(entity2 is SpellCastingEntity && AiConfig.entities.isEntityPvpTeammate(entity, entity2, augment))){
                val bl = entity2.damage(
                    DamageSource.thrownProjectile(this,owner),
                    max(1f,entityEffects.damage(0) - struckEntities.size)
                )
                if (!struckEntities.contains(entity2.uuid)){
                    struckEntities.add(entity2.uuid)
                }
                if (bl) {
                    entityEffects.accept(entity, AugmentConsumer.Type.BENEFICIAL)
                    applyDamageEffects(entity as LivingEntity?, entity2)
                    if (entity2 is LivingEntity) {
                        if (entity2.world.random.nextFloat() < 0.1){
                            entity2.frozenTicks = entityEffects.duration(0)
                        }
                        entityEffects.accept(entity2, AugmentConsumer.Type.HARMFUL)
                    }
                }
            }
        }
        if (struckEntities.size > entityEffects.amplifier(0)){
            discard()
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
        if (!inGround)
            addParticles(velocity.x, velocity.y, velocity.z)
    }

    private fun addParticles(x2: Double, y2: Double, z2: Double){
        if (this.isTouchingWater) {
            for (i in 0..2) {
                world.addParticle(
                    ParticleTypes.BUBBLE,
                    this.x + x2 * (world.random.nextFloat()-0.5f),
                    this.y + y2 * (world.random.nextFloat()-0.5f),
                    this.z + z2 * (world.random.nextFloat()-0.5f),
                    0.0,
                    0.0,
                    0.0
                )
            }
        } else {
            for (i in 0..2) {
                world.addParticle(
                    ParticleTypes.SNOWFLAKE,
                    this.x + x2 * (world.random.nextFloat()-0.5f),
                    this.y + y2 * (world.random.nextFloat()-0.5f),
                    this.z + z2 * (world.random.nextFloat()-0.5f),
                    0.0,
                    0.0,
                    0.0
                )
            }
        }
    }

}
