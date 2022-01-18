package adventurepack.mods.smab;

import adventurepack.mods.smab.smab.Ability;
import adventurepack.mods.smab.smab.DietaryEffect;
import adventurepack.mods.smab.smab.LevelAlgorithm;
import adventurepack.mods.smab.smab.SmabBundle;
import adventurepack.mods.smab.smab.Tag;
import adventurepack.mods.smab.smab.json.JsonDietaryPair;
import adventurepack.mods.smab.smab.json.JsonEntityAttributeBundle;
import adventurepack.mods.smab.smab.json.JsonSmabBundle;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public final class Smabs {

    public static final JsonSmabBundle MISSINGNO = new JsonSmabBundle(
        new Identifier(ModInit.MODID, "missingno"),
        0,
        0,
        0,
        0,
        new Ability[0], 
        LevelAlgorithm.STRAIGHT_LOG,
        new JsonDietaryPair[] {new JsonDietaryPair(Items.DIAMOND, new DietaryEffect(-5,0,0,5))},
        Tag.create(),
        false,
        1,
        1,
        true,
        Rarity.COMMON,
        new JsonEntityAttributeBundle[]{},
        SpawnGroup.CREATURE
    );

    public static void classLoad() {}
}
