package adventurepack.mods.smab.smab;

import java.util.Random;

/**
 * Randomly generated dna for any smab
 */
public record DNA(int intelligence, int strength, int dexterity, int vitality) {
    public static DNA getRandom() {
        Random random = new Random();
        return new DNA(random.nextInt(63),random.nextInt(63),random.nextInt(63),random.nextInt(63));
    }
}
