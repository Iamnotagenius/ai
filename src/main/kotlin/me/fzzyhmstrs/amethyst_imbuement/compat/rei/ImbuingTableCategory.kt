package me.fzzyhmstrs.amethyst_imbuement.compat.rei

import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterBlock
import me.fzzyhmstrs.amethyst_imbuement.util.ImbuingRecipe
import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.Renderer
import me.shedaniel.rei.api.client.gui.widgets.Slot
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.client.registry.display.DisplayCategory
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.entry.EntryStack
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier


@Suppress("UnstableApiUsage")
class ImbuingTableCategory: DisplayCategory<ImbuingTableDisplay> {
    override fun getIcon(): Renderer {
        return getIconEntryStack()
    }
    fun getIconEntryStack(): EntryStack<ItemStack> {
        return EntryStacks.of(ItemStack(RegisterBlock.IMBUING_TABLE.asItem()))
    }

    override fun getTitle(): Text {
        return TranslatableText("recipe.imbuing")
    }

    override fun getCategoryIdentifier(): CategoryIdentifier<out ImbuingTableDisplay> {
        return CategoryIdentifier.of(identifier)
    }

    override fun getIdentifier(): Identifier {
        return Identifier(ImbuingRecipe.Type.ID)
    }

    override fun setupDisplay(display: ImbuingTableDisplay, bounds: Rectangle): MutableList<Widget> {
        val widgets: MutableList<Widget> = mutableListOf()
        val xOffset = 5
        val yOffset = 5

        println("made it here!")
        println(display.inputEntries)
        widgets.add(Widgets.createCategoryBase(bounds))

        val middleSlotBackground =
            Widgets.createResultSlotBackground(Point(bounds.x + xOffset + 39, bounds.y + yOffset + 21))
        widgets.add(middleSlotBackground)

        val slot1: Slot = Widgets.createSlot(Point(bounds.x + xOffset, bounds.y + yOffset))
        slot1.entries(display.inputEntries[0])
        widgets.add(slot1)

        val slot2: Slot = Widgets.createSlot(Point(bounds.x + xOffset + 87, bounds.y + yOffset))
        slot2.entries(display.inputEntries[1])
        widgets.add(slot2)

        val slot3: Slot = Widgets.createSlot(Point(bounds.x + xOffset + 20, bounds.y + yOffset + 2))
        slot3.entries(display.inputEntries[2])
        widgets.add(slot3)

        val slot4: Slot = Widgets.createSlot(Point(bounds.x + xOffset + 43, bounds.y + yOffset + 2))
        slot4.entries(display.inputEntries[3])
        widgets.add(slot4)

        val slot5: Slot = Widgets.createSlot(Point(bounds.x + xOffset + 66, bounds.y + yOffset + 2))
        slot5.entries(display.inputEntries[4])
        widgets.add(slot5)

        val slot6: Slot = Widgets.createSlot(Point(bounds.x + xOffset + 20, bounds.y + yOffset + 25))
        slot6.entries(display.inputEntries[5])
        widgets.add(slot6)

        val slot7: Slot = Widgets.createSlot(Point(bounds.x + xOffset + 43, bounds.y + yOffset + 25))
        slot7.entries(display.inputEntries[6])
        widgets.add(slot7)

        val slot8: Slot = Widgets.createSlot(Point(bounds.x + xOffset + 66, bounds.y + yOffset + 25))
        slot8.entries(display.inputEntries[7])
        widgets.add(slot8)

        val slot9: Slot = Widgets.createSlot(Point(bounds.x + xOffset + 20, bounds.y + yOffset + 48))
        slot9.entries(display.inputEntries[8])
        widgets.add(slot9)

        val slot10: Slot = Widgets.createSlot(Point(bounds.x + xOffset + 43, bounds.y + yOffset + 48))
        slot10.entries(display.inputEntries[9])
        widgets.add(slot10)

        val slot11: Slot = Widgets.createSlot(Point(bounds.x + xOffset + 66, bounds.y + yOffset + 48))
        slot11.entries(display.inputEntries[10])
        widgets.add(slot11)

        val slot12: Slot = Widgets.createSlot(Point(bounds.x + xOffset, bounds.y + yOffset+50))
        slot12.entries(display.inputEntries[11])
        widgets.add(slot12)

        val slot13: Slot = Widgets.createSlot(Point(bounds.x + xOffset + 87, bounds.y + yOffset+50))
        slot13.entries(display.inputEntries[12])
        widgets.add(slot13)

        val arrow1 = Widgets.createArrow(Point(bounds.x + xOffset + 88, bounds.y + yOffset+25))
        widgets.add(arrow1)

        val outputSlotBackground =
            Widgets.createResultSlotBackground(Point(bounds.x + xOffset + 116, bounds.y + yOffset + 21))
        widgets.add(outputSlotBackground)

        val outputSlot =
            Widgets.createSlot(Point(bounds.x + xOffset + 116, bounds.y + yOffset + 21))
        outputSlot.entries(display.outputEntries[0])
        widgets.add(outputSlot)

        return widgets
    }

    override fun getDisplayHeight(): Int {
        return 78
    }

    override fun getDisplayWidth(display: ImbuingTableDisplay): Int {
        return 152
    }


}