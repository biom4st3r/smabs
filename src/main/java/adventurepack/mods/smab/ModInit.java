package adventurepack.mods.smab;

import java.io.File;
import java.io.FileOutputStream;

import com.google.gson.JsonObject;

import adventurepack.mods.smab.minecraft.GuiItem;
import adventurepack.mods.smab.smab.Ability;
import adventurepack.mods.smab.smab.LevelAlgorithm;
import adventurepack.mods.smab.smab.Smab;
import adventurepack.mods.smab.smab.SmabBundle;
import biom4st3r.libs.biow0rks.NoEx;
import biom4st3r.libs.biow0rks.autojson.AutoJson;
import biom4st3r.libs.biow0rks.autonbt.AutoNbt;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import software.bernie.geckolib3.GeckoLib;

public class ModInit implements ModInitializer {

	public static final boolean disable_animations = true;

	public static Item TEMPLATE = null;
	public static final String MODID = "ap_smabs";
	public static final ItemGroup SMABS_GROUP = FabricItemGroupBuilder
		.create(new Identifier(MODID,"item_group"))
		.icon(()-> new ItemStack(TEMPLATE))
		.build();


	@Override
	public void onInitialize() {
		GeckoLib.initialize();
		AutoJson.INSTANCE.register(
			LevelAlgorithm.class, 
			(o,hint) -> AutoJson.serialize(Registries.LEVEL_ARGOS.getIdOrDefault(o)), 
			(o,hint)->Registries.LEVEL_ARGOS.getOrDefault(AutoJson.deserialize(Identifier.class, o)));
		AutoJson.INSTANCE.register(
			Ability.class, 
			(o,hint) -> AutoJson.serialize(Registries.ABILITIES.getIdOrDefault(o)), 
			(o,hint)->Registries.ABILITIES.getOrDefault(AutoJson.deserialize(Identifier.class, o)));
		AutoJson.INSTANCE.register(
			EntityAttribute.class, 
			Registry.ATTRIBUTE);
		AutoJson.INSTANCE.register(
			SmabBundle.class, 
			(o,hint) -> AutoJson.serialize(Registries.SMABS.getIdOrDefault(o)), 
			(o,hint)->Registries.SMABS.getOrDefault(AutoJson.deserialize(Identifier.class, o)));
		
		AutoNbt.INSTANCE.register(
			LevelAlgorithm.class, 
			(o,hint) -> AutoNbt.serialize(Registries.LEVEL_ARGOS.getIdOrDefault(o)), 
			(o,hint)->Registries.LEVEL_ARGOS.getOrDefault(AutoNbt.deserialize(Identifier.class, o)));
		AutoNbt.INSTANCE.register(
			Ability.class, 
			(o,hint) -> AutoNbt.serialize(Registries.ABILITIES.getIdOrDefault(o)), 
			(o,hint)->Registries.ABILITIES.getOrDefault(AutoNbt.deserialize(Identifier.class, o)));
		AutoNbt.INSTANCE.register(
			EntityAttribute.class, 
			Registry.ATTRIBUTE);
		AutoNbt.INSTANCE.register(
			SmabBundle.class, 
			(o,hint) -> AutoNbt.serialize(Registries.SMABS.getIdOrDefault(o)), 
			(o,hint)->Registries.SMABS.getOrDefault(AutoNbt.deserialize(Identifier.class, o)));
		AutoNbt.INSTANCE.register(
			Smab.class, 
			(o,hint) -> o.serialize(), 
			(o,hint) -> new Smab(o));
		// export_json();
		SmabLoader.init();
		ItemLoader.init();

		TEMPLATE = Registry.register(Registry.ITEM, new Identifier(MODID, "templatinggui"), new GuiItem());
		Smabs.classLoad();

	}

	@SuppressWarnings({"unused"})
	private static void export_json() {
		JsonObject obj = AutoJson.serialize(Smabs.MISSINGNO_TEST_BUNDLE);
		File file = new File("test.json");
		FileOutputStream stream = NoEx.run(()->new FileOutputStream(file));
		NoEx.run(()->stream.write(obj.toString().getBytes()));
		NoEx.run(()->stream.close());
		System.exit(0);
	}
}
