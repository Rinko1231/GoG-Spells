package com.rinko1231.gogspells.renderer.summonedValkyrie;

import com.rinko1231.gogspells.entity.SummonedValkyrie;
import gaia.client.ClientHandler;
import gaia.client.renderer.layer.AuraLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class SummonedValkyrieRenderer extends MobRenderer<SummonedValkyrie, SummonedValkyrieModel> {
    public static final ResourceLocation[] VALKYRIE_LOCATIONS = new ResourceLocation[]{new ResourceLocation("grimoireofgaia", "textures/entity/valkyrie/valkyrie.png")};

    public SummonedValkyrieRenderer(EntityRendererProvider.Context context) {
        super(context, new SummonedValkyrieModel(context.bakeLayer(ClientHandler.VALKYRIE)), 0.5F);
        this.addLayer(new CustomHeadLayer(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new ItemInHandLayer(this, context.getItemInHandRenderer()));
        this.addLayer(new AuraLayer(this, () -> new SummonedValkyrieModel(context.bakeLayer(ClientHandler.VALKYRIE))));
    }

    public ResourceLocation getTextureLocation(SummonedValkyrie valkyrie) {
        return VALKYRIE_LOCATIONS[valkyrie.getVariant()];
    }
}
