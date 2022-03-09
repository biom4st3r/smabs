package adventurepack.mods.smab.minecraft.client.itemodel;

import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public record JsonItemDefinition(Identifier id, Rarity rarity, Identifier card_face, Identifier sprite_icon) {
    
}
