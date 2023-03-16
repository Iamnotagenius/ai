package me.fzzyhmstrs.amethyst_imbuement.tool

import me.fzzyhmstrs.amethyst_core.scepter_util.ScepterToolMaterial
import me.fzzyhmstrs.amethyst_imbuement.config.AiConfig
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import kotlin.math.max


object ScepterLvl3ToolMaterial: ScepterToolMaterial(){
    override fun getDurability(): Int {
        return AiConfig.items.lustrousDurability
    }
    fun defaultDurability(): Int{
        return 1450
    }
    override fun getMiningSpeedMultiplier(): Float {
        return 1.0f
    }
    override fun getAttackDamage(): Float {
        return 0.0f
    }
    override fun getAttackSpeed(): Double {
        return -3.0
    }
    override fun getMiningLevel(): Int {
        return 1
    }
    override fun getEnchantability(): Int {
        return 25
    }
    override fun getRepairIngredient(): Ingredient {
        return Ingredient.ofItems(Items.NETHERITE_INGOT)
    }
    override fun healCooldown(): Long {
        return max(AiConfig.items.baseRegenRateTicks - 70L,minCooldown())
    }

    override fun baseCooldown(): Long {
        return 80L
    }
    override fun scepterTier(): Int{
        return 3
    }
}
