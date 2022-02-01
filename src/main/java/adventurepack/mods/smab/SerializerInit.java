package adventurepack.mods.smab;

import adventurepack.mods.smab.smab.Ability;
import adventurepack.mods.smab.smab.LevelAlgorithm;
import adventurepack.mods.smab.smab.Smab;
import adventurepack.mods.smab.smab.SmabBundle;
import biom4st3r.libs.biow0rks.autojson.AutoJson;
import biom4st3r.libs.biow0rks.autonbt.AutoNbt;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SerializerInit {
    public static void init() {
        AutoJson.INSTANCE.register(
			LevelAlgorithm.class, 
			(o,hint) -> AutoJson.serialize(Registries.LEVEL_ARGOS.getIdOrDefault(o)), 
			(o,hint) -> Registries.LEVEL_ARGOS.getOrDefault(AutoJson.deserialize(Identifier.class, o)));
		AutoJson.INSTANCE.register(
			Ability.class, 
			(o,hint) -> AutoJson.serialize(Registries.ABILITIES.getIdOrDefault(o)), 
			(o,hint) -> Registries.ABILITIES.getOrDefault(AutoJson.deserialize(Identifier.class, o)));
		AutoJson.INSTANCE.register(
			EntityAttribute.class, 
			Registry.ATTRIBUTE);
		AutoJson.INSTANCE.register(
			SmabBundle.class, 
			(o,hint) -> AutoJson.serialize(Registries.SMABS.getIdOrDefault(o)), 
			(o,hint) -> Registries.SMABS.getOrDefault(AutoJson.deserialize(Identifier.class, o)));
		
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
    }
}
