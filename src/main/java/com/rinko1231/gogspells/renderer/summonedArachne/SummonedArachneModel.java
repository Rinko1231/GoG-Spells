package com.rinko1231.gogspells.renderer.summonedArachne;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rinko1231.gogspells.entity.SummonedArachne;
import gaia.config.GaiaConfig;
import gaia.entity.Arachne;
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

public class SummonedArachneModel extends EntityModel<SummonedArachne> implements HeadedModel, ArmedModel {
    private final ModelPart root;
    private final ModelPart bodytop;
    private final ModelPart body4;
    private final ModelPart body5;
    private final ModelPart head;
    private final ModelPart headeyes;
    private final ModelPart chest;
    private final ModelPart leftarm;
    private final ModelPart rightarm;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightMiddleHindLeg;
    private final ModelPart leftMiddleHindLeg;
    private final ModelPart rightMiddleFrontLeg;
    private final ModelPart leftMiddleFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;

    public SummonedArachneModel(ModelPart root) {
        this.root = root.getChild("arachne");
        ModelPart bodybottom = this.root.getChild("body1").getChild("bodybottom");
        ModelPart body3 = this.root.getChild("body3");
        this.body4 = body3.getChild("body4");
        this.body5 = body3.getChild("body5");
        this.bodytop = bodybottom.getChild("bodymiddle").getChild("bodytop");
        this.head = this.bodytop.getChild("neck").getChild("head");
        this.headeyes = this.head.getChild("headeyes");
        this.chest = this.bodytop.getChild("chest");
        this.leftarm = this.bodytop.getChild("leftarm");
        this.rightarm = this.bodytop.getChild("rightarm");
        this.rightHindLeg = this.root.getChild("right_hind_leg");
        this.leftHindLeg = this.root.getChild("left_hind_leg");
        this.rightMiddleHindLeg = this.root.getChild("right_middle_hind_leg");
        this.leftMiddleHindLeg = this.root.getChild("left_middle_hind_leg");
        this.rightMiddleFrontLeg = this.root.getChild("right_middle_front_leg");
        this.leftMiddleFrontLeg = this.root.getChild("left_middle_front_leg");
        this.rightFrontLeg = this.root.getChild("right_front_leg");
        this.leftFrontLeg = this.root.getChild("left_front_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition arachne = partdefinition.addOrReplaceChild("arachne", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition body1 = arachne.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(64, 0).addBox(-3.0F, -4.0F, -8.0F, 6.0F, 8.0F, 8.0F), PartPose.offsetAndRotation(0.0F, -8.0F, -1.0F, -0.3491F, 0.0F, 0.0F));
        PartDefinition bodybottom = body1.addOrReplaceChild("bodybottom", CubeListBuilder.create().texOffs(0, 30).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 3.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 2.0F, -8.0F, 0.5236F, 0.0F, 0.0F));
        PartDefinition waist_r1 = bodybottom.addOrReplaceChild("waist_r1", CubeListBuilder.create().texOffs(36, 37).addBox(-3.5F, 0.0F, -2.0F, 7.0F, 5.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -3.5F, 1.5F, -0.1745F, 0.0F, 0.0F));
        PartDefinition bodymiddle = bodybottom.addOrReplaceChild("bodymiddle", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -2.5F, -1.5F, 4.0F, 3.0F, 2.0F).texOffs(0, 25).addBox(-0.5F, -2.0F, -1.6F, 1.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -3.0F, 2.0F, -0.1745F, 0.0F, 0.0F));
        PartDefinition bodytop = bodymiddle.addOrReplaceChild("bodytop", CubeListBuilder.create().texOffs(0, 16).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 6.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -2.0F, 0.5F, -0.0873F, 0.0F, 0.0F));
        PartDefinition neck = bodytop.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -7.0F, -1.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F).texOffs(36, 0).addBox(-3.5F, -6.5F, -3.5F, 7.0F, 9.0F, 7.0F).texOffs(36, 16).addBox(-4.0F, -7.0F, -2.0F, 2.0F, 2.0F, 2.0F).texOffs(36, 16).mirror().addBox(2.0F, -7.0F, -2.0F, 2.0F, 2.0F, 2.0F).mirror(false).texOffs(36, 20).addBox(-2.0F, -7.0F, 1.0F, 4.0F, 4.0F, 4.0F), PartPose.offset(0.0F, 1.0F, 0.0F));
        PartDefinition headeyes = head.addOrReplaceChild("headeyes", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -3.1F, 6.0F, 6.0F, 0.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        bodytop.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 36).addBox(-2.3F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F).texOffs(0, 36).mirror().addBox(0.3F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F).mirror(false), PartPose.offsetAndRotation(0.0F, -5.5F, -2.7F, 0.7854F, 0.0F, 0.0F));
        PartDefinition rightarm = bodytop.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(16, 12).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F), PartPose.offsetAndRotation(-2.5F, -4.5F, -1.0F, 0.0873F, 0.0F, 0.1745F));
        PartDefinition rightarmlower = rightarm.addOrReplaceChild("rightarmlower", CubeListBuilder.create().texOffs(16, 19).addBox(-1.005F, 0.0F, -2.0F, 2.0F, 5.0F, 2.0F).texOffs(36, 28).addBox(-1.5F, -0.5F, -2.5F, 2.0F, 6.0F, 3.0F), PartPose.offset(-1.0F, 4.0F, 1.0F));
        PartDefinition leftarm = bodytop.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(16, 12).mirror().addBox(0.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F).mirror(false), PartPose.offsetAndRotation(2.5F, -4.5F, -1.0F, 0.0873F, 0.0F, -0.1745F));
        leftarm.addOrReplaceChild("leftarmlower", CubeListBuilder.create().texOffs(16, 19).mirror().addBox(-0.995F, 0.0F, -2.0F, 2.0F, 5.0F, 2.0F).mirror(false).texOffs(36, 28).mirror().addBox(-0.5F, -0.5F, -2.5F, 2.0F, 6.0F, 3.0F).mirror(false), PartPose.offset(1.0F, 4.0F, 1.0F));
        PartDefinition body2 = body1.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(64, 16).addBox(-3.5F, -6.0F, 0.0F, 7.0F, 6.0F, 8.0F), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.5236F, 0.0F, 0.0F));
        PartDefinition body3 = arachne.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(78, 34).addBox(-5.0F, -4.0F, -6.0F, 10.0F, 10.0F, 12.0F), PartPose.offsetAndRotation(0.0F, -11.0F, 7.0F, -0.3491F, 0.0F, 0.0F));
        PartDefinition body4 = body3.addOrReplaceChild("body4", CubeListBuilder.create().texOffs(64, 30).addBox(-2.5F, 0.0F, -8.0F, 5.0F, 8.0F, 8.0F), PartPose.offsetAndRotation(0.0F, -1.5F, -6.0F, 0.5236F, 0.0F, 0.0F));
        PartDefinition body5 = body3.addOrReplaceChild("body5", CubeListBuilder.create().texOffs(64, 56).addBox(-2.0F, -1.5F, -1.5F, 4.0F, 3.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 6.0F, 6.0F, -0.3491F, 0.0F, 0.0F));
        PartDefinition right_hind_leg = arachne.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(92, 0).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-4.0F, -9.0F, 2.0F, 0.0F, 0.576F, -0.4887F));
        PartDefinition left_hind_leg = arachne.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(92, 0).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(4.0F, -9.0F, 2.0F, 0.0F, -0.576F, 0.4887F));
        PartDefinition right_middle_hind_leg = arachne.addOrReplaceChild("right_middle_hind_leg", CubeListBuilder.create().texOffs(92, 0).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-4.0F, -9.0F, 1.0F, 0.0F, 0.2793F, -0.5585F));
        PartDefinition left_middle_hind_leg = arachne.addOrReplaceChild("left_middle_hind_leg", CubeListBuilder.create().texOffs(92, 0).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(4.0F, -9.0F, 1.0F, 0.0F, -0.2793F, 0.5585F));
        PartDefinition right_middle_front_leg = arachne.addOrReplaceChild("right_middle_front_leg", CubeListBuilder.create().texOffs(92, 0).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-4.0F, -9.0F, 0.0F, 0.0F, -0.2793F, -0.5585F));
        PartDefinition left_middle_front_leg = arachne.addOrReplaceChild("left_middle_front_leg", CubeListBuilder.create().texOffs(92, 0).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(4.0F, -9.0F, 0.0F, 0.0F, 0.2793F, 0.5585F));
        PartDefinition right_front_leg = arachne.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(92, 0).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-4.0F, -9.0F, -1.0F, 0.0F, -0.576F, -0.5585F));
        PartDefinition left_front_leg = arachne.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(92, 0).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(4.0F, -9.0F, -1.0F, 0.0F, 0.576F, 0.5585F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    public void prepareMobModel(SummonedArachne arachne, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(arachne, limbSwing, limbSwingAmount, partialTick);
        this.chest.visible = !(Boolean) GaiaConfig.CLIENT.genderNeutral.get() && !arachne.isBaby();
    }

    public void setupAnim(SummonedArachne arachne, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.headeyes.visible = ageInTicks % 60.0F == 0.0F && limbSwingAmount <= 0.1F;
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        int attackType = arachne.getAttackType();
        if (attackType == 0) {
            this.rightarm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.8F * limbSwingAmount * 0.5F;
            this.leftarm.xRot = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount * 0.5F;
            this.rightarm.zRot = 0.0F;
            this.leftarm.zRot = 0.0F;
            if (this.attackTime > 0.0F) {
                this.holdingMelee();
            }

            ModelPart var10000 = this.rightarm;
            var10000.zRot += Mth.cos(ageInTicks * 0.09F) * 0.025F + 0.025F;
            var10000 = this.rightarm;
            var10000.xRot += Mth.sin(ageInTicks * 0.067F) * 0.025F;
            var10000 = this.leftarm;
            var10000.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.025F + 0.025F;
            var10000 = this.leftarm;
            var10000.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.025F;
            var10000 = this.rightarm;
            var10000.zRot += 0.3490659F;
            var10000 = this.leftarm;
            var10000.zRot -= 0.3490659F;
        } else if (attackType == 1) {
            this.animationThrow();
        } else if (attackType == 2) {
            this.animationCast();
        }

        float chestDefaultRotateAngleX = 0.8726646F;
        this.chest.xRot = Mth.cos(limbSwing * 0.6662F) * 0.2F * limbSwingAmount + chestDefaultRotateAngleX;
        this.body4.xRot = Mth.cos(limbSwing * 0.6662F) * 0.1F * limbSwingAmount;
        this.body5.xRot = Mth.cos(limbSwing * 0.6662F) * 0.05F * limbSwingAmount;
        ModelPart var14 = this.body4;
        var14.xRot -= 0.4363323F;
        this.moveLegs(limbSwing, limbSwingAmount);
    }

    public void holdingMelee() {
        float f6 = 1.0F - this.attackTime;
        f6 *= f6;
        f6 *= f6;
        f6 = 1.0F - f6;
        float f7 = Mth.sin(f6 * (float)Math.PI);
        float f8 = Mth.sin(this.attackTime * (float)Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
        this.leftarm.xRot = (float)((double)this.leftarm.xRot - ((double)f7 * 1.2 + (double)f8));
        ModelPart var10000 = this.leftarm;
        var10000.xRot += this.bodytop.yRot * 2.0F;
        this.leftarm.zRot = Mth.sin(this.attackTime * (float)Math.PI) * -0.4F;
    }

    private void animationThrow() {
        this.rightarm.xRot = -1.0472F;
    }

    private void animationCast() {
        this.rightarm.xRot = -1.0472F;
        this.leftarm.xRot = -1.0472F;
        this.rightarm.zRot = -0.261799F;
        this.leftarm.zRot = 0.261799F;
    }

    public void moveLegs(float limbSwing, float limbSwingAmount) {
        float f = ((float)Math.PI / 4F);
        this.rightHindLeg.zRot = -f;
        this.leftHindLeg.zRot = f;
        this.rightMiddleHindLeg.zRot = -0.58119464F;
        this.leftMiddleHindLeg.zRot = 0.58119464F;
        this.rightMiddleFrontLeg.zRot = -0.58119464F;
        this.leftMiddleFrontLeg.zRot = 0.58119464F;
        this.rightFrontLeg.zRot = -f;
        this.leftFrontLeg.zRot = f;
        float f2 = ((float)Math.PI / 8F);
        this.rightHindLeg.yRot = f;
        this.leftHindLeg.yRot = -f;
        this.rightMiddleHindLeg.yRot = f2;
        this.leftMiddleHindLeg.yRot = (-(float)Math.PI / 8F);
        this.rightMiddleFrontLeg.yRot = (-(float)Math.PI / 8F);
        this.leftMiddleFrontLeg.yRot = f2;
        this.rightFrontLeg.yRot = -f;
        this.leftFrontLeg.yRot = f;
        float f3 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
        float f4 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * limbSwingAmount;
        float f5 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * limbSwingAmount;
        float f6 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + ((float)Math.PI * 1.5F)) * 0.4F) * limbSwingAmount;
        float f7 = Math.abs(Mth.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
        float f8 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float)Math.PI) * 0.4F) * limbSwingAmount;
        float f9 = Math.abs(Mth.sin(limbSwing * 0.6662F + ((float)Math.PI / 2F)) * 0.4F) * limbSwingAmount;
        float f10 = Math.abs(Mth.sin(limbSwing * 0.6662F + ((float)Math.PI * 1.5F)) * 0.4F) * limbSwingAmount;
        ModelPart var10000 = this.rightHindLeg;
        var10000.yRot += f3;
        var10000 = this.leftHindLeg;
        var10000.yRot -= f3;
        var10000 = this.rightMiddleHindLeg;
        var10000.yRot += f4;
        var10000 = this.leftMiddleHindLeg;
        var10000.yRot -= f4;
        var10000 = this.rightMiddleFrontLeg;
        var10000.yRot += f5;
        var10000 = this.leftMiddleFrontLeg;
        var10000.yRot -= f5;
        var10000 = this.rightFrontLeg;
        var10000.yRot += f6;
        var10000 = this.leftFrontLeg;
        var10000.yRot -= f6;
        var10000 = this.rightHindLeg;
        var10000.zRot += f7;
        var10000 = this.leftHindLeg;
        var10000.zRot -= f7;
        var10000 = this.rightMiddleHindLeg;
        var10000.zRot += f8;
        var10000 = this.leftMiddleHindLeg;
        var10000.zRot -= f8;
        var10000 = this.rightMiddleFrontLeg;
        var10000.zRot += f9;
        var10000 = this.leftMiddleFrontLeg;
        var10000.zRot -= f9;
        var10000 = this.rightFrontLeg;
        var10000.zRot += f10;
        var10000 = this.leftFrontLeg;
        var10000.zRot -= f10;
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int unused) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }

    public ModelPart getHead() {
        return this.head;
    }

    private ModelPart getArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.leftarm : this.rightarm;
    }

    public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
        poseStack.translate((double)0.0F, (double)0.5F, -0.425);
        this.getArm(arm).translateAndRotate(poseStack);
    }
}