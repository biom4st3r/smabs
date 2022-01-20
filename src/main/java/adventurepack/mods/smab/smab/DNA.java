package adventurepack.mods.smab.smab;

import java.util.Random;

/**
 * Randomly generated dna for any smab
 */
public record DNA(int intelligence, int strength, int dexterity, int vitality) {
    public static final int MAX_DNA_VALUE = 15;
    public static final int MASK;
    public static final int BITS;
    static {
        int mask = 0b1;
        int bits = 1;
        while (mask < MAX_DNA_VALUE) {
            mask <<= 1;
            mask |= 0b1;
            bits++;
        }
        MASK = mask;
        BITS = bits;
    }

    public static DNA getRandom() {
        Random random = new Random();
        return new DNA(
            random.nextInt(MAX_DNA_VALUE),
            random.nextInt(MAX_DNA_VALUE),
            random.nextInt(MAX_DNA_VALUE),
            random.nextInt(MAX_DNA_VALUE)
        );
    }
}
