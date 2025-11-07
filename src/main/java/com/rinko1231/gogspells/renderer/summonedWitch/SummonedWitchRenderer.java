package com.rinko1231.gogspells.renderer.summonedWitch;

import com.rinko1231.gogspells.entity.SummonedWitch;
import gaia.client.ClientHandler;
import gaia.client.renderer.layer.GaiaItemInHandLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;

public class SummonedWitchRenderer extends MobRenderer<SummonedWitch, SummonedWitchModel> {
    public static final ResourceLocation[] WITCH_LOCATIONS = new ResourceLocation[]{new ResourceLocation("grimoireofgaia", "textures/entity/witch/witch01.png"), new ResourceLocation("grimoireofgaia", "textures/entity/witch/witch02.png")};

    public SummonedWitchRenderer(EntityRendererProvider.Context context) {
        super(context, new SummonedWitchModel(context.bakeLayer(ClientHandler.WITCH)), 0.25F);
        this.addLayer(new CustomHeadLayer(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new GaiaItemInHandLayer(this, HumanoidArm.RIGHT, context.getItemInHandRenderer()));
    }

    public ResourceLocation getTextureLocation(SummonedWitch witch) {
        return WITCH_LOCATIONS[witch.getVariant()];
    }
}
