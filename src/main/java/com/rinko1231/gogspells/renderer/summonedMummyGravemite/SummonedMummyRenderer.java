package com.rinko1231.gogspells.renderer.summonedMummyGravemite;

import com.rinko1231.gogspells.entity.SummonedMummy;
import gaia.client.ClientHandler;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class SummonedMummyRenderer extends MobRenderer<SummonedMummy, SummonedMummyModel> {
    public static final ResourceLocation[] MUMMY_LOCATIONS = new ResourceLocation[]{new ResourceLocation("grimoireofgaia", "textures/entity/mummy/mummy.png")};

    public SummonedMummyRenderer(EntityRendererProvider.Context context) {
        super(context, new SummonedMummyModel(context.bakeLayer(ClientHandler.MUMMY)), 0.5F);
        this.addLayer(new CustomHeadLayer(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new ItemInHandLayer(this, context.getItemInHandRenderer()));
    }

    public ResourceLocation getTextureLocation(SummonedMummy mummy) {
        return MUMMY_LOCATIONS[mummy.getVariant()];
    }
}
