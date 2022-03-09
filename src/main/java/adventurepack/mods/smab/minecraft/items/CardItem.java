package adventurepack.mods.smab.minecraft.items;

import java.util.Set;

import com.google.common.collect.Sets;

import adventurepack.mods.smab.ModInit;
import adventurepack.mods.smab.smab.SmabSpecies;
import adventurepack.mods.smab.smab.attributes.BetterTag;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class CardItem extends Item {
    public final Set<BetterTag> TAGS;
    public CardItem(Rarity rarity, String s, Settings settings) {
        super(settings.group(ModInit.SMABS_GROUP).rarity(rarity));
        TAGS = Sets.newHashSet();
        switch (rarity) {
            case COMMON:
                TAGS.add(BetterTag.COMMON);
                break;
            case EPIC:
                TAGS.add(BetterTag.EPIC);
                break;
            case RARE:
                TAGS.add(BetterTag.RARE);
                break;
            case UNCOMMON:
                TAGS.add(BetterTag.UNCOMMON);
                break;
        }
        TAGS.add(BetterTag.getOrCreate(s));
    }
    public CardItem(Rarity rarity, String s) {
        this(rarity,s,new Settings());
    }
    public CardItem(SmabSpecies species, Settings settings) {
        super(settings);
        this.TAGS = species.tags();
    }
}