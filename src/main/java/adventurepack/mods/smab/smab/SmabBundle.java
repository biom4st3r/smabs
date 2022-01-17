package adventurepack.mods.smab.smab;

import adventurepack.mods.smab.minecraft.entity.SmabEntity;
import adventurepack.mods.smab.minecraft.entity.SmabEntityModel;
import adventurepack.mods.smab.minecraft.entity.SmabEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

/**
 * Fully handles registration
 */
public record SmabBundle(SmabSpecies species, EntityType<SmabEntity> type) {
    public SmabBundle(SmabSpecies species, EntityType<SmabEntity> type) {
        this.species = species;
        this.type = type;
        Registry.register(Registry.ENTITY_TYPE, species().id(), type);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            EntityRendererRegistry.register(type, ctx -> new SmabEntityRenderer(ctx,new SmabEntityModel(species)));
        }
    }

    public SmabBundle(SmabSpecies species, FabricEntityTypeBuilder<SmabEntity> builder) {
        this(species, builder.entityFactory((type,world) -> new SmabEntity(type, world, species)).build());
    }

    // public static <T extends FabricEntityTypeBuilder<SmabEntity>> SmabBundle<T> create(SmabSpecies species, T builder) {
    //     return new SmabBundle<T>(species, builder);
    // }

    public static SmabBundle create(SmabSpecies species, FabricEntityTypeBuilder<SmabEntity> builder) {
        return new SmabBundle(species, builder);
    }
}
