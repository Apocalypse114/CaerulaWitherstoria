
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.apocalypse.caerulawitherstoria.init;

import net.apocalypse.caerulawitherstoria.CaerulaWitherstoriaMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

import net.apocalypse.caerulawitherstoria.block.ProtocalNetherseaBlock;

public class CWModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, CaerulaWitherstoriaMod.MODID);
	public static final RegistryObject<Block> PROTOCAL_NETHERSEA = REGISTRY.register("protocal_nethersea", ProtocalNetherseaBlock::new);
	// Start of user code block custom blocks
	// End of user code block custom blocks
}
