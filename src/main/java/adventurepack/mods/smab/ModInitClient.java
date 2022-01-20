package adventurepack.mods.smab;

import com.mojang.blaze3d.systems.RenderSystem;

import adventurepack.mods.smab.smab.Smab;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

/**
 * ModInitClient
 */
public class ModInitClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

    }

    protected static void fillGradient(Matrix4f matrix, BufferBuilder builder, int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd) {
        float f = (float)(colorStart >> 24 & 0xFF) / 255.0F;
        float g = (float)(colorStart >> 16 & 0xFF) / 255.0F;
        float h = (float)(colorStart >> 8 & 0xFF) / 255.0F;
        float i = (float)(colorStart & 0xFF) / 255.0F;
        float j = (float)(colorEnd >> 24 & 0xFF) / 255.0F;
        float k = (float)(colorEnd >> 16 & 0xFF) / 255.0F;
        float l = (float)(colorEnd >> 8 & 0xFF) / 255.0F;
        float m = (float)(colorEnd & 0xFF) / 255.0F;
        builder.vertex(matrix, (float)endX, (float)startY, (float)z).color(g, h, i, f).next();
        builder.vertex(matrix, (float)startX, (float)startY, (float)z).color(g, h, i, f).next();
        builder.vertex(matrix, (float)startX, (float)endY, (float)z).color(k, l, m, j).next();
        builder.vertex(matrix, (float)endX, (float)endY, (float)z).color(k, l, m, j).next();
    }

    public static void renderSmabToolTip(MatrixStack matrices, Smab smab, int x, int y) {
        
        int box_width = 50;
        int box_height = 50;

        // box_width = MinecraftClient.getInstance().textRenderer.getWidth(smab.getNicknameText().toString()) + 12;

        int width = MinecraftClient.getInstance().currentScreen.width;
        int height = MinecraftClient.getInstance().currentScreen.height;

        int l = x + 12;
        int tooltipComponent = y - 12;
        if (l + box_width > width) {
            l -= 28 + box_width;
        }

        if (tooltipComponent + box_height + 6 > height) {
            tooltipComponent = height - box_height - 6;
        }

        matrices.push();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        fillGradient(matrix4f, bufferBuilder, l - 3, tooltipComponent - 4, l + box_width + 3, tooltipComponent - 3, 400, -267386864, -267386864);
        fillGradient(matrix4f, bufferBuilder, l - 3, tooltipComponent + box_height + 3, l + box_width + 3, tooltipComponent + box_height + 4, 400, -267386864, -267386864);
        fillGradient(matrix4f, bufferBuilder, l - 3, tooltipComponent - 3, l + box_width + 3, tooltipComponent + box_height + 3, 400, -267386864, -267386864);
        fillGradient(matrix4f, bufferBuilder, l - 4, tooltipComponent - 3, l - 3, tooltipComponent + box_height + 3, 400, -267386864, -267386864);
        fillGradient(matrix4f, bufferBuilder, l + box_width + 3, tooltipComponent - 3, l + box_width + 4, tooltipComponent + box_height + 3, 400, -267386864, -267386864);
        fillGradient(matrix4f, bufferBuilder, l - 3, tooltipComponent - 3 + 1, l - 3 + 1, tooltipComponent + box_height + 3 - 1, 400, 1347420415, 1344798847);
        fillGradient(matrix4f, bufferBuilder, l + box_width + 2, tooltipComponent - 3 + 1, l + box_width + 3, tooltipComponent + box_height + 3 - 1, 400, 1347420415, 1344798847);
        fillGradient(matrix4f, bufferBuilder, l - 3, tooltipComponent - 3, l + box_width + 3, tooltipComponent - 3 + 1, 400, 1347420415, 1347420415);
        fillGradient(matrix4f, bufferBuilder, l - 3, tooltipComponent + box_height + 2, l + box_width + 3, tooltipComponent + box_height + 3, 400, 1344798847, 1344798847);
        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        // Screen.drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, smab.getNicknameText(), (width/2), 5, 0xFFFF_FFFF);
        matrices.pop();
    }
    
}