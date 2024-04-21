package me.fzzyhmstrs.amethyst_imbuement.config

import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.amethyst_imbuement.material.AiArmorMaterialsConfig
import me.fzzyhmstrs.amethyst_imbuement.material.AiScepterMaterialsConfig
import me.fzzyhmstrs.amethyst_imbuement.material.AiToolMaterialsConfig
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom
import me.fzzyhmstrs.fzzy_config.annotations.RequiresRestart
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.config.ConfigSection

@ConvertFrom("materials_v2.json",AI.MOD_ID)
class MaterialsConfig: Config(AI.identity("materials_config")) {
    var armor = Armor()
    @RequiresRestart
    class Armor: ConfigSection() {
        var ametrine = AiArmorMaterialsConfig.AMETRINE
        var steel = AiArmorMaterialsConfig.STEEL
        var garnet = AiArmorMaterialsConfig.GARNET
        var glowing = AiArmorMaterialsConfig.GLOWING
        var shimmering = AiArmorMaterialsConfig.SHIMMERING
        var soulwoven = AiArmorMaterialsConfig.SOULWOVEN
    }
    var tools = Tools()
    @RequiresRestart
    class Tools: ConfigSection() {
        var garnet = AiToolMaterialsConfig.GARNET
        var glowing = AiToolMaterialsConfig.GLOWING
        var steel = AiToolMaterialsConfig.STEEL
        var ametrine = AiToolMaterialsConfig.AMETRINE
        var glistering = AiToolMaterialsConfig.GLISTERING
    }
    var scepters = Scepters()
    @RequiresRestart
    class Scepters: ConfigSection() {
        var tier1Scepter = AiScepterMaterialsConfig.SCEPTER_TIER_1
        var tier2Scepter = AiScepterMaterialsConfig.SCEPTER_TIER_2
        var tier3Scepter = AiScepterMaterialsConfig.SCEPTER_TIER_3

        var blades = AiScepterMaterialsConfig.SCEPTER_OF_BLADES
        var builder = AiScepterMaterialsConfig.BUILDERS_SCEPTER
        var fowl = AiScepterMaterialsConfig.SCEPTER_SO_FOWL
        var fzzyhammer = AiScepterMaterialsConfig.FZZYHAMMER
        var harvests = AiScepterMaterialsConfig.SCEPTER_OF_HARVESTS
        var lethality = AiScepterMaterialsConfig.LETHALITY
        var vanguard = AiScepterMaterialsConfig.SCEPTER_OF_THE_VANGUARD

    }
}