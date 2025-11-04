package com.rinko1231.gogspells.renderer.summonedNineTails;

import com.rinko1231.gogspells.entity.SummonedNineTails;
import gaia.client.ClientHandler;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class SummonedNineTailsRenderer extends MobRenderer<SummonedNineTails, SummonedNineTailsModel> {
        public static final ResourceLocation[] NINE_TAILS_LOCATIONS = new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath("grimoireofgaia", "textures/entity/nine_tails/nine_tails.png")};

        public SummonedNineTailsRenderer(EntityRendererProvider.Context context) {
            super(context, new SummonedNineTailsModel(context.bakeLayer(ClientHandler.NINE_TAILS)), 0.4F);
            this.addLayer(new CustomHeadLayer(this, context.getModelSet(), context.getItemInHandRenderer()));
            this.addLayer(new ItemInHandLayer(this, context.getItemInHandRenderer()));
        }

        public ResourceLocation getTextureLocation(SummonedNineTails kobold) {
            return NINE_TAILS_LOCATIONS[kobold.getVariant()];
        }
    }
