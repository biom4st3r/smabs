package adventurepack.mods.smab.smab;

import java.util.Map;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.text.Text;

public record SmabSpecies(
        Text name,
        Text lore,
        int base_intelligence, 
        int base_strength, 
        int base_dexterity, 
        int base_vitality,
        Ability[] abilities,
        LevelAlgorithm algo,
        Map<Item,DietaryEffect> diet,
        /**
         * Shouldn't be checked directly. Allow the Smab instance to add it own stuff
         */
        Set<Tag> tags
        ) {
}
