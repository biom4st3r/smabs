package adventurepack.mods.smab;

import adventurepack.mods.smab.smab.SmabBundle;
import adventurepack.mods.smab.smab.attributes.Ability;
import adventurepack.mods.smab.smab.attributes.BetterTag;
import adventurepack.mods.smab.smab.attributes.DietaryEffect;
import adventurepack.mods.smab.smab.attributes.LevelAlgorithm;
import adventurepack.mods.smab.smab.json.JsonDietaryPair;
import adventurepack.mods.smab.smab.json.JsonEntityAttributeBundle;
import adventurepack.mods.smab.smab.json.JsonSmabBundle;
import net.minecraft.block.Block;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public final class Smabs {

    public static final JsonSmabBundle MISSINGNO_TEST_BUNDLE = new JsonSmabBundle(
        new Identifier(ModInit.MODID, "missingno"),
        0,
        0,
        0,
        0,
        new Ability[0], 
        LevelAlgorithm.STRAIGHT_LOG,
        new JsonDietaryPair[] {new JsonDietaryPair(Items.DIAMOND, new DietaryEffect(-5,0,0,5))},
        new BetterTag[]{},
        false,
        2,
        4,
        true,
        false,
        new Block[]{},
        Rarity.COMMON,
        new JsonEntityAttributeBundle[]{},
        SpawnGroup.CREATURE
    );

    public static final SmabBundle MISSINGNO = MISSINGNO_TEST_BUNDLE.build();

    public static void classLoad() {}
}
