package com.rinko1231.gogspells.renderer.summonedWerecat;

import com.rinko1231.gogspells.entity.SummonedWerecat;
import gaia.client.ClientHandler;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;

public class SummonedWerecatRenderer extends MobRenderer<SummonedWerecat, SummonedWerecatModel> {
        public static final ResourceLocation[] WERECAT_LOCATIONS = new ResourceLocation[]{new ResourceLocation("grimoireofgaia", "textures/entity/werecat/werecat01.png"), new ResourceLocation("grimoireofgaia", "textures/entity/werecat/werecat02.png")};

        public SummonedWerecatRenderer(EntityRendererProvider.Context context) {
            super(context, new SummonedWerecatModel(context.bakeLayer(ClientHandler.WERECAT)), 0.4F);
            this.addLayer(new CustomHeadLayer(this, context.getModelSet(), context.getItemInHandRenderer()));
            this.addLayer(new SummonedWerecatEyesLayer(this));
        }

        public ResourceLocation getTextureLocation(SummonedWerecat werecat) {
            return WERECAT_LOCATIONS[werecat.getVariant()];
        }
    }
