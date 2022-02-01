package adventurepack.mods.smab.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import adventurepack.mods.smab.ModInitClient;
import adventurepack.mods.smab.component.SmabItemComponent;
import adventurepack.mods.smab.smab.SmabCardItem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

@Mixin({Screen.class})
public class ScreenMixin {

    @Inject(
        method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V",
        at = @At("HEAD"),
        cancellable = true
    )
    protected void smabs$renderToolTip(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci) {
        if (stack.getItem() instanceof SmabCardItem) {
            SmabItemComponent.KEY.borrowPooledComponent(stack, c -> ModInitClient.renderSmabToolTip(matrices, (SmabCardItem) stack.getItem(), c.smab, x, y), false);
            ci.cancel();
        }
    }

}
