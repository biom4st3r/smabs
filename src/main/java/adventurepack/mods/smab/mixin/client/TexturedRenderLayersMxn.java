package adventurepack.mods.smab.mixin.client;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;

@Mixin({TexturedRenderLayers.class})
public class TexturedRenderLayersMxn {
    // Thanks to BulkyShulkies by i509vcb
    @Inject(at = @At("HEAD"), method = "addDefaultTextures")
    private static void addCardAtlas(Consumer<SpriteIdentifier> adder, CallbackInfo ci) {
        // ModInitClient.registerAtlas(adder);
        // TODO
    }
}
