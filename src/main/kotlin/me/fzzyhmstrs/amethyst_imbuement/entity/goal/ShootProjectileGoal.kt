package me.fzzyhmstrs.amethyst_imbuement.entity.goal

import me.fzzyhmstrs.amethyst_core.augments.paired.ProcessContext
import me.fzzyhmstrs.amethyst_core.interfaces.SpellCastingEntity
import me.fzzyhmstrs.amethyst_imbuement.entity.BasicShardEntity
import me.fzzyhmstrs.amethyst_imbuement.entity.living.BonestormEntity
import me.fzzyhmstrs.amethyst_imbuement.entity.living.PlayerCreatedConstructEntity
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEntity
import me.fzzyhmstrs.amethyst_imbuement.spells.pieces.ContextData
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.util.Hand
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.WorldEvents
import java.util.*
import kotlin.math.sqrt

internal class ShootProjectileGoal(private val constructEntity: PlayerCreatedConstructEntity, private val projectiles: Int = 3) : Goal() {
    private var fireballsFired = 0
    private var fireballCooldown = 0
    private var targetNotVisibleTicks = 0
    override fun canStart(): Boolean {
        val livingEntity = constructEntity.target
        return livingEntity != null && livingEntity.isAlive && constructEntity.canTarget(livingEntity)
    }

    override fun start() {
        fireballsFired = 0
    }

    override fun stop() {
        if (constructEntity is BonestormEntity) {
            constructEntity.setFireActive(false)
        }
        targetNotVisibleTicks = 0
    }

    override fun shouldRunEveryTick(): Boolean {
        return true
    }

    override fun tick() {
        --fireballCooldown
        val livingEntity = constructEntity.target ?: return
        val bl = constructEntity.visibilityCache.canSee(livingEntity)
        targetNotVisibleTicks = if (bl) 0 else ++targetNotVisibleTicks
        val d = constructEntity.squaredDistanceTo(livingEntity)
        val speed = MathHelper.clamp(constructEntity.processContext.get(ContextData.SPEEDY),0f,0.9f)
        if (d < 4.0) {
            if (!bl) {
                return
            }
            if (fireballCooldown <= 0) {
                fireballCooldown = (20 * (1f-speed)).toInt()
                constructEntity.tryAttack(livingEntity)
            }
            constructEntity.moveControl.moveTo(livingEntity.x, livingEntity.y, livingEntity.z, 1.0)
        } else if (d < followRange * followRange && bl) {

            if (fireballCooldown <= 0) {
                ++fireballsFired
                if (fireballsFired == 1) {
                    if (constructEntity is BonestormEntity) {
                        fireballCooldown = (60 * (1f-speed)).toInt()
                        constructEntity.setFireActive(true)
                    }
                } else if (fireballsFired <= 1 + projectiles) {
                    fireballCooldown = 6
                } else {
                    fireballCooldown = (100 * (1f-speed)).toInt()
                    fireballsFired = 0
                    if (constructEntity is BonestormEntity) {
                        constructEntity.setFireActive(false)
                    }
                }
                if (fireballsFired > 1) {
                    val h = (sqrt(sqrt(d)) * 0.5f).toFloat()
                    if (!constructEntity.isSilent) {
                        constructEntity.world.syncWorldEvent(null, WorldEvents.BLAZE_SHOOTS, constructEntity.blockPos, 0)
                    }
                    val rot = Vec3d(
                        livingEntity.x - constructEntity.x,
                        livingEntity.getBodyY(0.5) - constructEntity.getBodyY(0.5),
                        livingEntity.z - constructEntity.z
                    )
                    val pos = Vec3d(constructEntity.x, constructEntity.getBodyY(0.5) + 0.5, constructEntity.z)
                    val owner = constructEntity.owner ?: constructEntity
                    val bse = BasicShardEntity(RegisterEntity.BONE_SHARD_ENTITY, constructEntity.world, owner, 4.0f, 1.75f * h, pos, rot)
                    bse.passEffects(constructEntity.spells,constructEntity.entityEffects,constructEntity.level)
                    bse.passContext(constructEntity.processContext.set(ProcessContext.FROM_ENTITY,true))
                    val bseFinal = if (owner is SpellCastingEntity) {
                        constructEntity.spells.provideProjectile(
                            bse,
                            owner,
                            constructEntity.world,
                            Hand.MAIN_HAND,
                            constructEntity.level,
                            constructEntity.entityEffects
                        )
                    } else {
                        bse
                    }
                    constructEntity.world.spawnEntity(bseFinal)

                }
            }
            constructEntity.lookControl.lookAt(livingEntity, 10.0f, 10.0f)
        } else if (targetNotVisibleTicks < 5) {
            constructEntity.moveControl.moveTo(livingEntity.x, livingEntity.y, livingEntity.z, 1.0)
        }
        super.tick()
    }

    private val followRange: Double = constructEntity.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE)

    init {
        controls = EnumSet.of(Control.MOVE, Control.LOOK)
    }
}