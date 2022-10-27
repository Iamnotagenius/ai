package me.fzzyhmstrs.amethyst_imbuement.augment

import io.github.ladysnake.pal.AbilitySource
import io.github.ladysnake.pal.Pal
import io.github.ladysnake.pal.PlayerAbility
import io.github.ladysnake.pal.VanillaAbilities
import me.fzzyhmstrs.amethyst_core.coding_util.AcText
import me.fzzyhmstrs.amethyst_core.nbt_util.NbtKeys
import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.amethyst_imbuement.augment.base_augments.ActiveAugment
import me.fzzyhmstrs.amethyst_imbuement.item.TotemItem
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEnchantment
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterItem
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class AngelicAugment(weight: Rarity, mxLvl: Int = 1, vararg slot: EquipmentSlot): ActiveAugment(weight,mxLvl,*slot) {

    private val ability: PlayerAbility = VanillaAbilities.ALLOW_FLYING
    private val abilitySource: AbilitySource = Pal.getAbilitySource(Identifier(AI.MOD_ID,"angelic"))

    override fun activateEffect(user: LivingEntity, level: Int, stack: ItemStack) {
        user as PlayerEntity
        if (!user.abilities.creativeMode) {
            if (!abilitySource.grants(user,ability)){
                abilitySource.grantTo(user,ability)
            }
            if(user.abilities.flying) {
                if (RegisterItem.TOTEM_OF_AMETHYST.manaDamage(stack, user.world, user, 2)) {
                    RegisterItem.TOTEM_OF_AMETHYST.burnOutHandler(
                        stack,
                        RegisterEnchantment.ANGELIC,
                        user,
                        AcText.translatable("augment_damage.angelic.burnout")
                    )
                    if (abilitySource.grants(user, ability)) {
                        abilitySource.revokeFrom(user, ability)
                    }
                }
            }
        }
    }

    override fun deactivateEffect(user: LivingEntity, level: Int, stack: ItemStack) {
        user as PlayerEntity
        if (abilitySource.grants(user,ability)){
            abilitySource.revokeFrom(user,ability)
        }
    }

}