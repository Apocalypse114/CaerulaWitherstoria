
package net.apocalypse.caerulawitherstoria.item;

import net.apocalypse.caerulawitherstoria.mixins.LivingAccessor;
import net.apocalypse.caerulawitherstoria.util.Tools;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.ChatFormatting;
import java.util.List;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;

public class SuperRedstoniumSwordItem extends SwordItem {
	public SuperRedstoniumSwordItem() {
		super(new Tier() {
			public int getUses() {
				return 99999;
			}

			public float getSpeed() {
				return 20f;
			}

			public float getAttackDamageBonus() {
				return 321f;
			}

			public int getLevel() {
				return 4;
			}

			public int getEnchantmentValue() {
				return 10;
			}

			public @NotNull Ingredient getRepairIngredient() {
				return Ingredient.of();
			}
		}, 3, 0f, new Item.Properties().fireResistant().rarity(Rarity.EPIC));
	}

	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity){
		if (entity instanceof LivingEntity living) {
			setHealthRaw(living, getHealthRaw(living) - 325);
			if (living.isDeadOrDying()){
				living.die(player.level().damageSources().playerAttack(player));
			}
		}
		return true;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, level, list, flag);
		ChatFormatting f = Tools.randomFormat(level == null ? RandomSource.create() : level.getRandom());
		list.add(Component.translatable("item.caerula_witherstoria.super_redstonium_sword.desc").withStyle(f));
		list.add(Component.translatable("item.caerula_witherstoria.super_redstonium_sword.desc_1").withStyle(f));
	}
	@Override
    public @NotNull Component getName(@NotNull ItemStack stack){
        MutableComponent component = (MutableComponent) super.getName(stack);
        return component.withStyle(Tools.randomFormat(RandomSource.create()));
    }

	public static void setHealthRaw(LivingEntity living, float h){
		EntityDataAccessor<Float> data_h = LivingAccessor.getHealthID();
		living.getEntityData().set(data_h, h);
	}
	public static float getHealthRaw(LivingEntity living){
		EntityDataAccessor<Float> data_h = LivingAccessor.getHealthID();
		return living.getEntityData().get(data_h);
	}
}
