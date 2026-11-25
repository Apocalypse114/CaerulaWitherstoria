package net.apocalypse.caerulawitherstoria;

import net.apocalypse.caerulawitherstoria.init.CWModEntities;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;

import net.apocalypse.caerulawitherstoria.init.CWModTabs;
import net.apocalypse.caerulawitherstoria.init.CWModItems;
import net.apocalypse.caerulawitherstoria.init.CWModBlocks;

import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.AbstractMap;

@Mod("caerula_witherstoria")
public class CaerulaWitherstoriaMod {
	public static final Logger LOGGER = LogManager.getLogger(CaerulaWitherstoriaMod.class);
	public static final String MODID = "caerula_witherstoria";

	public CaerulaWitherstoriaMod() {
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		CWModBlocks.REGISTRY.register(bus);
		CWModItems.REGISTRY.register(bus);
		CWModEntities.REGISTRY.register(bus);
		CWModTabs.REGISTRY.register(bus);
	}

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	private static int messageID = 0;

	public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
		PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
		messageID++;
	}

	private static final Collection<Work> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
			workQueue.add(new Work(tick, action));
	}

	@SubscribeEvent
	public void tick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			workQueue.removeIf(Work::tickAndRun);
		}
	}

	private static class Work{
		protected Work(int tickLeft, Runnable work){
			this.work = work;
			this.tickLeft = tickLeft;
		}
		private int tickLeft;
		private final Runnable work;
		public boolean tickAndRun(){
			tickLeft--;
			if (tickLeft <= 0){
				work.run();
				return true;
			}
			return false;
		}
	}
}
