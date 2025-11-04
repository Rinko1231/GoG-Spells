package com.rinko1231.gogspells.renderer.summonedWerecat;

import com.rinko1231.gogspells.entity.SummonedWerecat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class SummonedWerecatEyesLayer extends EyesLayer<SummonedWerecat, SummonedWerecatModel> {
    private static final RenderType WERECAT_EYES = RenderType.eyes(ResourceLocation.fromNamespaceAndPath("grimoireofgaia", "textures/entity/werecat/eyes_werecat.png"));

    public SummonedWerecatEyesLayer(RenderLayerParent<SummonedWerecat, SummonedWerecatModel> renderLayerParent) {
        super(renderLayerParent);
    }

    public RenderType renderType() {
        return WERECAT_EYES;
    }
}
