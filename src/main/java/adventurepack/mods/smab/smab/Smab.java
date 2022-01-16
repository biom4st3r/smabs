package adventurepack.mods.smab.smab;

import net.minecraft.item.Item;

public final class Smab {
    private final DNA dna = null;
    private final SmabType type = null;
    private int happiness;
    private int individual_intelligence;
    private int individual_strength;
    private int individual_dexterity;
    private int individual_vitality;
    private int level;
    private Item heldItem;

    /**
     * index of the ability from Smab.type.abilities[]
     */
    private final int ability_index = -1;


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
}
