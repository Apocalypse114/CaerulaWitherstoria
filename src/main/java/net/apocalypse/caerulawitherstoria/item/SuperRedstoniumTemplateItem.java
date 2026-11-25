
package net.apocalypse.caerulawitherstoria.item;

import net.apocalypse.caerulawitherstoria.util.Tools;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import java.util.Set;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.item.SmithingTemplateItem;
import java.util.HashSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class SuperRedstoniumTemplateItem extends Item {
	public SuperRedstoniumTemplateItem() {
		super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
	}

    public static final Set<SmithingTemplateItem> SMITHING_TEMPLATES = new HashSet<>();

    private static void dropItem(ServerLevel level, Vec3 pos, Item item){
        RandomSource source = level.getRandom();
        pos = pos.add(new Vec3(
                Mth.nextDouble(source,-3, 3),
                Mth.nextDouble(source,3, 4),
                Mth.nextDouble(source,-3, 3)));
        ItemEntity ie = new ItemEntity(level, pos.x, pos.y, pos.z, new ItemStack(item));
        level.addFreshEntity(ie);
    }

	@Override
	public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, level, list, flag);
		ChatFormatting f = Tools.randomFormat(level == null ? RandomSource.create() : level.getRandom());
		list.add(Component.translatable("item.caerula_witherstoria.super_redstonium_template.description_0").withStyle(f));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player pPlayer, InteractionHand pHand){
        if(SMITHING_TEMPLATES.isEmpty()){
            Set<Map.Entry<ResourceKey<Item>, Item>> FORGE_ITEMS = ForgeRegistries.ITEMS.getEntries();
            for (Map.Entry<ResourceKey<Item>, Item> itemMap: FORGE_ITEMS){
                if(itemMap.getValue() instanceof SmithingTemplateItem t) SMITHING_TEMPLATES.add(t);
            }
        }
        if(level instanceof ServerLevel sLevel) {
            sLevel.playSound(pPlayer, pPlayer.blockPosition(), SoundEvents.SMITHING_TABLE_USE, SoundSource.PLAYERS, 1, 1);
            SMITHING_TEMPLATES.forEach(t -> dropItem(sLevel, pPlayer.position(), t));
            pPlayer.getItemInHand(pHand).shrink(1);
        }
		return InteractionResultHolder.success(pPlayer.getItemInHand(pHand));
	}
	@Override
    public @NotNull Component getName(@NotNull ItemStack stack){
        MutableComponent component = (MutableComponent) super.getName(stack);
        return component.withStyle(Tools.randomFormat(RandomSource.create()));
    }
}
