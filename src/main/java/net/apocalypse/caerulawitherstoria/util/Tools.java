package net.apocalypse.caerulawitherstoria.util;

import net.mcreator.caerulaarbor.procedures.GetCosineProcedure;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.inventory.Hotbar;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.SeedCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CarrotBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Tools {
	public static void summonFromRegName(LevelAccessor world, double x, double y, double z, String name) {
		EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(name));
        if (world instanceof ServerLevel sLevel) {
            if (type == null) {
                type = EntityType.PIG;
                Entity pig = type.spawn(sLevel, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
                if (pig != null) {
                    pig.setYBodyRot(world.getRandom().nextFloat() * 360F);
                    pig.setCustomName(Component.translatable("id.caerula_witherstoria.chat_member"));
                }
            } else {
                Entity summoned = type.spawn(sLevel, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
                if (summoned != null) {
                    summoned.setYBodyRot(world.getRandom().nextFloat() * 360F);
                }
            }
        }
	}
    public static void playSound(Level world, double x, double y, double z, SoundEvent sound, SoundSource source, float v, float p){
        if (!world.isClientSide()) {
            world.playSound(null, BlockPos.containing(x, y, z), sound, source, v, p);
        } else {
            world.playLocalSound(x, y, z, sound, source, v, p, false);
        }
    }
    public static ChatFormatting randomFormat(RandomSource source){
        List<ChatFormatting> chatFormattingList = List.of(ChatFormatting.values());
        int index = Mth.nextInt(source, 0, chatFormattingList.size() -1);
        return chatFormattingList.get(index);
    }

    @Nullable
    public static LivingEntity validTarget(@NotNull Mob mob){
        LivingEntity t = mob.getTarget();
        return t != null && t.isAlive() ? t : null;
    }

    public double getCosine(Vec3 a, Vec3 b){
        double product = a.dot(b);
        double norm = a.length();
        double norm1 = b.length();
        return norm * norm1 == 0 ? 1 : product / (norm * norm1);
    }
    public boolean withinFan(Entity object, Entity another, float deg){
        return getCosine(
                object.getLookAngle(),
                object.position().vectorTo(another.position())
        ) <= Math.toRadians(deg);
    }
    public void test(CompoundTag tag, Level level, @Nullable Monster e){

    }
}
