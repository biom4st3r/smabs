package adventurepack.mods.smab;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import adventurepack.mods.smab.smab.Ability;
import adventurepack.mods.smab.smab.LevelAlgorithm;
import net.minecraft.util.Identifier;

public class Registries {
    public static final BiMap<Identifier,LevelAlgorithm> LEVEL_ARGOS = HashBiMap.create();
    public static final BiMap<Identifier,Ability> ABILITIES = HashBiMap.create();

}
