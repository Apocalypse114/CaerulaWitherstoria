
package net.apocalypse.caerulawitherstoria.item;

import net.apocalypse.caerulawitherstoria.util.Tools;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import net.minecraft.ChatFormatting;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.MutableComponent;

public abstract class SuperRedstoniumSetItem extends ArmorItem {
	public SuperRedstoniumSetItem(ArmorItem.Type type, Item.Properties properties) {
		super(new ArmorMaterial() {
			@Override
			public int getDurabilityForType(ArmorItem.Type type) {
				return new int[]{13, 15, 16, 11}[type.getSlot().getIndex()] * 32767;
			}

			@Override
			public int getDefenseForType(ArmorItem.Type type) {
				return new int[]{328, 799, 648, 325}[type.getSlot().getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 10;
			}

			@Override
			public SoundEvent getEquipSound() {
				return SoundEvents.ARMOR_EQUIP_NETHERITE;
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of();
			}

			@Override
			public String getName() {
				return "super_redstonium_set";
			}

			@Override
			public float getToughness() {
				return 32.5f;
			}

			@Override
			public float getKnockbackResistance() {
				return 7.99f;
			}
		}, type, properties);
	}

	public static class Helmet extends SuperRedstoniumSetItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties().fireResistant());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "caerula_witherstoria:textures/models/armor/redstonium_armor_layer_1.png";
		}
	}

	public static class Chestplate extends SuperRedstoniumSetItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "caerula_witherstoria:textures/models/armor/redstonium_armor_layer_1.png";
		}
	}

	public static class Leggings extends SuperRedstoniumSetItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "caerula_witherstoria:textures/models/armor/redstonium_armor_layer_2.png";
		}
	}

	public static class Boots extends SuperRedstoniumSetItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties().fireResistant());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "caerula_witherstoria:textures/models/armor/redstonium_armor_layer_1.png";
		}
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, level, list, flag);
		ChatFormatting f = Tools.randomFormat(level == null ? RandomSource.create() : level.getRandom());
		list.add(Component.translatable("item.caerula_witherstoria.super_redstonium_set_helmet.description_0").withStyle(f));
		list.add(Component.translatable("item.caerula_witherstoria.super_redstonium_set_helmet.description_1").withStyle(f));
	}
	@Override
    public @NotNull Component getName(@NotNull ItemStack stack){
        MutableComponent component = (MutableComponent) super.getName(stack);
        return component.withStyle(Tools.randomFormat(RandomSource.create()));
    }

}
