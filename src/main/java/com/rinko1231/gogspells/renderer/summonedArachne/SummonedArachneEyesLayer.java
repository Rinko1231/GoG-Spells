package com.rinko1231.gogspells.renderer.summonedArachne;

import com.rinko1231.gogspells.entity.SummonedArachne;
import gaia.client.model.ArachneModel;
import gaia.entity.Arachne;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class SummonedArachneEyesLayer extends EyesLayer<SummonedArachne, SummonedArachneModel> {
    private static final RenderType ARACHNE_EYES = RenderType.eyes(ResourceLocation.fromNamespaceAndPath("grimoireofgaia", "textures/entity/arachne/arachne_eyes.png"));

    public SummonedArachneEyesLayer(RenderLayerParent<SummonedArachne, SummonedArachneModel> layerParent) {
        super(layerParent);
    }

    public RenderType renderType() {
        return ARACHNE_EYES;
    }
}
