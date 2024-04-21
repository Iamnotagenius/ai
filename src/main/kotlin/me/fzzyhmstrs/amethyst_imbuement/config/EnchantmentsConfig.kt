package me.fzzyhmstrs.amethyst_imbuement.config

import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedStringMap
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedString
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt
import me.fzzyhmstrs.fzzy_core.coding_util.FzzyPort
import net.minecraft.enchantment.Enchantment

@ConvertFrom("enchantments_v3.json", AI.MOD_ID)
class EnchantmentsConfig: Config(AI.identity("enchantments_config")) {

    fun isEnchantEnabled(enchantment: Enchantment): Boolean{
        val id = (FzzyPort.ENCHANTMENT.getId(enchantment) ?: return true).toString()
        return enabledEnchants.get()[id] ?: true
    }
    fun getAiMaxLevel(enchantment: Enchantment, fallback: Int): Int{
        val id = (FzzyPort.ENCHANTMENT.getId(enchantment) ?: return fallback).toString()
        val amount = aiEnchantMaxLevels.get()[id] ?: fallback
        if (disableIncreaseMaxLevels.get() && amount > fallback) return fallback
        return amount
    }

    fun getVanillaMaxLevel(enchantment: Enchantment, fallback: Int): Int{
        val id = (FzzyPort.ENCHANTMENT.getId(enchantment) ?: return fallback).toString()
        val amount = vanillaEnchantMaxLevels.get()[id] ?: fallback
        if (disableIncreaseMaxLevels.get() && amount > fallback) return fallback
        return amount
    }

    //@ReadMeText("readme.enchants.disableIncreaseMaxLevels")
    var disableIncreaseMaxLevels = ValidatedBoolean(false)

    //@ReadMeText("readme.enchants.enabledEnchants")
    var enabledEnchants = ValidatedStringMap(AiConfigDefaults.enabledEnchantments,ValidatedString(),ValidatedBoolean())

    //@ReadMeText("readme.enchants.aiEnchantMaxLevels")
    var aiEnchantMaxLevels = ValidatedStringMap(AiConfigDefaults.aiEnchantmentMaxLevels,ValidatedString(), ValidatedInt(3,7,1))

    //@ReadMeText("readme.enchants.vanillaEnchantMaxLevels")
    var vanillaEnchantMaxLevels = ValidatedStringMap(AiConfigDefaults.vanillaEnchantmentMaxLevels,ValidatedString(), ValidatedInt(3,7,1))

}