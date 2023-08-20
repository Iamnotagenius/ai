package me.fzzyhmstrs.amethyst_imbuement.entity.golem

import com.google.common.collect.ImmutableMap
import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.amethyst_imbuement.config.AiConfig
import me.fzzyhmstrs.fzzy_config.config_util.ConfigSection
import me.fzzyhmstrs.fzzy_config.validated_field.ValidatedDouble
import me.fzzyhmstrs.fzzy_config.validated_field.ValidatedFloat
import me.fzzyhmstrs.fzzy_config.validated_field.ValidatedInt
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent

open class LavaGolemEntity: CrystallineGolemEntity {

    constructor(entityType: EntityType<LavaGolemEntity>, world: World): super(entityType, world)

    constructor(entityType: EntityType<LavaGolemEntity>, world: World, ageLimit: Int, createdBy: LivingEntity?) : super(entityType, world, ageLimit, createdBy)

    class LavaGolem: ConfigSection(Header.Builder().space().add("readme.entities.lavaGolem_1").build()){
        var baseLifespan = ValidatedInt(4800, Int.MAX_VALUE-120000,20)
        var baseHealth = ValidatedDouble(120.0,800.0,1.0)
        var baseDamage = ValidatedFloat(16.0f,500f,0f)
        var baseMoveSpeed = ValidatedDouble(0.4,1.0,0.01)
        var baseKnockbackResist = ValidatedDouble(1.0,1.0,0.0)
    }

    companion object {
        fun createGolemAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, AiConfig.entities.lavaGolem.baseHealth.get())
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, AiConfig.entities.lavaGolem.baseMoveSpeed.get())
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, AiConfig.entities.lavaGolem.baseKnockbackResist.get())
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, AiConfig.entities.lavaGolem.baseDamage.get().toDouble())
        }
    }

    private val crackIdentifierMap: Map<Crack, Identifier> = ImmutableMap.of(
        CrystallineGolemEntity.Crack.LOW,
        AI.identity("textures/entity/crystal_golem/lava_golem_crackiness_low.png"),
        CrystallineGolemEntity.Crack.MEDIUM,
        AI.identity("textures/entity/crystal_golem/lava_golem_crackiness_medium.png"),
        CrystallineGolemEntity.Crack.HIGH,
        AI.identity("textures/entity/crystal_golem/lava_golem_crackiness_high.png")
    )

    override fun attackEffects(target: Entity, damageSource: DamageSource, damage: Float) {
        target.setOnFireFor(5 * (getCrack().index + 1))
    }

    override fun getBaseDamage(): Float {
        return super.getBaseDamage() + getCrack().index.toFloat()
    }

    override fun interactMob(player: PlayerEntity, hand: Hand?): ActionResult? {
        val itemStack = player.getStackInHand(hand)
        if (!itemStack.isOf(Items.MAGMA_BLOCK)) {
            return ActionResult.PASS
        }
        val f = this.health
        heal(7.5f)
        if (this.health == f) {
            return ActionResult.PASS
        }
        val g = 1.0f + (random.nextFloat() - random.nextFloat()) * 0.2f
        playSound(SoundEvents.ENTITY_IRON_GOLEM_REPAIR, 1.0f, g)
        this.emitGameEvent(GameEvent.ENTITY_INTERACT, this)
        if (!player.abilities.creativeMode) {
            itemStack.decrement(1)
        }
        return ActionResult.success(world.isClient)
    }

    override fun playStepSound(pos: BlockPos, state: BlockState) {
        playSound(SoundEvents.ENTITY_IRON_GOLEM_STEP, 1.0f, 1.0f)
    }

    override fun getCrackTextureMap(): Map<Crack, Identifier> {
        return crackIdentifierMap
    }
}