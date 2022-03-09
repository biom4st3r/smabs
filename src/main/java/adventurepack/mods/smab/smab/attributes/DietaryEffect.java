package adventurepack.mods.smab.smab.attributes;

public record DietaryEffect(int intelligence, int strength, int dexterity, int vitality) {
    public static DietaryEffect ZERO = new DietaryEffect(0, 0, 0, 0);
}
