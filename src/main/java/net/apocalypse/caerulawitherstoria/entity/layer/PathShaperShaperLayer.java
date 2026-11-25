package net.apocalypse.caerulawitherstoria.entity.layer;

import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import net.apocalypse.caerulawitherstoria.entity.PathShaperShaperEntity;
import net.apocalypse.caerulawitherstoria.util.SimpleRainbowGenerator;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.phys.Vec3;

public class PathShaperShaperLayer extends SimpleGeoLayer<PathShaperShaperEntity> {
	public PathShaperShaperLayer(GeoRenderer<PathShaperShaperEntity> entityRenderer) {
		super("routeshaper", entityRenderer);
	}

	@Override
	public void render(PoseStack poseStack, PathShaperShaperEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		RenderType glowRenderType = RenderType.eyes(LAYER);
		Vec3 color = getRGB(animatable.tickCount);
		getRenderer().reRender(getDefaultBakedModel(animatable), 
			poseStack, bufferSource, animatable, glowRenderType, 
			bufferSource.getBuffer(glowRenderType), partialTick, packedLight, 
			OverlayTexture.NO_OVERLAY, (float)(color.x()), (float)(color.y()), (float)(color.z()), 1);
	}

	private static Vec3 getRGB(int t){
		return SimpleRainbowGenerator.getRainbowColor(t);
	}
}
