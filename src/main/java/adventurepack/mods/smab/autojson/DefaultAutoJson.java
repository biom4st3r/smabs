package adventurepack.mods.smab.autojson;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.List;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import biom4st3r.libs.biow0rks.NoEx;

@SuppressWarnings({"rawtypes","unchecked"})
public class DefaultAutoJson extends AutoJson {
    public AutoJsonSerialize getOrDefault(RecordComponent component) {
        if (component.getAnnotation(AutoJsonSerialize.class) == null) {
            return DEFAULT_HINTS;
        } else {
            return component.getAnnotation(AutoJsonSerialize.class);
        }
    }

    @Override
    public <T extends JsonElement> T serialize(Class<?> hostClass, Object host, AutoJsonSerialize hints) {
        // Try to use a serializer
        if (SERIALIZE.containsKey(hostClass)) {
            return (T) ((JsonSerial)SERIALIZE.get(hostClass)).serial(host, hints);
        }
        if (Record.class.isAssignableFrom(hostClass)) { // IF IS RECORD
            RecordComponent[] rcs = hostClass.getRecordComponents();
            JsonObject obj = new JsonObject();
            for (RecordComponent recordComponent : rcs) {
                try {
                    obj.add(recordComponent.getName(), serialize(recordComponent.getType(),recordComponent.getAccessor().invoke(host), getOrDefault(recordComponent)));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
            return (T) obj;
        }
        // Check for array not covered by serializers
        if (hostClass.getComponentType() != null) {
            JsonArray list = new JsonArray();
            int length = Array.getLength(host);
            for (int i = 0; i < length; i++) {
                list.add(serialize(Array.get(host, i)));
            }
            return (T) list;
        }

        // TODO Maybe asm to do direct field access instead of reflection/MethodHandles
        final List<SerialContext> ctxs = Stream
            .of(host.getClass().getDeclaredFields())
            .filter(f -> shouldSerialize(f, hostClass))
            .map(field -> new SerialContext(field.getType(), field.getName(), NoEx.run(() -> getLookup().unreflectGetter(field)), field.getAnnotation(AutoJsonSerialize.class)))
            .toList();

        JsonSerial serial = (obj,_hints)-> {
            JsonObject base = new JsonObject();
            for (SerialContext ctx : ctxs) {
                base.add(ctx.fieldName(), serialize(ctx.fieldType(), NoEx.run(() -> ctx.handle().invoke(obj)), _hints));
                // base.put(ctx.fieldName(), serialize(ctx.fieldType(), NoEx.run(() -> ctx.handle().invoke(obj)), _hints));
            }
            return base;
        };
        SERIALIZE.put(hostClass, serial);

        return (T) serial.serial(host, hints);
    }

    protected Lookup getLookup() {
        return MethodHandles.lookup();
    }

    protected boolean shouldSerialize(Field f, Class<?> hostClass) {
        return (isPublic(f) && !Modifier.isFinal(f.getModifiers())) && !Modifier.isFinal(f.getModifiers()) && ((!isStatic(f) && hasAnnotation(f)) || (hostClass.getAnnotation(AutoJsonSerialize.class) != null && f.getAnnotation(SkipAutoSerialize.class) == null));
        // return (isPublic(f) && !isStatic(f) && hasAnnotation(f)) || (hostClass.getAnnotation(AutoSerialize.class) != null && f.getAnnotation(SkipAutoSerialize.class) == null && isPublic(f));
    }

    public <T> T deserialize(Class<T> hostClass, JsonElement element, AutoJsonSerialize hints) {
        if (DESERIALIZE.containsKey(hostClass)) {
            return (T) ((JsonDeserial)DESERIALIZE.get(hostClass)).deserial(element, hints);
        }        
        if (Record.class.isAssignableFrom(hostClass)) { // IF IS RECORD 
            try {
                MethodHandle ctor = this.getLookup().unreflectConstructor(hostClass.getConstructors()[0]);
                RecordComponent[] rcs = hostClass.getRecordComponents();
                
                Object[] ctor_args = new Object[rcs.length];
                for (int i = 0; i < ctor_args.length; i++) {
                    ctor_args[i] = AutoJson.INSTANCE.deserialize(rcs[i].getType(), element.getAsJsonObject().get(rcs[i].getName()), getOrDefault(rcs[i]));
                }
                return (T) ctor.invokeWithArguments(ctor_args);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        if(hostClass.getComponentType() != null) {
            JsonArray list = (JsonArray) element;
            if (list.isEmpty()) return (T) Array.newInstance(hostClass.getComponentType(), 0);
            Object newArray = Array.newInstance(hostClass.getComponentType(), list.size());
            for (int i = 0; i < list.size(); i++) {
                Array.set(newArray, i, deserialize(hostClass.getComponentType(), list.get(i)));
            }
            return (T) newArray;
        }

        final List<SerialContext> ctxs = Stream
            .of(hostClass.getDeclaredFields())
            .filter(f -> shouldSerialize(f, hostClass))
            .map(field -> new SerialContext(field.getType(), field.getName(), NoEx.run(() -> getLookup().unreflectSetter(field)), field.getAnnotation(AutoJsonSerialize.class)))
            .toList();

        JsonDeserial deser = (ele,_hints) -> {
            T obj = NoEx.run(() -> (T)getLookup().unreflectConstructor(hostClass.getConstructor()).invokeExact());
            JsonObject c = (JsonObject) ele;
            for (SerialContext ctx : ctxs) {
                if(!c.keySet().contains(ctx.fieldName())) {
                    LOGGER.error("Class: %s has new field %s. Setting null!", hostClass.getCanonicalName(), ctx.fieldName());
                    NoEx.run(() -> ctx.handle().invokeExact(obj,null));
                    continue;
                }
                NoEx.run(()-> ctx.handle().invokeExact(obj, deserialize(ctx.fieldType(), c.get(ctx.fieldName()), ctx.hints())));
            }
            return obj;
        };
        DESERIALIZE.put(hostClass, deser);

        return (T) deser.deserial(element, hints);
    }
}
