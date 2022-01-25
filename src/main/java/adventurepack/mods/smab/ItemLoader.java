package adventurepack.mods.smab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import adventurepack.mods.smab.autojson.AutoJson;
import adventurepack.mods.smab.minecraft.client.itemodel.JsonItemDefinition;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemLoader {
    public static Set<Item> LOADED_ITEMS = Sets.newHashSet();
    public static class CardItem extends Item {
        public CardItem() {
            super(new Settings().maxCount(16).group(ModInit.SMABS_GROUP));
        }
    }

    public static Map<Identifier, JsonItemDefinition> MAP = Maps.newHashMap();

    public static void init() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
        File dir = new File("config/smab_card_items/");
        File[] files = dir.listFiles();
        for(File item : files) {

            JsonElement ele = JsonParser.parseReader(new FileReader(item));
            JsonItemDefinition def = AutoJson.deserialize(JsonItemDefinition.class, ele);
            CardItem cardItem = Registry.register(Registry.ITEM, def.id(), new CardItem());
            
            LOADED_ITEMS.add(cardItem);
            MAP.put(def.id(), def);
        }


    }
}
