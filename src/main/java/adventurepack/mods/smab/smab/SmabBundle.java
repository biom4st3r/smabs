package adventurepack.mods.smab.smab;

import adventurepack.mods.smab.ItemLoader.CardItem;
import adventurepack.mods.smab.ModInit;
import adventurepack.mods.smab.Registries;
import adventurepack.mods.smab.minecraft.entity.SmabEntity;
import adventurepack.mods.smab.minecraft.entity.SmabEntityModel;
import adventurepack.mods.smab.minecraft.entity.SmabEntityRenderer;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

/**
 * Fully handles registration
 */
public record SmabBundle(SmabSpecies species, EntityType<SmabEntity> type, CardItem item) {
    public SmabBundle(SmabSpecies species, EntityType<SmabEntity> type, CardItem item) {
        this.species = species;
        this.type = type;
        this.item = item;
        Registry.register(Registry.ENTITY_TYPE, species().id(), type);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            EntityRendererRegistry.register(type, ctx -> new SmabEntityRenderer(ctx,new SmabEntityModel(species)));
        }
        Registry.register(Registry.ITEM, species().id(), item);
        TrinketsApi.registerTrinket(item, new SmabTrinket());
        Registries.SMABS.register(species().id(), this);
    }

    public SmabBundle(SmabSpecies species, FabricEntityTypeBuilder<SmabEntity> builder, Item.Settings settings) {
        this(species, builder.entityFactory((type,world) -> new SmabEntity(type, world, species)).build(), new SmabCardItem(species, settings.maxCount(1).group(ModInit.SMABS_GROUP)));
    }

    // public static <T extends FabricEntityTypeBuilder<SmabEntity>> SmabBundle<T> create(SmabSpecies species, T builder) {
    //     return new SmabBundle<T>(species, builder);
    // }

    public static SmabBundle create(SmabSpecies species, FabricEntityTypeBuilder<SmabEntity> builder, Item.Settings settings) {
        return new SmabBundle(species, builder, settings);
    }

    public static SmabBundle create(SmabSpecies species, FabricEntityTypeBuilder<SmabEntity> builder) {
        return new SmabBundle(species,builder,new Item.Settings());
    }
}
