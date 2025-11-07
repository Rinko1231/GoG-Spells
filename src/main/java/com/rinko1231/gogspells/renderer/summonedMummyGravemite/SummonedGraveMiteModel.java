package com.rinko1231.gogspells.renderer.summonedMummyGravemite;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rinko1231.gogspells.entity.SummonedGraveMite;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class SummonedGraveMiteModel extends EntityModel<SummonedGraveMite> {
    private final ModelPart root;

    public SummonedGraveMiteModel(ModelPart root) {
        this.root = root.getChild("mite");
    }
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition mite = partdefinition.addOrReplaceChild("mite", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition head = mite.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 2.0F), PartPose.offset(0.0F, -1.0F, -2.0F));
        PartDefinition rightantenna = head.addOrReplaceChild("rightantenna", CubeListBuilder.create().texOffs(22, 0).addBox(0.0F, -2.0F, -2.0F, 0.0F, 2.0F, 2.0F), PartPose.offset(-1.0F, -1.0F, -1.0F));
        PartDefinition leftantenna = head.addOrReplaceChild("leftantenna", CubeListBuilder.create().texOffs(22, 0).addBox(0.0F, -2.0F, -2.0F, 0.0F, 2.0F, 2.0F), PartPose.offset(1.0F, -1.0F, -1.0F));
        PartDefinition body = mite.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 5).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 4.0F, 5.0F), PartPose.offset(0.0F, -1.0F, -2.0F));
        PartDefinition rightleg = body.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(22, 2).addBox(-2.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(-3.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.7854F));
        PartDefinition rightleg2 = body.addOrReplaceChild("rightleg2", CubeListBuilder.create().texOffs(22, 2).addBox(-2.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(-3.0F, 1.0F, 2.5F, 0.0F, 0.0F, 0.7854F));
        PartDefinition rightleg3 = body.addOrReplaceChild("rightleg3", CubeListBuilder.create().texOffs(22, 2).addBox(-2.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(-3.0F, 1.0F, 4.0F, 0.0F, 0.0F, 0.7854F));
        body.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(22, 2).mirror().addBox(0.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F).mirror(false), PartPose.offsetAndRotation(3.0F, 1.0F, 1.0F, 0.0F, 0.0F, -0.7854F));
        body.addOrReplaceChild("leftleg2", CubeListBuilder.create().texOffs(22, 2).mirror().addBox(0.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F).mirror(false), PartPose.offsetAndRotation(3.0F, 1.0F, 2.5F, 0.0F, 0.0F, -0.7854F));
        body.addOrReplaceChild("leftleg3", CubeListBuilder.create().texOffs(22, 2).mirror().addBox(0.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F).mirror(false), PartPose.offsetAndRotation(3.0F, 1.0F, 4.0F, 0.0F, 0.0F, -0.7854F));
        PartDefinition back = mite.addOrReplaceChild("back", CubeListBuilder.create().texOffs(22, 4).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 3.0F, 2.0F), PartPose.offset(0.0F, -1.0F, 3.0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    public void setupAnim(SummonedGraveMite graveMite, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int unused) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
