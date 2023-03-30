package me.fzzyhmstrs.amethyst_imbuement.entity

import me.fzzyhmstrs.amethyst_core.entity_util.ModifiableEffectEntity
import me.fzzyhmstrs.amethyst_core.modifier_util.AugmentEffect
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEntity
import net.minecraft.entity.EntityStatuses
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.projectile.thrown.EggEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.particle.ItemStackParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World

class PlayerEggEntity: ThrownItemEntity, ModifiableEffectEntity {

    constructor (entityType: EntityType<out EggEntity?>?, world: World?):
        super(entityType as EntityType<out ThrownItemEntity?>?, world)

    constructor(world: World?, owner: LivingEntity?):
        super(EntityType.EGG as EntityType<out ThrownItemEntity?>, owner, world)

    constructor(world: World?, x: Double, y: Double, z: Double):
        super(EntityType.EGG as EntityType<out ThrownItemEntity?>, x, y, z, world)

    override var entityEffects: AugmentEffect = AugmentEffect().withDamage(3.0f)

    override fun passEffects(ae: AugmentEffect, level: Int) {
        super.passEffects(ae, level)
        entityEffects.setDamage(ae.damage(level))
    }

    override fun handleStatus(status: Byte) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            for (i in 0..7) {
                world.addParticle(
                    ItemStackParticleEffect(ParticleTypes.ITEM, this.stack),
                    this.x,
                    this.y,
                    this.z,
                    (random.nextFloat().toDouble() - 0.5) * 0.08,
                    (random.nextFloat().toDouble() - 0.5) * 0.08,
                    (random.nextFloat().toDouble() - 0.5) * 0.08
                )
            }
        }
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        entityHitResult.entity.damage(DamageSource.thrownProjectile(this, owner), entityEffects.damage(0))
    }

    override fun onCollision(hitResult: HitResult?) {
        super.onCollision(hitResult)
        if (!world.isClient) {
            if (random.nextInt(8) == 0) {
                var i = 1
                if (random.nextInt(32) == 0) {
                    i = 4
                }
                for (j in 0 until i) {
                    val chickenEntity = RegisterEntity.BOOM_CHICKEN_ENTITY.create(world) ?: continue
                    chickenEntity.breedingAge = -24000
                    chickenEntity.refreshPositionAndAngles(this.x, this.y, this.z, yaw, 0.0f)
                    if (this.owner is LivingEntity) {
                        chickenEntity.setOwner(this.owner as LivingEntity)
                    }
                    world.spawnEntity(chickenEntity)
                }
            }
            world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES)
            discard()
        }
    }

    override fun getDefaultItem(): Item? {
        return Items.EGG
    }

}