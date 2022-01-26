package adventurepack.mods.smab;

import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import adventurepack.mods.smab.minecraft.client.itemodel.JsonItemDefinition;
import biom4st3r.libs.biow0rks.NoEx;
import biom4st3r.libs.biow0rks.autojson.AutoJson;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
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

    public static void init() {
        File dir = new File("config/smab_card_items/");
        dir.mkdirs();
        File[] files = dir.listFiles();
        for(File item : files) {
            JsonElement ele = JsonParser.parseReader(NoEx.run(()->new FileReader(item)));
            JsonItemDefinition def = AutoJson.deserialize(JsonItemDefinition.class, ele);
            CardItem cardItem = Registry.register(Registry.ITEM, def.id(), new CardItem());
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                MAP.put(def.id(), def);
            }
            LOADED_ITEMS.add(cardItem);
        }
    }
}
