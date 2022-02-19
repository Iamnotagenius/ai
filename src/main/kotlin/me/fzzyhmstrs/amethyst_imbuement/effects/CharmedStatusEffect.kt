package me.fzzyhmstrs.amethyst_imbuement.effects

import net.minecraft.client.MinecraftClient
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.passive.VillagerEntity

class CharmedStatusEffect(statusEffectCategory:StatusEffectCategory, i: Int):
    StatusEffect(statusEffectCategory,i) {
    override fun onRemoved(entity: LivingEntity, attributes: AttributeContainer, amplifier: Int) {
        if (entity is MobEntity && entity !is VillagerEntity){
            println(entity.isAiDisabled)
            entity.isAiDisabled = false
        }
        super.onRemoved(entity, attributes, amplifier)
    }

    override fun onApplied(entity: LivingEntity, attributes: AttributeContainer, amplifier: Int) {
        if (entity is MobEntity && entity !is VillagerEntity){
            entity.isAiDisabled = true
        }
        super.onApplied(entity, attributes, amplifier)
    }
}