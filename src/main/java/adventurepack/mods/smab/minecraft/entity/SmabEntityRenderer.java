package adventurepack.mods.smab.minecraft.entity;

import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SmabEntityRenderer extends GeoEntityRenderer<SmabEntity> {
    public SmabEntityRenderer(Context ctx, AnimatedGeoModel<SmabEntity> modelProvider) {
        super(ctx, modelProvider);
    }
}
