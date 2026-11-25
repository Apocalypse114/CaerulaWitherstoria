package net.apocalypse.caerulawitherstoria.entity.base;

import software.bernie.geckolib.animatable.GeoEntity;

public interface SyncableGeo extends GeoEntity {
    String getOngoingAnimation();
    void setAnimation(String animation);
    void setOngoingAnimation(String animation);
}
