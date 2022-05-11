package me.fzzyhmstrs.amethyst_imbuement.scepter

import me.fzzyhmstrs.amethyst_imbuement.augment.base_augments.BaseAugment
import me.fzzyhmstrs.amethyst_imbuement.scepter.base_augments.MiscAugment
import me.fzzyhmstrs.amethyst_imbuement.util.SpellType
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.item.Items
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World

class MassFortifyAugment(tier: Int, maxLvl: Int, vararg slot: EquipmentSlot): MiscAugment(tier,maxLvl, *slot) {

    override fun effect(world: World, user: LivingEntity, entityList: MutableList<Entity>, level: Int): Boolean {
        var successes = 0

        if (entityList.isEmpty()){
            successes++
            BaseAugment.addStatusToQueue(user.uuid,StatusEffects.RESISTANCE, 600 + 200 * level, level + 1, true)
            BaseAugment.addStatusToQueue(user.uuid,StatusEffects.STRENGTH, 800 + 200 * level, level - 1, true)
        } else {
            entityList.add(user)
            for (entity3 in entityList) {
                if (entity3 !is Monster && entity3 !is PassiveEntity) {
                    successes++
                    BaseAugment.addStatusToQueue(entity3.uuid, StatusEffects.RESISTANCE, 600 + 200 * level, level, true)
                    BaseAugment.addStatusToQueue(
                        entity3.uuid,
                        StatusEffects.STRENGTH,
                        600 + 200 * level,
                        level - 1,
                        true
                    )
                }
            }
        }
        return successes > 0
    }

    override fun rangeOfEffect(): Double {
        return 9.0
    }

    override fun augmentStat(imbueLevel: Int): ScepterObject.AugmentDatapoint {
        return ScepterObject.AugmentDatapoint(SpellType.GRACE,1200,60,16,imbueLevel,2, Items.GOLDEN_APPLE)
    }

    override fun soundEvent(): SoundEvent {
        return SoundEvents.BLOCK_BEACON_ACTIVATE
    }
}