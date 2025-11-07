package com.rinko1231.gogspells.renderer.summonedMummyGravemite;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rinko1231.gogspells.entity.SummonedMummy;
import gaia.config.GaiaConfig;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class SummonedMummyModel extends EntityModel<SummonedMummy> implements HeadedModel, ArmedModel {
    private final ModelPart root;
    private final ModelPart bodytop;
    private final ModelPart head;
    private final ModelPart headeyes;
    private final ModelPart chest;
    private final ModelPart leftarm;
    private final ModelPart rightarm;
    private final ModelPart leftleg;
    private final ModelPart rightleg;
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
    public SummonedMummyModel(ModelPart root) {
        this.root = root.getChild("mummy");
        ModelPart bodybottom = this.root.getChild("bodybottom");
        this.bodytop = bodybottom.getChild("bodymiddle").getChild("bodytop");
        ModelPart neck = this.bodytop.getChild("neck");
        this.head = neck.getChild("head");
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
        PartDefinition mummy = partdefinition.addOrReplaceChild("mummy", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition bodybottom = mummy.addOrReplaceChild("bodybottom", CubeListBuilder.create().texOffs(0, 30).addBox(-3.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F).texOffs(68, 0).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 8.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -13.5F, 0.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition bodymiddle = bodybottom.addOrReplaceChild("bodymiddle", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 3.0F, 2.0F).texOffs(0, 25).addBox(-0.5F, -2.0F, -2.1F, 1.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -1.5F, 1.0F, -0.0873F, 0.0F, 0.0F));
        PartDefinition bodytop = bodymiddle.addOrReplaceChild("bodytop", CubeListBuilder.create().texOffs(0, 16).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 6.0F, 3.0F).texOffs(104, 0).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 6.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
        PartDefinition neck = bodytop.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -6.0F, -1.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F).texOffs(36, 0).addBox(-3.5F, -6.5F, -3.5F, 7.0F, 7.0F, 7.0F).texOffs(36, 14).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition headeyes = head.addOrReplaceChild("headeyes", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 0.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        bodytop.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 36).addBox(-2.3F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F).texOffs(0, 36).mirror().addBox(0.3F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F).mirror(false), PartPose.offsetAndRotation(0.0F, -5.5F, -2.5F, 0.7854F, 0.0F, 0.0F));
        PartDefinition rightarm = bodytop.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(16, 12).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F).texOffs(92, 0).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-2.5F, -4.5F, -1.0F, -1.4835F, 0.0F, 0.0F));
        PartDefinition rightarmbandage = rightarm.addOrReplaceChild("rightarmbandage", CubeListBuilder.create().texOffs(36, 30).addBox(-2.0F, -1.0F, 1.0F, 2.0F, 12.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition leftarm = bodytop.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(16, 12).mirror().addBox(0.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F).mirror(false).texOffs(92, 0).mirror().addBox(0.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(2.5F, -4.5F, -1.0F, -1.4835F, 0.0F, 0.0F));
        PartDefinition leftarmbandage = leftarm.addOrReplaceChild("leftarmbandage", CubeListBuilder.create().texOffs(36, 30).addBox(0.0F, -1.0F, 1.0F, 2.0F, 12.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        mummy.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(24, 12).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 14.0F, 3.0F).texOffs(92, 13).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 14.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-2.0F, -13.0F, 0.0F, 0.0F, 0.0F, 0.0349F));
        mummy.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(24, 12).mirror().addBox(-1.5F, -1.0F, -1.5F, 3.0F, 14.0F, 3.0F).mirror(false).texOffs(92, 13).mirror().addBox(-1.5F, -1.0F, -1.5F, 3.0F, 14.0F, 3.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(2.0F, -13.0F, 0.0F, 0.0F, 0.0F, -0.0349F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    public void prepareMobModel(SummonedMummy mummy, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(mummy, limbSwing, limbSwingAmount, partialTick);
        this.chest.visible = !(Boolean) GaiaConfig.CLIENT.genderNeutral.get() && !mummy.isBaby();
    }

    public void setupAnim(SummonedMummy mummy, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.headeyes.visible = ageInTicks % 60.0F == 0.0F && limbSwingAmount <= 0.1F;
        this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.head.xRot = headPitch / (180F / (float)Math.PI);
        this.rightarm.zRot = 0.0F;
        this.leftarm.zRot = 0.0F;
        if (this.attackTime > 0.0F) {
            this.holdingMelee();
        }

        if (!this.riding) {
            AnimationUtils.animateZombieArms(this.leftarm, this.rightarm, mummy.isAggressive(), this.attackTime, ageInTicks);
        } else {
            this.rightarm.zRot = Mth.cos(ageInTicks * 0.09F) * 0.025F + 0.025F + 0.2617994F;
            this.rightarm.xRot = Mth.sin(ageInTicks * 0.067F) * 0.025F;
            this.leftarm.zRot = Mth.cos(ageInTicks * 0.09F) * 0.025F + 0.025F + 0.2617994F;
            this.leftarm.xRot = Mth.sin(ageInTicks * 0.067F) * 0.025F;
        }

        this.rightleg.xRot = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount * 0.5F;
        this.leftleg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.8F * limbSwingAmount * 0.5F;
        this.rightleg.yRot = 0.0F;
        this.leftleg.yRot = 0.0F;
        this.rightleg.zRot = -0.0349066F;
        this.leftleg.zRot = 0.0349066F;
        if (this.riding) {
            ModelPart var10000 = this.rightarm;
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
        ModelPart var10000 = this.rightarm;
        var10000.xRot -= (float)((double)this.rightarm.xRot - ((double)f7 * 1.2 + (double)f8));
        var10000 = this.rightarm;
        var10000.yRot += this.bodytop.yRot * 2.0F;
        this.rightarm.zRot = Mth.sin(this.attackTime * (float)Math.PI) * -0.4F;
        var10000 = this.leftarm;
        var10000.xRot -= (float)((double)this.leftarm.xRot - ((double)f7 * 1.2 + (double)f8));
        var10000 = this.leftarm;
        var10000.yRot += this.bodytop.yRot * 2.0F;
        var10000 = this.leftarm;
        var10000.zRot -= Mth.sin(this.attackTime * (float)Math.PI) * -0.4F;
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
        poseStack.translate((double)-0.0625F, (double)0.5F, (double)0.0F);
        this.getArm(arm).translateAndRotate(poseStack);
    }
}
