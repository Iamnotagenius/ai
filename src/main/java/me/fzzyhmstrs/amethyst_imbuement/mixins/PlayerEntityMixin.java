package me.fzzyhmstrs.amethyst_imbuement.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fzzyhmstrs.amethyst_core.augments.paired.ProcessContext;
import me.fzzyhmstrs.amethyst_core.entity.ModifiableEffect;
import me.fzzyhmstrs.amethyst_core.entity.ModifiableEffectContainer;
import me.fzzyhmstrs.amethyst_core.entity.ModifiableEffectEntity;
import me.fzzyhmstrs.amethyst_imbuement.interfaces.ModifiableEffectMobOrPlayer;
import me.fzzyhmstrs.amethyst_imbuement.item.TotemItem;
import me.fzzyhmstrs.amethyst_imbuement.item.promise.GemOfPromiseItem;
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterEnchantment;
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterItem;
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterStatus;
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterTag;
import me.fzzyhmstrs.fzzy_core.trinket_util.base_augments.BaseAugment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ModifiableEffectMobOrPlayer {

    @Shadow @Final private PlayerInventory inventory;

    @Unique
    private DamageSource damageSource;

    @Unique
    final private ProcessContext processContext = ProcessContext.Companion.getEMPTY_CONTEXT();
    @Unique
    ModifiableEffectContainer modifiableEffectContainer = new ModifiableEffectContainer();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void amethyst_imbuement_addTemporaryEffect(Identifier type, ModifiableEffect effect, int lifespan){
        modifiableEffectContainer.addTemporary(type, effect, lifespan);
    }

    //credit for this mixin (C) Timefall Development, Chronos Sacaria, Kluzzio
    @Inject(method = "attack", at = @At(value = "INVOKE", target = "net/minecraft/entity/player/PlayerEntity.getAttributeValue (Lnet/minecraft/entity/attribute/EntityAttribute;)D"), cancellable = true)
    public void amethyst_imbuement_onPlayerAttackWhilstStunnedTarget(Entity target, CallbackInfo ci) {
        if (this.hasStatusEffect(RegisterStatus.INSTANCE.getSTUNNED())){
            ci.cancel();
        }
    }

    //credit for this mixin (C) Timefall Development, Chronos Sacaria, Kluzzio
    @Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
    public void amethyst_imbuement_onPlayerMovementWhilstStunnedTarget(CallbackInfo ci) {
        if (this.hasStatusEffect(RegisterStatus.INSTANCE.getSTUNNED())){
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void amethyst_imbuement_runTickingModifiableEffects(CallbackInfo ci) {
        modifiableEffectContainer.run(ModifiableEffectEntity.Companion.getTICK(), this,null, processContext);
        if (this.hasStatusEffect(RegisterStatus.INSTANCE.getSTUNNED())){
            ci.cancel();
        }
    }

    @Inject(method="isUsingSpyglass", at = @At(value = "HEAD"), cancellable = true)
    private void amethyst_imbuement_isUsingSpyglass(CallbackInfoReturnable<Boolean> cir){
        if(super.getActiveItem().isOf(RegisterItem.INSTANCE.getSNIPER_BOW())){
            if (CrossbowItem.isCharged(super.getActiveItem())) {
                if (EnchantmentHelper.getLevel(RegisterEnchantment.INSTANCE.getILLUMINATING(),super.getActiveItem()) > 0){
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION,260));
                }
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "net/minecraft/entity/player/PlayerInventory.dropAll ()V"), cancellable = true)
    private void amethyst_imbuement_checkForSoulbinding(CallbackInfo ci){
        if (this.hasStatusEffect(RegisterStatus.INSTANCE.getSOULBINDING())){
            Pair<ItemStack,Integer> chk = BaseAugment.Companion.getEquipmentWithAugment(RegisterEnchantment.INSTANCE.getSOULBINDING(),this.inventory, item -> item instanceof TotemItem);
            if (chk.getRight() > 0){
                if (RegisterEnchantment.INSTANCE.getSOULBINDING().specialEffect((PlayerEntity)(Object)this,1,chk.getLeft())){
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "damage", at = @At(value = "HEAD"))
    private void amethyst_imbuement_damageMixin(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        modifiableEffectContainer.run(ModifiableEffectEntity.Companion.getON_DAMAGED(), this,null, processContext);
        damageSource = source;
        Entity attacker = damageSource.getSource();
        if (attacker != null) {
            if (attacker instanceof LivingEntity) {
                Pair<ItemStack,Integer> chk = BaseAugment.Companion.getEquipmentWithAugment(RegisterEnchantment.INSTANCE.getACCURSED(),this.inventory,item -> item instanceof TotemItem);
                if (chk.getRight() > 0){
                    RegisterEnchantment.INSTANCE.getACCURSED().accursedEffect((PlayerEntity)(Object)this,(LivingEntity) attacker,chk.getRight(),chk.getLeft());
                }
            }
        }
        if (!(this.timeUntilRegen > 10)) {
            ItemStack stack2 = inventory.getStack(PlayerInventory.OFF_HAND_SLOT);
            if (stack2.isOf(RegisterItem.INSTANCE.getGEM_OF_PROMISE())) {
                RegisterItem.INSTANCE.getSPARKING_GEM().sparkingGemCheck(stack2, inventory, source);
                RegisterItem.INSTANCE.getBLAZING_GEM().blazingGemCheck(stack2, inventory, this, source);
                RegisterItem.INSTANCE.getBRUTAL_GEM().brutalGemCheck(stack2, inventory, source);
            }
        }
    }

    @Inject(method = "damageShield", at = @At(value = "HEAD"))
    private void amethyst_imbuement_checkShieldEnchants(float amount, CallbackInfo ci){
        ItemStack activeStack = this.activeItemStack;
        if (activeStack.getItem() instanceof ShieldItem){
            int level = EnchantmentHelper.getLevel(RegisterEnchantment.INSTANCE.getSPIKED(),activeStack);
            if (level > 0){
                Entity source = damageSource.getSource();
                if (source != null) {
                    if (source instanceof LivingEntity) {
                        RegisterEnchantment.INSTANCE.getSPIKED().specialEffect((LivingEntity) source, level, activeStack);
                    } else if (source instanceof ProjectileEntity){
                        Entity owner = ((ProjectileEntity) source).getOwner();
                        if (owner != null){
                            RegisterEnchantment.INSTANCE.getSPIKED().specialEffect((LivingEntity) owner, level, activeStack);
                        }
                    }
                }
            }
            level = EnchantmentHelper.getLevel(RegisterEnchantment.INSTANCE.getBULWARK(),activeStack);
            if (level > 0){
                Entity source = damageSource.getSource();
                if (source != null) {
                    if (source instanceof LivingEntity) {
                        RegisterEnchantment.INSTANCE.getBULWARK().specialEffect(this, level, activeStack);
                    }
                }
            }
        }
    }


    @WrapOperation(method = "damageShield", at = @At(value = "INVOKE", target = "net/minecraft/item/ItemStack.isOf (Lnet/minecraft/item/Item;)Z"))
    private boolean amethyst_imbuement_damageWards(ItemStack instance, Item item, Operation<Boolean> operation){
        return operation.call(instance,item) || instance.isIn(RegisterTag.INSTANCE.getBASIC_WARDS_TAG()) || instance.isOf(RegisterItem.INSTANCE.getIMBUED_WARD());
    }

    @Inject(method = "onKilledOther", at = @At(value = "HEAD"))
    private void amethyst_imbuement_checkForPromiseGemKill(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir){
        modifiableEffectContainer.run(ModifiableEffectEntity.Companion.getKILL(), this,null, processContext);
        ItemStack stack = inventory.getStack(PlayerInventory.OFF_HAND_SLOT);
        if (stack.getItem() instanceof GemOfPromiseItem){
            RegisterItem.INSTANCE.getLETHAL_GEM().lethalGemCheck(stack,inventory);
        }
    }
}
