package me.fzzyhmstrs.amethyst_imbuement.material

import me.fzzyhmstrs.amethyst_imbuement.AI
import net.fabricmc.yarn.constants.MiningLevels
import net.minecraft.util.Identifier

object AiScepterMaterialsConfig{
    ///// generic scepters /////
    val SCEPTER_TIER_1 = ScepterToolMaterial.Builder(1)
        .attackSpeed(-3.0)
        .healCooldown(150L)
        .durability(250,750)
        .miningSpeedMultiplier(1f)
        .attackDamage(0f)
        .miningLevel(1)
        .enchantability(25)
        .repairIngredient(AI.identity("beryl_copper_ingot"))
        .build()
    val SCEPTER_TIER_2 = ScepterToolMaterial.Builder(2)
        .attackSpeed(-3.0)
        .healCooldown(125L)
        .durability(650,1950)
        .miningSpeedMultiplier(1f)
        .attackDamage(0f)
        .miningLevel(1)
        .enchantability(25)
        .repairIngredient(Identifier("gold_ingot"))
        .build()
    val SCEPTER_TIER_3 = ScepterToolMaterial.Builder(3)
        .attackSpeed(-3.0)
        .healCooldown(80L)
        .durability(1450,4350)
        .miningSpeedMultiplier(1f)
        .attackDamage(0f)
        .miningLevel(1)
        .enchantability(25)
        .repairIngredient(Identifier("netherite_scrap"))
        .build()

    ///// special scepters /////
    val BUILDERS_SCEPTER = ScepterToolMaterial.Builder(2)
        .attackSpeed(-3.0)
        .healCooldown(125L)
        .durability(600,1800)
        .miningSpeedMultiplier(5f)
        .attackDamage(2f)
        .miningLevel(1)
        .enchantability(25)
        .repairIngredient(Identifier("gold_ingot"))
        .build()
    val FZZYHAMMER = ScepterToolMaterial.Builder(3)
        .attackSpeed(-3.2)
        .healCooldown(120L)
        .durability(1250,3750)
        .miningSpeedMultiplier(6f)
        .attackDamage(9.5f)
        .miningLevel(3)
        .enchantability(15)
        .repairIngredient(AI.identity("heartstone"))
        .build()
    val LETHALITY = ScepterToolMaterial.Builder(3)
        .attackSpeed(-2.7)
        .healCooldown(90L)
        .durability(1325,3975)
        .miningSpeedMultiplier(1.0f)
        .attackDamage(4f)
        .miningLevel(1)
        .enchantability(35)
        .repairIngredient(Identifier("netherite_scrap"))
        .build()
    val SCEPTER_OF_BLADES = ScepterToolMaterial.Builder(2)
        .attackSpeed(-2.7)
        .healCooldown(125L)
        .durability(550,1650)
        .miningSpeedMultiplier(1f)
        .attackDamage(1.5f)
        .miningLevel(1)
        .enchantability(25)
        .repairIngredient(AI.identity("glowing_fragment"))
        .build()
    val SCEPTER_OF_HARVESTS = ScepterToolMaterial.Builder(2)
        .attackSpeed(-3.0)
        .healCooldown(100L)
        .durability(750,2250)
        .miningSpeedMultiplier(4f)
        .attackDamage(0f)
        .miningLevel(MiningLevels.NETHERITE)
        .enchantability(15)
        .repairIngredient(Identifier("gold_ingot"))
        .build()
    val SCEPTER_OF_THE_VANGUARD = ScepterToolMaterial.Builder(2)
        .attackSpeed(-3.0)
        .healCooldown(125L)
        .durability(650,1950)
        .miningSpeedMultiplier(1f)
        .attackDamage(2f)
        .miningLevel(1)
        .enchantability(35)
        .repairIngredient(AI.identity("glowing_fragment"))
        .build()
    val SCEPTER_SO_FOWL = ScepterToolMaterial.Builder(2)
        .attackSpeed(-3.0)
        .healCooldown(12L)
        .durability(600,1800)
        .miningSpeedMultiplier(1f)
        .attackDamage(0f)
        .miningLevel(1)
        .enchantability(25)
        .repairIngredient(Identifier("cooked_chicken"))
        .build()
}