package adventurepack.mods.smab.smab;

import java.util.Map;
import java.util.Set;

import adventurepack.mods.smab.smab.attributes.Ability;
import adventurepack.mods.smab.smab.attributes.BetterTag;
import adventurepack.mods.smab.smab.attributes.DietaryEffect;
import adventurepack.mods.smab.smab.attributes.LevelAlgorithm;
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
        Set<BetterTag> tags,
        boolean hasEntity
        ) {

        public Text name() {
                return new TranslatableText(String.format("smab.name.%s.%s", this.id().getNamespace(),this.id().getPath()));
        }

        public Text lore() {
                return new TranslatableText(String.format("smab.lore.%s.%s", this.id().getNamespace(),this.id().getPath()));
        }
}
