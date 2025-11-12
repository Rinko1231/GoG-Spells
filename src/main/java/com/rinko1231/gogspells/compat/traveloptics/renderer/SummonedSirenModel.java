package com.rinko1231.gogspells.compat.traveloptics.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rinko1231.gogspells.compat.traveloptics.entity.SummonedSiren;
import gaia.config.GaiaConfig;
import gaia.entity.Siren;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BowItem;

public class SummonedSirenModel extends EntityModel<SummonedSiren> implements HeadedModel, ArmedModel {
    private static final double CYCLES_PER_BLOCK = 0.1;
    private final float[][] undulationCycle = new float[][]{{5.0F, 0.0F, -11.25F, -45.0F, -22.5F, 0.0F, 22.5F, 45.0F}, {10.0F, 10.0F, 0.0F, -22.5F, -45.0F, -22.5F, 0.0F, 22.5F}, {5.0F, 20.0F, 11.25F, 0.0F, -22.5F, -45.0F, -22.5F, 0.0F}, {0.0F, 10.0F, 22.5F, 22.5F, 0.0F, -22.5F, -45.0F, -22.5F}, {-5.0F, 0.0F, 11.25F, 45.0F, 22.5F, 0.0F, -22.5F, -45.0F}, {-10.0F, -10.0F, 0.0F, 22.5F, 45.0F, 22.5F, 0.0F, -22.5F}, {-5.0F, -20.0F, -11.25F, 0.0F, 22.5F, 45.0F, 22.5F, 0.0F}, {0.0F, -10.0F, -22.5F, -22.5F, 0.0F, 22.5F, 45.0F, 22.5F}};
    private final ModelPart root;
    private final ModelPart bodytop;
    private final ModelPart head;
    private final ModelPart headeyes;
    private final ModelPart hair1;
    private final ModelPart hair2;
    private final ModelPart chest;
    private final ModelPart leftarm;
    private final ModelPart rightarm;
    private final ModelPart leftarmextra;
    private final ModelPart rightarmextra;
    private final ModelPart leftarmextralower;
    private final ModelPart rightarmextralower;
    private final ModelPart[] tails = new ModelPart[8];

    public SummonedSirenModel(ModelPart root) {
        this.root = root.getChild("siren");
        ModelPart bodybottom = this.root.getChild("bodybottom");
        this.bodytop = bodybottom.getChild("bodymiddle").getChild("bodytop");
        ModelPart neck = this.bodytop.getChild("neck");
        this.head = neck.getChild("head");
        this.headeyes = this.head.getChild("headeyes");
        this.hair1 = neck.getChild("hair1");
        this.hair2 = this.hair1.getChild("hair2");
        this.chest = this.bodytop.getChild("chest");
        this.leftarm = this.bodytop.getChild("leftarm");
        this.rightarm = this.bodytop.getChild("rightarm");
        this.leftarmextra = this.bodytop.getChild("leftarmextra");
        this.rightarmextra = this.bodytop.getChild("rightarmextra");
        this.leftarmextralower = this.leftarmextra.getChild("leftarmextralower");
        this.rightarmextralower = this.rightarmextra.getChild("rightarmextralower");

        for(int i = 0; i < this.tails.length; ++i) {
            int index = i + 1;
            if (index == 1) {
                this.tails[i] = this.root.getChild("tail" + index);
            } else {
                this.tails[i] = this.tails[i - 1].getChild("tail" + index);
            }
        }

    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition siren = partdefinition.addOrReplaceChild("siren", CubeListBuilder.create(), PartPose.offset(0.0F, 27.0F, 0.0F));
        PartDefinition bodybottom = siren.addOrReplaceChild("bodybottom", CubeListBuilder.create().texOffs(0, 30).addBox(-3.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -13.5F, 0.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition bodymiddle = bodybottom.addOrReplaceChild("bodymiddle", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -2.5F, -1.45F, 4.0F, 3.0F, 2.0F).texOffs(0, 25).addBox(-0.5F, -2.0F, -1.55F, 1.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -1.5F, 0.45F, -0.0873F, 0.0F, 0.0F));
        PartDefinition bodytop = bodymiddle.addOrReplaceChild("bodytop", CubeListBuilder.create().texOffs(0, 16).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 6.0F, 3.0F).texOffs(36, 42).addBox(-4.5F, -6.0F, -3.0F, 9.0F, 5.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.55F, -0.0873F, 0.0F, 0.0F));
        PartDefinition neck = bodytop.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -6.0F, -1.0F, 0.0873F, 0.0F, 0.0F));
        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F).texOffs(36, 0).addBox(-3.5F, -6.5F, -3.5F, 7.0F, 7.0F, 7.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition headeyes = head.addOrReplaceChild("headeyes", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -3.1F, 6.0F, 6.0F, 0.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition finright = head.addOrReplaceChild("finright", CubeListBuilder.create().texOffs(36, 32).addBox(0.0F, -3.0F, 0.0F, 0.0F, 5.0F, 5.0F), PartPose.offsetAndRotation(-3.0F, -3.0F, -2.5F, 0.0F, -0.5236F, 0.0F));
        PartDefinition finleft = head.addOrReplaceChild("finleft", CubeListBuilder.create().texOffs(36, 32).addBox(0.0F, -3.0F, 0.0F, 0.0F, 5.0F, 5.0F), PartPose.offsetAndRotation(3.0F, -3.0F, -2.5F, 0.0F, 0.5236F, 0.0F));
        PartDefinition hair1 = neck.addOrReplaceChild("hair1", CubeListBuilder.create().texOffs(36, 14).addBox(-4.0F, -6.0F, 1.0F, 8.0F, 8.0F, 3.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition hair2 = hair1.addOrReplaceChild("hair2", CubeListBuilder.create().texOffs(36, 25).addBox(-4.5F, -1.0F, -1.5F, 9.0F, 9.0F, 3.0F), PartPose.offset(0.0F, 1.0F, 3.0F));
        bodytop.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 36).addBox(-2.3F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F).texOffs(0, 36).mirror().addBox(0.3F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F).mirror(false), PartPose.offsetAndRotation(0.0F, -5.5F, -2.5F, 0.7854F, 0.0F, 0.0F));
        PartDefinition rightarm = bodytop.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(16, 36).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(-2.5F, -4.5F, -0.5F, 0.0873F, 0.0F, 0.3491F));
        PartDefinition rightarmlower = rightarm.addOrReplaceChild("rightarmlower", CubeListBuilder.create().texOffs(16, 44).addBox(-1.005F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F), PartPose.offset(-1.0F, 5.0F, 1.0F));
        PartDefinition leftarm = bodytop.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(16, 12).addBox(0.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(2.5F, -4.5F, -0.5F, 0.0873F, 0.0F, -0.3491F));
        PartDefinition leftarmlower = leftarm.addOrReplaceChild("leftarmlower", CubeListBuilder.create().texOffs(16, 20).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F), PartPose.offset(1.005F, 5.0F, 1.0F));
        PartDefinition rightarmextra = bodytop.addOrReplaceChild("rightarmextra", CubeListBuilder.create().texOffs(24, 36).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(-2.5F, -3.5F, 0.5F, 0.2618F, 0.0F, 0.1745F));
        PartDefinition rightarmextralower = rightarmextra.addOrReplaceChild("rightarmextralower", CubeListBuilder.create().texOffs(24, 44).addBox(-1.005F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F), PartPose.offset(0.0F, 5.0F, 1.0F));
        PartDefinition leftarmextra = bodytop.addOrReplaceChild("leftarmextra", CubeListBuilder.create().texOffs(24, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(2.5F, -3.5F, 0.5F, 0.2618F, 0.0F, -0.1745F));
        PartDefinition leftarmextralower = leftarmextra.addOrReplaceChild("leftarmextralower", CubeListBuilder.create().texOffs(24, 20).addBox(-0.995F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F), PartPose.offset(0.0F, 5.0F, 1.0F));
        PartDefinition tail1 = siren.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(64, 0).addBox(-3.5F, -1.0F, -2.5F, 7.0F, 4.0F, 4.0F), PartPose.offset(0.0F, -13.0F, 0.5F));
        PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(64, 8).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 4.0F, 4.0F), PartPose.offset(0.0F, 3.0F, -2.5F));
        PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(64, 16).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 4.0F, 4.0F), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition tail4 = tail3.addOrReplaceChild("tail4", CubeListBuilder.create().texOffs(64, 16).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 4.0F, 4.0F), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition tail5 = tail4.addOrReplaceChild("tail5", CubeListBuilder.create().texOffs(64, 24).addBox(-2.0F, 0.0F, 0.5F, 4.0F, 4.0F, 3.0F), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition tail6 = tail5.addOrReplaceChild("tail6", CubeListBuilder.create().texOffs(64, 24).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 3.0F), PartPose.offset(0.0F, 4.0F, 0.5F));
        PartDefinition tail7 = tail6.addOrReplaceChild("tail7", CubeListBuilder.create().texOffs(64, 31).addBox(-1.5F, 0.0F, 0.5F, 3.0F, 3.0F, 2.0F), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition tail8 = tail7.addOrReplaceChild("tail8", CubeListBuilder.create().texOffs(64, 36).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 3.0F, 0.5F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    public void prepareMobModel(SummonedSiren siren, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(siren, limbSwing, limbSwingAmount, partialTick);
        this.chest.visible = !(Boolean) GaiaConfig.CLIENT.genderNeutral.get() && !siren.isBaby();
    }

    public void setupAnim(SummonedSiren siren, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.headeyes.visible = ageInTicks % 60.0F == 0.0F && limbSwingAmount <= 0.1F;
        this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.head.xRot = headPitch / (180F / (float)Math.PI);
        this.hair1.yRot = this.head.yRot;
        this.hair1.xRot = this.head.xRot;
        this.hair2.xRot = this.head.xRot * 0.75F;
        float armextraDefaultAngleX = 0.261799F;
        this.rightarm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.8F * limbSwingAmount * 0.5F;
        this.leftarm.xRot = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount * 0.5F;
        this.rightarm.zRot = 0.0F;
        this.leftarm.zRot = 0.0F;
        if (siren.isAggressive() && siren.getMainHandItem().getItem() instanceof BowItem) {
            this.holdingBow(ageInTicks);
        } else if (this.attackTime > 0.0F) {
            this.holdingMelee();
        }

        ModelPart var10000 = this.rightarm;
        var10000.zRot += Mth.cos(ageInTicks * 0.09F) * 0.025F + 0.025F + 0.4363323F;
        var10000 = this.rightarm;
        var10000.xRot += Mth.sin(ageInTicks * 0.067F) * 0.025F;
        var10000 = this.leftarm;
        var10000.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.025F + 0.025F + 0.4363323F;
        var10000 = this.leftarm;
        var10000.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.025F;
        this.rightarmextra.zRot = armextraDefaultAngleX;
        this.leftarmextra.zRot = -armextraDefaultAngleX;
        this.rightarmextra.xRot = armextraDefaultAngleX;
        this.leftarmextra.xRot = armextraDefaultAngleX;
        this.rightarmextralower.xRot = -armextraDefaultAngleX;
        this.leftarmextralower.xRot = -armextraDefaultAngleX;
        this.tails[0].xRot = -0.1308997F;
        this.tails[1].xRot = ((float)Math.PI / 8F);
        this.tails[2].xRot = ((float)Math.PI / 8F);
        this.tails[3].xRot = 0.785398F;
        this.tails[7].xRot = ((float)Math.PI / 8F);
        int cycleIndex = (int)((double)limbSwing * 0.1 % (double)this.undulationCycle.length);
        this.tails[4].zRot = 0.3F * Mth.cos(limbSwing * ((float)Math.PI / 180F) * this.undulationCycle[cycleIndex][4]);
        this.tails[5].zRot = 0.3F * Mth.cos(limbSwing * ((float)Math.PI / 180F) * this.undulationCycle[cycleIndex][5]);
        this.tails[6].zRot = 0.3F * Mth.cos(limbSwing * ((float)Math.PI / 180F) * this.undulationCycle[cycleIndex][6]);
        this.tails[7].zRot = 0.3F * Mth.cos(limbSwing * ((float)Math.PI / 180F) * this.undulationCycle[cycleIndex][7]);
    }

    private void holdingBow(float ageInTicks) {
        float f = Mth.sin(this.attackTime * (float)Math.PI);
        float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float)Math.PI);
        this.rightarm.zRot = -0.3F;
        this.leftarm.zRot = 0.3F;
        this.rightarm.yRot = -(0.1F - f * 0.6F);
        this.leftarm.yRot = 0.3F - f * 0.6F;
        this.rightarm.xRot = (-(float)Math.PI / 2F);
        this.leftarm.xRot = (-(float)Math.PI / 2F);
        ModelPart var10000 = this.rightarm;
        var10000.xRot -= f * 1.2F - f1 * 0.4F;
        var10000 = this.leftarm;
        var10000.xRot -= f * 1.2F - f1 * 0.4F;
        var10000 = this.rightarm;
        var10000.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        var10000 = this.leftarm;
        var10000.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        var10000 = this.rightarm;
        var10000.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
        var10000 = this.leftarm;
        var10000.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
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
        poseStack.translate((double)-0.125F, (double)0.5F, (double)0.0F);
        if (arm == HumanoidArm.LEFT) {
            poseStack.translate((double)0.125F, (double)0.0F, (double)0.0625F);
        }

        this.getArm(arm).translateAndRotate(poseStack);
    }
}
