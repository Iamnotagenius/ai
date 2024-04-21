package me.fzzyhmstrs.amethyst_imbuement.config

import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.config.ConfigSection
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt
import net.minecraft.enchantment.Enchantment

@ConvertFrom("blocks_v0.json",AI.MOD_ID)
class BlocksConfig: Config(AI.identity("blocks_config")){

    fun isCreateBlockTemporary(): Boolean{
        return hardLight.createTemporary.get() && hardLight.temporaryDuration.get() > 0
    }

    fun isBridgeBlockTemporary(): Boolean{
        return hardLight.bridgeTemporary.get() && hardLight.temporaryDuration.get() > 0
    }

    var hardLight = HardLight()
    class HardLight: ConfigSection(){
        //@ReadMeText("readme.altars.hardLight.bridgeTemporary")
        var bridgeTemporary = ValidatedBoolean(false)
        //@ReadMeText("readme.altars.hardLight.createTemporary")
        var createTemporary = ValidatedBoolean(false)
        //@ReadMeText("readme.altars.hardLight.temporaryDuration")
        var temporaryDuration = ValidatedInt(600,Int.MAX_VALUE)
    }

    var xpBush = XpBush()
    class XpBush: ConfigSection(){
        var bonemealChance = ValidatedFloat(0.4f,1f,0f)
        var growChance = ValidatedFloat(0.15f,1f,0f)
    }

    var disenchanter = Disenchanter()
    class Disenchanter: ConfigSection(){

        fun ignoreEnchantment(enchantment: Enchantment): Boolean{
            return if (ignoreCurses.get()) enchantment.isCursed else false
        }

        //@ReadMeText("readme.altars.disenchanter.levelCosts")
        var levelCosts = ValidatedList(listOf(11, 17, 24, 33, 44), ValidatedInt(0, Int.MAX_VALUE,0))
        var baseDisenchantsAllowed = ValidatedInt(1,Int.MAX_VALUE,0)
        var ignoreCurses = ValidatedBoolean(false)
    }

    var imbuing = Imbuing()
    class Imbuing: ConfigSection(){

        fun getRerollEnabled(): Boolean{
            return easyMagic.matchEasyMagicBehavior.get() && easyMagic.rerollEnabled.get()

        }

        fun getLapisCost(): Int{
            if(getRerollEnabled()){
                return easyMagic.lapisCost.get()
            }
            return 0
        }

        fun getLevelCost(): Int{
            if(getRerollEnabled()){
                return easyMagic.levelCost.get()
            }
            return 0
        }

        var enchantingEnabled = ValidatedBoolean(true)
        var replaceEnchantingTable = ValidatedBoolean(false)
        //@ReadMeText("readme.altars.imbuing.difficultyModifier")
        var difficultyModifier = ValidatedFloat(1.0F,10f,0f)

        var easyMagic = EasyMagic()
        class EasyMagic: ConfigSection(){
            var matchEasyMagicBehavior = ValidatedBoolean(true)
            var rerollEnabled = ValidatedBoolean(true)
            var levelCost = ValidatedInt(5,Int.MAX_VALUE,0)
            var lapisCost = ValidatedInt(1,Int.MAX_VALUE,0)
            var showTooltip = ValidatedBoolean(true)
            var singleEnchantTooltip = ValidatedBoolean(true)
        }

        @Deprecated("Don't need this, as I'm not mod-checking any more. Can remove next config update")
        var reroll = Reroll()
        class Reroll: ConfigSection(){
            var matchRerollBehavior = ValidatedBoolean(true)
            var levelCost = ValidatedInt(1,Int.MAX_VALUE,0)
            var lapisCost = ValidatedInt(0,Int.MAX_VALUE,0)
        }
    }

    var altar = Altar()
    class Altar: ConfigSection(){
        var baseLevels = ValidatedInt(35,Int.MAX_VALUE,0)
        var candleLevelsPer = ValidatedInt(5,Int.MAX_VALUE/16,0)
        //@ReadMeText("readme.altars.altar.customXpMethod")
        var customXpMethod = ValidatedBoolean(true)
    }
}