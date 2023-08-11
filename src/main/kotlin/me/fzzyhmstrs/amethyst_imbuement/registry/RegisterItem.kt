@file:Suppress("unused")

package me.fzzyhmstrs.amethyst_imbuement.registry

import me.fzzyhmstrs.amethyst_core.augments.ScepterAugment
import me.fzzyhmstrs.amethyst_core.registry.ModifierRegistry
import me.fzzyhmstrs.amethyst_core.registry.RegisterAttribute
import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.amethyst_imbuement.config.AiConfig
import me.fzzyhmstrs.amethyst_imbuement.item.*
import me.fzzyhmstrs.amethyst_imbuement.item.AiItemSettings.AiItemGroup
import me.fzzyhmstrs.amethyst_imbuement.item.SpellScrollItem.Companion.createSpellScroll
import me.fzzyhmstrs.amethyst_imbuement.item.custom.*
import me.fzzyhmstrs.amethyst_imbuement.item.promise.*
import me.fzzyhmstrs.amethyst_imbuement.item.scepter.*
import me.fzzyhmstrs.amethyst_imbuement.spells.DebugAugment
import me.fzzyhmstrs.fzzy_core.item_util.CustomFlavorItem
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.*
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.Rarity
import java.util.*

// don't know if this is better as a class or object. as an object it allows me to call it without needing to initialize an instance of it.
object RegisterItem {

    private val regItem: MutableList<Item> = mutableListOf()

    private fun <T: Item> register(item: T, name: String): T{
        if (item is IgnitedGemItem){
            GemOfPromiseItem.register(item)
        }
        regItem.add(item)
        return Registry.register(Registries.ITEM,AI.identity(name), item)
    }

    
    //basic ingredients
    val CITRINE = register(Item(AiItemSettings().aiGroup(AiItemGroup.GEM)),"citrine")
    val SMOKY_QUARTZ = register(Item(AiItemSettings().aiGroup(AiItemGroup.GEM)),"smoky_quartz")
    val IMBUED_QUARTZ = register(Item(AiItemSettings().aiGroup(AiItemGroup.GEM)),"imbued_quartz")
    val IMBUED_LAPIS = register(Item(AiItemSettings().aiGroup(AiItemGroup.GEM)),"imbued_lapis")
    val DANBURITE = register(Item(AiItemSettings().aiGroup(AiItemGroup.GEM)),"danburite")
    val MOONSTONE = register(Item(AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.UNCOMMON)),"moonstone")
    val OPAL = register(Item(AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.UNCOMMON)),"opal")
    val GARNET = register(Item(AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.UNCOMMON)),"garnet")
    val PYRITE = register(Item(AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.UNCOMMON)),"pyrite")
    val TIGERS_EYE = register(SpellcastersReagentFlavorItem(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,
        EntityAttributeModifier(UUID.fromString("64399f14-d25b-11ed-afa1-0242ac120002"),"tigers_eye_modifier",0.15,EntityAttributeModifier.Operation.ADDITION),
        AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.RARE)),"tigers_eye")
    val CHARGED_MOONSTONE = register(Item(AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.RARE)),"charged_moonstone")
    val ENERGETIC_OPAL = register(SpellcastersReagentFlavorItem(EntityAttributes.GENERIC_MOVEMENT_SPEED,
        EntityAttributeModifier(UUID.fromString("1ac772d4-d25b-11ed-afa1-0242ac120002"),"energetic_modifier",0.03,EntityAttributeModifier.Operation.MULTIPLY_TOTAL),
        AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.RARE)).withGlint(),"energetic_opal")
    val AMETRINE = register(CustomFlavorItem(AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.RARE)),"ametrine") // item is custom for flavor text
    val CELESTINE = register(CustomFlavorItem(AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.EPIC)).withGlint(),"celestine") // item is custom for flavor text. need texture
    val STEEL_INGOT = register(Item(AiItemSettings().aiGroup(AiItemGroup.GEM)),"steel_ingot")
    val BERYL_COPPER_INGOT = register(Item(AiItemSettings().aiGroup(AiItemGroup.GEM)),"beryl_copper_ingot")


    //scepter update gem and found/crafted items
    val GEM_OF_PROMISE = register(GemOfPromiseItem(AiItemSettings().aiGroup(AiItemGroup.GEM).maxCount(1))
        .withFlavorDefaultPath(AI.identity("gem_of_promise"))
        .withFlavorDescDefaultPath(AI.identity("gem_of_promise")),"gem_of_promise")
    val GEM_DUST = register(Item(AiItemSettings().aiGroup(AiItemGroup.GEM)),"gem_dust")
    val SPARKING_GEM = register(SparkingGemItem(AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.UNCOMMON)),"sparking_gem")
    val BLAZING_GEM = register(BlazingGemItem(AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.UNCOMMON)),"blazing_gem")
    val INQUISITIVE_GEM = register(InquisitiveGemItem(AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.UNCOMMON)),"inquisitive_gem")
    val LETHAL_GEM = register(LethalGemItem(AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.UNCOMMON)),"lethal_gem")
    val HEALERS_GEM = register(HealersGemItem(AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.UNCOMMON)),"healers_gem")
    val BRUTAL_GEM = register(BrutalGemItem(AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.UNCOMMON)),"brutal_gem")
    val MYSTICAL_GEM = register(MysticalGemItem(AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.UNCOMMON)),"mystical_gem")
    val GLOWING_FRAGMENT = register(SpellcastersReagentFlavorItem(RegisterAttribute.SPELL_MANA_COST,
        EntityAttributeModifier(UUID.fromString("38ea2c82-ce89-11ed-afa1-0242ac120002"),"glowing_modifier",-0.02,EntityAttributeModifier.Operation.MULTIPLY_TOTAL),
        AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.RARE)),"glowing_fragment")
    val BRILLIANT_DIAMOND = register(SpellcastersReagentFlavorItem(RegisterAttribute.SPELL_MANA_COST,
        EntityAttributeModifier(UUID.fromString("402ea570-c404-11ed-afa1-0242ac120002"),"brilliant_modifier",-0.06,EntityAttributeModifier.Operation.MULTIPLY_TOTAL),
        AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.EPIC)).withGlint(),"brilliant_diamond")
    val ACCURSED_FIGURINE = register(SpellcastersReagentFlavorItem(RegisterAttribute.DAMAGE_MULTIPLICATION,
        EntityAttributeModifier(UUID.fromString("57ac057e-c505-11ed-afa1-0242ac120002"),"accursed_modifier",0.1,EntityAttributeModifier.Operation.MULTIPLY_TOTAL),
        AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.RARE)),"accursed_figurine")
    val MALACHITE_FIGURINE = register(SpellcastersReagentFlavorItem(RegisterAttribute.SPELL_DURATION,
        EntityAttributeModifier(UUID.fromString("402ebf88-c404-11ed-afa1-0242ac120002"),"malachite_modifier",0.05,EntityAttributeModifier.Operation.MULTIPLY_TOTAL),
        AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.RARE)),"malachite_figurine")
    val SARDONYX_FIGURINE = register(SpellcastersReagentFlavorItem(RegisterAttribute.SPELL_DAMAGE,
        EntityAttributeModifier(UUID.fromString("ea73a9c8-3159-11ee-be56-0242ac120002"),"sardonyx_modifier",0.075,EntityAttributeModifier.Operation.MULTIPLY_TOTAL),
        AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.EPIC)),"sardonyx_figurine")
    val RESONANT_ROD = register(SpellcastersReagentFlavorItem(RegisterAttribute.SPELL_DAMAGE,
        EntityAttributeModifier(UUID.fromString("402ec2da-c404-11ed-afa1-0242ac120002"),"resonant_modifier",0.03,EntityAttributeModifier.Operation.MULTIPLY_TOTAL),
        AiItemSettings().aiGroup(AiItemGroup.GEM)),"resonant_rod")
    //val SURVEY_MAP = SurveyMapItem(FabricItemSettings()),"survey_map")
    val HEARTSTONE = register(SpellcastersReagentFlavorItem(RegisterAttribute.SPELL_AMPLIFIER,
        EntityAttributeModifier(UUID.fromString("402ec528-c404-11ed-afa1-0242ac120002"),"heartstone_modifier",1.0,EntityAttributeModifier.Operation.ADDITION),
        AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.UNCOMMON)).withGlint(),"heartstone")
    val IRIDESCENT_ORB = register(CustomFlavorItem(FabricItemSettings().rarity(Rarity.UNCOMMON)),"iridescent_orb")
    val LUSTROUS_SPHERE = register(SpellcastersReagentFlavorItem(RegisterAttribute.SPELL_LEVEL,
        EntityAttributeModifier(UUID.fromString("402ec79e-c404-11ed-afa1-0242ac120002"),"lustrous_modifier",0.05,EntityAttributeModifier.Operation.MULTIPLY_TOTAL),
        FabricItemSettings().rarity(Rarity.RARE)).withGlint(),"lustrous_sphere")
    val KNOWLEDGE_POWDER = register(SpellcastersReagentFlavorItem(RegisterAttribute.PLAYER_EXPERIENCE,
        EntityAttributeModifier(UUID.fromString("72321934-ccc0-11ed-afa1-0242ac120002"),"knowledge_modifier",0.05,EntityAttributeModifier.Operation.ADDITION),
        AiItemSettings().aiGroup(AiItemGroup.GEM)),"knowledge_powder")
    val XP_BUSH_SEED = register(AliasedBlockItem(RegisterBlock.EXPERIENCE_BUSH,FabricItemSettings()),"xp_bush_seed")
    val GOLDEN_HEART = register(SpellcastersReagentFlavorItem(RegisterAttribute.SPELL_RANGE,
        EntityAttributeModifier(UUID.fromString("f62a18b6-c407-11ed-afa1-0242ac120002"),"golden_modifier",0.1,EntityAttributeModifier.Operation.MULTIPLY_TOTAL),
        AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.UNCOMMON)),"golden_heart")
    val CRYSTALLINE_HEART = register(CustomFlavorItem(AiItemSettings().aiGroup(AiItemGroup.GEM).rarity(Rarity.RARE)).withGlint(),"crystalline_heart") //item is custom for flavor text


    //tool and weapon items
    val FZZYHAMMER = register(FzzyhammerItem(FabricItemSettings().rarity(Rarity.EPIC)),"fzzyhammer")
    val GLISTERING_TRIDENT = register(GlisteringTridentItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxDamage(550).rarity(Rarity.RARE)),"glistering_trident")
    val SNIPER_BOW = register(SniperBowItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxDamage(500).rarity(Rarity.RARE)),"sniper_bow")
    val GARNET_SWORD = register(SwordItem(AiConfig.materials.tools.garnet,3 ,-2.4f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"garnet_sword")
    val GARNET_SHOVEL = register(ShovelItem(AiConfig.materials.tools.garnet,1.5f,-3.0f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"garnet_shovel")
    val GARNET_PICKAXE = register(PickaxeItem(AiConfig.materials.tools.garnet,1,-2.8f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"garnet_pickaxe")
    val GARNET_AXE = register(AxeItem(AiConfig.materials.tools.garnet,5.0f,-3.0f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"garnet_axe")
    val GARNET_HOE = register(HoeItem(AiConfig.materials.tools.garnet,-3,0.0f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"garnet_hoe")
    val GARNET_HORSE_ARMOR = register(HorseArmorItem(12,"garnet",AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"garnet_horse_armor")
    val GLOWING_BLADE = register(CustomSwordItem(AiConfig.materials.tools.glowing,3 ,-2.4f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"glowing_blade")
    val GLOWING_SPADE = register(CustomShovelItem(AiConfig.materials.tools.glowing,1.5f ,-3.0f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"glowing_spade")
    val GLOWING_PICK = register(CustomPickaxeItem(AiConfig.materials.tools.glowing,1 ,-2.8f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"glowing_pick")
    val GLOWING_AXE = register(CustomAxeItem(AiConfig.materials.tools.glowing,5.0f ,-3.0f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"glowing_axe")
    val GLOWING_HOE = register(CustomHoeItem(AiConfig.materials.tools.glowing,-3 ,-3.0f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"glowing_hoe")
    val GLOWING_HORSE_ARMOR = register(FlavorHorseArmorItem(14,"glowing",AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"glowing_horse_armor")
    val STEEL_AXE = register(AxeItem(AiConfig.materials.tools.steel,5.0f,-3.0f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"steel_axe")
    val STEEL_HOE = register(CustomHoeItem(AiConfig.materials.tools.steel,-3,-3.0f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"steel_hoe")
    val STEEL_PICKAXE = register(PickaxeItem(AiConfig.materials.tools.steel,1,-2.8f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"steel_pickaxe")
    val STEEL_SHOVEL = register(ShovelItem(AiConfig.materials.tools.steel,1.5f,-3.0f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"steel_shovel")
    val STEEL_SWORD = register(SwordItem(AiConfig.materials.tools.steel,3,-2.4f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"steel_sword")
    val STEEL_HORSE_ARMOR = register(HorseArmorItem(9,"steel",AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"steel_horse_armor")
    val AMETRINE_AXE = register(AxeItem(AiConfig.materials.tools.ametrine,5.0f,-3.0f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"ametrine_axe")
    val AMETRINE_HOE = register(CustomHoeItem(AiConfig.materials.tools.ametrine,-3,-3.0f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"ametrine_hoe")
    val AMETRINE_PICKAXE = register(PickaxeItem(AiConfig.materials.tools.ametrine,1,-2.8f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"ametrine_pickaxe")
    val AMETRINE_SHOVEL = register(ShovelItem(AiConfig.materials.tools.ametrine,1.5f,-3.0f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"ametrine_shovel")
    val AMETRINE_SWORD = register(SwordItem(AiConfig.materials.tools.ametrine,3,-2.4f,AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"ametrine_sword")
    val AMETRINE_HORSE_ARMOR = register(HorseArmorItem(15,"ametrine",AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"ametrine_horse_armor")

    //trinket and books
    val COPPER_RING = register(CopperJewelryItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxCount(1)),"copper_ring")
    val COPPER_HEADBAND = register(CopperJewelryItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxCount(1)),"copper_headband")
    val COPPER_AMULET = register(CopperJewelryItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxCount(1)),"copper_amulet")
    val COPPER_WARD = register(CopperWardItem(RegisterAttribute.SHIELDING,
        EntityAttributeModifier(UUID.fromString("c66fd31a-ce6e-11ed-afa1-0242ac120002"),"ward_modifier",0.025,EntityAttributeModifier.Operation.ADDITION),
        AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxDamage(336)),"copper_ward")
    val STEEL_AMULET = register(SteelJewelryItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxCount(1)),"steel_amulet")
    val STEEL_HEADBAND = register(SteelJewelryItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxCount(1)),"steel_headband")
    val STEEL_RING = register(SteelJewelryItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxCount(1)),"steel_ring")
    val STEEL_WARD = register(SteelWardItem(EntityAttributes.GENERIC_ARMOR,
        EntityAttributeModifier(UUID.fromString("1f6875e4-d167-11ed-afa1-0242ac120002"),"steel_ward_modifier",1.2, EntityAttributeModifier.Operation.ADDITION),
        AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxDamage(424)),"steel_ward")
    val IMBUED_RING = register(ImbuedJewelryItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxDamage(AiConfig.items.manaItems.imbuedJewelryDurability.get())),"imbued_ring")
    val IMBUED_HEADBAND = register(ImbuedJewelryItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxDamage(AiConfig.items.manaItems.imbuedJewelryDurability.get())),"imbued_headband")
    val IMBUED_AMULET = register(ImbuedJewelryItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxDamage(AiConfig.items.manaItems.imbuedJewelryDurability.get())),"imbued_amulet")
    val IMBUED_WARD = register(ImbuedWardItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxDamage(AiConfig.items.manaItems.totemOfAmethystDurability.get())),"imbued_ward")
    val TOTEM_OF_AMETHYST = register(TotemItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxDamage(AiConfig.items.manaItems.totemOfAmethystDurability.get()).rarity(Rarity.UNCOMMON)),"totem_of_amethyst")
    val SPELLCASTERS_FOCUS = register(SpellcastersFocusItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).rarity(Rarity.UNCOMMON)),"spellcasters_focus")
    val WITCHES_ORB = register(WitchesOrbItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT).maxCount(1).rarity(Rarity.RARE)).withGlint(),"witches_orb")
    val BOOK_OF_LORE = register(BookOfLoreItem(FabricItemSettings().maxCount(8)).withFlavorDefaultPath(AI.identity("book_of_lore")),"book_of_lore")
    val BOOK_OF_MYTHOS = register(BookOfMythosItem(FabricItemSettings().maxCount(8).rarity(Rarity.RARE)).withFlavorDefaultPath(AI.identity("book_of_mythos")).withGlint(),"book_of_mythos")
    //val GLISTERING_TOME = GlisteringTomeItem(FabricItemSettings()),"glistering_tome")
    val MANA_POTION = register(ManaPotionItem(FabricItemSettings().maxCount(16)),"mana_potion")
    val DAZZLING_MELON_SLICE = register(Item(FabricItemSettings().rarity(Rarity.UNCOMMON).food(FoodComponent.Builder().hunger(4).saturationModifier(0.75f).statusEffect(
        StatusEffectInstance(RegisterStatus.BLESSED, 300),1f).build())),"dazzling_melon_slice")


    //Basic scepters
    val OPALINE_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier1Scepter, AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.COMMON)),"opaline_scepter")
    val IRIDESCENT_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier2Scepter, AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.UNCOMMON)),"iridescent_scepter")
    val LUSTROUS_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier3Scepter, AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE)),"lustrous_scepter")
    val DEBUG_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier3Scepter, AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.EPIC))
        .withAugments(listOf(RegisterEnchantment.DEBUG))
        .withModifiers(listOf(ModifierRegistry.MODIFIER_DEBUG,ModifierRegistry.MODIFIER_DEBUG_2,ModifierRegistry.MODIFIER_DEBUG_3)),"debug_scepter")
        
    // scepter upgrade scepters
    val FURIOUS_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier1Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.UNCOMMON))
        .withModifiers(listOf(RegisterModifier.FURIOUS))
        ,"furious_scepter")
    val WITTY_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier1Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.UNCOMMON))
        .withModifiers(listOf(RegisterModifier.WITTY))
        ,"witty_scepter")
    val GRACEFUL_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier1Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.UNCOMMON))
        .withModifiers(listOf(RegisterModifier.GRACEFUL))
        ,"graceful_scepter")
    val DANGEROUS_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier1Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.UNCOMMON))
        .withModifiers(listOf(RegisterModifier.DANGEROUS, RegisterModifier.DANGEROUS_PACT))
        ,"dangerous_scepter")
    val SKILLFUL_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier1Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.UNCOMMON))
        .withModifiers(listOf(RegisterModifier.SKILLFUL))
        ,"skillful_scepter")
    val ENDURING_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier1Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.UNCOMMON))
        .withModifiers(listOf(ModifierRegistry.LESSER_ENDURING))
        ,"enduring_scepter")
    val BLAZING_SCEPTER = register(ParticleScepterItem(ParticleTypes.SMOKE,10,AiConfig.materials.scepters.tier2Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.FIREBALL))
        .withModifiers(listOf(RegisterModifier.FIRE_ASPECT))
        ,"blazing_scepter")
    val SPARKING_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier2Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.LIGHTNING_BOLT))
        .withModifiers(listOf(RegisterModifier.LIGHTNING_ASPECT))
        ,"sparking_scepter")
    val FROSTED_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier2Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.ICE_SPIKES))
        .withModifiers(listOf(RegisterModifier.ICE_ASPECT))
        ,"frosted_scepter")
    val SCEPTER_OF_BLADES = register(SpellbladeItem(AiConfig.materials.scepters.blades,3,-2.7f,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withModifiers(listOf(RegisterModifier.BLADE_ASPECT))
        ,"scepter_of_blades")
    val CORRUPTED_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier2Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.SOUL_MISSILE))
        .withModifiers(listOf(RegisterModifier.NECROTIC))
        ,"corrupted_scepter")
    val SCEPTER_OF_AGONIES = register(SpellbladeItem(AiConfig.materials.scepters.tier2Scepter,3,-3f,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.CRIPPLE))
        .withModifiers(listOf(ModifierRegistry.ENDURING, ModifierRegistry.LESSER_REACH))
        ,"scepter_of_agonies")  
    val SCEPTER_OF_INSIGHT = register(ScepterItem(AiConfig.materials.scepters.tier2Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.MENTAL_CLARITY))
        .withModifiers(listOf(RegisterModifier.INSIGHTFUL, ModifierRegistry.LESSER_REACH))
        ,"scepter_of_insight")
    val SCEPTER_OF_SUMMONING = register(ScepterItem(AiConfig.materials.scepters.tier2Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.SUMMON_ZOMBIE))
        .withModifiers(listOf(RegisterModifier.SUMMONERS_ASPECT))
        ,"scepter_of_summoning")
    val PERSUASIVE_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier2Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.PERSUADE))
        .withModifiers(listOf(RegisterModifier.WITTY, ModifierRegistry.LESSER_ATTUNED))
        ,"persuasive_scepter")
    val TRAVELERS_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier2Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.SUMMON_SEAHORSE))
        .withModifiers(listOf(RegisterModifier.TRAVELER))
        ,"travelers_scepter")
    val SCEPTER_OF_RECALL = register(ParticleScepterItem(ParticleTypes.PORTAL,10,AiConfig.materials.scepters.tier2Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.RECALL))
        .withModifiers(listOf(ModifierRegistry.GREATER_REACH))
        ,"scepter_of_recall")
    val BUILDERS_SCEPTER = register(BuilderScepterItem(AiConfig.materials.scepters.builder,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.HARD_LIGHT_BRIDGE))
        .withModifiers(listOf(RegisterModifier.BUILDERS_ASPECT, ModifierRegistry.LESSER_REACH))
        ,"builders_scepter")
    val SCEPTER_OF_THE_VANGUARD = register(SpellbladeItem(AiConfig.materials.scepters.vanguard,3,-3f,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.BARRIER))
        .withModifiers(listOf(RegisterModifier.SMITING, RegisterModifier.GRACEFUL))
        ,"scepter_of_the_vanguard")
    val SCEPTER_OF_THE_PALADIN = register(ScepterItem(AiConfig.materials.scepters.tier2Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.FORTIFY))
        .withModifiers(listOf(RegisterModifier.PROTECTIVE, ModifierRegistry.LESSER_ENDURING))
        ,"scepter_of_the_paladin")
    val SCEPTER_OF_HARVESTS = register(ScepterOfHarvestsItem(AiConfig.materials.scepters.harvests,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.ANIMAL_HUSBANDRY))
        .withModifiers(listOf(ModifierRegistry.REACH, ModifierRegistry.LESSER_THRIFTY))
        ,"scepter_of_harvests")
    val SCEPTER_OF_THE_PACIFIST = register(ScepterItem(AiConfig.materials.scepters.tier2Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.BEDAZZLE))
        .withModifiers(listOf(RegisterModifier.HEALERS_GRACE, RegisterModifier.HEALERS_PACT))
        ,"scepter_of_the_pacifist")
    val CLERICS_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier2Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.MINOR_HEAL))
        .withModifiers(listOf(RegisterModifier.HEALING))
        ,"clerics_scepter")
    val BARDIC_SCEPTER = register(ScepterItem(AiConfig.materials.scepters.tier2Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.RARE))
        .withAugments(listOf(RegisterEnchantment.INSPIRING_SONG))
        .withModifiers(listOf(ModifierRegistry.LESSER_ENDURING, ModifierRegistry.ATTUNED))
        ,"bardic_scepter")
    val EQUINOX = register(EquinoxScepterItem(AiConfig.materials.scepters.tier3Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.EPIC))
        .withModifiers(listOf(RegisterModifier.ELEMENTAL, RegisterModifier.FURIOUS))
        ,"equinox")
    val LETHALITY = register(LethalityScepterItem(AiConfig.materials.scepters.lethality,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.EPIC))
        .withModifiers(listOf(RegisterModifier.DANGEROUS,RegisterModifier.DANGEROUS_PACT))
        ,"lethality")
    val RESONANCE = register(ScepterItem(AiConfig.materials.scepters.tier3Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.EPIC))
        .withAugments(listOf(RegisterEnchantment.RESONATE))
        .withModifiers(listOf(RegisterModifier.ECHOING,RegisterModifier.SKILLFUL))
        ,"resonance")
    val SOJOURN = register(SojournScepterItem(AiConfig.materials.scepters.tier3Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.EPIC))
        .withAugments(listOf(RegisterEnchantment.SURVEY))
        .withModifiers(listOf(RegisterModifier.WITTY,RegisterModifier.TRAVELER))
        ,"sojourn")
    val AEGIS = register(SpellbladeItem(AiConfig.materials.scepters.tier3Scepter,5,-3f,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.EPIC))
        .withModifiers(listOf(RegisterModifier.PROTECTIVE, RegisterModifier.SMITING))
        ,"aegis")
    val REDEMPTION = register(ScepterItem(AiConfig.materials.scepters.tier3Scepter,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.EPIC))
        .withModifiers(listOf(RegisterModifier.HEALERS_PACT,RegisterModifier.HEALERS_GRACE, ModifierRegistry.LESSER_ENDURING))
        ,"redemption")

    val A_SCEPTER_SO_FOWL = register(ScepterItem(AiConfig.materials.scepters.fowl,AiItemSettings().aiGroup(AiItemGroup.SCEPTER).rarity(Rarity.EPIC))
        .withAugments(listOf(RegisterEnchantment.TORRENT_OF_BEAKS,RegisterEnchantment.CHICKENFORM,RegisterEnchantment.POULTRYMORPH))
        .withModifiers(listOf(RegisterModifier.FOWL))
        ,"a_scepter_so_fowl")


    // Spell scrolls
    val EMPTY_SPELL_SCROLL = register(CustomFlavorItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"empty_spell_scroll")
    val SPELL_SCROLL = register(SpellScrollItem(AiItemSettings().aiGroup(AiItemGroup.EQUIPMENT)),"spell_scroll")

    ///////////////////////////

    val AI_GROUP: ItemGroup by lazy{
        registerItemGroup()
    }

    fun registerItemGroup(): ItemGroup{
        return Registry.register(Registries.ITEM_GROUP,AI.identity("ai_group"), FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup.amethyst_imbuement.ai_group"))
            .icon { ItemStack(RegisterBlock.IMBUING_TABLE.asItem()) }
            .entries { _, entries ->
                entries.addAll(regItem.stream()
                    .filter { item -> item !== SPELL_SCROLL }
                    .map { item -> ItemStack(item) }.toList())
                entries.addAll(Registries.ENCHANTMENT.stream()
                    .filter { enchant -> enchant is ScepterAugment && enchant !is DebugAugment }
                    .map { enchant ->  createSpellScroll(enchant as ScepterAugment)}
                    .toList()
                )
                entries.addAll(RegisterArmor.regArmor.stream().map { item -> ItemStack(item) }.toList())
                RegisterBlock.regBlockItem.stream()
                    .map { block -> ItemStack(block.asItem()) }
                    .forEach {
                        entries.add(it)
                    }

            }.build())
    }

    fun registerAll() {
        val group = AI_GROUP
    }
}
