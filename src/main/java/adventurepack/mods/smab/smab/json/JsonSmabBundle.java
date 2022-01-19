package adventurepack.mods.smab.smab.json;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import adventurepack.mods.smab.ModInit;
import adventurepack.mods.smab.minecraft.entity.SmabEntity;
import adventurepack.mods.smab.smab.Ability;
import adventurepack.mods.smab.smab.LevelAlgorithm;
import adventurepack.mods.smab.smab.SmabBundle;
import adventurepack.mods.smab.smab.SmabSpecies;
import adventurepack.mods.smab.smab.Tag;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer.Builder;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;

public record JsonSmabBundle(
    Identifier id,
    int base_intelligence, 
    int base_strength, 
    int base_dexterity, 
    int base_vitality,
    Ability[] abilities,
    LevelAlgorithm algo,
    JsonDietaryPair[] diet,
    /**
     * Shouldn't be checked directly. Allow the Smab instance to add it own stuff
     */
    Tag[] tags,
    boolean has_entity,
    // END OF SMAB_SPECIES
    float entity_dimension_width,
    float entity_dimension_height,
    boolean entity_dimension_fixed_size,
    boolean is_fire_immune,
    Block[] can_only_spawn_on,
    // END OF ENTITY
    Rarity item_rarity,
    //  END OF ITEM
    JsonEntityAttributeBundle[] entity_attributes,
    SpawnGroup entity_spawn_group
    ) {
    public SmabBundle build() {
        SmabSpecies species = new SmabSpecies(
            id, 
            base_intelligence, 
            base_strength, 
            base_dexterity, 
            base_vitality, 
            abilities, 
            algo, 
            Util.make(Maps.newHashMap(),map -> {
                for (JsonDietaryPair pair : diet) {
                    map.put(pair.item(), pair.dietary_effect());
                }
            }), 
            Sets.newHashSet(tags),
            has_entity
        );
        FabricEntityTypeBuilder.Mob<SmabEntity> builder = FabricEntityTypeBuilder
            .<SmabEntity>createMob()
            .spawnGroup(entity_spawn_group)
            .dimensions(
                new EntityDimensions(
                    entity_dimension_width, 
                    entity_dimension_height, 
                    entity_dimension_fixed_size
                )
            )
            ;
        builder.defaultAttributes(()-> {
            Builder i = MobEntity.createMobAttributes();
            for (JsonEntityAttributeBundle bundle : entity_attributes) {
                i.add(bundle.attribute(), bundle.value());
            }
            return i;
        });
        if (is_fire_immune) builder.fireImmune();
        if (can_only_spawn_on.length != 0) {
            builder.specificSpawnBlocks(can_only_spawn_on);
        }

        return SmabBundle.create(
            species,
            builder, 
            new FabricItemSettings()
                .group(ModInit.SMABS_GROUP)
                .rarity(item_rarity)
                .maxCount(1)
        );
    }
}
