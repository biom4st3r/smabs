package adventurepack.mods.smab;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;

import adventurepack.mods.smab.smab.Smab;
import adventurepack.mods.smab.smab.SmabItem;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
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

    @SuppressWarnings({"resource"})
    public static void renderSmabToolTip(MatrixStack matrices, SmabItem item, Smab smab, int x, int y) {
        
        List<OrderedText> lines = Lists.newArrayList();
        lines.add(smab != null ? smab.getNicknameText().asOrderedText() : item.species.name().asOrderedText());

        int box_width = 0;
        for (OrderedText text : lines) {
            if (box_width <  MinecraftClient.getInstance().textRenderer.getWidth(text)) {
                box_width =  MinecraftClient.getInstance().textRenderer.getWidth(text);
            }
        }
        int box_height = 9 * lines.size();


        int width = MinecraftClient.getInstance().currentScreen.width;
        int height = MinecraftClient.getInstance().currentScreen.height;

        int a_x = x + 12;
        int a_y = y - 12;
        if (a_x + box_width > width) {
            a_x -= 28 + box_width;
        }

        if (a_y + box_height + 6 > height) {
            a_y = height - box_height - 6;
        }

        matrices.push();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        int color0 = 0xF0_100010;
        int color1 = 0x50_5000FF;
        int color2 = 0x50_28007F;

        fillGradient(matrix4f, bufferBuilder, a_x - 3, a_y - 4, a_x + box_width + 3, a_y - 3, 400, color0, color0);
        fillGradient(matrix4f, bufferBuilder, a_x - 3, a_y + box_height + 3, a_x + box_width + 3, a_y + box_height + 4, 400, color0, color0);
        fillGradient(matrix4f, bufferBuilder, a_x - 3, a_y - 3, a_x + box_width + 3, a_y + box_height + 3, 400, color0, color0);
        fillGradient(matrix4f, bufferBuilder, a_x - 4, a_y - 3, a_x - 3, a_y + box_height + 3, 400, color0, color0);
        fillGradient(matrix4f, bufferBuilder, a_x + box_width + 3, a_y - 3, a_x + box_width + 4, a_y + box_height + 3, 400, color0, color0);
        fillGradient(matrix4f, bufferBuilder, a_x - 3, a_y - 3 + 1, a_x - 3 + 1, a_y + box_height + 3 - 1, 400, color1, color2);
        fillGradient(matrix4f, bufferBuilder, a_x + box_width + 2, a_y - 3 + 1, a_x + box_width + 3, a_y + box_height + 3 - 1, 400, color1, color2);
        fillGradient(matrix4f, bufferBuilder, a_x - 3, a_y - 3, a_x + box_width + 3, a_y - 3 + 1, 400, color1, color1);
        fillGradient(matrix4f, bufferBuilder, a_x - 3, a_y + box_height + 2, a_x + box_width + 3, a_y + box_height + 3, 400, color2, color2);
        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        // VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        matrices.translate(0.0, 0.0, 400.0);

        int i = 0;
        for (OrderedText orderedText : lines) {
            MinecraftClient.getInstance().textRenderer.draw(matrices, orderedText, (float)a_x, (float)a_y + (i * 9), -1);
        }

        // Screen.drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, smab.getNicknameText(), x, y, 0xFFFF_FFFF);
        matrices.pop();
    }
    
}