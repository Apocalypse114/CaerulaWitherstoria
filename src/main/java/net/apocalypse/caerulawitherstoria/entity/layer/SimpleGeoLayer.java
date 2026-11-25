package net.apocalypse.caerulawitherstoria.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.apocalypse.caerulawitherstoria.CaerulaWitherstoriaMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class SimpleGeoLayer<T extends GeoAnimatable> extends GeoRenderLayer<T>{

    public ResourceLocation LAYER;

    public SimpleGeoLayer(String layer, GeoRenderer<T> entityRenderer) {
        super(entityRenderer);
        this.LAYER = new ResourceLocation(CaerulaWitherstoriaMod.MODID, "textures/entity/" + layer + ".png");
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.eyes(LAYER);
        getRenderer().reRender(getDefaultBakedModel(animatable),
                poseStack, bufferSource, animatable, glowRenderType,
                bufferSource.getBuffer(glowRenderType), partialTick, packedLight,
                OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}
