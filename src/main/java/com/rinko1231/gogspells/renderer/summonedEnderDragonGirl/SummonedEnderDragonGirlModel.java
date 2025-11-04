package com.rinko1231.gogspells.renderer.summonedEnderDragonGirl;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rinko1231.gogspells.entity.SummonedEnderDragonGirl;
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

public class SummonedEnderDragonGirlModel extends EntityModel<SummonedEnderDragonGirl> implements HeadedModel, ArmedModel {
    private final ModelPart root;
    private final ModelPart bodytop;
    private final ModelPart head;
    private final ModelPart headeyes;
    private final ModelPart chest;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart leftarm;
    private final ModelPart rightarm;
    private final ModelPart leftleg;
    private final ModelPart rightleg;
    private final ModelPart leftwing;
    private final ModelPart rightwing;
    private boolean isCarrying;

    public SummonedEnderDragonGirlModel(ModelPart root) {
        this.root = root.getChild("ender_dragon_girl");
        ModelPart bodybottom = this.root.getChild("bodybottom");
        this.bodytop = bodybottom.getChild("bodymiddle").getChild("bodytop");
        ModelPart neck = this.bodytop.getChild("neck");
        this.head = neck.getChild("head");
        this.headeyes = this.head.getChild("headeyes");
        this.chest = this.bodytop.getChild("chest");
        this.leftarm = this.bodytop.getChild("leftarm");
        this.rightarm = this.bodytop.getChild("rightarm");
        this.leftwing = this.bodytop.getChild("leftwing");
        this.rightwing = this.bodytop.getChild("rightwing");
        this.tail1 = bodybottom.getChild("tail1");
        this.tail2 = this.tail1.getChild("tail2");
        this.tail3 = this.tail2.getChild("tail3");
        this.leftleg = this.root.getChild("leftleg1");
        this.rightleg = this.root.getChild("rightleg1");
        this.setCarrying(false);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition ender_dragon_girl = partdefinition.addOrReplaceChild("ender_dragon_girl", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition bodybottom = ender_dragon_girl.addOrReplaceChild("bodybottom", CubeListBuilder.create().texOffs(0, 30).addBox(-3.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F).texOffs(64, 6).addBox(-3.5F, -0.9F, -2.0F, 7.0F, 3.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -18.5F, -0.5F, 0.0873F, 0.0F, 0.0F));
        PartDefinition bodymid = bodybottom.addOrReplaceChild("bodymiddle", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 3.0F, 2.0F).texOffs(0, 25).addBox(-0.5F, -2.0F, -2.1F, 1.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -1.5F, 1.0F, -0.0873F, 0.0F, 0.0F));
        PartDefinition bodytop = bodymid.addOrReplaceChild("bodytop", CubeListBuilder.create().texOffs(0, 16).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 6.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
        PartDefinition neck = bodytop.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -6.0F, -1.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F).texOffs(36, 0).addBox(-3.5F, -6.5F, -3.5F, 7.0F, 7.0F, 7.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("lefthorn_r1", CubeListBuilder.create().texOffs(36, 14).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 2.0F, 7.0F), PartPose.offsetAndRotation(3.0F, -6.0F, -2.5F, 0.3491F, 0.0F, 0.0F));
        head.addOrReplaceChild("righthorn_r1", CubeListBuilder.create().texOffs(36, 14).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 7.0F), PartPose.offsetAndRotation(-3.0F, -5.5F, -2.5F, 0.3491F, 0.0F, 0.0F));
        head.addOrReplaceChild("headeyes", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -3.1F, 6.0F, 6.0F, 0.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("rightear", CubeListBuilder.create().texOffs(36, 19).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(-3.0F, -2.0F, -2.0F, 0.0F, -0.5236F, 0.0F));
        head.addOrReplaceChild("leftear", CubeListBuilder.create().texOffs(36, 19).mirror().addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 4.0F).mirror(false), PartPose.offsetAndRotation(3.0F, -2.0F, -2.0F, 0.0F, 0.5236F, 0.0F));
        bodytop.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 36).addBox(-2.3F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F).texOffs(0, 36).mirror().addBox(0.3F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F).mirror(false), PartPose.offsetAndRotation(0.0F, -5.5F, -2.5F, 0.7854F, 0.0F, 0.0F));
        PartDefinition rightarm = bodytop.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(16, 12).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(-2.5F, -4.5F, -1.0F, 0.3491F, 0.0F, 0.3491F));
        PartDefinition rightarmlower = rightarm.addOrReplaceChild("rightarmlower", CubeListBuilder.create().texOffs(16, 20).addBox(-1.005F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(-1.0F, 5.0F, 1.0F, -0.7854F, 0.0F, 0.0F));
        rightarmlower.addOrReplaceChild("rightclaw", CubeListBuilder.create().texOffs(64, 0).addBox(-0.005F, 0.0F, -1.5F, 2.0F, 3.0F, 3.0F), PartPose.offsetAndRotation(-1.0F, 6.0F, -1.0F, 0.0F, 0.0F, -0.5236F));
        PartDefinition leftarm = bodytop.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(16, 12).addBox(0.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(2.5F, -4.5F, -1.0F, 0.3491F, 0.0F, -0.3491F));
        PartDefinition leftarmlower = leftarm.addOrReplaceChild("leftarmlower", CubeListBuilder.create().texOffs(16, 20).addBox(-0.995F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(1.0F, 5.0F, 1.0F, -0.7854F, 0.0F, 0.0F));
        leftarmlower.addOrReplaceChild("leftclaw", CubeListBuilder.create().texOffs(64, 0).mirror().addBox(-1.995F, 0.0F, -1.5F, 2.0F, 3.0F, 3.0F).mirror(false), PartPose.offsetAndRotation(1.0F, 6.0F, -1.0F, 0.0F, 0.0F, 0.5236F));
        bodytop.addOrReplaceChild("rightwing", CubeListBuilder.create().texOffs(104, 34).addBox(0.0F, -4.0F, 0.0F, 0.0F, 18.0F, 12.0F), PartPose.offsetAndRotation(-2.0F, -4.0F, -0.5F, 0.5236F, -0.5236F, 0.0F));
        bodytop.addOrReplaceChild("leftwing", CubeListBuilder.create().texOffs(104, 34).addBox(0.0F, -4.0F, 0.0F, 0.0F, 18.0F, 12.0F), PartPose.offsetAndRotation(2.0F, -4.0F, -0.5F, 0.5236F, 0.5236F, 0.0F));
        PartDefinition tail1 = bodybottom.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(64, 13).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -0.5F, 1.5F, 0.5236F, 0.0F, 0.0F));
        PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(64, 24).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.5236F, 0.0F, 0.0F));
        tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(64, 32).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.5672F, 0.0F, 0.0F));
        PartDefinition rightleg1 = ender_dragon_girl.addOrReplaceChild("rightleg1", CubeListBuilder.create().texOffs(86, 0).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 11.0F, 3.0F).texOffs(86, 14).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 8.0F, 4.0F), PartPose.offsetAndRotation(-2.0F, -17.0F, -0.5F, -0.2618F, 0.1745F, 0.0F));
        PartDefinition rightleg3 = rightleg1.addOrReplaceChild("rightleg3", CubeListBuilder.create().texOffs(86, 26).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 9.5F, -1.0F, -0.5236F, 0.0F, 0.0F));
        PartDefinition rightleg4 = rightleg3.addOrReplaceChild("rightleg4", CubeListBuilder.create().texOffs(86, 33).addBox(-1.5F, -2.0F, -6.0F, 3.0F, 2.0F, 6.0F), PartPose.offsetAndRotation(0.0F, -2.0F, 5.0F, 2.0944F, 0.0F, 0.0F));
        rightleg4.addOrReplaceChild("rightleg5", CubeListBuilder.create().texOffs(86, 41).addBox(-1.5F, -4.0F, -1.0F, 3.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -5.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition leftleg1 = ender_dragon_girl.addOrReplaceChild("leftleg1", CubeListBuilder.create().texOffs(86, 0).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 11.0F, 3.0F).texOffs(86, 14).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 8.0F, 4.0F), PartPose.offsetAndRotation(2.0F, -17.0F, -0.5F, -0.2618F, -0.1745F, 0.0F));
        PartDefinition leftleg3 = leftleg1.addOrReplaceChild("leftleg3", CubeListBuilder.create().texOffs(86, 26).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 9.5F, -1.0F, -0.5236F, 0.0F, 0.0F));
        PartDefinition leftleg4 = leftleg3.addOrReplaceChild("leftleg4", CubeListBuilder.create().texOffs(86, 33).addBox(-1.5F, -2.0F, -6.0F, 3.0F, 2.0F, 6.0F), PartPose.offsetAndRotation(0.0F, -2.0F, 5.0F, 2.0944F, 0.0F, 0.0F));
        leftleg4.addOrReplaceChild("leftleg5", CubeListBuilder.create().texOffs(86, 41).addBox(-1.5F, -4.0F, -1.0F, 3.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -5.0F, 0.2618F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    public void prepareMobModel(SummonedEnderDragonGirl enderDragonGirl, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(enderDragonGirl, limbSwing, limbSwingAmount, partialTick);
        this.chest.visible = !(Boolean) GaiaConfig.CLIENT.genderNeutral.get() && !enderDragonGirl.isBaby();
    }

    public void setupAnim(SummonedEnderDragonGirl enderDragonGirl, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.headeyes.visible = ageInTicks % 60.0F == 0.0F && limbSwingAmount <= 0.1F;
        this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.head.xRot = headPitch / (180F / (float)Math.PI) + 0.1745329F;
        this.rightarm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.8F * limbSwingAmount * 0.5F;
        this.leftarm.xRot = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount * 0.5F;
        this.rightarm.zRot = 0.0F;
        this.leftarm.zRot = 0.0F;
        if (this.isCarrying) {
            ModelPart var10000 = this.rightarm;
            var10000.xRot -= 0.5F;
            var10000 = this.leftarm;
            var10000.xRot -= 0.5F;
            var10000 = this.rightarm;
            var10000.zRot += 0.05F;
            var10000 = this.leftarm;
            var10000.zRot -= 0.05F;
        } else if (this.attackTime > 0.0F) {
            this.holdingMelee();
        }

        ModelPart var10 = this.rightarm;
        var10.zRot += Mth.cos(ageInTicks * 0.09F) * 0.025F + 0.025F + 0.4363323F;
        var10 = this.rightarm;
        var10.xRot += Mth.sin(ageInTicks * 0.067F) * 0.025F;
        var10 = this.leftarm;
        var10.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.025F + 0.025F + 0.4363323F;
        var10 = this.leftarm;
        var10.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.025F;
        this.rightwing.yRot = Mth.cos(ageInTicks * 0.6662F + (float)Math.PI) * 1.0F * limbSwingAmount * 0.5F;
        this.leftwing.yRot = Mth.cos(ageInTicks * 0.6662F) * 1.0F * limbSwingAmount * 0.5F;
        var10 = this.rightwing;
        var10.yRot -= ((float)Math.PI / 6F);
        var10 = this.leftwing;
        var10.yRot += ((float)Math.PI / 6F);
        this.tail1.yRot = Mth.cos(ageInTicks * 7.0F * ((float)Math.PI / 180F)) * 0.05235988F;
        this.tail2.yRot = Mth.cos(ageInTicks * 7.0F * ((float)Math.PI / 180F)) * 0.10471976F;
        this.tail3.yRot = Mth.cos(ageInTicks * 7.0F * ((float)Math.PI / 180F)) * 0.15707964F;
        this.rightleg.xRot = Mth.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount * 1.5F;
        var10 = this.rightleg;
        var10.xRot -= 0.3490659F;
        this.leftleg.xRot = Mth.cos(limbSwing * 0.6665F + (float)Math.PI) * 0.5F * limbSwingAmount * 1.5F;
        var10 = this.leftleg;
        var10.xRot -= 0.3490659F;
        this.rightleg.yRot = 0.1745329F;
        this.leftleg.yRot = -0.1745329F;
        this.rightleg.zRot = 0.0F;
        this.leftleg.zRot = 0.0F;
        if (this.riding) {
            var10 = this.rightarm;
            var10.xRot -= ((float)Math.PI / 5F);
            var10 = this.leftarm;
            var10.xRot -= ((float)Math.PI / 5F);
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

    public void setCarrying(boolean carrying) {
        this.isCarrying = carrying;
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
        poseStack.translate((double)0.0F, (double)0.5F, (double)0.0F);
        this.getArm(arm).translateAndRotate(poseStack);
    }
}
