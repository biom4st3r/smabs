package adventurepack.mods.smab.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;

@Mixin({ItemRenderer.class})
public class ItemRendererMixin {
    @Inject(
        at = @At(
            value = "INVOKE", 
            target = "com/mojang/blaze3d/systems/RenderSystem.enableBlend()V",
            shift = Shift.BEFORE
            ), 
        method = "renderGuiItemModel")
    private void smabs$renderWithCustomTexture(ItemStack stack, int x, int y, BakedModel model, CallbackInfo ci) {
        // TODO Shift to card texture
    }
}
