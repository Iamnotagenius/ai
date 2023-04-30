package me.fzzyhmstrs.amethyst_imbuement.item

import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.registry.Registry
import java.util.*

interface SpellcastersReagent: Reagent{
    fun getAttributeModifier(): Pair<EntityAttribute,EntityAttributeModifier>

    companion object{
        fun toNbt(attribute: Pair<EntityAttribute,EntityAttributeModifier>): NbtCompound {
            val nbtCompound = NbtCompound()
            nbtCompound.putString("Name", attribute.second.name)
            nbtCompound.putDouble("Amount", attribute.second.value)
            nbtCompound.putInt("Operation", attribute.second.operation.id)
            nbtCompound.putUuid("UUID", UUID.randomUUID())
            nbtCompound.putString("AttributeName", Registry.ATTRIBUTE.getId(attribute.first).toString())
            return nbtCompound
        }


    }
}
