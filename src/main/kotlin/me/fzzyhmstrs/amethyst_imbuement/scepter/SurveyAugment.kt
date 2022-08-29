package me.fzzyhmstrs.amethyst_imbuement.scepter

import me.fzzyhmstrs.amethyst_core.coding_util.PerLvlI
import me.fzzyhmstrs.amethyst_core.coding_util.PersistentEffectHelper
import me.fzzyhmstrs.amethyst_core.modifier_util.AugmentEffect
import me.fzzyhmstrs.amethyst_core.scepter_util.LoreTier
import me.fzzyhmstrs.amethyst_core.scepter_util.SpellType
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.AugmentDatapoint
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.AugmentPersistentEffectData
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.MiscAugment
import me.fzzyhmstrs.amethyst_core.scepter_util.augments.TravelerAugment
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.FilledMapItem
import net.minecraft.item.Items
import net.minecraft.item.map.MapIcon
import net.minecraft.item.map.MapState
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.tag.ConfiguredStructureFeatureTags
import net.minecraft.tag.TagKey
import net.minecraft.text.TranslatableText
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.gen.feature.ConfiguredStructureFeature

class SurveyAugment(tier: Int, maxLvl: Int, vararg slot: EquipmentSlot): MiscAugment(tier,maxLvl, *slot),
    TravelerAugment {

    override val baseEffect: AugmentEffect
        get() = super.baseEffect.withRange(16.0,0.0,0.0)

    override fun effect(
        world: World,
        target: Entity?,
        user: LivingEntity,
        level: Int,
        hit: HitResult?,
        effect: AugmentEffect
    ): Boolean {
        if (user !is PlayerEntity) return false
        if (world !is ServerWorld) return false
        val type = mapList[world.random.nextInt(mapList.size)]
        val blockPos: BlockPos? = world.locateStructure(type.structure, user.getBlockPos(), 100, true)
        return if (blockPos != null) {
            val mapStack = FilledMapItem.createMap(world, blockPos.x, blockPos.z, 2.toByte(), true, true)
            FilledMapItem.fillExplorationMap(world,mapStack)
            MapState.addDecorationsNbt(mapStack,blockPos,"+",type.iconType)
            mapStack.setCustomName(TranslatableText(type.nameKey))
            if (type.tint >= 0){
                val nbtCompound2: NbtCompound = mapStack.getOrCreateSubNbt("display")
                nbtCompound2.putInt("MapColor", type.tint)
            }
            mapStack.setCustomName(Text.translatable(type.nameKey))
            if (!user.inventory.insertStack(mapStack)) {
                user.dropItem(mapStack, false)
            }
            true
        } else {
            false
        }
    }

    override fun augmentStat(imbueLevel: Int): AugmentDatapoint {
        return AugmentDatapoint(SpellType.WIT,1200,100,5,imbueLevel, LoreTier.NO_TIER, Items.MAP)
    }

    override fun soundEvent(): SoundEvent {
        return SoundEvents.ITEM_SPYGLASS_USE
    }

    companion object{

        private val mapList: List<MapType> = listOf(
            MapType(ConfiguredStructureFeatureTags.ON_OCEAN_EXPLORER_MAPS,MapIcon.Type.MONUMENT,"filled_map.monument"),
            MapType(ConfiguredStructureFeatureTags.ON_WOODLAND_EXPLORER_MAPS,MapIcon.Type.MANSION,"filled_map.mansion"),
            MapType(ConfiguredStructureFeatureTags.ON_TREASURE_MAPS,MapIcon.Type.RED_X,"filled_map.buried_treasure"),
            MapType(ConfiguredStructureFeatureTags.VILLAGE,MapIcon.Type.TARGET_X,"filled_map.village",0x007F0E),
            MapType(ConfiguredStructureFeatureTags.MINESHAFT,MapIcon.Type.TARGET_POINT,"filled_map.mineshaft",0x7F0000),
        )

        private class MapType(val structure: TagKey<ConfiguredStructureFeature<*,*>>, val iconType: MapIcon.Type,val nameKey: String, val tint: Int = -1)
    }


}
