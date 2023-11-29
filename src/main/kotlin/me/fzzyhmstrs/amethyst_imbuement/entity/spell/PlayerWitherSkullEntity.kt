package me.fzzyhmstrs.amethyst_imbuement.entity.spell

import me.fzzyhmstrs.amethyst_core.entity_util.ModifiableEffectEntity
import me.fzzyhmstrs.amethyst_core.modifier_util.AugmentConsumer
import me.fzzyhmstrs.amethyst_core.modifier_util.AugmentEffect
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.ScepterAugment
import me.fzzyhmstrs.amethyst_imbuement.config.AiConfig
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEnchantment
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.projectile.WitherSkullEntity
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.Difficulty
import net.minecraft.world.World
import kotlin.math.sqrt

class PlayerWitherSkullEntity: WitherSkullEntity, ModifiableEffectEntity {
    constructor(entityType: EntityType<out PlayerWitherSkullEntity?>, world: World): super(entityType, world)
    constructor(world: World, owner: LivingEntity, directionX: Double, directionY: Double, directionZ: Double): super(RegisterEntity.PLAYER_WITHER_SKULL, world){
        this.owner = owner
        this.setRotation(owner.yaw, owner.pitch)
        this.refreshPositionAndAngles(owner.x, owner.eyeY - (user.height * 0.25), owner.z, yaw, pitch)
        this.refreshPosition()
        val d = sqrt(directionX * directionX + directionY * directionY + directionZ * directionZ)
        if (d != 0.0){
            powerX = directionX / d * 0.1
            powerY = directionY / d * 0.1
            powerZ = directionZ / d * 0.1
        }
    }

    override var entityEffects: AugmentEffect = AugmentEffect().withDamage(8.0f).withAmplifier(1).withDuration(200,600)
    override fun passEffects(ae: AugmentEffect, level: Int) {
        super.passEffects(ae, level)
        entityEffects.setDamage(ae.damage(level))
        entityEffects.addAmplifier(ae.amplifier(level))
        entityEffects.addDuration(ae)
    }
    private var augment: ScepterAugment = RegisterEnchantment.WITHERING_BOLT
    
    fun setAugment(aug: ScepterAugment){
        this.augment = aug
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        val bl: Boolean
        //super.onEntityHit(entityHitResult) <- Check on this
        if (world.isClient) {
            return
        }
        val entity = entityHitResult.entity
        val entity2 = owner
        if (entity is LivingEntity && AiConfig.entities.shouldItHitBase(entity2, entity, augment)) {
            bl = entity.damage(SpellDamageSource(this.damageSources.witherSkull(this, entity2),augment), entityEffects.damage(0))
            if (bl) {
                if (entity.isAlive) {
                    if (entity is LivingEntity) {
                        entityEffects.accept(entity, AugmentConsumer.Type.HARMFUL)
                        entityEffects.accept(entity2, AugmentConsumer.Type.BENEFICIAL)
                    }
                    applyDamageEffects(entity2, entity)
                } else {
                    entity2.heal(5.0f)
                    entityEffects.accept(entity2, AugmentConsumer.Type.BENEFICIAL)
                }
            }
        } else {
            return
        }
        if (bl && entity is LivingEntity) {
            if (i > -1) {
                entity.addStatusEffect(StatusEffectInstance(StatusEffects.WITHER, entityEffects.duration(0), entityEffects.amplifier(0)), this.effectCause)
            }
        }
    }

}
