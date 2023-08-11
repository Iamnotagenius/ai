package me.fzzyhmstrs.amethyst_imbuement.entity

import me.fzzyhmstrs.amethyst_core.augments.paired.PairedAugments
import me.fzzyhmstrs.amethyst_core.augments.paired.ProcessContext
import me.fzzyhmstrs.amethyst_core.entity.ModifiableEffectContainer
import me.fzzyhmstrs.amethyst_core.entity.ModifiableEffectEntity
import me.fzzyhmstrs.amethyst_core.interfaces.SpellCastingEntity
import me.fzzyhmstrs.amethyst_core.modifier.AugmentEffect
import me.fzzyhmstrs.amethyst_imbuement.config.AiConfig
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.AbstractFireballEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World

open class PlayerFireballEntity: AbstractFireballEntity, ModifiableEffectEntity {
    constructor(entityType: EntityType<out PlayerFireballEntity?>, world: World): super(entityType, world)
    constructor(world: World, owner: LivingEntity, velocityX: Double, velocityY: Double, velocityZ: Double):
            super(RegisterEntity.PLAYER_FIREBALL,owner,velocityX,velocityY,velocityZ, world)

    override var entityEffects: AugmentEffect = AugmentEffect().withDamage(6.0F).withAmplifier(1)
    override var level: Int = 1
    override var spells: PairedAugments = PairedAugments()
    override var modifiableEffects = ModifiableEffectContainer()
    override var processContext: ProcessContext = ProcessContext.FROM_ENTITY_CONTEXT

    override fun tick() {
        super.tick()
        tickTickEffects(this,owner,processContext)
    }

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)
        val entity = owner
        if (entity is LivingEntity)
            spells.causeExplosion(processContext,this,entity,world,Hand.MAIN_HAND,level,entityEffects)
        if (!world.isClient) {
            discard()
        }
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        if (world.isClient) {
            return
        }
        val augment = spells.primary() ?: return
        val entity = entityHitResult.entity
        val entity2 = owner
        if (entity2 is LivingEntity && !(entity is SpellCastingEntity && AiConfig.entities.isEntityPvpTeammate(entity2, entity, augment))) {
            onFireballEntityHit(entityHitResult, entity2)
        }
    }

    open fun onFireballEntityHit(entityHitResult: EntityHitResult, entity: LivingEntity?){
        if (entity is LivingEntity){
            if (entity is SpellCastingEntity && !spells.empty()) {
                runEffect(ModifiableEffectEntity.DAMAGE,this,entity,processContext)
                spells.processSingleEntityHit(entityHitResult,processContext,world,this,entity,Hand.MAIN_HAND,level,entityEffects)
                if (!entityHitResult.entity.isAlive){
                    runEffect(ModifiableEffectEntity.KILL,this,entity,processContext)
                    spells.processOnKill(entityHitResult,processContext,world,this,entity,Hand.MAIN_HAND,level,entityEffects)
                }
            } else {
                val bl = entityHitResult.entity.damage(this.damageSources.mobProjectile(this,entity),entityEffects.damage(0))
                if (bl){
                    entity.applyDamageEffects(entity,entityHitResult.entity)
                }
            }
        }
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        onFireballBlockHit(blockHitResult)
    }

    open fun onFireballBlockHit(blockHitResult: BlockHitResult){
        val entity = owner
        if (entity is LivingEntity && entity is SpellCastingEntity) {
            spells.processSingleBlockHit(blockHitResult,world,this,entity,Hand.MAIN_HAND,level,entityEffects)
        }
    }

    override fun remove(reason: RemovalReason?) {
        processContext.beforeRemoval()
        runEffect(ModifiableEffectEntity.ON_REMOVED,this,owner,processContext)
        super.remove(reason)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        writeModifiableNbt(nbt)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        readModifiableNbt(nbt)
    }
}
