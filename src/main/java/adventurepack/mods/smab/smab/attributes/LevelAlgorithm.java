package adventurepack.mods.smab.smab.attributes;

import adventurepack.mods.smab.ModInit;
import adventurepack.mods.smab.Registries;
import net.minecraft.util.Identifier;

/**
 * Convert an amount of experience into a level
 */
public interface LevelAlgorithm {
    public static final LevelAlgorithm STRAIGHT_LOG = Registries.LEVEL_ARGOS.register(new Identifier(ModInit.MODID, "straight_log"), exp -> (int)Math.log10(exp));
    public static final LevelAlgorithm MEDIUM_FAST = Registries.LEVEL_ARGOS.register(new Identifier(ModInit.MODID, "medium_fast"), exp -> (int) Math.pow(exp,1.0F / 3.0F)+1);
    public static final LevelAlgorithm FAST = Registries.LEVEL_ARGOS.register(new Identifier(ModInit.MODID, "fast"), exp -> (int) Math.cbrt( (5F * exp) / 4F) );
    public static final LevelAlgorithm SLOW = Registries.LEVEL_ARGOS.register(new Identifier(ModInit.MODID, "slow"), exp -> (int) Math.cbrt( (4F * exp) / 5F) );
    int getLevel(int exp);
}
