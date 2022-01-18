package adventurepack.mods.smab.autojson;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.jetbrains.annotations.ApiStatus.Internal;

import biom4st3r.libs.biow0rks.BioLogger;
import biom4st3r.libs.biow0rks.NoEx;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@SuppressWarnings({"rawtypes","unchecked"})
public abstract class AutoJson {

    public static AutoJson INSTANCE = new DefaultAutoJson();

    protected static final BioLogger LOGGER = new BioLogger("AUTO_JSON");
    public static final AutoJsonSerialize DEFAULT_HINTS = spoofAnnotation(null, null, null);

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface AutoJsonSerialize {
        Class<?> mapKeyHint() default Void.class;
        Class<?> mapValHint() default Void.class;
        Class<?> collectionHint() default Void.class;
        boolean collectionUseLookupTable() default false;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface SkipAutoSerialize {}

    /**
     * B is purely for type hinting in the register function
     */
    public static interface JsonSerial<T,B extends JsonElement> {
        B serial(T t, AutoJsonSerialize hints);
    }

    /**
     * B is purely for type hinting in the register function
     */
    public static interface JsonDeserial<T,B extends JsonElement> {
        T deserial(B toTag, AutoJsonSerialize hints);
    }

    public static record SerialContext(Class<?> fieldType, String fieldName, MethodHandle handle, AutoJsonSerialize hints){}

    protected static boolean isPublic(Field f) {
        return Modifier.isPublic(f.getModifiers());
    }

    protected static boolean isStatic(Field f) {
        return Modifier.isStatic(f.getModifiers());
    }

    protected static boolean hasAnnotation(Field f) {
        return f.getAnnotation(AutoJsonSerialize.class) != null;
    }

    private static AutoJsonSerialize spoofAnnotation(Class<?> mapKeyType, Class<?> mapValType, Class<?> collectionsType) {
        return new AutoJsonSerialize() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return AutoJsonSerialize.class;
            }

            @Override
            public Class<?> mapKeyHint() {
                return mapKeyType == null ? Void.class : mapKeyType;
            }

            @Override
            public Class<?> mapValHint() {
                return mapValType == null ? Void.class : mapValType;
            }

            @Override
            public Class<?> collectionHint() {
                return collectionsType == null ? Void.class : collectionsType;
            }

            @Override
            public boolean collectionUseLookupTable() {
                return false;
            }
            
        };
    }

    protected static final Map<Class<?>,JsonSerial<?,?>> SERIALIZE = Maps.newHashMap();
    protected static final Map<Class<?>,JsonDeserial<?,?>> DESERIALIZE = Maps.newHashMap();

    public <T,B extends JsonElement> void register(Class<T> t, JsonSerial<T,B> ser, JsonDeserial<T,B> deser) {
        SERIALIZE.put(t, ser);
        DESERIALIZE.put(t, deser);
    }

    public <T, B extends JsonElement> void register(Class<T> t, Registry<T> registry) {
        INSTANCE.register(t, (item,hints) -> serialize(registry.getId(item)), (tag,hints)->registry.get(deserialize(Identifier.class, tag)));
    }

    /**
     * Converts any object to nbt
     * @param host - Object attempting to serialize
     * @return
     */
    public static <T extends JsonElement> T serialize(Object host) {
        return INSTANCE.serialize(host.getClass(), host, DEFAULT_HINTS);
    }

    @Internal
    public static <T extends JsonElement> T serialize(Object host, AutoJsonSerialize hints) {
        return INSTANCE.serialize(host.getClass(), host, hints);
    }

    public abstract <T extends JsonElement> T serialize(Class<?> hostClass, Object host, AutoJsonSerialize hints);

    public static <T> T deserialize(Class<T> host, JsonElement element) {
        return INSTANCE.deserialize(host, element, DEFAULT_HINTS);
    }

    @Internal
    public abstract <T> T deserialize(Class<T> hostClass, JsonElement element, AutoJsonSerialize hints);

    private static JsonObject serializeListNoLookup(List o, AutoJsonSerialize hints) {
        JsonArray list = new JsonArray();

        JsonSerial serial = hints.collectionHint() == Void.class ?  null : SERIALIZE.get(hints.collectionHint());

        for (Object object : o) {
            if (serial != null) {
                list.add(serial.serial(o, DEFAULT_HINTS));
            } else {
                list.add(serialize(object));
            }
        }
        JsonObject holder = new JsonObject();
        if (serial == null) {
            holder.add("TYPE", new JsonPrimitive(o.get(0).getClass().getCanonicalName()));
            // holder.putString("TYPE", o.get(0).getClass().getCanonicalName());
        } else {
            holder.add("TYPE", new JsonPrimitive(hints.collectionHint().getCanonicalName()));
            // holder.putString("TYPE", hints.collectionHint().getCanonicalName());
        }
        holder.add("LIST", list);
        // holder.put("LIST", list);
        return holder;
    }

    private static List deserializeListNolookup(JsonObject nbtcompound, AutoJsonSerialize hints) {
        Class<?> clazz = NoEx.run(() -> Class.forName(nbtcompound.get("TYPE").getAsString()));
        JsonArray nbtlist = nbtcompound.get("LIST").getAsJsonArray();

        List newList = Lists.newArrayListWithExpectedSize(nbtlist.size());
        nbtlist.forEach(ele -> newList.add(deserialize(clazz, ele)));
        return newList;
    }

    static {
        INSTANCE.register(List.class, (list,hints) -> {
            if (list.isEmpty()) {
                return new JsonObject();
            } else if(!hints.collectionUseLookupTable()) {
                return serializeListNoLookup(list, hints);
            } else {
                // dedup the list
                List lookup = Lists.newArrayList(Sets.newHashSet(list));
                int[] collection = new int[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    collection[i] = lookup.indexOf(list.get(i));
                }
                JsonObject nbt = new JsonObject();
                nbt.add("LOOKUP", serialize(lookup));
                // nbt.put("LOOKUP", serialize(lookup));
                nbt.add("COLLECTION", serialize(collection));
                // nbt.put("COLLECTION", serialize(collection));
                return nbt;
            }
        }, (nbtcompound, hints) -> {
            if (nbtcompound.keySet().isEmpty()) return Collections.emptyList();
            else if(!hints.collectionUseLookupTable()) {
                return deserializeListNolookup(nbtcompound, hints);
            } else {
                List lookup = deserialize(List.class, nbtcompound.get("LOOKUP"));
                int[] collection = deserialize(int[].class, nbtcompound.get("COLLECTION"));
                List list = Lists.newArrayListWithExpectedSize(collection.length);
                for (int i : collection) {
                    list.add(lookup.get(i));
                }
                return list;
            }
        });
        INSTANCE.register(Set.class, (o,hints) -> {
            if (o.isEmpty()) return new JsonObject();
            JsonArray list = new JsonArray();

            JsonSerial serial = hints.collectionHint() == Void.class ?  null : SERIALIZE.get(hints.collectionHint());

            for (Object object : o) {
                if (serial != null) {
                    list.add(serial.serial(o, DEFAULT_HINTS));
                } else {
                    list.add(serialize(object));
                }
            }
            JsonObject holder = new JsonObject();
            if (serial == null) {
                holder.add("TYPE", new JsonPrimitive(o.iterator().next().getClass().getCanonicalName()));
            } else {
                holder.add("TYPE", new JsonPrimitive(hints.collectionHint().getCanonicalName()));
            }
            holder.add("SET", list);
            return holder;
        }, (nbtcompound,hints) -> {
            if (nbtcompound.keySet().isEmpty()) return Collections.emptySet();
            
            Class<?> clazz = NoEx.run(() -> Class.forName(nbtcompound.get("TYPE").getAsString()));
            JsonArray nbtlist = (JsonArray) nbtcompound.get("SET");

            Set newList = Sets.newHashSetWithExpectedSize(nbtlist.size());
            nbtlist.forEach(ele -> newList.add(deserialize(clazz, ele)));
            return newList;
        });
        INSTANCE.register(Map.class, (map, hints) -> {
            if (map.isEmpty()) return new JsonArray();

            JsonArray host = new JsonArray();
            JsonElement keys;
            if (hints.mapKeyHint() != Void.class) {
                keys = serialize(map.keySet(), spoofAnnotation(null, null, hints.mapKeyHint()));
            } else {
                keys = INSTANCE.serialize(Set.class,map.keySet(),DEFAULT_HINTS);
            }
            JsonElement vals;
            if (hints.mapValHint() != Void.class) {
                vals = serialize(Lists.newArrayList(map.values()), spoofAnnotation(null, null, hints.mapValHint()));
            } else {
                vals = INSTANCE.serialize(List.class, Lists.newArrayList(map.values()), DEFAULT_HINTS);
            }
            host.add(keys);
            host.add(vals);
            return host;
        }, (nbtlist,hints) -> {
            if (nbtlist.isEmpty()) return Collections.emptyMap();
        
            Map map = Maps.newHashMap();
            Iterator keys = deserialize(Set.class, nbtlist.get(0)).iterator();
            Iterator vals = deserialize(List.class, nbtlist.get(1)).iterator();
            while(keys.hasNext()) {
                map.put(keys.next(), vals.next());
            }
            return map;
        });

        INSTANCE.register(Identifier.class, (id,hints) -> new JsonPrimitive(id.toString()),     (nbtstring,hints) -> new Identifier(nbtstring.getAsString()));
        INSTANCE.register(String.class,  (s,hints) -> new JsonPrimitive(s),                     (nbtstring,hints) -> nbtstring.getAsString());
        INSTANCE.register(Boolean.class, (b,hints) -> new JsonPrimitive(b),                     (nbtbyte,hints) -> nbtbyte.getAsBoolean());
        INSTANCE.register(Double.class,  (s,hints) -> new JsonPrimitive(s),                     (nbtdouble,hints) -> nbtdouble.getAsDouble());
        INSTANCE.register(Float.class,   (s,hints) -> new JsonPrimitive(s),                     (nbtfloat,hints) -> nbtfloat.getAsFloat());
        INSTANCE.register(Byte.class,    (b,hints) -> new JsonPrimitive(b),                     (nbtbyte,hints) -> nbtbyte.getAsByte());
        INSTANCE.register(Integer.class, (s,hints) -> new JsonPrimitive(s),                     (nbtint,hints) -> nbtint.getAsInt());
        INSTANCE.register(Long.class,    (s,hints) -> new JsonPrimitive(s),                     (nbtlong,hints) -> nbtlong.getAsLong());
        INSTANCE.register(boolean.class, (b,hints) -> new JsonPrimitive(b),                     (nbtbyte,hints) -> nbtbyte.getAsBoolean());
        INSTANCE.register(double.class,  (s,hints) -> new JsonPrimitive(s),                     (nbtdouble,hints) -> nbtdouble.getAsDouble());
        INSTANCE.register(float.class,   (s,hints) -> new JsonPrimitive(s),                     (nbtfloat,hints) -> nbtfloat.getAsFloat());
        INSTANCE.register(byte.class,    (b,hints) -> new JsonPrimitive(b),                     (nbtbyte,hints) -> nbtbyte.getAsByte());
        INSTANCE.register(int.class,     (s,hints) -> new JsonPrimitive(s),                     (nbtint,hints) -> nbtint.getAsInt());
        INSTANCE.register(long.class,    (s,hints) -> new JsonPrimitive(s),                     (nbtlong,hints) -> nbtlong.getAsLong());
        // INSTANCE.register(byte[].class,  (b,hints) -> new NbtByteArray(b),               (nbtba,hints) -> nbtba.getByteArray());
        // INSTANCE.register(int[].class,   (s,hints) -> new NbtIntArray(s),                (nbtia,hints) -> nbtia.getIntArray());
        // INSTANCE.register(long[].class,  (s,hints) -> new NbtLongArray(s),               (nbtla,hints) -> nbtla.getLongArray());
        // INSTANCE.register(BlockState.class, 
        //     (bs,hints)-> BlockState.CODEC.stable().encodeStart(NbtOps.INSTANCE, bs).getOrThrow(true, (s)->new RuntimeException(s)), 
        //     (nbtelement,hints)->BlockState.CODEC.stable().decode(NbtOps.INSTANCE, nbtelement).getOrThrow(true, (str) -> new RuntimeException(str)).getFirst()
        // );
        INSTANCE.register(Block.class, Registry.BLOCK);
        INSTANCE.register(Item.class, Registry.ITEM);
        INSTANCE.register(Enchantment.class, Registry.ENCHANTMENT);
        INSTANCE.register(StatusEffect.class, Registry.STATUS_EFFECT);
        INSTANCE.register(EntityType.class, (Registry)Registry.ENTITY_TYPE);
        INSTANCE.register(BlockEntityType.class, (Registry)Registry.BLOCK_ENTITY_TYPE);

        // INSTANCE.register(ItemStack.class, (stack,hints)-> stack.writeNbt(new JsonObject()), (tag,hints)->ItemStack.fromNbt(tag));
        INSTANCE.register(Class.class, (clazz,hints) -> serialize(clazz.getCanonicalName()), (nbtString, hints) -> NoEx.run(()->Class.forName(deserialize(String.class, nbtString))));

    }
}
