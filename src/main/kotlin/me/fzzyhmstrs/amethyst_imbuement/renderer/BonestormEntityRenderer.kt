package me.fzzyhmstrs.amethyst_imbuement.renderer

import me.fzzyhmstrs.amethyst_imbuement.AI
import me.fzzyhmstrs.amethyst_imbuement.entity.living.BonestormEntity
import me.fzzyhmstrs.amethyst_imbuement.registry.RegisterRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.BlazeEntityModel
import net.minecraft.util.Identifier

@Suppress("PrivatePropertyName")
class BonestormEntityRenderer(ctx: EntityRendererFactory.Context): MobEntityRenderer<BonestormEntity, BlazeEntityModel<BonestormEntity>>(ctx, BlazeEntityModel(ctx.getPart(RegisterRenderer.BONESTORM_ENTITY)), 0.5f) {
    private val TEXTURE = AI.identity("textures/entity/bonestorm.png")


    override fun getTexture(bonestormEntity: BonestormEntity): Identifier {
        return TEXTURE
    }

}