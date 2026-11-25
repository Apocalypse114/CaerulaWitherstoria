package net.apocalypse.caerulawitherstoria.entity.model;

import net.apocalypse.caerulawitherstoria.entity.ChitinGhastEntity;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.ArrayList;
import java.util.List;

public class ChitinGhastModel extends SimpleGeoModel<ChitinGhastEntity>{
    public ChitinGhastModel() {
        super("chitin_ghast", "chitin_ghast", "chitin_ghast");
    }

    public static final List<String> NAMES = List.of("36","37","38","39","40","41","42","43");
    @Override
    public void setCustomAnimations(ChitinGhastEntity animatable, long instanceId, AnimationState<ChitinGhastEntity> animationState){
        List<CoreGeoBone> LEGS = new ArrayList<>();
        NAMES.forEach(n->{
            LEGS.add(this.getAnimationProcessor().getBone("bone"+n));
        });
        int offset = 0;
        float tick = animatable.tickCount + animationState.getPartialTick();
        for(CoreGeoBone leg: LEGS){
            if(leg != null)
                leg.setRotZ(0.5f * (float) Math.sin(Math.toRadians(tick * 4.5 + offset * 20)));
            offset += 1;
        }
        CoreGeoBone head = this.getAnimationProcessor().getBone("axis");
        if(head != null){
            EntityModelData data = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(data.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(data.netHeadYaw() * Mth.DEG_TO_RAD);

        }
    }
}
