package adventurepack.mods.smab.minecraft;

import com.mojang.blaze3d.systems.RenderSystem;

import adventurepack.mods.smab.ModInit;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WText;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

public class TemplatingGui extends LightweightGuiDescription {

    public static void drawTexturedQuad(Matrix4f matrix, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1, int color) {
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(matrix, (float)x0, (float)y1, (float)z).texture(u0, v1).color(color).next();
        bufferBuilder.vertex(matrix, (float)x1, (float)y1, (float)z).texture(u1, v1).color(color).next();
        bufferBuilder.vertex(matrix, (float)x1, (float)y0, (float)z).texture(u1, v0).color(color).next();
        bufferBuilder.vertex(matrix, (float)x0, (float)y0, (float)z).texture(u0, v0).color(color).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
    }

    private static void drawTexture(MatrixStack matrices, int x0, int x1, int y0, int y1, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight, int color) {
        drawTexturedQuad(matrices.peek().getPositionMatrix(), x0, x1, y0, y1, z, (u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight, color);
    }

    public static void drawTexture(MatrixStack matrices, int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int color) {
        drawTexture(matrices, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight, color);
    }

    public TemplatingGui() {
        WGridPanel root = new WGridPanel(10);
        setRootPanel(root);
        root.setSize(500, 250);

        WPlainPanel icons = new WPlainPanel();
        // icons.setSize(5, 5);
        icons.setBackgroundPainter(BackgroundPainter.createColorful(0xFF_22aa00));
        root.add(icons, 4, 1, 10, 18);
        WButton bbutton = new WButton(new LiteralText("Prev"));
        WButton fbutton = new WButton(new LiteralText("Next"));
        root.add(bbutton, 1, 10,3,1);
        root.add(fbutton, 14, 10,3,1);
        WText text = new WText(new LiteralText("Page 1/X"));
        WTextField textField = new WTextField(new LiteralText("page#"));
        root.add(text, 3, 20, 10,1);
        root.add(textField, 9, 20, 4,1);

        int i = 5;
        for (String rarity : new String[]{"COMMON","UNCOMMON","RARE","EPIC"}) {
            WButton button = new WButton(new LiteralText(rarity));
            root.add(button, (i+=6), 23, 5, 2);
        }

        i = 0;

        WPlainPanel card = new WPlainPanel();
        card.setBackgroundPainter((matrix,left,top,panel) -> {
            RenderSystem.setShaderTexture(0, new Identifier(ModInit.MODID, "textures/colorless_card.png"));
            // System.out.println(panel.getWidth());
            // DrawableHelper.drawTexture(matrix, left, top, 0, 0, 48, 60, 24, 30);
            // System.out.println(panel.getWidth());
            // System.out.println((panel.getWidth()%24) * 24);
            // System.out.println(panel.getWidth());
            // System.out.println(panel.getWidth());
            int size = panel.getWidth()/24;
            drawTexture(matrix, left, top, size * 24, size * 30, 0, 0, 24, 30, 24, 30, 0xFF0000FF);
        });
        
        root.add(card, 18, 1, 18, 20);

        WModel model = new WModel(MinecraftClient.getInstance().getBakedModelManager().getModel(new ModelIdentifier(new Identifier(ModInit.MODID, "card_model_finished"), "")));
        // WItem item = new WItem(new ItemStack(Items.APPLE));
        // WModel model = new WModel(MinecraftClient.getInstance().getBakedModelManager().getModel(new ModelIdentifier(new Identifier(ModInit.MODID, "card_model_finished"), "")));
        root.add(model, 25, 13, 4, 4);

        for (String color : new String[] {"opulent_oceans", "agonizing_abyss", "grindy_gear", "fascinating_flora", "fantastic_fauna", "mythic_misc", "tinkers_toolbox", "integral_interior", "famous_foodstuff", "typical_treassures", "boundless_blocks"}) {
            WButton button = new WButton(new LiteralText(color));
            // System.out.println(30+((i%2)*9));
            root.add(button, 38, 1+(i*2), 10, 2);
            i++;
        }


        root.validate(this);
    }

}
