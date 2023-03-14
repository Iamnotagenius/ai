package me.fzzyhmstrs.amethyst_imbuement.item.promise

import net.minecraft.item.Item
import net.minecraft.text.Text
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterCriteria
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterItem
import me.fzzyhmstrs.fzzy_core.coding_util.AcText
import me.fzzyhmstrs.fzzy_core.item_util.interfaces.Flavorful
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Registries
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Formatting
import net.minecraft.world.World

abstract class InquisitiveGemItem(settings: Settings): IgnitedGemItem(settings) {
  
    private val STATUS_TARGET = 8
    private val GOAL_STATUSES: List<String> = listOf(
        "minecraft:darkness",
        "minecraft:hero_of_the_village",
        "minecraft:bad_omen",
        "minecraft:dolphins_grace",
        "minecraft:conduit_power",
        "minecraft:levitation",
        "amethyst_imbuement:draconic_vision",
        "amethyst_imbuement:leapt"
    )

    override fun giveTooltipHint(nbt: NbtCompound, stack: Itemstack, tooltip: MutableList<Text>){
        if (nbt.contains("statuses")){
            val compound = nbt.get("statuses") as NbtCompound
            val status = compound.keys.size.toFloat()
            val progress = status/ STATUS_TARGET.toFloat()*100.0F
            tooltip.add(AcText.translatable("item.amethyst_imbuement.gem_of_promise.inquisitive", progress).append(AcText.literal("%")).formatted(Formatting.BLUE))
        }
    }
    
    fun inquisitiveGemCheck(stack: ItemStack, inventory: PlayerInventory, statusEffect: StatusEffect){
            val nbt = stack.orCreateNbt
            var compound = NbtCompound()
            if (nbt.contains("statuses")){
                compound = nbt.get("statuses") as NbtCompound
            }
            val statusIdentifier = Registries.STATUS_EFFECT.getId(statusEffect)?:return
            val id = statusIdentifier.toString()
            if (compound.contains(id)) return
            compound.putInt(id, 1)
            val keys = compound.keys
            if (keys.size >= STATUS_TARGET){
                stack.decrement(1)
                val newStack = ItemStack(RegisterItem.INQUISITIVE_GEM)
                inventory.offerOrDrop(newStack)
                val player = inventory.player
                if (player is ServerPlayerEntity){
                    RegisterCriteria.IGNITE.trigger(player)
                    if (keys.containsAll(GOAL_STATUSES)){
                        RegisterCriteria.MASTER_RESEARCHER.trigger(player)
                    }
                }
                return
            }
            nbt.put("statuses",compound)
        }

}