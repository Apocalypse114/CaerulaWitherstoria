package net.apocalypse.caerulawitherstoria.init;

import net.apocalypse.caerulawitherstoria.entity.base.SyncableGeo;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

@Mod.EventBusSubscriber
public class EntityAnimationFactory {
	@SubscribeEvent
	public static void onEntityTick(LivingEvent.LivingTickEvent event) {
		if (event != null && event.getEntity() != null) {
			if (event.getEntity() instanceof SyncableGeo syncable) {
				String animation = syncable.getOngoingAnimation();
				if (!animation.equals("undefined")) {
					syncable.setAnimation("undefined");
					syncable.setOngoingAnimation(animation);
				}
			}
		}
	}
}
