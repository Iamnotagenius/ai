package me.fzzyhmstrs.amethyst_imbuement.material

import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import me.fzzyhmstrs.fzzy_config.util.Walkable
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIngredient
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedAny
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Item
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.tag.TagKey
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier

open class ValidatedArmorMaterial protected constructor(
    protected val armorName: String,
    protected val armorSoundEvent: SoundEvent,
    repairIngredientDefault: ValidatedIngredient,
    enchantabilityDefault: ValidatedInt,
    protectionAmountsDefault: ProtectionAmounts,
    durabilityMultiplierDefault: ValidatedInt,
    knockbackResistanceDefault: ValidatedFloat,
    toughnessDefault: ValidatedFloat)
    :
    ArmorMaterial, Walkable
{
    private val BASE_DURABILITY = intArrayOf(13, 15, 16, 11)

    var repairIngredient = repairIngredientDefault
    var enchantability = enchantabilityDefault
    var protectionAmounts = ValidatedAny(protectionAmountsDefault)
    var durabilityMultiplier = durabilityMultiplierDefault
    var knockbackResistance = knockbackResistanceDefault
    var toughness = toughnessDefault

    override fun getName(): String{
        return armorName
    }
    override fun getEquipSound(): SoundEvent {
        return armorSoundEvent
    }
    override fun getRepairIngredient(): Ingredient?{
        return repairIngredient.toIngredient()
    }
    override fun getEnchantability(): Int{
        return enchantability.get()
    }
    override fun getProtection(type: ArmorItem.Type): Int {
        return protectionAmounts.get().getProtection(type.equipmentSlot.entitySlotId)
    }
    override fun getDurability(type: ArmorItem.Type): Int{
        return BASE_DURABILITY[type.equipmentSlot.entitySlotId] * durabilityMultiplier.get()
    }
    override fun getKnockbackResistance(): Float{
        return knockbackResistance.get()
    }
    override fun getToughness(): Float{
        return toughness.get()
    }

    override fun toString(): String {
        return ConfigApi.serializeConfig(this, mutableListOf())
    }

    class Builder(name: String, soundEvent: SoundEvent): AbstractBuilder<ValidatedArmorMaterial, Builder>(name, soundEvent){
        override fun builderClass(): Builder{
            return this
        }
        override fun build(): ValidatedArmorMaterial {
            return ValidatedArmorMaterial(name, soundEvent, rI, e, pA, dM, kR, t)
        }
    }

    abstract class AbstractBuilder<T: ArmorMaterial, U: AbstractBuilder<T,U>>(protected val name: String, protected val soundEvent: SoundEvent){
        protected var rI = ValidatedIngredient(setOf())
        protected var e = ValidatedInt(1,50,0)
        protected var pA = ProtectionAmounts()
        protected var dM = ValidatedInt(1,100,0)
        protected var kR = ValidatedFloat(0f,0.5f,0f)
        protected var t = ValidatedFloat(0f,10f,0f)

        abstract fun builderClass(): U

        fun repairIngredient(ingredient: Identifier): U{
            rI = ValidatedIngredient(ingredient)
            return builderClass()
        }
        fun repairIngredient(ingredient: Set<Identifier>): U{
            rI = ValidatedIngredient(ingredient)
            return builderClass()
        }
        fun repairIngredient(ingredient: TagKey<Item>): U{
            rI = ValidatedIngredient(ingredient)
            return builderClass()
        }
        fun enchantability(default: Int, max: Int = 50): U{
            e = ValidatedInt(default,max,1)
            return builderClass()
        }
        fun protectionAmounts(helmet: Int, chestplate: Int, leggings: Int, boots: Int): U{
            pA = ProtectionAmounts(helmet,chestplate,leggings,boots)
            return builderClass()
        }
        fun durabilityMultiplier(default: Int, max: Int = 100): U{
            dM = ValidatedInt(default, max, 1)
            return builderClass()
        }
        fun knockbackResistance(default: Float): U{
            kR = ValidatedFloat(default,0.5f,0f)
            return builderClass()
        }
        fun toughness(default: Float, max: Float = 10f): U{
            t = ValidatedFloat(default,max,0f)
            return builderClass()
        }

        abstract fun build(): T
    }

    class ProtectionAmounts(h: Int, c: Int, l: Int, b: Int){
        constructor(): this(1,1,1,1)

        var head = h
        var chest = c
        var legs = l
        var boots = b

        fun getProtection(slot: Int): Int{
            return when (slot){
                0 -> boots
                1 -> legs
                2 -> chest
                else -> head
            }
        }
    }
}