
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.apocalypse.caerulawitherstoria.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.apocalypse.caerulawitherstoria.CaerulaWitherstoriaMod;

public class CWModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CaerulaWitherstoriaMod.MODID);
	public static final RegistryObject<CreativeModeTab> CAERULA_WITHERSTORIA = REGISTRY.register("caerula_witherstoria",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.caerula_witherstoria.caerula_witherstoria")).icon(() -> new ItemStack(CWModBlocks.PROTOCAL_NETHERSEA.get())).displayItems((parameters, tabData) -> {
				tabData.accept(CWModBlocks.PROTOCAL_NETHERSEA.get().asItem());
				tabData.accept(CWModItems.PATH_SHAPER_SHAPER_SPAWN_EGG.get());
				tabData.accept(CWModItems.CHITIN_GHAST_SPAWN_EGG.get());
				tabData.accept(CWModItems.SUPER_REDSTONIUM_SWORD.get());
				tabData.accept(CWModItems.SUPER_REDSTONIUM_SET_HELMET.get());
				tabData.accept(CWModItems.SUPER_REDSTONIUM_SET_CHESTPLATE.get());
				tabData.accept(CWModItems.SUPER_REDSTONIUM_SET_LEGGINGS.get());
				tabData.accept(CWModItems.SUPER_REDSTONIUM_SET_BOOTS.get());
				tabData.accept(CWModItems.SUPER_REDSTONIUM_TEMPLATE.get());
			}).build());
}
