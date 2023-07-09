package me.fzzyhmstrs.amethyst_imbuement.registry

import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.amethyst_imbuement.recipe.AltarRecipe
import me.fzzyhmstrs.amethyst_imbuement.recipe.AltarRecipeSerializer
import me.fzzyhmstrs.amethyst_imbuement.recipe.ImbuingRecipe
import me.fzzyhmstrs.amethyst_imbuement.recipe.ImbuingRecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RegisterRecipe {

    fun registerAll(){
        Registry.register(Registries.RECIPE_SERIALIZER, ImbuingRecipeSerializer.ID, ImbuingRecipeSerializer)
        Registry.register(Registries.RECIPE_TYPE, Identifier(AI.MOD_ID, ImbuingRecipe.Type.ID), ImbuingRecipe.Type)
        Registry.register(Registries.RECIPE_SERIALIZER, AltarRecipeSerializer.ID, AltarRecipeSerializer)
        Registry.register(Registries.RECIPE_TYPE, Identifier(AI.MOD_ID, AltarRecipe.Type.ID), AltarRecipe.Type)
    }

}