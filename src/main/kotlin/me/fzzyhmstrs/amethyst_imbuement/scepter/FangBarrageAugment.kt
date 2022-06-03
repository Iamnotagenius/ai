package me.fzzyhmstrs.amethyst_imbuement.scepter

import me.fzzyhmstrs.amethyst_imbuement.entity.PlayerFangsEntity
import me.fzzyhmstrs.amethyst_imbuement.scepter.base_augments.*
import me.fzzyhmstrs.amethyst_imbuement.util.LoreTier
import me.fzzyhmstrs.amethyst_imbuement.util.SpellType
import net.minecraft.block.BlockState
import net.minecraft.entity.*
import net.minecraft.entity.mob.EvokerFangsEntity
import net.minecraft.item.Items
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.World
import kotlin.math.max
import kotlin.math.min

class FangBarrageAugment(tier: Int, maxLvl: Int, vararg slot: EquipmentSlot): MiscAugment(tier, maxLvl, *slot), PersistentAugment {

    override val baseEffect: AugmentEffect
        get() = super.baseEffect.withDuration(28,0,0)
            .withAmplifier(12,0,0)
            .withDamage(6.0F)

    override val delay = PerLvlI(15,-1,0)

    override fun effect(
        world: World,
        target: Entity?,
        user: LivingEntity,
        level: Int,
        hit: HitResult?,
        effect: AugmentEffect
    ): Boolean {
        val entityList: MutableList<Entity> = mutableListOf()
        val d: Double
        val e: Double
        if (target != null){
            d = min(target.y, user.y)
            e = max(target.y, user.y) + 1.0
            entityList.add(target)
        } else {
            d = user.y
            e = user.y + 1.0
        }
        val f = (user.yaw + 90) * MathHelper.PI / 180
        val successes = conjureBarrage(user,world,d,e,f, effect, level)
        val bl = successes > 0
        if (bl){
            effect.accept(user,AugmentConsumer.Type.BENEFICIAL)
        }
        ScepterObject.setPersistentTickerNeed(world,user,entityList,level, BlockPos.ORIGIN,this, delay.value(level),effect.duration(level), effect)
        return bl
    }

    override fun persistentEffect(
        world: World,
        user: LivingEntity,
        blockPos: BlockPos,
        entityList: MutableList<Entity>,
        level: Int,
        effect: AugmentEffect
    ) {
        val d: Double
        val e: Double
        if (entityList.isNotEmpty()){
            val target = entityList[0]
            d = min(target.y, user.y)
            e = max(target.y, user.y) + 1.0
            entityList.add(target)
        } else {
            d = user.y
            e = user.y + 1.0
        }
        val f = (user.yaw + 90) * MathHelper.PI / 180
        conjureBarrage(user,world,d,e,f, effect, level)
    }

    override fun augmentStat(imbueLevel: Int): ScepterObject.AugmentDatapoint {
        return ScepterObject.AugmentDatapoint(SpellType.FURY,100,40,22,imbueLevel,LoreTier.HIGH_TIER, Items.EMERALD_BLOCK)
    }

    override fun soundEvent(): SoundEvent {
        return SoundEvents.ENTITY_EVOKER_FANGS_ATTACK
    }

    private fun conjureBarrage(user: LivingEntity, world: World, d: Double, e: Double, f: Float, effect: AugmentEffect, level: Int): Int{
        var successes = 0
        val fangs = effect.amplifier(level)
        for (i in 0..fangs) {
            val g = 1.25 * (i + 1).toDouble()
            for (k in -1..1){
                val success = PlayerFangsEntity.conjureFangs(
                    world,
                    user,
                    user.x + MathHelper.cos(f + (11.0F * MathHelper.PI / 180 * k)).toDouble() * g,
                    user.z + MathHelper.sin(f + (11.0F * MathHelper.PI / 180 * k)).toDouble() * g,
                    d,
                    e,
                    f,
                    i,
                    effect,
                    level
                )
                if (success) successes++
            }
        }
        return successes
    }
}