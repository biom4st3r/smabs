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
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

public class ModInit implements ModInitializer
{
	public static final String MODID = "ap_smabs";

	public static final List<SmabBundle> bundles = Lists.newArrayList();

	@Override
	public void onInitialize() {
		File[] files = new File("smabs").listFiles();
		for (File file : files) {
			try (FileReader stream = new FileReader(file)) {
				JsonElement object = JsonParser.parseReader(stream);
				SmabSpecies species = AutoJson.deserialize(SmabSpecies.class, object);
				SmabBundle bundle = new SmabBundle(species, FabricEntityTypeBuilder.createMob());
				bundles.add(bundle);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
