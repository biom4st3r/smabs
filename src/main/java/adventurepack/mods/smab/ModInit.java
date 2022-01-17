package adventurepack.mods.smab;

import adventurepack.mods.SmabLoader;
import adventurepack.mods.smab.autojson.AutoJson;
import adventurepack.mods.smab.smab.Ability;
import adventurepack.mods.smab.smab.LevelAlgorithm;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public class ModInit implements ModInitializer {

	public static final String MODID = "ap_smabs";
	public static final ItemGroup SMABS_GROUP = FabricItemGroupBuilder.create(new Identifier(MODID,"item_group")).build();

	@Override
	public void onInitialize() {
		Smabs.classLoad();
		AutoJson.INSTANCE.register(LevelAlgorithm.class, (o,hint) -> AutoJson.serialize(Registries.LEVEL_ARGOS.inverse().getOrDefault(o, Registries.DEFAULT)), (o,hint)->Registries.LEVEL_ARGOS.get(AutoJson.deserialize(Identifier.class, o)));
		AutoJson.INSTANCE.register(Ability.class, (o,hint) -> AutoJson.serialize(Registries.ABILITIES.inverse().getOrDefault(o, Registries.DEFAULT)), (o,hint)->Registries.ABILITIES.get(AutoJson.deserialize(Identifier.class, o)));

		SmabLoader.init();
	}
}
