package adventurepack.mods.smab.smab;

import java.util.Map;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public record SmabSpecies(
        Identifier id,
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

        public Text name() {
                return new TranslatableText(String.format("%s.smab.name.%s", this.id().getNamespace(),this.id().getPath()));
        }
        public Text lore() {
                return new TranslatableText(String.format("%s.smab.lore.%s", this.id().getNamespace(),this.id().getPath()));
        }
}
