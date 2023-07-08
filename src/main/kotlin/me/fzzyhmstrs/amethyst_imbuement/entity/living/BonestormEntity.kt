package me.fzzyhmstrs.amethyst_imbuement.entity.living

import me.fzzyhmstrs.amethyst_core.interfaces.SpellCastingEntity
import me.fzzyhmstrs.amethyst_imbuement.config.AiConfig
import me.fzzyhmstrs.amethyst_imbuement.entity.goal.ShootProjectileGoal
import net.minecraft.entity.EntityGroup
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

open class BonestormEntity: PlayerCreatedConstructEntity, SpellCastingEntity {

    constructor(entityType: EntityType<BonestormEntity>, world: World): super(entityType, world)

    constructor(entityType: EntityType<BonestormEntity>, world: World, ageLimit: Int, createdBy: LivingEntity? = null) : super(entityType, world, ageLimit, createdBy)

    companion object {
        private  val baseMaxHealth = AiConfig.entities.bonestorm.baseHealth.get()
        private  val baseAttackDamage = AiConfig.entities.bonestorm.baseDamage.get()
        private val BONESTORM_FLAGS = DataTracker.registerData(BonestormEntity::class.java,TrackedDataHandlerRegistry.BOOLEAN)

        fun createBonestormAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, baseMaxHealth)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, baseAttackDamage.toDouble())
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24.0)
        }
    }

    private var eyeOffsetCooldown: Int = 0
    private var eyeOffset: Float = 0f
    override var entityGroup: EntityGroup = EntityGroup.UNDEAD

    override fun initGoals() {
        goalSelector.add(4, ShootProjectileGoal(this))
        goalSelector.add(5, GoToWalkTargetGoal(this, 1.0))
        goalSelector.add(7, WanderAroundFarGoal(this as PathAwareEntity, 1.0, 0.0f))
        goalSelector.add(8, LookAtEntityGoal(this, PlayerEntity::class.java, 6.0f))
        goalSelector.add(8, LookAroundGoal(this))
        targetSelector.add(2, RevengeGoal(this, *arrayOfNulls(0)))
        targetSelector.add(3, ActiveTargetGoal(this, PlayerEntity::class.java, 10, true, false) { entity: LivingEntity? -> shouldAngerAt(entity) })
        targetSelector.add(3, ActiveTargetGoal(this, MobEntity::class.java, 5, false, false) { entity: LivingEntity? -> entity is Monster })
        targetSelector.add(4, UniversalAngerGoal(this, true))
    }

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(BONESTORM_FLAGS,false)
    }

    override fun tickMovement() {
        if (!isOnGround && velocity.y < 0.0) {
            velocity = velocity.multiply(1.0, 0.6, 1.0)
        }
        if (world.isClient) {
            if (random.nextInt(24) == 0 && !this.isSilent) {
                world.playSound(
                    this.x + 0.5,
                    this.y + 0.5,
                    this.z + 0.5,
                    SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT,
                    this.soundCategory,
                    1.0f + random.nextFloat(),
                    random.nextFloat() * 0.7f + 0.3f,
                    false
                )
            }
            val particles = if(isFireActive()) 8 else 1
            for (i in 0..particles) {
                world.addParticle(
                    ParticleTypes.END_ROD,
                    getParticleX(0.5), this.getBodyY(world.random.nextDouble() * 0.72), getParticleZ(0.5), 0.0, 0.0, 0.0
                )
            }
        }
        super.tickMovement()
    }

    override fun mobTick() {
        --this.eyeOffsetCooldown
        if (this.eyeOffsetCooldown <= 0) {
            this.eyeOffsetCooldown = 100
            this.eyeOffset = random.nextTriangular(0.5, 6.891).toFloat()
        }
        val livingEntity = target
        if (livingEntity != null && livingEntity.eyeY > this.eyeY + this.eyeOffset.toDouble() && this.canTarget(livingEntity)) {
            val vec3d = velocity
            velocity = velocity.add(0.0, (0.3 - vec3d.y) * 0.3, 0.0)
            velocityDirty = true
        }
        super.mobTick()
    }

    override fun getAmbientSound(): SoundEvent? {
        return SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT
    }

    override fun getHurtSound(source: DamageSource): SoundEvent {
        return SoundEvents.ENTITY_WITHER_SKELETON_HURT
    }

    override fun getDeathSound(): SoundEvent {
        return SoundEvents.ENTITY_WITHER_SKELETON_DEATH
    }

    override fun getStepSound(): SoundEvent {
        return SoundEvents.ENTITY_ZOMBIE_STEP
    }

    override fun getGroup(): EntityGroup {
        return EntityGroup.UNDEAD
    }

    override fun handleFallDamage(fallDistance: Float, damageMultiplier: Float, damageSource: DamageSource?): Boolean {
        return false
    }

    fun isFireActive(): Boolean{
        return dataTracker.get(BONESTORM_FLAGS)
    }

    fun setFireActive(fireActive: Boolean){
        dataTracker.set(BONESTORM_FLAGS,fireActive)
    }

    override fun getRotationVec3d(): Vec3d {
        return this.rotationVector
    }

}