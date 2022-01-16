package adventurepack.mods.smab.smab;

import java.util.Random;

import net.minecraft.item.ItemStack;

public final class Smab {
    private final SmabSpecies type;
    private final DNA dna;
    private int happiness;
    private int individual_intelligence;
    private int individual_strength;
    private int individual_dexterity;
    private int individual_vitality;
    private ItemStack heldItem;
    private String nickname;
    private int experience;
    /**
     * index of the ability from Smab.type.abilities[]
     */
    private final int ability_index;

    public Smab(SmabSpecies species, DNA dna, int happiness, 
            int individual_intelligence, int individual_strength,
            int individual_dexterity, int individual_vitality,
            int experience, int ability_index, ItemStack heldItem, String nickname) {
        this.dna = dna;
        this.type = species;
        this.happiness = happiness;
        this.individual_vitality = individual_vitality;
        this.individual_dexterity = individual_dexterity;
        this.individual_intelligence = individual_intelligence;
        this.individual_strength = individual_strength;
        // this.level = level;
        this.experience = experience;
        this.ability_index = ability_index;
        this.heldItem = heldItem;
        this.nickname = nickname;
    }

    private static int getRandomAbility(SmabSpecies species) {
        return species.abilities().length == 0 ? -1 : new Random().nextInt(species.abilities().length);
    }


    public Smab(SmabSpecies species) {
        this(species, DNA.getRandom(), ItemStack.EMPTY,"");
    }

    public Smab(SmabSpecies species, DNA dna, ItemStack heldItem, String nickname) {
        this(species, dna, 0,0,0,0,0,0, getRandomAbility(species), heldItem, nickname);
    }

    public Smab(SmabSpecies species, String nickname) {
        this(species, DNA.getRandom(), ItemStack.EMPTY, nickname);
    }

    public int getIntelligence() {
        return dna.intelligence() + type.base_intelligence() + individual_intelligence;
    }

    public int getStrength() {
        return dna.strength() + type.base_strength() + individual_strength;
    }

    public int getDexterity() {
        return dna.dexterity() + type.base_dexterity() + individual_dexterity;
    }

    public int getVitality() {
        return dna.vitality() + type.base_vitality() + individual_vitality;
    }

    public int getHappiness() {
        return happiness;
    }

    public Ability getAbility() {
        if (this.ability_index == -1) {
            return null; // TODO make Empty Ability to avoid null
        } else {
            return this.type.abilities()[this.ability_index];
        }
    }

    public ItemStack removeHeldItem() {
        ItemStack is = this.heldItem;
        this.heldItem = null;
        return is;
    }

    public ItemStack getHeldItem() {
        return this.heldItem;
    }

    public void setHeldItem(ItemStack is) {
        this.heldItem = is;
    }

    public int getLevel() {
        return this.type.algo().getLevel(this.experience);
    }

    public void addExperience(int exp) {
        this.experience += exp;
    }

    public void removeExperience(int exp) {
        this.addExperience(-exp);
    }

    public void setNickname(String s) {
        this.nickname = s;
    }

    public String getNickname() {
        return this.nickname;
    }
}