package me.fzzyhmstrs.amethyst_imbuement.registry

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback.LootTableSetter
import net.minecraft.block.Blocks
import net.minecraft.item.Items
import net.minecraft.loot.LootManager
import net.minecraft.loot.LootTables
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.*
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier


object RegisterLoot {

    //private val COAL_ORE_LOOT_TABLE_ID = Blocks.CHEST.lootTableId
    fun registerAll(){
        LootTableLoadingCallback.EVENT.register(LootTableLoadingCallback { _: ResourceManager?, _: LootManager?, id: Identifier?, table: FabricLootSupplierBuilder, _: LootTableSetter? ->
            if (Blocks.NETHER_QUARTZ_ORE.lootTableId.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(Items.AMETHYST_SHARD).weight(3))
                    .with(ItemEntry.builder(RegisterItem.CITRINE).weight(2))
                    .with(ItemEntry.builder(RegisterItem.SMOKY_QUARTZ).weight(2))
                    .with(ItemEntry.builder(Items.AIR).weight(113))
                table.pool(poolBuilder)
            } else if (LootTables.VILLAGE_ARMORER_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(UniformLootNumberProvider.create(1.0F,2.0F))
                    .with(ItemEntry.builder(RegisterItem.STEEL_INGOT).weight(35))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_HELMET).weight(10)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.75f)))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_CHESTPLATE).weight(10)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.75f)))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_LEGGINGS).weight(10)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.75f)))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_BOOTS).weight(10)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.75f)))
                    .with(ItemEntry.builder(Items.AIR).weight(75))
                table.pool(poolBuilder)
            } else if (LootTables.VILLAGE_FLETCHER_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.SNIPER_BOW).weight(5)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.75f)))
                    .with(ItemEntry.builder(Items.AIR).weight(50))
                table.pool(poolBuilder)
            } else if (LootTables.VILLAGE_TEMPLE_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.IMBUED_LAPIS).weight(10))
                    .with(ItemEntry.builder(RegisterItem.GLISTERING_TOME).weight(10))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_LORE).weight(5))
                    .with(ItemEntry.builder(Items.AIR).weight(75))
                table.pool(poolBuilder)
            } else if (LootTables.VILLAGE_WEAPONSMITH_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(UniformLootNumberProvider.create(1.0F,3.0F))
                    .with(ItemEntry.builder(RegisterItem.BERYL_COPPER_INGOT).weight(15))
                    .with(ItemEntry.builder(Items.AIR).weight(85))
                table.pool(poolBuilder)
                val poolBuilder2 = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.GARNET_SWORD).weight(1))
                    .with(ItemEntry.builder(RegisterItem.COPPER_RING).weight(5))
                    .with(ItemEntry.builder(RegisterItem.COPPER_AMULET).weight(5))
                    .with(ItemEntry.builder(RegisterItem.COPPER_HEADBAND).weight(5))
                    .with(ItemEntry.builder(Items.AIR).weight(54))
                table.pool(poolBuilder2)
            } else if (LootTables.VILLAGE_PLAINS_CHEST.equals(id) ||
                LootTables.VILLAGE_SAVANNA_HOUSE_CHEST.equals(id) ||
                LootTables.VILLAGE_SNOWY_HOUSE_CHEST.equals(id) ||
                LootTables.VILLAGE_TAIGA_HOUSE_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.GLISTERING_TOME).weight(30))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_LORE).weight(15))
                    .with(ItemEntry.builder(Items.AIR).weight(255))
                table.pool(poolBuilder)
            } else if (LootTables.SHIPWRECK_SUPPLY_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_LORE).weight(5))
                    .with(ItemEntry.builder(RegisterItem.GARNET_SWORD).weight(5)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.75f)))
                    .with(ItemEntry.builder(RegisterItem.GARNET_PICKAXE).weight(5)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.75f)))
                    .with(ItemEntry.builder(RegisterItem.GARNET_AXE).weight(5)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.75f)))
                    .with(ItemEntry.builder(RegisterItem.GARNET_HOE).weight(5)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.75f)))
                    .with(ItemEntry.builder(RegisterItem.GARNET_SHOVEL).weight(5)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.75f)))
                    .with(ItemEntry.builder(Items.AIR).weight(70))
                table.pool(poolBuilder)
            } else if (LootTables.SHIPWRECK_MAP_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_LORE).weight(1))
                    .with(ItemEntry.builder(Items.AIR).weight(9))
                table.pool(poolBuilder)
            } else if (LootTables.SHIPWRECK_TREASURE_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.BRILLIANT_DIAMOND).weight(5))
                    .with(ItemEntry.builder(RegisterItem.IMBUED_QUARTZ).weight(10))
                    .with(ItemEntry.builder(RegisterItem.IMBUED_LAPIS).weight(10))
                    .with(ItemEntry.builder(Items.AIR).weight(75))
                table.pool(poolBuilder)
            } else if (LootTables.DESERT_PYRAMID_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.PYRITE).weight(2))
                    .with(ItemEntry.builder(RegisterItem.HEARTSTONE).weight(4))
                    .with(ItemEntry.builder(RegisterItem.IRIDESCENT_ORB).weight(16))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_LORE).weight(5))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_HELMET).weight(4)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.75f)))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_CHESTPLATE).weight(4)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.75f)))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_LEGGINGS).weight(4)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.75f)))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_BOOTS).weight(4)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.75f)))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_HELMET).weight(1).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(15.0f,39.0f)).allowTreasureEnchantments())).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.95f)))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_CHESTPLATE).weight(1).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(15.0f,39.0f)).allowTreasureEnchantments())).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.95f)))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_LEGGINGS).weight(1).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(15.0f,39.0f)).allowTreasureEnchantments())).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.95f)))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_BOOTS).weight(1).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(15.0f,39.0f)).allowTreasureEnchantments())).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.95f)))
                    .with(ItemEntry.builder(Items.AIR).weight(43))
                table.pool(poolBuilder)
            } else if (LootTables.UNDERWATER_RUIN_SMALL_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.COPPER_RING).weight(1))
                    .with(ItemEntry.builder(RegisterItem.COPPER_AMULET).weight(1))
                    .with(ItemEntry.builder(RegisterItem.COPPER_HEADBAND).weight(1))
                    .with(ItemEntry.builder(Items.AIR).weight(37))
                table.pool(poolBuilder)
            } else if (LootTables.UNDERWATER_RUIN_BIG_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.IMBUED_QUARTZ).weight(10))
                    .with(ItemEntry.builder(RegisterItem.IMBUED_LAPIS).weight(10))
                    .with(ItemEntry.builder(RegisterItem.COPPER_RING).weight(5))
                    .with(ItemEntry.builder(RegisterItem.COPPER_AMULET).weight(5))
                    .with(ItemEntry.builder(RegisterItem.COPPER_HEADBAND).weight(5))
                    .with(ItemEntry.builder(Items.AIR).weight(105))
                table.pool(poolBuilder)
            } else if (LootTables.RUINED_PORTAL_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.STEEL_INGOT).weight(5))
                    .with(ItemEntry.builder(RegisterItem.BLOODSTONE).weight(1))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_LORE).weight(1))
                    .with(ItemEntry.builder(RegisterItem.IRIDESCENT_ORB).weight(5))
                    .with(ItemEntry.builder(Items.AIR).weight(78))
                table.pool(poolBuilder)
            } else if (LootTables.ABANDONED_MINESHAFT_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(UniformLootNumberProvider.create(1.0F,2.0F))
                    .with(ItemEntry.builder(RegisterItem.STEEL_INGOT).weight(5))
                    .with(ItemEntry.builder(RegisterItem.BERYL_COPPER_INGOT).weight(5))
                    .with(ItemEntry.builder(RegisterItem.OPAL).weight(5))
                    .with(ItemEntry.builder(RegisterItem.IRIDESCENT_ORB).weight(2))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_LORE).weight(2))
                    .with(ItemEntry.builder(Items.AIR).weight(31))
                table.pool(poolBuilder)
            } else if (LootTables.BASTION_TREASURE_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.GOLDEN_HEART).weight(5))
                    .with(ItemEntry.builder(RegisterItem.PYRITE).weight(10))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_LORE).weight(5))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_MYTHOS).weight(3))
                    .with(ItemEntry.builder(Items.AIR).weight(52))
                table.pool(poolBuilder)
            } else if (LootTables.BASTION_BRIDGE_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.IMBUED_QUARTZ).weight(10))
                    .with(ItemEntry.builder(RegisterItem.PYRITE).weight(1))
                    .with(ItemEntry.builder(Items.AIR).weight(49))
                table.pool(poolBuilder)
            } else if (LootTables.BASTION_OTHER_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.PYRITE).weight(5))
                    .with(ItemEntry.builder(Items.AIR).weight(45))
                table.pool(poolBuilder)
            } else if (LootTables.NETHER_BRIDGE_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(UniformLootNumberProvider.create(1.0F,2.0F))
                    .with(ItemEntry.builder(RegisterItem.BLOODSTONE).weight(12))
                    .with(ItemEntry.builder(RegisterItem.OPAL).weight(6))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_HELMET).weight(2).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(15.0f,39.0f)).allowTreasureEnchantments())).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.95f)))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_CHESTPLATE).weight(2).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(15.0f,39.0f)).allowTreasureEnchantments())).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.95f)))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_LEGGINGS).weight(2).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(15.0f,39.0f)).allowTreasureEnchantments())).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.95f)))
                    .with(ItemEntry.builder(RegisterArmor.STEEL_BOOTS).weight(2).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(15.0f,39.0f)).allowTreasureEnchantments())).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.95f)))
                    .with(ItemEntry.builder(RegisterItem.OPALINE_SCEPTER).weight(6))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_LORE).weight(2))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_MYTHOS).weight(1))
                    .with(ItemEntry.builder(Items.AIR).weight(65))
                table.pool(poolBuilder)
            } else if (LootTables.PIGLIN_BARTERING_GAMEPLAY.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.BRILLIANT_DIAMOND).weight(5))
                    .with(ItemEntry.builder(RegisterItem.GOLDEN_HEART).weight(5))
                    .with(ItemEntry.builder(Items.AIR).weight(90))
                table.pool(poolBuilder)
            } else if (LootTables.SIMPLE_DUNGEON_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.OPAL).weight(16))
                    .with(ItemEntry.builder(RegisterItem.OPALINE_SCEPTER).weight(2))
                    .with(ItemEntry.builder(RegisterItem.GLISTERING_TOME).weight(1))
                    .with(ItemEntry.builder(RegisterItem.IRIDESCENT_ORB).weight(8))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_LORE).weight(10))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_MYTHOS).weight(1))
                    .with(ItemEntry.builder(Items.AIR).weight(62))
                table.pool(poolBuilder)
            } else if (LootTables.STRONGHOLD_CORRIDOR_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.BRILLIANT_DIAMOND).weight(12))
                    .with(ItemEntry.builder(RegisterItem.MOONSTONE).weight(16))
                    .with(ItemEntry.builder(RegisterItem.BERYL_COPPER_INGOT).weight(22))
                    .with(ItemEntry.builder(Items.AIR).weight(50))
                table.pool(poolBuilder)
            } else if (LootTables.STRONGHOLD_CROSSING_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(UniformLootNumberProvider.create(1.0F,5.0f))
                    .with(ItemEntry.builder(RegisterItem.BRILLIANT_DIAMOND).weight(2))
                    .with(ItemEntry.builder(RegisterItem.MOONSTONE).weight(2))
                    .with(ItemEntry.builder(RegisterItem.BERYL_COPPER_INGOT).weight(5))
                    .with(ItemEntry.builder(Items.AIR).weight(90))
                table.pool(poolBuilder)
            } else if (LootTables.STRONGHOLD_LIBRARY_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(UniformLootNumberProvider.create(1.0F,3.0F))
                    .with(ItemEntry.builder(RegisterItem.IMBUED_LAPIS).weight(2))
                    .with(ItemEntry.builder(RegisterItem.IRIDESCENT_ORB).weight(2))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_LORE).weight(40))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_MYTHOS).weight(5))
                    .with(ItemEntry.builder(Items.AIR).weight(51))
                table.pool(poolBuilder)
                val poolBuilder2 = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.GLISTERING_TOME))
                table.pool(poolBuilder2)
            } else if (LootTables.END_CITY_TREASURE_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(UniformLootNumberProvider.create(1.0F,2.0F))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_LORE).weight(2))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_MYTHOS).weight(5))
                    .with(ItemEntry.builder(RegisterArmor.AMETRINE_HELMET).weight(5).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0f,39.0f)).allowTreasureEnchantments()))
                    .with(ItemEntry.builder(RegisterArmor.AMETRINE_CHESTPLATE).weight(5).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0f,39.0f)).allowTreasureEnchantments()))
                    .with(ItemEntry.builder(RegisterArmor.AMETRINE_LEGGINGS).weight(5).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0f,39.0f)).allowTreasureEnchantments()))
                    .with(ItemEntry.builder(RegisterArmor.AMETRINE_BOOTS).weight(5).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0f,39.0f)).allowTreasureEnchantments()))
                    .with(ItemEntry.builder(RegisterItem.GARNET_SWORD).weight(5).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0f,39.0f)).allowTreasureEnchantments()))
                    .with(ItemEntry.builder(RegisterItem.GARNET_AXE).weight(5).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0f,39.0f)).allowTreasureEnchantments()))
                    .with(ItemEntry.builder(RegisterItem.GARNET_HOE).weight(5).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0f,39.0f)).allowTreasureEnchantments()))
                    .with(ItemEntry.builder(RegisterItem.GARNET_SHOVEL).weight(5).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0f,39.0f)).allowTreasureEnchantments()))
                    .with(ItemEntry.builder(RegisterItem.GARNET_PICKAXE).weight(5).apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0f,39.0f)).allowTreasureEnchantments()))
                    .with(ItemEntry.builder(RegisterItem.IMBUED_RING).weight(1))
                    .with(ItemEntry.builder(RegisterItem.IMBUED_HEADBAND).weight(1))
                    .with(ItemEntry.builder(RegisterItem.IMBUED_AMULET).weight(1))
                    .with(ItemEntry.builder(Items.AIR).weight(70))
                table.pool(poolBuilder)
                val poolBuilder2 = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.BRILLIANT_DIAMOND).weight(20))
                    .with(ItemEntry.builder(RegisterItem.LUSTROUS_SPHERE).weight(40))
                    .with(ItemEntry.builder(Items.AIR).weight(40))
                table.pool(poolBuilder2)
            } else if (LootTables.JUNGLE_TEMPLE_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.PYRITE).weight(1))
                    .with(ItemEntry.builder(RegisterItem.HEARTSTONE).weight(20))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_LORE).weight(10))
                    .with(ItemEntry.builder(RegisterItem.IRIDESCENT_ORB).weight(10))
                    .with(ItemEntry.builder(Items.AIR).weight(19))
                table.pool(poolBuilder)
            } else if (LootTables.PILLAGER_OUTPOST_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.SNIPER_BOW).weight(2).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0f)).allowTreasureEnchantments())).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.05f, 0.35f)))
                    .with(ItemEntry.builder(RegisterItem.SNIPER_BOW).weight(3)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.05f, 0.35f)))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_LORE).weight(1))
                    .with(ItemEntry.builder(Items.AIR).weight(44))
                table.pool(poolBuilder)
            } else if (LootTables.WOODLAND_MANSION_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.SNIPER_BOW).weight(2).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0f)).allowTreasureEnchantments())).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.05f, 0.35f)))
                    .with(ItemEntry.builder(RegisterItem.SNIPER_BOW).weight(3)).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.05f, 0.35f)))
                    .with(ItemEntry.builder(RegisterItem.TOTEM_OF_AMETHYST).weight(1))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_MYTHOS).weight(1))
                    .with(ItemEntry.builder(Items.AIR).weight(43))
                table.pool(poolBuilder)
            } else if (LootTables.BURIED_TREASURE_CHEST.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(UniformLootNumberProvider.create(1.0F, 2.0F))
                    .with(ItemEntry.builder(RegisterItem.GOLDEN_HEART).weight(1))
                    .with(ItemEntry.builder(RegisterItem.BRILLIANT_DIAMOND).weight(1))
                    .with(ItemEntry.builder(RegisterItem.GLISTERING_TRIDENT).weight(2).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0f)).allowTreasureEnchantments())).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.05f, 0.35f)))
                    .with(ItemEntry.builder(Items.AIR).weight(16))
                table.pool(poolBuilder)
            } else if (LootTables.FISHING_TREASURE_GAMEPLAY.equals(id)) {
                val poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(ItemEntry.builder(RegisterItem.BOOK_OF_LORE).weight(1))
                    .with(ItemEntry.builder(RegisterItem.HEARTSTONE).weight(1))
                    .with(ItemEntry.builder(RegisterItem.BRILLIANT_DIAMOND).weight(1))
                    .with(ItemEntry.builder(Items.AIR).weight(47))
                table.pool(poolBuilder)
            }
        })
    }
}