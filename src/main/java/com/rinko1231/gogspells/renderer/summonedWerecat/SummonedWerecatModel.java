package com.rinko1231.gogspells.renderer.summonedWerecat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rinko1231.gogspells.entity.SummonedWerecat;
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

public class SummonedWerecatModel  extends EntityModel<SummonedWerecat> implements HeadedModel, ArmedModel {
        private final ModelPart root;
        private final ModelPart bodytop;
        private final ModelPart head;
        private final ModelPart rightear;
        private final ModelPart leftear;
        private final ModelPart headeyes;
        private final ModelPart chest;
        private final ModelPart tail1;
        private final ModelPart tail2;
        private final ModelPart leftarm;
        private final ModelPart rightarm;
        private final ModelPart leftleg;
        private final ModelPart rightleg;

        public SummonedWerecatModel(ModelPart root) {
            this.root = root.getChild("werecat");
            ModelPart bodybottom = this.root.getChild("bodybottom");
            this.bodytop = bodybottom.getChild("bodymiddle").getChild("bodytop");
            this.head = this.bodytop.getChild("neck").getChild("head");
            this.rightear = this.head.getChild("rightear");
            this.leftear = this.head.getChild("leftear");
            this.headeyes = this.head.getChild("headeyes");
            this.chest = this.bodytop.getChild("chest");
            this.leftarm = this.bodytop.getChild("leftarm");
            this.rightarm = this.bodytop.getChild("rightarm");
            this.tail1 = bodybottom.getChild("tail1");
            this.tail2 = this.tail1.getChild("tail2");
            this.leftleg = this.root.getChild("leftleg");
            this.rightleg = this.root.getChild("rightleg");
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();
            PartDefinition werecat = partdefinition.addOrReplaceChild("werecat", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
            PartDefinition bodybottom = werecat.addOrReplaceChild("bodybottom", CubeListBuilder.create().texOffs(0, 30).addBox(-3.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F).texOffs(64, 19).addBox(-3.5F, -2.0F, -2.0F, 7.0F, 4.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -13.5F, 0.0F, 0.3927F, 0.0F, 0.0F));
            PartDefinition bodymiddle = bodybottom.addOrReplaceChild("bodymiddle", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -2.5F, -1.5F, 4.0F, 3.0F, 2.0F).texOffs(0, 25).addBox(-0.5F, -2.0F, -1.6F, 1.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -1.5F, 0.5F, -0.1745F, 0.0F, 0.0F));
            PartDefinition bodytop = bodymiddle.addOrReplaceChild("bodytop", CubeListBuilder.create().texOffs(0, 16).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 6.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -2.0F, 0.5F, -0.1745F, 0.0F, 0.0F));
            PartDefinition neck = bodytop.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F).texOffs(36, 35).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -6.0F, -1.0F, -0.0436F, 0.0F, 0.0F));
            PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F).texOffs(36, 0).addBox(-3.5F, -6.5F, -3.5F, 7.0F, 7.0F, 7.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
            PartDefinition headeyes = head.addOrReplaceChild("headeyes", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -3.1F, 6.0F, 6.0F, 0.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
            PartDefinition rightear = head.addOrReplaceChild("rightear", CubeListBuilder.create().texOffs(36, 28).addBox(0.0F, 0.0F, -4.0F, 3.0F, 3.0F, 4.0F), PartPose.offsetAndRotation(-2.0F, -6.0F, 3.0F, -0.7854F, 0.7854F, 0.0F));
            head.addOrReplaceChild("leftear", CubeListBuilder.create().texOffs(36, 28).mirror().addBox(-3.0F, 0.0F, -4.0F, 3.0F, 3.0F, 4.0F).mirror(false), PartPose.offsetAndRotation(2.0F, -6.0F, 3.0F, -0.7854F, -0.7854F, 0.0F));
            PartDefinition hair1 = head.addOrReplaceChild("hair1", CubeListBuilder.create().texOffs(36, 14).addBox(-4.0F, -6.0F, 1.0F, 8.0F, 4.0F, 3.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
            PartDefinition hair2 = hair1.addOrReplaceChild("hair2", CubeListBuilder.create().texOffs(36, 21).addBox(-4.5F, -3.0F, 1.5F, 9.0F, 4.0F, 3.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
            bodytop.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 36).addBox(-2.3F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F).texOffs(0, 36).mirror().addBox(0.3F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F).mirror(false), PartPose.offsetAndRotation(0.0F, -5.5F, -2.5F, 0.7854F, 0.0F, 0.0F));
            PartDefinition rightarm = bodytop.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(16, 12).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(-2.5F, -4.5F, -1.0F, 0.0F, 0.0F, 0.2618F));
            PartDefinition rightarmlower = rightarm.addOrReplaceChild("rightarmlower", CubeListBuilder.create().texOffs(64, 0).addBox(-1.5F, -3.0F, -2.5F, 2.0F, 8.0F, 3.0F), PartPose.offsetAndRotation(-1.0F, 5.0F, 1.0F, -0.5236F, 0.0F, 0.0F));
            PartDefinition rightarmhand = rightarmlower.addOrReplaceChild("rightarmhand", CubeListBuilder.create().texOffs(64, 11).addBox(0.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F), PartPose.offsetAndRotation(-0.5F, 5.0F, -1.0F, 0.0F, 0.0F, -0.2618F));
            PartDefinition leftarm = bodytop.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(16, 12).mirror().addBox(0.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F).mirror(false), PartPose.offsetAndRotation(2.5F, -4.5F, -1.0F, 0.0F, 0.0F, -0.2618F));
            PartDefinition leftarmlower = leftarm.addOrReplaceChild("leftarmlower", CubeListBuilder.create().texOffs(64, 0).mirror().addBox(-0.5F, -3.0F, -2.5F, 2.0F, 8.0F, 3.0F).mirror(false), PartPose.offsetAndRotation(1.0F, 5.0F, 1.0F, -0.5236F, 0.0F, 0.0F));
            leftarmlower.addOrReplaceChild("leftarmhand", CubeListBuilder.create().texOffs(64, 11).mirror().addBox(-2.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F).mirror(false), PartPose.offset(0.5F, 5.0F, -1.0F));
            PartDefinition tail1 = bodybottom.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(64, 27).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -2.0F, 1.0F, 0.2618F, 0.0F, 0.0F));
            PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(64, 34).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.1309F, 0.0F, 0.0F));
            PartDefinition leftleg = werecat.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(88, 0).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 8.0F, 3.0F), PartPose.offsetAndRotation(2.5F, -12.5F, -0.5F, -0.2618F, 0.0F, 0.0436F));
            PartDefinition leftleglower = leftleg.addOrReplaceChild("leftleglower", CubeListBuilder.create().texOffs(88, 11).addBox(-1.495F, 0.0F, 0.0F, 3.0F, 8.0F, 2.0F), PartPose.offset(0.0F, 4.0F, 1.5F));
            PartDefinition leftlegfoot = leftleglower.addOrReplaceChild("leftlegfoot", CubeListBuilder.create().texOffs(88, 21).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 7.0F, 2.0F, 0.2618F, 0.0F, 0.0F));
            PartDefinition rightleg = werecat.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(88, 0).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 8.0F, 3.0F).texOffs(36, 35).addBox(-2.0F, 3.0F, -2.0F, 4.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(-2.5F, -12.5F, -0.5F, -0.2618F, 0.0F, -0.0436F));
            PartDefinition rightleglower = rightleg.addOrReplaceChild("rightleglower", CubeListBuilder.create().texOffs(88, 11).addBox(-1.505F, 0.0F, 0.0F, 3.0F, 8.0F, 2.0F), PartPose.offset(0.0F, 4.0F, 1.5F));
            PartDefinition rightlegfoot = rightleglower.addOrReplaceChild("rightlegfoot", CubeListBuilder.create().texOffs(88, 21).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 7.0F, 2.0F, 0.2618F, 0.0F, 0.0F));
            return LayerDefinition.create(meshdefinition, 128, 64);
        }

        public void prepareMobModel(SummonedWerecat werecat, float limbSwing, float limbSwingAmount, float partialTick) {
            super.prepareMobModel(werecat, limbSwing, limbSwingAmount, partialTick);
            this.chest.visible = !(Boolean) GaiaConfig.CLIENT.genderNeutral.get() && !werecat.isBaby();
        }

        public void setupAnim(SummonedWerecat werecat, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.headeyes.visible = ageInTicks % 60.0F == 0.0F && limbSwingAmount <= 0.1F;
            this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
            this.head.xRot = headPitch / (180F / (float)Math.PI);
            float earDefaultAngleX = (-(float)Math.PI / 4F);
            this.rightear.xRot = Mth.cos(ageInTicks * 7.0F * ((float)Math.PI / 180F)) * 0.06981317F;
            ModelPart var10000 = this.rightear;
            var10000.xRot += earDefaultAngleX;
            this.leftear.xRot = Mth.cos(ageInTicks * 7.0F * ((float)Math.PI / 180F)) * 0.06981317F;
            var10000 = this.leftear;
            var10000.xRot += earDefaultAngleX;
            this.rightarm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.8F * limbSwingAmount * 0.5F;
            this.leftarm.xRot = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount * 0.5F;
            this.rightarm.zRot = 0.0F;
            this.leftarm.zRot = 0.0F;
            if (this.attackTime > 0.0F) {
                this.holdingMelee();
            }

            var10000 = this.rightarm;
            var10000.zRot += Mth.cos(ageInTicks * 0.09F) * 0.025F + 0.025F + 0.1745329F;
            var10000 = this.rightarm;
            var10000.xRot += Mth.sin(ageInTicks * 0.067F) * 0.025F;
            var10000 = this.leftarm;
            var10000.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.025F + 0.025F + 0.1745329F;
            var10000 = this.leftarm;
            var10000.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.025F;
            if (werecat.isFleeing()) {
                Vec3 movement = werecat.getDeltaMovement();
                if (movement.x * movement.x + movement.z * movement.z > (double)2.5000003E-7F) {
                    this.animationFlee();
                }
            }

            this.tail1.yRot = Mth.cos(ageInTicks * 7.0F * ((float)Math.PI / 180F)) * 0.2617994F;
            this.tail2.yRot = Mth.cos(ageInTicks * 7.0F * ((float)Math.PI / 180F)) * 0.34906584F;
            this.rightleg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
            this.leftleg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
            var10000 = this.rightleg;
            var10000.xRot -= 0.4363323F;
            var10000 = this.leftleg;
            var10000.xRot -= 0.4363323F;
            this.rightleg.yRot = -0.0872665F;
            this.leftleg.yRot = 0.0872665F;
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
            var10000.yRot += this.bodytop.yRot * 2.0F;
            this.rightarm.zRot = Mth.sin(this.attackTime * (float)Math.PI) * -0.4F;
            this.leftarm.xRot = (float)((double)this.leftarm.xRot - ((double)f7 * 1.2 + (double)f8));
            var10000 = this.leftarm;
            var10000.yRot += this.bodytop.yRot * 2.0F;
            var10000 = this.leftarm;
            var10000.zRot -= Mth.sin(this.attackTime * (float)Math.PI) * -0.4F;
        }

        private void animationFlee() {
            ++this.rightarm.xRot;
            ++this.leftarm.xRot;
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
            poseStack.translate((double)0.0F, (double)0.5F, (double)0.0F);
            this.getArm(arm).translateAndRotate(poseStack);
        }
    }
