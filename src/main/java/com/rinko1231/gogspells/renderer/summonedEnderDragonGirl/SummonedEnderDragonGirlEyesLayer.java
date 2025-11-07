package com.rinko1231.gogspells.renderer.summonedEnderDragonGirl;

import com.rinko1231.gogspells.entity.SummonedEnderDragonGirl;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class SummonedEnderDragonGirlEyesLayer  extends EyesLayer<SummonedEnderDragonGirl, SummonedEnderDragonGirlModel> {
        private static final RenderType ENDER_DRAGON_GIRL_EYES = RenderType.eyes(new ResourceLocation("grimoireofgaia", "textures/entity/ender_dragon_girl/eyes_ender_dragon_girl.png"));

        public SummonedEnderDragonGirlEyesLayer(RenderLayerParent<SummonedEnderDragonGirl, SummonedEnderDragonGirlModel> renderLayerParent) {
            super(renderLayerParent);
        }

        public RenderType renderType() {
            return ENDER_DRAGON_GIRL_EYES;
        }
}
