package com.github.alexthe666.rats.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public class LayerBasicOverlay<T extends LivingEntity> extends LayerRenderer<T, EntityModel<T>> {
    private final IEntityRenderer<T, EntityModel<T>> ratRenderer;
    private final ResourceLocation texture;
    private final RenderType renderType;

    public LayerBasicOverlay(IEntityRenderer<T, EntityModel<T>> ratRendererIn, ResourceLocation texture) {
        super(ratRendererIn);
        this.ratRenderer = ratRendererIn;
        this.texture = texture;
        this.renderType = RenderType.getEntityNoOutline(texture);
    }

    public boolean shouldCombineTextures() {
        return true;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(renderType);
        this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

    }
}
