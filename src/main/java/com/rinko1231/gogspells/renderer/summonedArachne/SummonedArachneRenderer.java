package com.rinko1231.gogspells.renderer.summonedArachne;

import com.rinko1231.gogspells.entity.SummonedArachne;
import gaia.client.ClientHandler;
import gaia.client.model.ArachneModel;
import gaia.client.renderer.layer.ArachneEyesLayer;
import gaia.entity.Arachne;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class SummonedArachneRenderer extends MobRenderer<SummonedArachne, SummonedArachneModel> {
    public static final ResourceLocation[] ARACHNE_LOCATIONS = new ResourceLocation[]{new ResourceLocation("grimoireofgaia", "textures/entity/arachne/arachne.png")};

    public SummonedArachneRenderer(EntityRendererProvider.Context context) {
        super(context, new SummonedArachneModel(context.bakeLayer(ClientHandler.ARACHNE)), 0.7F);
        this.addLayer(new CustomHeadLayer(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new ItemInHandLayer(this, context.getItemInHandRenderer()));
        this.addLayer(new SummonedArachneEyesLayer(this));
    }

    protected float getFlipDegrees(SummonedArachne arachne) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(SummonedArachne arachne) {
        return ARACHNE_LOCATIONS[arachne.getVariant()];
    }
}
