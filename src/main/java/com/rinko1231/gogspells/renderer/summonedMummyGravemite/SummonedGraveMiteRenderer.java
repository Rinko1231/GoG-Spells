package com.rinko1231.gogspells.renderer.summonedMummyGravemite;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rinko1231.gogspells.entity.SummonedGraveMite;
import gaia.client.ClientHandler;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SummonedGraveMiteRenderer extends MobRenderer<SummonedGraveMite, SummonedGraveMiteModel> {
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath("grimoireofgaia", "textures/entity/mummy/mummy_mite.png");

    public SummonedGraveMiteRenderer(EntityRendererProvider.Context context) {
        super(context, new SummonedGraveMiteModel(context.bakeLayer(ClientHandler.GRAVEMITE)), 0.3F);
    }

    protected void scale(SummonedGraveMite graveMite, PoseStack poseStack, float scale) {
        poseStack.scale(1.0F, 1.0F, 1.0F);
        super.scale(graveMite, poseStack, scale);
    }

    public ResourceLocation getTextureLocation(SummonedGraveMite graveMite) {
        return LOCATION;
    }
}
