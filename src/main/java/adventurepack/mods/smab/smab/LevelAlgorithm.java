package adventurepack.mods.smab.smab;

/**
 * Convert an amount of experience into a level
 */
public interface LevelAlgorithm {
    public static final LevelAlgorithm STRAIGHT_LOG = exp -> (int)Math.log10(exp);
    int getLevel(int exp);
}
