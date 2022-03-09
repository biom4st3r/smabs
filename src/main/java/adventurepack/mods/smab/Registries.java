package adventurepack.mods.smab;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import adventurepack.mods.smab.smab.SmabBundle;
import adventurepack.mods.smab.smab.attributes.Ability;
import adventurepack.mods.smab.smab.attributes.LevelAlgorithm;
import biom4st3r.libs.biow0rks.BioLogger;
import net.minecraft.util.Identifier;

public class Registries {
    public static class Census<T> {
        private static BioLogger LOGGER = new BioLogger("CENSUS"); 
        private Identifier default_id;
        private T default_value;
        private final BiMap<Identifier,T> MAP = HashBiMap.create();
        public Census(Identifier default_id, T default_value) {
            this.default_id = default_id;
            this.default_value = default_value;
        }

        public T register(Identifier id, T t) {
            if (MAP.containsKey(id)) {
                LOGGER.error("%s is already registered. Skipping.", id.toString());
                new RuntimeException().printStackTrace();
                return t;
            }
            MAP.put(id, t);
            return t;
        }

        public T get(Identifier id) {
            return MAP.get(id);
        }
        public T getOrDefault(Identifier id) {
            return MAP.getOrDefault(id, default_value);
        }

        public Identifier getId(T t) {
            return MAP.inverse().get(t);
        }

        public Identifier getIdOrDefault(T t) {
            return MAP.inverse().getOrDefault(t, default_id);
        }
    }

    public static final Identifier DEFAULT = new Identifier(ModInit.MODID, "missingno");
    public static final Census<LevelAlgorithm> LEVEL_ARGOS = new Census<>(DEFAULT, LevelAlgorithm.MEDIUM_FAST);
    public static final Census<Ability> ABILITIES = new Census<>(DEFAULT, null);
    public static final Census<SmabBundle> SMABS = new Census<>(DEFAULT, Smabs.MISSINGNO);
    static {
    }

}
