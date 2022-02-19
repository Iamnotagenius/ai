package me.fzzyhmstrs.amethyst_imbuement.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import vazkii.patchouli.api.PatchouliAPI

class GlisteringTomeItem(settings: Item.Settings): Item(settings) {
    override fun use(world: World, playerEntity: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient) {
            PatchouliAPI.get().openBookGUI(playerEntity as ServerPlayerEntity, Identifier("amethyst_imbuement","glistering_tome"))
            return TypedActionResult.success(playerEntity.getStackInHand(hand))
        }
        return TypedActionResult.consume(playerEntity.getStackInHand(hand))
    }
}