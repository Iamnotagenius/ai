package me.fzzyhmstrs.amethyst_imbuement.util

import me.fzzyhmstrs.amethyst_imbuement.item.Reactant
import me.fzzyhmstrs.amethyst_imbuement.item.Reagent
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.util.Identifier
import net.minecraft.world.World

class AltarRecipe(
    private val id: Identifier,
    val dust: Ingredient,
    val base: Ingredient,
    val addition: Ingredient,
    val result: ItemStack
) :
    Recipe<SimpleInventory> {

    override fun matches(inventory: SimpleInventory, world: World): Boolean {
        var bl = dust.test(inventory.getStack(0)) && base.test(inventory.getStack(1)) && addition.test(inventory.getStack(2))
        if (!bl) return false
        val item = result.item
        if (item is Reactant){
            bl = bl && item.canReact(result,Reagent.getReagents(inventory))
        }
        return bl
    }

    override fun craft(inventory: SimpleInventory): ItemStack {
        val itemStack = result.copy()
        val nbtCompound = inventory.getStack(1).nbt
        if (nbtCompound != null) {
            itemStack.nbt = nbtCompound.copy()
        }
        val item = itemStack.item
        if (item is Reactant){
            item.react(itemStack,Reagent.getReagents(inventory))
        }
        return itemStack
    }

    override fun fits(width: Int, height: Int): Boolean {
        return width * height >= 3
    }

    override fun getOutput(): ItemStack {
        return result.copy()
    }

    fun testDust(stack: ItemStack): Boolean {
        return dust.test(stack)
    }
    
    fun testAddition(stack: ItemStack): Boolean {
        return addition.test(stack)
    }

    override fun getId(): Identifier {
        return id
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return AltarRecipeSerializer
    }


    object Type : RecipeType<AltarRecipe> {
        // This will be needed in step 4
        const val ID = "altar_recipe"
    }

    override fun getType(): RecipeType<*> {
        return Type
    }
}
