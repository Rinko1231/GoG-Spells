package com.rinko1231.gogspells.compat.traveloptics.renderer;

import com.rinko1231.gogspells.compat.traveloptics.entity.SummonedSiren;
import gaia.client.ClientHandler;
import gaia.client.model.SirenModel;
import gaia.entity.Siren;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class SummonedSirenRenderer extends MobRenderer<SummonedSiren, SummonedSirenModel> {

    public static final ResourceLocation[] SIREN_LOCATIONS = new ResourceLocation[]{new ResourceLocation("grimoireofgaia", "textures/entity/siren/siren.png"), new ResourceLocation("grimoireofgaia", "textures/entity/siren/siren_halloween.png")};

    public SummonedSirenRenderer(EntityRendererProvider.Context context) {
        super(context, new SummonedSirenModel(context.bakeLayer(ClientHandler.SIREN)), 0.4F);
        this.addLayer(new CustomHeadLayer(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new ItemInHandLayer(this, context.getItemInHandRenderer()));
    }

    public ResourceLocation getTextureLocation(SummonedSiren shaman) {
        return SIREN_LOCATIONS[shaman.getVariant()];
    }
}
