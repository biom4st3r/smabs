package adventurepack.mods.smab.smab;

import java.util.Random;
import java.util.UUID;

import com.google.common.base.Preconditions;

import adventurepack.mods.smab.Registries;
import adventurepack.mods.smab.component.SmabItemComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class Smab {

    private static final LevelAlgorithm STAT = exp -> (int)exp/100;
    private static final int MAX_INDIVIDUAL_STAT = 31 * 100;
    private static final int MAX_HAPPINESS = 10;
    private static final int MAX_LEVEL = 33;

    /**
     * Access to this should done through getters
     */
    private final SmabSpecies type;
    /**
     * index of the ability from Smab.type.abilities[]
     */
    private final int ability_index;
    private final DNA dna;

    private int happiness;
    private int individual_intelligence;
    private int individual_strength;
    private int individual_dexterity;
    private int individual_vitality;

    private ItemStack heldItem;
    private String nickname;
    private int experience;

    private String OT = "";
    private UUID OT_UUID;

    public String getOt() {
        return OT;
    }

    public static ItemStack convertToItem(Smab smab) {
        ItemStack is = new ItemStack(Registries.SMABS.get(smab.type.id()).item());
        SmabItemComponent.KEY.borrowPooledComponent(is, comp->comp.smab = smab, true);
        return is;
    }

    public void setOT(PlayerEntity player) {
        if(OT.isEmpty()) {
            OT = player.getEntityName();
            OT_UUID = player.getUuid();
        }
    }

    public boolean isOT(PlayerEntity pe) {
        return pe.getUuid().equals(OT_UUID);
    }

    public boolean hasOT() {
        return OT_UUID != null;
    }

    public Smab(SmabSpecies species, DNA dna, int happiness, 
            int individual_intelligence, int individual_strength,
            int individual_dexterity, int individual_vitality,
            int experience, int ability_index, ItemStack heldItem, String nickname) {

        Preconditions.checkNotNull(species);
        Preconditions.checkNotNull(dna);
        Preconditions.checkNotNull(heldItem);
        Preconditions.checkNotNull(nickname);
        this.dna = dna;
        this.type = species;
        this.ability_index = ability_index;

        this.happiness = happiness;
        this.individual_vitality = individual_vitality;
        this.individual_dexterity = individual_dexterity;
        this.individual_intelligence = individual_intelligence;
        this.individual_strength = individual_strength;

        this.experience = experience;
        this.heldItem = heldItem;
        this.nickname = nickname;
    }

    public Smab(SmabSpecies species) {
        this(species, DNA.getRandom(), ItemStack.EMPTY, "");
    }

    public Smab(SmabSpecies species, DNA dna, ItemStack heldItem, String nickname) {
        this(species, dna, 0,0,0,0,0,0, getRandomAbility(species), heldItem, nickname);
    }

    public Smab(SmabSpecies species, String nickname) {
        this(species, DNA.getRandom(), ItemStack.EMPTY, nickname);
    }

    private static int getRandomAbility(SmabSpecies species) {
        return species.abilities().length == 0 ? -1 : new Random().nextInt(species.abilities().length);
    }

    private int getStat(int dna, int species, int base) {
        return dna + (int)(species * (this.getLevel()/((float)MAX_LEVEL/2))) + STAT.getLevel(base);
    }

    public int getIntelligence() {
        return getStat(dna.intelligence(), type.base_intelligence(), this.individual_intelligence);
        // return dna.intelligence() + (int)(type.base_intelligence() * (this.getLevel()/(float)MAX_LEVEL)) + STAT.getLevel(individual_intelligence);
    }

    public int getStrength() {
        return getStat(dna.strength(), type.base_strength(), this.individual_strength);
        // return dna.strength() + (int)(type.base_strength() * (this.getLevel()/(float)MAX_LEVEL)) + STAT.getLevel(individual_strength);
    }

    public int getDexterity() {
        return getStat(dna.dexterity(), type.base_dexterity(), this.individual_dexterity);
        // return dna.dexterity() + (int)(type.base_dexterity() * (this.getLevel()/(float)MAX_LEVEL)) + STAT.getLevel(individual_dexterity);
    }

    public int getVitality() {
        return getStat(dna.vitality(), type.base_vitality(), this.individual_vitality);
        // return dna.vitality() + (int)(type.base_vitality() * (this.getLevel()/(float)MAX_LEVEL)) + STAT.getLevel(individual_vitality);
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
        // TODO Should probably cap this?
        return this.type.algo().getLevel(this.experience);
    }

    public void addExperience(int exp) {
        if (exp > 0 && this.getLevel() >= MAX_LEVEL) {

        } else {
            this.experience += exp;
        }
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

    private void addToVitality(int i) {
        this.individual_vitality = Math.max(Math.min(this.individual_vitality + i, MAX_INDIVIDUAL_STAT), 0);
    }
    private void addToStrength(int i) {
        this.individual_strength += Math.max(Math.min(this.individual_vitality + i, MAX_INDIVIDUAL_STAT), 0);
    }
    private void addToIntelligence(int i) {
        this.individual_intelligence += Math.max(Math.min(this.individual_vitality + i, MAX_INDIVIDUAL_STAT), 0);
    }
    private void addToDexterity(int i) {
        this.individual_dexterity += Math.max(Math.min(this.individual_vitality + i, MAX_INDIVIDUAL_STAT), 0);
    }
    public void addToHappiness(int i) {
        this.happiness = Math.max(Math.min(this.happiness + i, MAX_HAPPINESS), 0);
    }

    public void feed(Item item) {
        DietaryEffect effect = this.type.diet().getOrDefault(item, DietaryEffect.ZERO);
        addToVitality(effect.vitality());
        addToStrength(effect.strength());
        addToIntelligence(effect.intelligence());
        addToDexterity(effect.dexterity());
    }

    public Item[] getFoods() {
        return this.type.diet().keySet().toArray(Item[]::new);
    }

    public boolean hasTag(Tag t) {
        return this.type.tags().contains(t);
    }
}
