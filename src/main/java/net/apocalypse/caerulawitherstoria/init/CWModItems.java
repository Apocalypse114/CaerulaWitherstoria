
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.apocalypse.caerulawitherstoria.init;

import net.apocalypse.caerulawitherstoria.CaerulaWitherstoriaMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.ForgeSpawnEggItem;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.apocalypse.caerulawitherstoria.item.SuperRedstoniumTemplateItem;
import net.apocalypse.caerulawitherstoria.item.SuperRedstoniumSwordItem;
import net.apocalypse.caerulawitherstoria.item.SuperRedstoniumSetItem;

import java.util.function.Supplier;

public class CWModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, CaerulaWitherstoriaMod.MODID);
	public static final RegistryObject<Item> PROTOCAL_NETHERSEA = block(CWModBlocks.PROTOCAL_NETHERSEA);
	public static final RegistryObject<Item> SUPER_REDSTONIUM_SWORD = REGISTRY.register("super_redstonium_sword", SuperRedstoniumSwordItem::new);
	public static final RegistryObject<Item> SUPER_REDSTONIUM_SET_HELMET = REGISTRY.register("super_redstonium_set_helmet", SuperRedstoniumSetItem.Helmet::new);
	public static final RegistryObject<Item> SUPER_REDSTONIUM_SET_CHESTPLATE = REGISTRY.register("super_redstonium_set_chestplate", SuperRedstoniumSetItem.Chestplate::new);
	public static final RegistryObject<Item> SUPER_REDSTONIUM_SET_LEGGINGS = REGISTRY.register("super_redstonium_set_leggings", SuperRedstoniumSetItem.Leggings::new);
	public static final RegistryObject<Item> SUPER_REDSTONIUM_SET_BOOTS = REGISTRY.register("super_redstonium_set_boots", SuperRedstoniumSetItem.Boots::new);
	public static final RegistryObject<Item> SUPER_REDSTONIUM_TEMPLATE = REGISTRY.register("super_redstonium_template", SuperRedstoniumTemplateItem::new);

	public static final RegistryObject<Item> PATH_SHAPER_SHAPER_SPAWN_EGG = spawnEgg("path_shaper_shaper_spawn_egg", CWModEntities.PATH_SHAPER_SHAPER::get, -1, -1);
	public static final RegistryObject<Item> CHITIN_GHAST_SPAWN_EGG = spawnEgg("chitin_ghast_spawn_egg", CWModEntities.CHITIN_GHAST::get, -1, -1);

	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
	private static RegistryObject<Item> spawnEgg(String name, Supplier<EntityType<? extends Mob>> sup, int colorBg, int colorDot, Rarity rarity){
		return REGISTRY.register(name, ()->simpleSpawnEgg(sup, colorBg, colorDot, rarity));
	}
	private static RegistryObject<Item> spawnEgg(String name, Supplier<EntityType<? extends Mob>> sup, int colorBg, int colorDot){
		return REGISTRY.register(name, ()->simpleSpawnEgg(sup, colorBg, colorDot));
	}
	private static ForgeSpawnEggItem simpleSpawnEgg(Supplier<EntityType<? extends Mob>> sup, int colorBG, int colorDot, Rarity rarity){
		return new ForgeSpawnEggItem(sup, colorBG, colorDot, new Item.Properties().rarity(rarity));
	}
	private static ForgeSpawnEggItem simpleSpawnEgg(Supplier<EntityType<? extends Mob>> sup, int colorBG, int colorDot){
		return simpleSpawnEgg(sup, colorBG, colorDot, Rarity.COMMON);
	}
}
