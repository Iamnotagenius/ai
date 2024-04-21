package me.fzzyhmstrs.amethyst_imbuement.config

import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom
import me.fzzyhmstrs.fzzy_config.annotations.RequiresRestart
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.config.ConfigSection
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt

@ConvertFrom("villages_v3.json", AI.MOD_ID)
class VillagesConfig: Config(AI.identity("")){
    var vanilla = Vanilla()
    class Vanilla: ConfigSection(){
        @RequiresRestart
        var enableDesertWorkshops = ValidatedBoolean(true)
        var desertWorkshopWeight = ValidatedInt(2,150,1)
        @RequiresRestart
        var enablePlainsWorkshops = ValidatedBoolean(true)
        var plainsWorkshopWeight = ValidatedInt(3,150,1)
        @RequiresRestart
        var enableSavannaWorkshops = ValidatedBoolean(true)
        var savannaWorkshopWeight = ValidatedInt(3,150,1)
        @RequiresRestart
        var enableSnowyWorkshops = ValidatedBoolean(true)
        var snowyWorkshopWeight = ValidatedInt(2,150,1)
        @RequiresRestart
        var enableTaigaWorkshops = ValidatedBoolean(true)
        var taigaWorkshopWeight = ValidatedInt(3,150,1)
    }

    var ctov = Ctov()
    class Ctov: ConfigSection(){
        @RequiresRestart
        var enableCtovWorkshops = ValidatedBoolean(true)
        var beachWorkshopWeight = ValidatedInt(4,150,1)
        var darkForestWorkshopWeight = ValidatedInt(4,150,1)
        var jungleWorkshopWeight = ValidatedInt(4,150,1)
        var jungleTreeWorkshopWeight = ValidatedInt(4,150,1)
        var mesaWorkshopWeight = ValidatedInt(4,150,1)
        var mesaFortifiedWorkshopWeight = ValidatedInt(4,150,1)
        var mountainWorkshopWeight = ValidatedInt(4,150,1)
        var mountainAlpineWorkshopWeight = ValidatedInt(4,150,1)
        var mushroomWorkshopWeight = ValidatedInt(4,150,1)
        var swampWorkshopWeight = ValidatedInt(4,150,1)
        var swampFortifiedWorkshopWeight = ValidatedInt(4,150,1)
    }

    var rs = Rs()
    class Rs: ConfigSection(){
        @RequiresRestart
        var enableRsWorkshops = ValidatedBoolean(true)
        var badlandsWorkshopWeight = ValidatedInt(2,150,1)
        var birchWorkshopWeight = ValidatedInt(2,150,1)
        var darkForestWorkshopWeight = ValidatedInt(2,150,1)
        var giantTaigaWorkshopWeight = ValidatedInt(1,150,1)
        var jungleWorkshopWeight = ValidatedInt(2,150,1)
        var mountainsWorkshopWeight = ValidatedInt(2,150,1)
        var mushroomsWorkshopWeight = ValidatedInt(2,150,1)
        var oakWorkshopWeight = ValidatedInt(2,150,1)
        var swampWorkshopWeight = ValidatedInt(2,150,1)
        var crimsonWorkshopWeight = ValidatedInt(2,150,1)
        var warpedWorkshopWeight = ValidatedInt(2,150,1)
    }

    var sky = Sky()
    class Sky: ConfigSection(){
        @RequiresRestart
        var enableSkyWorkshops = ValidatedBoolean(true)
        var skyWorkshopWeight = ValidatedInt(40,150,1)
    }
}