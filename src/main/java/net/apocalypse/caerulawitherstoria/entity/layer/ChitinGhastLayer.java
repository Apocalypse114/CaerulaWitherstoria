package net.apocalypse.caerulawitherstoria.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.apocalypse.caerulawitherstoria.CaerulaWitherstoriaMod;
import net.apocalypse.caerulawitherstoria.entity.ChitinGhastEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class ChitinGhastLayer extends SimpleGeoLayer<ChitinGhastEntity> {
    public ChitinGhastLayer(GeoRenderer<ChitinGhastEntity> entityRenderer) {
        super("chitin_ghast_lit", entityRenderer);
    }

    public static final ResourceLocation LIT_ALL = new ResourceLocation(CaerulaWitherstoriaMod.MODID, "textures/entity/chitin_ghast_lit.png");
    public static final ResourceLocation LIT_EYE = new ResourceLocation(CaerulaWitherstoriaMod.MODID, "textures/entity/chitin_ghast_eye.png");

    @Override
    public void render(PoseStack poseStack, ChitinGhastEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        int t = (animatable.tickCount / 20) % 2;
        if (t == 0) LAYER = LIT_ALL;
        else LAYER = LIT_EYE;
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }
}
