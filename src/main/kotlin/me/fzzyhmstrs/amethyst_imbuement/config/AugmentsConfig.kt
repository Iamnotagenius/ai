package me.fzzyhmstrs.amethyst_imbuement.config

import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedStringMap
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedString
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt

@ConvertFrom("augments_v4.json",AI.MOD_ID)
class AugmentsConfig: Config(AI.identity("augments_config")) {
    //@ReadMeText("readme.trinkets.enableBurnout")
    var enableBurnout = ValidatedBoolean(true)
    //@ReadMeText("readme.trinkets.draconicVisionRange")
    var draconicVisionRange = ValidatedInt(5,16,1)
    //@ReadMeText("readme.trinkets.enabledAugments")
    var enabledAugments = ValidatedStringMap(AiConfigDefaults.enabledAugments,ValidatedString(), ValidatedBoolean())

}