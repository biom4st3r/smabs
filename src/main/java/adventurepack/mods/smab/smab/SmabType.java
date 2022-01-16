package adventurepack.mods.smab.smab;

import net.minecraft.text.Text;

public record SmabType(
        Text name,
        Text lore,
        int base_intelligence, 
        int base_strength, 
        int base_dexterity, 
        int base_vitality,
        Ability[] abilities
        ) {

}
