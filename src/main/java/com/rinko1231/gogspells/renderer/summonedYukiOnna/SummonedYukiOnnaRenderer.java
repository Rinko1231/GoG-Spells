package com.rinko1231.gogspells.renderer.summonedYukiOnna;

import com.rinko1231.gogspells.entity.SummonedYukiOnna;
import gaia.client.ClientHandler;
import gaia.client.renderer.GaiaBabyMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class SummonedYukiOnnaRenderer extends GaiaBabyMobRenderer<SummonedYukiOnna, SummonedYukiOnnaModel> {
    public static final ResourceLocation[] YUKI_ONNA_LOCATIONS = new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath("grimoireofgaia", "textures/entity/yuki_onna/yuki_onna.png")};

    public SummonedYukiOnnaRenderer(EntityRendererProvider.Context context) {
        super(context, new SummonedYukiOnnaModel(context.bakeLayer(ClientHandler.YUKI_ONNA)), 0.4F);
        this.addLayer(new CustomHeadLayer(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new ItemInHandLayer(this, context.getItemInHandRenderer()));
    }

    public ResourceLocation getTextureLocation(SummonedYukiOnna yukiOnna) {
        return YUKI_ONNA_LOCATIONS[yukiOnna.getVariant()];
    }
}

