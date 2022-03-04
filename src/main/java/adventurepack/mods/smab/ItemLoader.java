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
import adventurepack.mods.smab.minecraft.items.CardItem;
import biom4st3r.libs.biow0rks.NoEx;
import biom4st3r.libs.biow0rks.autojson.AutoJson;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemLoader {
    public static Set<Item> LOADED_ITEMS = Sets.newHashSet();
    
    public static Map<Identifier, JsonItemDefinition> MAP = Maps.newHashMap();

    public static void init() {
        // FileWrapper.of("config/smab_card_items/")
        //     .wrappedStream()
        //     .withValue(stream -> {
        //         stream.forEach(file -> {
        //             file.readerAdapter(reader -> AutoJson.deserialize(JsonItemDefinition.class, JsonParser.parseReader(reader)))
        //                 .withValue(def -> {
        //                     CardItem cardItem = Registry.register(Registry.ITEM, def.id(), new CardItem(def.rarity(), def.card_icon().getPath().replace("textures/cards/", "").replace("_card.png", "")));
        //                     if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
        //                         MAP.put(def.id(), def);
        //                     }
        //                     LOADED_ITEMS.add(cardItem);
        //                 });
        //         });
        //     });


        File dir = new File("config/smab_card_items/");
        dir.mkdirs();
        File[] files = dir.listFiles();
        for(File item : files) {
            JsonElement ele = JsonParser.parseReader(NoEx.run(()->new FileReader(item)));
            JsonItemDefinition def = AutoJson.deserialize(JsonItemDefinition.class, ele);
            if (!Registry.ITEM.containsId(def.id())) {
                CardItem cardItem = Registry.register(Registry.ITEM, def.id(), new CardItem(def.rarity(), def.card_icon().getPath().replace("textures/cards/", "").replace("_card.png", "")));
                LOADED_ITEMS.add(cardItem);
            }
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                MAP.put(def.id(), def);
            }
        }
    }
}
