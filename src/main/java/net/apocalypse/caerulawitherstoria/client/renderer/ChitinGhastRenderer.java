package net.apocalypse.caerulawitherstoria.client.renderer;

import net.apocalypse.caerulawitherstoria.entity.ChitinGhastEntity;
import net.apocalypse.caerulawitherstoria.entity.layer.ChitinGhastLayer;
import net.apocalypse.caerulawitherstoria.entity.model.ChitinGhastModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;

public class ChitinGhastRenderer extends SimpleGeoRenderer<ChitinGhastEntity> {
    public ChitinGhastRenderer(EntityRendererProvider.Context manager) {
        super(manager, 1.5f,new ChitinGhastModel(), true);
        this.scaleHeight = 3f; this.scaleWidth = 3f;
        this.addRenderLayer(new ChitinGhastLayer(this));
    }
}
