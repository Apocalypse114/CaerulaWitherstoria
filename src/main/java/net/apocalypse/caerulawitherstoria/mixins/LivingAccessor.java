package net.apocalypse.caerulawitherstoria.mixins;

import net.minecraft.network.syncher.EntityDataAccessor;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(LivingEntity.class)
public interface LivingAccessor {
	@Accessor("DATA_HEALTH_ID")
    static EntityDataAccessor<Float> getHealthID(){
        throw new AssertionError();
    }
}
