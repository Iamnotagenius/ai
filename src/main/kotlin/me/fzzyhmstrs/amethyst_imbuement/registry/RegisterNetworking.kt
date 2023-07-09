package me.fzzyhmstrs.amethyst_imbuement.registry

import me.fzzyhmstrs.amethyst_imbuement.spells.ResonateAugment
import me.fzzyhmstrs.amethyst_imbuement.spells.SmitingBlowAugment
import me.fzzyhmstrs.amethyst_imbuement.screen.ImbuingTableScreenHandler
import me.fzzyhmstrs.amethyst_imbuement.recipe.RecipeUtil

object RegisterNetworking {

    fun registerServer(){
        RegisterKeybindServer.registerServer()
        ImbuingTableScreenHandler.registerServer()
        RecipeUtil.registerServer()
    }
    fun registerClient(){
        ImbuingTableScreenHandler.registerClient()
        ResonateAugment.registerClient()
        SmitingBlowAugment.registerClient()
        //ImbuingRecipeBookScreen.registerClientReceiver()
    }
}