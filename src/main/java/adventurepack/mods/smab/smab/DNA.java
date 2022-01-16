package adventurepack.mods.smab.smab;

import java.util.Random;

/**
 * Randomly generated dna for any smab
 */
public record DNA(int intelligence, int strength, int dexterity, int vitality) {
    private static final int MAX_DNA_VALUE = 63;

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
