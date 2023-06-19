package me.fzzyhmstrs.amethyst_imbuement.entity

import me.fzzyhmstrs.amethyst_core.entity_util.ModifiableEffectEntity
import me.fzzyhmstrs.amethyst_core.modifier_util.AugmentEffect
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class IceShardEntity(entityType: EntityType<out IceShardEntity?>, world: World): BaseShardEntity(entityType, world), ModifiableEffectEntity {

    constructor(world: World, owner: LivingEntity, speed: Float, divergence: Float, pos: Vec3d, rot: Vec3d): this(
        RegisterEntity.ICE_SHARD_ENTITY,world){
        this.owner = owner
        this.setVelocity(rot.x,rot.y,rot.z,speed,divergence)
        this.setPosition(pos)
    }

    override var entityEffects: AugmentEffect = super.entityEffects.withDuration(180)


    override fun passEffects(ae: AugmentEffect, level: Int) {
        super<BaseShardEntity>.passEffects(ae, level)
        entityEffects.setDuration(ae.duration(level))
    }

    override fun onEntityHitSideEffects(entity: LivingEntity) {
        if (entity.world.random.nextFloat() < 0.1){
            entity.frozenTicks = entityEffects.duration(0)
        }
    }

    override fun particle(): ParticleEffect {
        return ParticleTypes.SNOWFLAKE
    }



}
