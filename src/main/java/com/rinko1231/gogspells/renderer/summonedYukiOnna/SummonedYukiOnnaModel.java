package com.rinko1231.gogspells.renderer.summonedYukiOnna;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rinko1231.gogspells.entity.SummonedYukiOnna;
import gaia.config.GaiaConfig;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;

public class SummonedYukiOnnaModel extends EntityModel<SummonedYukiOnna> implements HeadedModel, ArmedModel {
    private final ModelPart root;
    private final ModelPart bodytop;
    private final ModelPart head;
    private final ModelPart headeyes;
    private final ModelPart chest;
    private final ModelPart leftarm;
    private final ModelPart rightarm;
    private final ModelPart leftleg;
    private final ModelPart rightleg;

    public SummonedYukiOnnaModel(ModelPart root) {
        this.root = root.getChild("yuki_onna");
        ModelPart bodybottom = this.root.getChild("bodybottom");
        this.bodytop = bodybottom.getChild("bodymiddle").getChild("bodytop");
        this.head = this.bodytop.getChild("neck").getChild("head");
        this.headeyes = this.head.getChild("headeyes");
        this.chest = this.bodytop.getChild("chest");
        this.leftarm = this.bodytop.getChild("leftarm");
        this.rightarm = this.bodytop.getChild("rightarm");
        this.leftleg = this.root.getChild("leftleg");
        this.rightleg = this.root.getChild("rightleg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition yuki_onna = partdefinition.addOrReplaceChild("yuki_onna", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition bodybottom = yuki_onna.addOrReplaceChild("bodybottom", CubeListBuilder.create().texOffs(0, 30).addBox(-3.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -13.5F, 0.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition bodymiddle = bodybottom.addOrReplaceChild("bodymiddle", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -2.5F, -1.5F, 4.0F, 3.0F, 2.0F).texOffs(0, 25).addBox(-0.5F, -2.0F, -2.1F, 1.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -1.5F, 0.5F, -0.0873F, 0.0F, 0.0F));
        PartDefinition bodytop = bodymiddle.addOrReplaceChild("bodytop", CubeListBuilder.create().texOffs(0, 16).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 6.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -2.0F, 0.5F, -0.0873F, 0.0F, 0.0F));
        PartDefinition neck = bodytop.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -6.0F, -1.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F).texOffs(36, 0).addBox(-3.5F, -6.5F, -3.5F, 7.0F, 7.0F, 7.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition headeyes = head.addOrReplaceChild("headeyes", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -3.1F, 6.0F, 6.0F, 0.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition hair1 = neck.addOrReplaceChild("hair1", CubeListBuilder.create().texOffs(36, 14).addBox(-4.0F, -6.0F, 1.0F, 8.0F, 8.0F, 3.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition hair2 = hair1.addOrReplaceChild("hair2", CubeListBuilder.create().texOffs(36, 25).addBox(-4.5F, -1.0F, -1.5F, 9.0F, 9.0F, 3.0F), PartPose.offset(0.0F, 0.0F, 3.0F));
        bodytop.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 36).addBox(-2.3F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F).texOffs(0, 36).mirror().addBox(0.3F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F).mirror(false), PartPose.offsetAndRotation(0.0F, -5.5F, -2.5F, 0.7854F, 0.0F, 0.0F));
        PartDefinition rightarm = bodytop.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(16, 12).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F).texOffs(76, 0).addBox(-2.5F, 1.0F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.offsetAndRotation(-2.5F, -4.5F, -1.0F, 0.0873F, 0.0F, 0.1745F));
        PartDefinition rightarmlower = rightarm.addOrReplaceChild("rightarmlower", CubeListBuilder.create().texOffs(16, 12).addBox(-1.005F, 0.0F, -2.5F, 2.0F, 6.0F, 2.0F).texOffs(76, 7).addBox(-1.505F, 0.0F, -3.0F, 3.0F, 6.0F, 3.0F), PartPose.offset(-1.0F, 5.0F, 1.5F));
        PartDefinition leftarm = bodytop.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(16, 12).addBox(0.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F).texOffs(64, 0).addBox(-0.5F, 1.0F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.offsetAndRotation(2.5F, -4.5F, -1.0F, 0.0873F, 0.0F, -0.1745F));
        PartDefinition leftarmlower = leftarm.addOrReplaceChild("leftarmlower", CubeListBuilder.create().texOffs(16, 18).addBox(-0.995F, 0.0F, -2.5F, 2.0F, 6.0F, 2.0F).texOffs(64, 7).addBox(-1.495F, 0.0F, -3.0F, 3.0F, 6.0F, 3.0F), PartPose.offset(1.0F, 5.0F, 1.5F));
        PartDefinition rightleg = yuki_onna.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(24, 12).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F).texOffs(104, 0).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F).mirror(false), PartPose.offset(-2.0F, -13.0F, 0.0F));
        rightleg.addOrReplaceChild("rightleglower", CubeListBuilder.create().texOffs(24, 22).addBox(-1.505F, 0.0F, 0.5F, 3.0F, 7.0F, 3.0F).texOffs(104, 11).mirror().addBox(-2.005F, 0.0F, 0.0F, 4.0F, 5.0F, 4.0F).mirror(false), PartPose.offset(0.0F, 6.0F, -2.0F));
        PartDefinition leftleg = yuki_onna.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(24, 12).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F).texOffs(88, 0).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F).mirror(false), PartPose.offset(2.0F, -13.0F, 0.0F));
        leftleg.addOrReplaceChild("leftleglower", CubeListBuilder.create().texOffs(24, 22).addBox(-1.495F, 0.0F, 0.5F, 3.0F, 7.0F, 3.0F).texOffs(88, 11).mirror().addBox(-1.995F, 0.0F, 0.0F, 4.0F, 5.0F, 4.0F).mirror(false), PartPose.offset(0.0F, 6.0F, -2.0F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    public void prepareMobModel(SummonedYukiOnna yukiOnna, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(yukiOnna, limbSwing, limbSwingAmount, partialTick);
        this.chest.visible = !(Boolean) GaiaConfig.CLIENT.genderNeutral.get() && !yukiOnna.isBaby();
    }

    public void setupAnim(SummonedYukiOnna yukiOnna, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.headeyes.visible = ageInTicks % 60.0F == 0.0F && limbSwingAmount <= 0.1F;
        this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.head.xRot = headPitch / (180F / (float)Math.PI);
        this.rightarm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.8F * limbSwingAmount * 0.5F;
        this.leftarm.xRot = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount * 0.5F;
        this.rightarm.zRot = 0.0F;
        this.leftarm.zRot = 0.0F;
        if (this.attackTime > 0.0F) {
            this.holdingMelee();
        }

        ModelPart var10000 = this.rightarm;
        var10000.zRot += Mth.cos(ageInTicks * 0.09F) * 0.025F + 0.025F + 0.1745329F;
        var10000 = this.rightarm;
        var10000.xRot += Mth.sin(ageInTicks * 0.067F) * 0.025F;
        var10000 = this.leftarm;
        var10000.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.025F + 0.025F + 0.1745329F;
        var10000 = this.leftarm;
        var10000.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.025F;
        if (yukiOnna.isShooting()) {
            Vec3 movement = yukiOnna.getDeltaMovement();
            if (movement.x * movement.x + movement.z * movement.z > (double)2.5000003E-7F) {
                this.animationShoot();
            }
        }

        this.rightleg.xRot = Mth.cos(limbSwing * 0.6662F) * 0.1F * limbSwingAmount;
        this.leftleg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.1F * limbSwingAmount;
        this.rightleg.yRot = 0.0F;
        this.leftleg.yRot = 0.0F;
        this.rightleg.zRot = -0.0349066F;
        this.leftleg.zRot = 0.0349066F;
        if (this.riding) {
            var10000 = this.rightarm;
            var10000.xRot -= ((float)Math.PI / 5F);
            var10000 = this.leftarm;
            var10000.xRot -= ((float)Math.PI / 5F);
            this.rightleg.xRot = -1.4137167F;
            this.rightleg.yRot = ((float)Math.PI / 10F);
            this.rightleg.zRot = 0.07853982F;
            this.leftleg.xRot = -1.4137167F;
            this.leftleg.yRot = (-(float)Math.PI / 10F);
            this.leftleg.zRot = -0.07853982F;
        }

    }

    public void holdingMelee() {
        float f6 = 1.0F - this.attackTime;
        f6 *= f6;
        f6 *= f6;
        f6 = 1.0F - f6;
        float f7 = Mth.sin(f6 * (float)Math.PI);
        float f8 = Mth.sin(this.attackTime * (float)Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
        this.rightarm.xRot = (float)((double)this.rightarm.xRot - ((double)f7 * 1.2 + (double)f8));
        ModelPart var10000 = this.rightarm;
        var10000.xRot += this.bodytop.yRot * 2.0F;
        this.rightarm.zRot = Mth.sin(this.attackTime * (float)Math.PI) * -0.4F;
    }

    private void animationShoot() {
        //++this.rightarm.xRot;
        --this.rightarm.xRot;
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public ModelPart getHead() {
        return this.head;
    }

    private ModelPart getArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.leftarm : this.rightarm;
    }

    public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
        poseStack.translate((double)-0.0625F, (double)0.5F, (double)0.0F);
        this.getArm(arm).translateAndRotate(poseStack);
    }
}
