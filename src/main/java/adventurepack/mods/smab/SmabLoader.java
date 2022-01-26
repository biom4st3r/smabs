package adventurepack.mods.smab;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import adventurepack.mods.smab.smab.SmabBundle;
import adventurepack.mods.smab.smab.SmabSpecies;
import biom4st3r.libs.biow0rks.autojson.AutoJson;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.item.Item;

public final class SmabLoader {

	public static final List<SmabBundle> bundles = Lists.newArrayList();

    private static final String DIR = "smabs";

    public static void init() {
        File folder = new File(DIR);
        folder.mkdirs();
        File[] smabs = folder.listFiles();
        if (smabs == null || smabs.length == 0) {
            return;
        }

        for (File file : smabs) {
			try (FileReader stream = new FileReader(file)) {
				JsonElement object = JsonParser.parseReader(stream);
				SmabSpecies species = AutoJson.deserialize(SmabSpecies.class, object);
				SmabBundle bundle = new SmabBundle(species, FabricEntityTypeBuilder.createMob(), new Item.Settings());
				bundles.add(bundle);
			} catch (IOException ee) {
				ee.printStackTrace();
			}
		}
    }
}
