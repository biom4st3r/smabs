package adventurepack.mods.smab;

import com.google.common.collect.Maps;

import adventurepack.mods.smab.minecraft.entity.SmabEntity;
import adventurepack.mods.smab.smab.Ability;
import adventurepack.mods.smab.smab.DietaryEffect;
import adventurepack.mods.smab.smab.LevelAlgorithm;
import adventurepack.mods.smab.smab.SmabBundle;
import adventurepack.mods.smab.smab.SmabSpecies;
import adventurepack.mods.smab.smab.Tag;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public final class Smabs {
    public static final SmabBundle MISSINGNO = SmabBundle.create(
            new SmabSpecies(
                new Identifier(ModInit.MODID, "missingno"),
                0,
                0,
                0,
                0,
                new Ability[0], 
                LevelAlgorithm.STRAIGHT_LOG,
                Util.make(Maps.newHashMap(), map -> {
                    map.put(Items.DIAMOND, new DietaryEffect(-5,0,0,5));
                }),
                Tag.create(),
                false
            ),
            FabricEntityTypeBuilder.<SmabEntity>createMob().spawnGroup(SpawnGroup.CREATURE).defaultAttributes(()-> MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3F).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0))
        );
    public static void classLoad() {}
}
