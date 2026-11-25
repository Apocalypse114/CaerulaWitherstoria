package net.apocalypse.caerulawitherstoria.entity.model;

import net.apocalypse.caerulawitherstoria.CaerulaWitherstoriaMod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class SimpleGeoModel<T extends GeoAnimatable> extends GeoModel<T> {
    public SimpleGeoModel(String anim, String model, String texture){
        this.ANIM = anim;
        this.MODEL = model;
        this.TEXTURE = texture;
    }

    public final String ANIM;
    public final String MODEL;
    public final String TEXTURE;

    @Override
    public ResourceLocation getAnimationResource(T entity) {
        return new ResourceLocation(CaerulaWitherstoriaMod.MODID, "animations/" + ANIM + ".animation.json");
    }

    @Override
    public ResourceLocation getModelResource(T entity) {
        return new ResourceLocation(CaerulaWitherstoriaMod.MODID, "geo/" + MODEL + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T entity) {
        return new ResourceLocation(CaerulaWitherstoriaMod.MODID, "textures/entity/" + TEXTURE + ".png");
    }
}
