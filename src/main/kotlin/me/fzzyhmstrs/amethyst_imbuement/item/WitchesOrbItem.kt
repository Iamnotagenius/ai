package me.fzzyhmstrs.amethyst_imbuement.item

import me.fzzyhmstrs.amethyst_core.event.ModifyModifiersEvent
import me.fzzyhmstrs.amethyst_core.modifier_util.ModifierHelper
import me.fzzyhmstrs.amethyst_imbuement.item.promise.IgnitedGemItem
import me.fzzyhmstrs.fzzy_core.interfaces.Modifiable
import me.fzzyhmstrs.fzzy_core.item_util.CustomFlavorItem
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack

class WitchesOrbItem(settings: Settings)
    : 
    CustomFlavorItem(settings), Reactant, Modifiable
{

    init{
        ModifyModifiersEvent.EVENT.register{ _,user,_,modifiers ->
            for (stack in user.handItems) {
                if (stack.item is WitchesOrbItem) {
                    val focusMods = ModifierHelper.getActiveModifiers(stack)
                    val mods = modifiers.combineWith(focusMods)
                    return@register mods
                }
            }
            modifiers
        }
    }

    override fun canReact(stack: ItemStack, reagents: List<ItemStack>): Boolean {
        return true
    }

    override fun react(stack: ItemStack, reagents: List<ItemStack>) {
        for (reagent in reagents){
            val item = reagent.item
            if (item is IgnitedGemItem){
                val id = item.getModifier()
                if (ModifierHelper.getModifierByType(id) != null){
                    ModifierHelper.addModifier(id,stack)
                }
                break
            }
        }
    }
}