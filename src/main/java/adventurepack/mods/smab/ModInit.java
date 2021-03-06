package adventurepack.mods.smab;

import java.io.File;
import java.io.FileOutputStream;

import com.google.gson.JsonObject;

import adventurepack.mods.smab.minecraft.items.GuiItem;
import adventurepack.mods.smab.resourceloaders.ItemLoader;
import adventurepack.mods.smab.resourceloaders.SmabLoader;
import biom4st3r.libs.biow0rks.NoEx;
import biom4st3r.libs.biow0rks.autojson.AutoJson;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
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
		SerializerInit.init();
		GeckoLib.initialize();
		// UseItemCallback.EVENT.register((player,world,hand) -> {
		// 	NbtCompound compound = new NbtCompound();
		// 	player.writeNbt(compound);
		// 	String formatted_nbt = new NbtFormater().accept(compound.toString());
		// 	try {
		// 		FileOutputStream i = new FileOutputStream(new File("test.hrnbt"));
		// 		i.write(formatted_nbt.getBytes());
		// 		i.close();
		// 		// FileOutputStream i = new FileOutputStream(new File("test.nbt"));
		// 		// i.write(compound.toString().getBytes());
		// 		// i.close();
		// 	} catch (Throwable e1) {
		// 		// TODO Auto-generated catch block
		// 		e1.printStackTrace();
		// 	}

		// 	try {
		// 		player.readNbt(StringNbtReader.parse(formatted_nbt));
		// 	} catch (CommandSyntaxException e) {
		// 		e.printStackTrace();
		// 	}
		// 	return TypedActionResult.pass(null);
		// });
		Smabs.classLoad();
		SmabLoader.init();
		ItemLoader.init();

		TEMPLATE = Registry.register(Registry.ITEM, new Identifier(MODID, "templatinggui"), new GuiItem());
	}

	@SuppressWarnings({"unused"})
	private static void export_json() {
		JsonObject obj = AutoJson.serialize(Smabs.MISSINGNO_TEST_BUNDLE);
		// FileWrapper f = new FileWrapper("test.json");
		// f.writeToFile(obj.toString(), false);

		File file = new File("test.json");
		FileOutputStream stream = NoEx.run(()->new FileOutputStream(file));
		NoEx.run(()->stream.write(obj.toString().getBytes()));
		NoEx.run(()->stream.close());
		System.exit(0);
	}
}
