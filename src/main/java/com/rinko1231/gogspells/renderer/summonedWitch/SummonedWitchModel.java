package com.rinko1231.gogspells.renderer.summonedWitch;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.rinko1231.gogspells.entity.SummonedWitch;
import gaia.config.GaiaConfig;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class SummonedWitchModel extends EntityModel<SummonedWitch> implements HeadedModel, ArmedModel {
    private final ModelPart root;
    private final ModelPart broom;
    private final ModelPart bodybottom;
    private final ModelPart bodymiddle;
    private final ModelPart bodytop;
    private final ModelPart head;
    private final ModelPart headeyes;
    private final ModelPart hat1;
    private final ModelPart hat3;
    private final ModelPart hat4;
    private final ModelPart hat5;
    private final ModelPart hat6;
    private final ModelPart chest;
    private final ModelPart leftarm;
    private final ModelPart rightarm;
    private final ModelPart leftleg;
    private final ModelPart rightleg;
    private final ModelPart leftleglower;
    private final ModelPart rightleglower;
    private float xRot = 0.0F;
    private float offset = 0.0F;

    public SummonedWitchModel(ModelPart root) {
        this.root = root.getChild("witch");
        this.broom = this.root.getChild("broom");
        this.bodybottom = this.root.getChild("bodybottom");
        this.bodymiddle = this.bodybottom.getChild("bodymiddle");
        this.bodytop = this.bodymiddle.getChild("bodytop");
        ModelPart neck = this.bodytop.getChild("neck");
        this.head = neck.getChild("head");
        this.headeyes = this.head.getChild("headeyes");
        this.hat1 = this.head.getChild("hat1");
        this.hat3 = this.hat1.getChild("hat2").getChild("hat3");
        this.hat4 = this.hat3.getChild("hat4");
        this.hat5 = this.hat4.getChild("hat5");
        this.hat6 = this.hat5.getChild("hat6");
        this.chest = this.bodytop.getChild("chest");
        this.leftarm = this.bodytop.getChild("leftarm");
        this.rightarm = this.bodytop.getChild("rightarm");
        this.leftleg = this.root.getChild("leftleg");
        this.rightleg = this.root.getChild("rightleg");
        this.leftleglower = this.leftleg.getChild("leftleglower");
        this.rightleglower = this.rightleg.getChild("rightleglower");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition witch = partdefinition.addOrReplaceChild("witch", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition bodybottom = witch.addOrReplaceChild("bodybottom", CubeListBuilder.create().texOffs(0, 30).addBox(-3.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F), PartPose.offset(0.0F, -13.5F, 0.5F));
        PartDefinition skirt1 = bodybottom.addOrReplaceChild("skirt1", CubeListBuilder.create().texOffs(92, 21).addBox(-3.5F, 0.5F, -3.0F, 7.0F, 2.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -1.5F, 0.0F));
        skirt1.addOrReplaceChild("skirt2", CubeListBuilder.create().texOffs(92, 29).addBox(-4.0F, -1.0F, -3.5F, 8.0F, 4.0F, 7.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 3.0F, 0.0F));
        PartDefinition bodymiddle = bodybottom.addOrReplaceChild("bodymiddle", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 3.0F, 2.0F).texOffs(0, 25).addBox(-0.5F, -2.0F, -2.1F, 1.0F, 2.0F, 0.0F), PartPose.offset(0.0F, -1.5F, 1.0F));
        PartDefinition bodytop = bodymiddle.addOrReplaceChild("bodytop", CubeListBuilder.create().texOffs(0, 16).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 6.0F, 3.0F), PartPose.offset(0.0F, -2.0F, 0.0F));
        PartDefinition amulet = bodytop.addOrReplaceChild("amulet", CubeListBuilder.create().texOffs(36, 55).addBox(-1.0F, -1.0F, -3.5F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -6.0F, -1.0F, 0.4363F, 0.0F, 0.0F));
        PartDefinition mantle = bodytop.addOrReplaceChild("mantle", CubeListBuilder.create().texOffs(92, 8).addBox(-4.5F, 0.5F, 0.0F, 9.0F, 10.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -6.0F, -1.0F, 0.1745F, 0.0F, 0.0F));
        PartDefinition neck = bodytop.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, -7.0F, -1.5F));
        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F).texOffs(36, 0).addBox(-3.5F, -6.5F, -3.5F, 7.0F, 7.0F, 7.0F), PartPose.offset(0.0F, 1.0F, 0.5F));
        PartDefinition headeyes = head.addOrReplaceChild("headeyes", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -3.1F, 6.0F, 6.0F, 0.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition hat1 = head.addOrReplaceChild("hat1", CubeListBuilder.create().texOffs(36, 14).addBox(-7.0F, -1.0F, -7.0F, 14.0F, 2.0F, 14.0F), PartPose.offset(0.0F, -4.0F, 0.0F));
        PartDefinition hat2 = hat1.addOrReplaceChild("hat2", CubeListBuilder.create().texOffs(36, 30).addBox(-4.0F, -5.0F, 0.0F, 8.0F, 5.0F, 8.0F), PartPose.offset(0.0F, -1.0F, -4.0F));
        PartDefinition hat3 = hat2.addOrReplaceChild("hat3", CubeListBuilder.create().texOffs(68, 30).addBox(-6.0F, -6.0F, 0.0F, 6.0F, 6.0F, 6.0F), PartPose.offset(3.0F, -5.0F, 1.0F));
        PartDefinition hat4 = hat3.addOrReplaceChild("hat4", CubeListBuilder.create().texOffs(36, 43).addBox(-4.0F, -4.0F, 0.0F, 4.0F, 4.0F, 4.0F), PartPose.offset(-1.0F, -6.0F, 1.0F));
        PartDefinition hat5 = hat4.addOrReplaceChild("hat5", CubeListBuilder.create().texOffs(52, 43).addBox(-3.0F, -3.0F, 0.0F, 3.0F, 3.0F, 3.0F), PartPose.offset(-0.5F, -4.0F, 0.5F));
        PartDefinition hat6 = hat5.addOrReplaceChild("hat6", CubeListBuilder.create().texOffs(36, 51).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(-0.5F, -3.0F, 0.5F));
        bodytop.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 36).addBox(-2.3F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F).texOffs(0, 36).mirror().addBox(0.3F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F).mirror(false), PartPose.offsetAndRotation(0.0F, -4.5F, -2.5F, 0.7854F, 0.0F, 0.0F));
        PartDefinition rightarm = bodytop.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(16, 12).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F).texOffs(92, 0).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F), PartPose.offset(-2.5F, -4.5F, -1.5F));
        PartDefinition rightarmlower = rightarm.addOrReplaceChild("rightarmlower", CubeListBuilder.create().texOffs(16, 20).addBox(-1.005F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F), PartPose.offset(-1.0F, 5.0F, 1.0F));
        PartDefinition leftarm = bodytop.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(16, 12).mirror().addBox(0.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F).mirror(false).texOffs(92, 0).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F).mirror(false), PartPose.offset(2.5F, -4.5F, -1.5F));
        leftarm.addOrReplaceChild("leftarmlower", CubeListBuilder.create().texOffs(16, 20).mirror().addBox(-0.995F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F).mirror(false), PartPose.offset(1.0F, 5.0F, 1.0F));
        PartDefinition rightleg = witch.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(24, 22).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F), PartPose.offset(-2.0F, -13.0F, 0.5F));
        PartDefinition rightleglower = rightleg.addOrReplaceChild("rightleglower", CubeListBuilder.create().texOffs(24, 32).addBox(-1.505F, 0.0F, 0.0F, 3.0F, 7.0F, 3.0F).texOffs(92, 40).addBox(-2.005F, 3.0F, -0.5F, 4.0F, 4.0F, 4.0F), PartPose.offset(0.0F, 6.0F, -1.5F));
        PartDefinition leftleg = witch.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(24, 12).mirror().addBox(-1.5F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F).mirror(false), PartPose.offset(2.0F, -13.0F, 0.5F));
        leftleg.addOrReplaceChild("leftleglower", CubeListBuilder.create().texOffs(24, 32).mirror().addBox(-1.495F, 0.0F, 0.0F, 3.0F, 7.0F, 3.0F).mirror(false).texOffs(92, 40).mirror().addBox(-1.995F, 3.0F, -0.5F, 4.0F, 4.0F, 4.0F).mirror(false), PartPose.offset(0.0F, 6.0F, -1.5F));
        PartDefinition broom = witch.addOrReplaceChild("broom", CubeListBuilder.create().texOffs(112, 43).addBox(-0.5F, -11.0F, -0.5F, 1.0F, 20.0F, 1.0F).texOffs(116, 43).addBox(-1.5F, 8.0F, -1.5F, 3.0F, 10.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -12.0F, 0.5F, 1.5708F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    public void prepareMobModel(SummonedWitch witch, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(witch, limbSwing, limbSwingAmount, partialTick);
        this.chest.visible = !(Boolean) GaiaConfig.CLIENT.genderNeutral.get() && !witch.isBaby();
        this.hat1.zRot = (-(float)Math.PI / 6F);
        this.hat3.xRot = -0.34906584F;
        this.hat3.zRot = 0.34906584F;
        this.hat4.xRot = -0.34906584F;
        this.hat4.zRot = 0.34906584F;
        this.hat5.xRot = -0.34906584F;
        this.hat5.zRot = 0.34906584F;
        this.hat6.xRot = 0.17453292F;
        this.hat6.zRot = -0.17453292F;
    }

    public void setupAnim(SummonedWitch witch, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.headeyes.visible = ageInTicks % 60.0F == 0.0F && limbSwingAmount <= 0.1F;
        this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.head.xRot = headPitch / (180F / (float)Math.PI);
        this.root.y = 24.0F;
        float defaultAngle = 0.0F;
        boolean moveExtremities;
        float rightArmAngleMoving;
        float leftArmAngleMoving;
        if (witch.isRidingBroom()) {
            moveExtremities = false;
            rightArmAngleMoving = ((float)Math.PI / 6F);
            leftArmAngleMoving = ((float)Math.PI / 4F);
            this.rightarm.xRot = -rightArmAngleMoving;
            this.rightarm.yRot = -rightArmAngleMoving;
            this.rightarm.zRot = 0.0F;
            this.leftarm.xRot = -leftArmAngleMoving;
            this.leftarm.yRot = leftArmAngleMoving;
            this.leftarm.zRot = 0.0F;
            this.bodytop.xRot = 0.43633232F;
            this.bodymiddle.xRot = 0.17453292F;
            this.bodybottom.xRot = 0.2617994F;
            this.rightleg.xRot = -1.3962634F;
            this.rightleg.yRot = -0.08726646F;
            this.leftleg.xRot = (-(float)Math.PI / 3F);
            this.leftleg.yRot = 0.08726646F;
            this.rightleglower.xRot = ((float)Math.PI / 4F);
            this.leftleglower.xRot = 1.3089969F;
            this.broom.visible = true;
            this.xRot = Mth.cos(0.10471976F * ageInTicks) * 0.1F;
            this.root.xRot = this.xRot - 0.3F;
            this.offset = Mth.cos(ageInTicks * 0.18F) * 0.9F;
            this.root.y = 24.0F - this.offset;
        } else {
            moveExtremities = true;
            rightArmAngleMoving = defaultAngle;
            leftArmAngleMoving = defaultAngle;
            this.rightarm.xRot = defaultAngle;
            this.rightarm.yRot = defaultAngle;
            this.leftarm.xRot = defaultAngle;
            this.leftarm.yRot = defaultAngle;
            this.bodytop.xRot = defaultAngle;
            this.bodymiddle.xRot = defaultAngle;
            this.bodybottom.xRot = defaultAngle;
            this.rightleg.xRot = defaultAngle;
            this.rightleg.yRot = defaultAngle;
            this.rightleg.zRot = defaultAngle;
            this.leftleg.xRot = defaultAngle;
            this.leftleg.yRot = defaultAngle;
            this.leftleg.zRot = defaultAngle;
            this.rightleglower.xRot = defaultAngle;
            this.leftleglower.xRot = defaultAngle;
            this.broom.visible = false;
            this.xRot = 0.0F;
            this.root.xRot = 0.0F;
        }

        if (moveExtremities) {
            this.rightarm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.8F * limbSwingAmount * 0.5F;
            this.leftarm.xRot = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount * 0.5F;
            ModelPart var10000 = this.rightarm;
            var10000.xRot += Mth.sin(ageInTicks * 0.067F) * 0.025F;
            var10000 = this.leftarm;
            var10000.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.025F;
            this.rightarm.zRot = 0.0F;
            this.leftarm.zRot = 0.0F;
            if (this.attackTime > 0.0F) {
                this.holdingMelee();
            }

            var10000 = this.rightarm;
            var10000.zRot += Mth.cos(ageInTicks * 0.09F) * 0.025F + 0.025F + 0.1745329F;
            var10000 = this.leftarm;
            var10000.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.025F + 0.025F + 0.1745329F;
        }

        if (!moveExtremities) {
            this.rightarm.xRot = -rightArmAngleMoving;
            this.leftarm.xRot = -leftArmAngleMoving;
        }

        if (moveExtremities) {
            this.rightleg.xRot = Mth.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount;
            this.leftleg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.5F * limbSwingAmount;
        }

        if (this.riding && !witch.isRidingBroom()) {
            ModelPart var14 = this.rightarm;
            var14.xRot -= ((float)Math.PI / 5F);
            var14 = this.leftarm;
            var14.xRot -= ((float)Math.PI / 5F);
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
        poseStack.translate((double)0.0F, (double)0.5F, (double)0.25F);
        poseStack.mulPose(Axis.XP.rotation(this.xRot));
        poseStack.translate(0.0F, -this.offset * 0.0725F, 0.0F);
        this.getArm(arm).translateAndRotate(poseStack);
    }
}
