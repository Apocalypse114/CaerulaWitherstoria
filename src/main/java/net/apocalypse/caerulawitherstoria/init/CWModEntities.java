
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.apocalypse.caerulawitherstoria.init;

import net.apocalypse.caerulawitherstoria.CaerulaWitherstoriaMod;
import net.apocalypse.caerulawitherstoria.entity.ChitinGhastEntity;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

import net.apocalypse.caerulawitherstoria.entity.PathShaperShaperEntity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CWModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CaerulaWitherstoriaMod.MODID);
	public static final RegistryObject<EntityType<PathShaperShaperEntity>> PATH_SHAPER_SHAPER = register("path_shaper_shaper",
			EntityType.Builder.<PathShaperShaperEntity>of(PathShaperShaperEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true)
					.setTrackingRange(18)
					.setUpdateInterval(3)
					.setCustomClientFactory(PathShaperShaperEntity::new)
					.sized(6f, 15f));
	public static final RegistryObject<EntityType<ChitinGhastEntity>> CHITIN_GHAST = register("chitin_ghast",
			EntityType.Builder.<ChitinGhastEntity>of(ChitinGhastEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true)
					.setTrackingRange(18)
					.setUpdateInterval(3)
					.setCustomClientFactory(ChitinGhastEntity::new)
					.sized(4f, 4f));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(PATH_SHAPER_SHAPER.get(), PathShaperShaperEntity.createAttributes().build());
		event.put(CHITIN_GHAST.get(), ChitinGhastEntity.createAttributes().build());
	}
}
