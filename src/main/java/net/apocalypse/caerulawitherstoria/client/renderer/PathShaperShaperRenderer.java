
package net.apocalypse.caerulawitherstoria.client.renderer;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import net.apocalypse.caerulawitherstoria.entity.model.PathShaperShaperModel;
import net.apocalypse.caerulawitherstoria.entity.layer.PathShaperShaperLayer;
import net.apocalypse.caerulawitherstoria.entity.PathShaperShaperEntity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class PathShaperShaperRenderer extends SimpleGeoRenderer<PathShaperShaperEntity> {
	public PathShaperShaperRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, 3f, new PathShaperShaperModel(), true);
		this.addRenderLayer(new PathShaperShaperLayer(this));
	}

	@Override
	public void preRender(PoseStack poseStack, PathShaperShaperEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		float scale = 10f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
