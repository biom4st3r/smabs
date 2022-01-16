package adventurepack.mods.smab.smab;

import net.minecraft.text.Text;

public record SmabSpecies(
        Text name,
        Text lore,
        int base_intelligence, 
        int base_strength, 
        int base_dexterity, 
        int base_vitality,
        Ability[] abilities,
        LevelAlgorithm algo
        ) {
}
