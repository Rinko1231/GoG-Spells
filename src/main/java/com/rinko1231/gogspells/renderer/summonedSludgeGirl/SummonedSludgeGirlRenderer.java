package com.rinko1231.gogspells.renderer.summonedSludgeGirl;

import com.rinko1231.gogspells.entity.SummonedSludgeGirl;
import gaia.client.ClientHandler;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SummonedSludgeGirlRenderer extends MobRenderer<SummonedSludgeGirl, SummonedSludgeGirlModel> {
        public static final ResourceLocation[] SLUDGE_GIRL_LOCATIONS = new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath("grimoireofgaia", "textures/entity/sludge_girl/sludge_girl01.png"), ResourceLocation.fromNamespaceAndPath("grimoireofgaia", "textures/entity/sludge_girl/sludge_girl02.png"), ResourceLocation.fromNamespaceAndPath("grimoireofgaia", "textures/entity/sludge_girl/sludge_girl03.png")};

        public SummonedSludgeGirlRenderer(EntityRendererProvider.Context context) {
            super(context, new SummonedSludgeGirlModel(context.bakeLayer(ClientHandler.SLUDGE_GIRL)), 0.4F);
            this.addLayer(new CustomHeadLayer(this, context.getModelSet(), context.getItemInHandRenderer()));
            this.addLayer(new SummonedSludgeHairLayer(this, context.getModelSet()));
        }

        public @NotNull ResourceLocation getTextureLocation(SummonedSludgeGirl sludgeGirl) {
            return SLUDGE_GIRL_LOCATIONS[sludgeGirl.getVariant()];
        }
    }
