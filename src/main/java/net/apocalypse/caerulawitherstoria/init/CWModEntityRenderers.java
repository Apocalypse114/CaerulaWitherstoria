
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.apocalypse.caerulawitherstoria.init;

import net.apocalypse.caerulawitherstoria.client.renderer.ChitinGhastRenderer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.apocalypse.caerulawitherstoria.client.renderer.PathShaperShaperRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CWModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CWModEntities.PATH_SHAPER_SHAPER.get(), PathShaperShaperRenderer::new);
		event.registerEntityRenderer(CWModEntities.CHITIN_GHAST.get(), ChitinGhastRenderer::new);
	}
}
