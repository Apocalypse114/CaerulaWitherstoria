package net.apocalypse.caerulawitherstoria.mixins;

import net.apocalypse.caerulawitherstoria.entity.base.ReinforcedMob;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = -65536)
public class LivingMixin {
    @Inject(
            method = "getMaxHealth",
            at = @At("HEAD"),
            cancellable = true)
    private void getMaxHealth(CallbackInfoReturnable<Float> cir){
        LivingEntity me = (LivingEntity) (Object) this;
        if (me instanceof ReinforcedMob mob){
            cir.setReturnValue(mob.getMaxDura());
        }
    }
}
