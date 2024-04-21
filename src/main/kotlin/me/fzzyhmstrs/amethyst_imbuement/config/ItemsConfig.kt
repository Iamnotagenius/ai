package me.fzzyhmstrs.amethyst_imbuement.config

import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom
import me.fzzyhmstrs.fzzy_config.annotations.NonSync
import me.fzzyhmstrs.fzzy_config.annotations.RequiresRestart
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.config.ConfigSection
import me.fzzyhmstrs.fzzy_config.util.Walkable
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt
import net.minecraft.item.ItemStack
import net.minecraft.util.math.ColorHelper
import kotlin.math.max

@ConvertFrom("items_v6.json",AI.MOD_ID)
class ItemsConfig: Config(AI.identity("items_config")) {

    var giveGlisteringTome = ValidatedBoolean(true)

    @RequiresRestart
    var sniperBowDurability = ValidatedInt(500,5000,0)

    var manaItems = ManaItems()
    class ManaItems: ConfigSection() {
        @RequiresRestart
        var totemOfAmethystDurability = ValidatedInt(360, 1000, 32)
        @RequiresRestart
        var imbuedJewelryDurability = ValidatedInt(120, 1000, 32)
        var imbuedJewelryDamagePerAmplifier = ValidatedInt(6,30,0)
        @NonSync
        var fullManaColor = ValidatedColor(0,85,255)
        @NonSync
        var emptyManaColor = ValidatedColor(255,0,85)

        fun getItemBarColor(stack: ItemStack): Int {
            val f = max(0.0f, (stack.maxDamage.toFloat() - stack.damage.toFloat()) / stack.maxDamage.toFloat())
            val r = ((f * fullManaColor.get().r) + ((1-f)*emptyManaColor.get().r)).toInt()
            val g = ((f * fullManaColor.get().g) + ((1-f)*emptyManaColor.get().g)).toInt()
            val b = ((f * fullManaColor.get().b) + ((1-f)*emptyManaColor.get().b)).toInt()
            return ColorHelper.Argb.getArgb(255,r,g,b)
        }
    }

    var scepters = ScepterSection()
    class ScepterSection: ConfigSection(){
        var fowlChestChance = ValidatedFloat(0.02f,1f,0f)
        var fzzyChestChance = ValidatedFloat(0.002f,1f,0f)
        var uniqueWitherChance = ValidatedFloat(0.01f,1f,0f)
    }

    var gems = Gems()
    class Gems: ConfigSection(){
        var fireTarget = ValidatedInt(120,1200,1)
        var hitTarget = ValidatedInt(60,600,1)
        var healTarget = ValidatedFloat(120f,1200f,1f)
        var statusesTarget = ValidatedInt(8,42,1)
        var killTarget = ValidatedInt(30,300,1)
        var spellXpTarget = ValidatedInt(350,3500,1)
    }

    var focus = Focus()
    class Focus: ConfigSection() {
        var bolsteringRange = ValidatedDouble(5.0,50.0,1.0)
        var xpPerTier = XpPerTier()
        class XpPerTier: Walkable {
            var tierOne = ValidatedInt(500,1500,250)
            var tierTwo = ValidatedInt(1500,3000,500)
            var tierThree = ValidatedInt(3000,5000,1500)
            var tierFour = ValidatedInt(5000,15000,3000)
        }
    }

    var scroll = Scroll()
    class Scroll: ConfigSection() {
        var casts = Casts()
        class Casts: Walkable{
            var tierOne = ValidatedInt(16,32,1)
            var tierTwo = ValidatedInt(24,64,2)
            var tierThree = ValidatedInt(32,128,3)
        }
        var spellLevels = SpellLevels()
        class SpellLevels: Walkable{
            var tierOne = ValidatedInt(1,Int.MAX_VALUE,1)
            var tierTwo = ValidatedInt(2,Int.MAX_VALUE,1)
            var tierThree = ValidatedInt(3,Int.MAX_VALUE,1)
            var tierFour = ValidatedInt(5,Int.MAX_VALUE,1)
            var tierFive = ValidatedInt(7,Int.MAX_VALUE,1)
        }
    }
}