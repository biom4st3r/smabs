package adventurepack.mods.smab;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import adventurepack.mods.smab.autojson.AutoJson;
import adventurepack.mods.smab.smab.LevelAlgorithm;
import adventurepack.mods.smab.smab.SmabBundle;
import adventurepack.mods.smab.smab.SmabSpecies;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

public class ModInit implements ModInitializer
{
	public static final String MODID = "ap_smabs";

	public static final List<SmabBundle> bundles = Lists.newArrayList();

	@Override
	public void onInitialize() {
		// // TODO BAD Remove this
		AutoJson.INSTANCE.register(LevelAlgorithm.class, (o,hints) -> new JsonObject(), (element,hints)-> LevelAlgorithm.STRAIGHT_LOG);
		JsonElement e = AutoJson.serialize(Smabs.MISSINGNO.species());
		// File f = new File("test.json");
		// try (FileOutputStream stream = new FileOutputStream(f)) {
		// 	stream.write(e.toString().getBytes());
		// 	stream.close();
		// } catch (IOException e1) {
		// 	// TODO Auto-generated catch block
		// 	e1.printStackTrace();
		// }


		File[] files = new File("smabs").listFiles();
		if (files == null || files.length == 0) return;
		for (File file : files) {
			try (FileReader stream = new FileReader(file)) {
				JsonElement object = JsonParser.parseReader(stream);
				SmabSpecies species = AutoJson.deserialize(SmabSpecies.class, object);
				SmabBundle bundle = new SmabBundle(species, FabricEntityTypeBuilder.createMob());
				bundles.add(bundle);
			} catch (IOException ee) {
				// TODO Auto-generated catch block
				ee.printStackTrace();
			}
		}
	}
}
