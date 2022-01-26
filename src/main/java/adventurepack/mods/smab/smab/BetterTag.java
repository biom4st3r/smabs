package adventurepack.mods.smab.smab;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.gson.JsonPrimitive;

import adventurepack.mods.smab.ItemLoader.CardItem;
import biom4st3r.libs.biow0rks.BioLogger;
import biom4st3r.libs.biow0rks.autojson.AutoJson;
import net.minecraft.item.Item;

public final class BetterTag {
    private static final BioLogger logger = new BioLogger("SMAB_TAGS");
    private static final Map<String, BetterTag> TAGS = Maps.newHashMap();
    private String name;
    public static final BetterTag 
        COMMON = new BetterTag("common"),
        UNCOMMON = new BetterTag("uncommon"),
        RARE = new BetterTag("rare"),
        EPIC = new BetterTag("epic"),

        opulent_oceans = new BetterTag("opulent_oceans"),
        agonizing_abyss = new BetterTag("agonizing_abyss"),
        grindy_gear = new BetterTag("grindy_gear"),
        fascinating_flora = new BetterTag("fascinating_flora"),
        fantastic_fauna = new BetterTag("fantastic_fauna"),
        mythic_misc = new BetterTag("mythic_misc"),
        tinkers_toolbox = new BetterTag("tinkers_toolbox"),
        integral_interior = new BetterTag("integral_interior"),
        famous_foodstuff = new BetterTag("famous_foodstuff"),
        typical_treassures = new BetterTag("typical_treassures")
    ;


    private BetterTag(String string) {
        this.name = string;
        TAGS.put(this.name, this);
    }

    public static BetterTag getOrCreate(String s) {
        BetterTag tag = TAGS.get(s);
        if (tag != null) return tag;
        logger.error("NEW TAG %s CREATED!", s);
        return new BetterTag(s);
    }

    @Override
    public String toString() {
        return "[BetterTag: " + this.name + "]";
    }

    public static Set<BetterTag> getTags(Item item) {
        if (item instanceof CardItem card) {
            
        } else if (item instanceof SmabItem smab) {
            return smab.species.tags();
        }
        return Collections.emptySet();
    }

    static {
        AutoJson.INSTANCE.register(BetterTag.class, (o,hints)->new JsonPrimitive(o.name), (o,hints)->getOrCreate(o.getAsString()));
    }
}
