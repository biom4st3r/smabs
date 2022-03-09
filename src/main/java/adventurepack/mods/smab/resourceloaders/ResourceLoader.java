package adventurepack.mods.smab.resourceloaders;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.google.common.collect.Sets;

import adventurepack.mods.smab.ModInit;
import adventurepack.mods.smab.ModInitClient;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;

import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

public final class ResourceLoader implements IdentifiableResourceReloadListener {
    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager,
            Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor,
            Executor applyExecutor) {
        return CompletableFuture.runAsync(()-> {
            Set<Identifier> ids = Sets.newHashSet();

            manager.findResources("textures/foils", s->s.endsWith(".png")).forEach(id -> {
                ids.add(new Identifier(id.getNamespace(), id.getPath().replace("textures/", "").replace(".png", "")));
            });

            manager.findResources("textures/cards", s->s.endsWith(".png")).forEach(id -> {
                ids.add(new Identifier(id.getNamespace(), id.getPath().replace("textures/", "").replace(".png", "")));
            });

            // TextureLoader.init(manager, ids);

            ClientSpriteRegistryCallback.event(ModInitClient.BLOCK_ATLAS_TEXTURE).register((atlas,registry) -> {
                ids.forEach(registry::register);
            });

        }, applyExecutor).thenCompose(synchronizer::whenPrepared);
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(ModInit.MODID, "reloader");
    }
}