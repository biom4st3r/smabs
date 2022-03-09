package adventurepack.mods.smab.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemStack;

@Mixin({ItemRenderer.class})
public class ItemRendererMixin {
    @Shadow
    @Final
    private TextureManager textureManager;

    @Inject(
        at = @At(
            value = "INVOKE", 
            target = "com/mojang/blaze3d/systems/RenderSystem.enableBlend()V",
            shift = Shift.BEFORE
            ), 
        method = "renderGuiItemModel")
    private void smabs$renderWithCustomTexture(ItemStack stack, int x, int y, BakedModel model, CallbackInfo ci) {
        // // TODO Shift to card texture
        // if (stack.getItem() instanceof CardItem && Boolean.FALSE) {
        //     textureManager.getTexture(TextureLoader.CARD_ATLAS_ID).setFilter(false, false);
        //     RenderSystem.setShaderTexture(0, TextureLoader.CARD_ATLAS_ID);
        //     // System.out.println("TTTTT");
        // }
    }
}
