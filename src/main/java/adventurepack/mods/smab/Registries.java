package adventurepack.mods.smab;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import adventurepack.mods.smab.smab.Ability;
import adventurepack.mods.smab.smab.LevelAlgorithm;
import adventurepack.mods.smab.smab.SmabBundle;
import net.minecraft.util.Identifier;

public class Registries {
    public static final Identifier DEFAULT = new Identifier(ModInit.MODID, "missingno");
    public static final BiMap<Identifier,LevelAlgorithm> LEVEL_ARGOS = HashBiMap.create();
    public static final BiMap<Identifier,Ability> ABILITIES = HashBiMap.create();
    public static final BiMap<Identifier,SmabBundle> SMABS = HashBiMap.create();
    static {
        LEVEL_ARGOS.put(DEFAULT, exp -> 0);
        ABILITIES.put(DEFAULT, null); // TODO Remove null and make an Empty ability
        // SMABS.put(DEFAULT, Smabs)
    }

}
