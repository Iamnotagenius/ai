package me.fzzyhmstrs.amethyst_imbuement.config

import me.fzzyhmstrs.amethyst_core.event.ShouldScrollEvent
import me.fzzyhmstrs.fzzy_config.annotations.NonSync
import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import me.fzzyhmstrs.fzzy_config.api.RegisterType


object AiConfig {

    val items: ItemsConfig = ConfigApi.registerAndLoadConfig({ ItemsConfig() })
    val materials: MaterialsConfig = ConfigApi.registerAndLoadConfig({ MaterialsConfig() })
    val blocks: BlocksConfig = ConfigApi.registerAndLoadConfig({ BlocksConfig() })
    var villages: VillagesConfig = ConfigApi.registerAndLoadConfig({ VillagesConfig() })
    var enchants: EnchantmentsConfig = ConfigApi.registerAndLoadConfig({ EnchantmentsConfig() })
    var trinkets: AugmentsConfig = ConfigApi.registerAndLoadConfig({ AugmentsConfig() })
    var entities: EntitiesConfig = ConfigApi.registerAndLoadConfig({ EntitiesConfig() })
    var resources: ResourcesConfig = ConfigApi.registerAndLoadConfig({ ResourcesConfig() })
    @NonSync
    var hud: HudConfig = ConfigApi.registerAndLoadConfig({ HudConfig() }, RegisterType.CLIENT)

    fun registerAll(){}

    fun registerClient(){
        ShouldScrollEvent.EVENT.register{_, _ ->
            hud.scrollChangesSpells.get()
        }
    }

}