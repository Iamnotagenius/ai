package me.fzzyhmstrs.amethyst_imbuement.config

import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom
import me.fzzyhmstrs.fzzy_config.annotations.RequiresRestart
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList

@ConvertFrom("resources_v0.json", AI.MOD_ID)
class ResourcesConfig: Config(AI.identity("resources_config")){

    fun isEnabled(id: String): Boolean{
        return !disabledResources.contains(id)
    }

    @RequiresRestart
    var disabledResources = ValidatedList.ofString("optional/iridescent_scepter_imbuing", "optional/lustrous_scepter_imbuing")

}