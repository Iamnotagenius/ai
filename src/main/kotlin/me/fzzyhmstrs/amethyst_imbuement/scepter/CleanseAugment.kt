package me.fzzyhmstrs.amethyst_imbuement.scepter

import me.fzzyhmstrs.amethyst_imbuement.augment.base_augments.BaseAugment
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterStatus
import me.fzzyhmstrs.amethyst_imbuement.scepter.base_augments.MinorSupportAugment
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.passive.GolemEntity
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World

class CleanseAugment(weight: Rarity, tier: Int, maxLvl: Int, vararg slot: EquipmentSlot): MinorSupportAugment(weight,tier,maxLvl, *slot) {

    override fun supportEffect(world: World, target: Entity?, user: LivingEntity?, level: Int): Boolean {
        if(target != null) {
            if (target is PassiveEntity || target is GolemEntity || target is PlayerEntity) {
                val statuses: MutableList<StatusEffectInstance> = mutableListOf()
                for (effect in (target as LivingEntity).statusEffects){
                    if (effect.effectType.isBeneficial) continue
                    statuses.add(effect)
                }
                //val statuses: Collection<StatusEffectInstance> = user.statusEffects
                for (effect in statuses) {
                    target.removeStatusEffect(effect.effectType)
                }
                target.fireTicks = 0
                BaseAugment.addStatusToQueue(target.uuid,RegisterStatus.IMMUNITY,300,0, true)
                world.playSound(null, target.blockPos, soundEvent(), SoundCategory.PLAYERS, 0.6F, 1.2F)
                return true
            }
        }
        return if(user != null){
            if (user is PlayerEntity) {
                val statuses: MutableList<StatusEffectInstance> = mutableListOf()
                for (effect in user.statusEffects){
                    if (effect.effectType.isBeneficial) continue
                    statuses.add(effect)
                }
                //val statuses: Collection<StatusEffectInstance> = user.statusEffects
                for (effect in statuses) {
                    user.removeStatusEffect(effect.effectType)
                }
                user.fireTicks = 0
                BaseAugment.addStatusToQueue(user.uuid,RegisterStatus.IMMUNITY,200,0, true)
                world.playSound(null, user.blockPos, soundEvent(), SoundCategory.PLAYERS, 0.6F, 1.2F)
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    override fun soundEvent(): SoundEvent {
        return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON
    }
}