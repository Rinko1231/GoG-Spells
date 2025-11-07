package com.rinko1231.gogspells.renderer.summonedEnderDragonGirl;

import com.rinko1231.gogspells.entity.SummonedEnderDragonGirl;
import gaia.client.ClientHandler;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class SummonedEnderDragonGirlRenderer  extends MobRenderer<SummonedEnderDragonGirl, SummonedEnderDragonGirlModel> {
        private final Random random = new Random();
        public static final ResourceLocation[] ENDER_DRAGON_GIRL_LOCATIONS = new ResourceLocation[]{new ResourceLocation("grimoireofgaia", "textures/entity/ender_dragon_girl/ender_dragon_girl.png")};

        public SummonedEnderDragonGirlRenderer(EntityRendererProvider.Context context) {
            super(context, new SummonedEnderDragonGirlModel(context.bakeLayer(ClientHandler.ENDER_DRAGON_GIRL)), 0.4F);
            this.addLayer(new CustomHeadLayer(this, context.getModelSet(), context.getItemInHandRenderer()));
            this.addLayer(new ItemInHandLayer(this, context.getItemInHandRenderer()));
            this.addLayer(new SummonedEnderDragonGirlEyesLayer(this));
        }

        public Vec3 getRenderOffset(SummonedEnderDragonGirl enderDragonGirl, float partialTick) {
            if (enderDragonGirl.isScreaming()) {
                double d0 = 0.02;
                return new Vec3(this.random.nextGaussian() * d0, (double)0.0F, this.random.nextGaussian() * d0);
            } else {
                return super.getRenderOffset(enderDragonGirl, partialTick);
            }
        }

        public ResourceLocation getTextureLocation(SummonedEnderDragonGirl enderDragonGirl) {
            return ENDER_DRAGON_GIRL_LOCATIONS[enderDragonGirl.getVariant()];
        }
    }
