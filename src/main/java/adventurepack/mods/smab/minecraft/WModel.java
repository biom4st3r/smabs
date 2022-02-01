// package adventurepack.mods.smab.minecraft;

// import java.util.Random;

// import com.mojang.blaze3d.platform.GlStateManager;
// import com.mojang.blaze3d.systems.RenderSystem;

// import io.github.cottonmc.cotton.gui.widget.WWidget;
// import net.minecraft.client.MinecraftClient;
// import net.minecraft.client.render.DiffuseLighting;
// import net.minecraft.client.render.OverlayTexture;
// import net.minecraft.client.render.RenderLayer;
// import net.minecraft.client.render.TexturedRenderLayers;
// import net.minecraft.client.render.VertexConsumer;
// import net.minecraft.client.render.VertexConsumerProvider;
// import net.minecraft.client.render.model.BakedModel;
// import net.minecraft.client.render.model.json.ModelTransformation;
// import net.minecraft.client.texture.SpriteAtlasTexture;
// import net.minecraft.client.util.math.MatrixStack;
// import net.minecraft.util.math.Direction;

// public class WModel extends WWidget {

//     private final BakedModel model;

//     public WModel(BakedModel model) {
//         this.model = model;
//     }
    
//     public static void renderGuiItemModel(int x, int y, BakedModel model) {
//         MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).setFilter(false, false);
//         RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
//         RenderSystem.enableBlend();
//         RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
//         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//         MatrixStack matrixStack = RenderSystem.getModelViewStack();
//         matrixStack.push();
//         matrixStack.translate((double)x, (double)y, (double)(100.0F));
//         matrixStack.translate(8.0, 8.0, 0.0);
//         matrixStack.scale(1.0F, -1.0F, 1.0F);
//         matrixStack.scale(16.0F, 16.0F, 16.0F);
//         RenderSystem.applyModelViewMatrix();

//         VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
//         boolean bl = !model.isSideLit();
//         if (bl) {
//             DiffuseLighting.disableGuiDepthLighting();
//         }

//         RenderLayer layer = TexturedRenderLayers.getEntityTranslucentCull();
//         VertexConsumer vc = immediate.getBuffer(layer);
//         Random random = new Random();
//         matrixStack.push();

//         model.getTransformation().getTransformation(ModelTransformation.Mode.GUI).apply(false, matrixStack);
//         matrixStack.translate(-0.5, -0.5, -0.5);
//         for (Direction direction : Direction.values()) {
//             model.getQuads(null, direction, random).forEach(quad -> vc.quad(matrixStack.peek(), quad, 1.0F, 1.0F, 1.0F, 0xF000F0, OverlayTexture.DEFAULT_UV));
//         }
//         model.getQuads(null, null, random).forEach(quad -> vc.quad(matrixStack.peek(), quad, 1.0F, 1.0F, 1.0F, 0xF000F0, OverlayTexture.DEFAULT_UV));

//         matrixStack.pop();
//         immediate.draw();
//         RenderSystem.enableDepthTest();
//         if (bl) {
//             DiffuseLighting.enableGuiDepthLighting();
//         }

//         matrixStack.pop();
//         RenderSystem.applyModelViewMatrix();
//     }

//     @Override
//     public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
// 		RenderSystem.enableDepthTest();

//         renderGuiItemModel(x + getWidth() / 2 - 9, y + getHeight() / 2 - 9, this.model);
//     }
// }
