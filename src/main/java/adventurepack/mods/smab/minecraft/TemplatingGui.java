package adventurepack.mods.smab.minecraft;

import java.util.Map;

import adventurepack.mods.smab.ModInit;
import biom4st3r.libs.biow0rks.reflection.FieldHandler;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.WText;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class TemplatingGui extends LightweightGuiDescription {

    public Identifier card = new Identifier(ModInit.MODID, "textures/cards/opulent_oceans_card.png");
    public Rarity rarity = Rarity.COMMON;
    public WText RARITY_TEXT = new WText(new LiteralText(rarity.toString()).formatted(rarity.formatting, Formatting.BOLD));
    public WSprite sprite = new WSprite(card) {
        @Override
        public void setSize(int x, int y) {
            int size = x/24;
            super.setSize(size * 24, size * 30);
        }
    };

    private static final FieldHandler<Map<Identifier,Sprite>> SpriteAtlasTexture$SPRITES = FieldHandler.get(SpriteAtlasTexture.class, f->f.getType()==Map.class);
    private Map<Identifier,Sprite> SPRITES = SpriteAtlasTexture$SPRITES.get(MinecraftClient.getInstance().getBakedModelManager().getAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE));

    public record SizePos(int x, int y, int width, int height){}

    private SizePos PANEL_ROOT = new SizePos(-1,-1,420,230);
    private SizePos PANEL_ICON = new SizePos(4,1,10,18);
    private SizePos BUTT_PREV = new SizePos(1,10,3,1);
    private SizePos BUTT_NEXT = new SizePos(14,10,3,1);
    private SizePos LABEL_PAGE = new SizePos(3,20,10,1);
    private SizePos FIELD_PAGE = new SizePos(9,20,4,1);
    private SizePos BUTT_RARITY = new SizePos(17,18,5,2);
    private SizePos SPRITE_CARD = new SizePos(19,1,9,0);

    private SizePos BUTT_COLOR = new SizePos(31, 1, 10, 2);

    private SizePos BUTT_SAVE = new SizePos(21,11,4,2);


    public TemplatingGui() {
        
        System.out.println(SPRITES.size());
        WGridPanel root = new WGridPanel(10);
        setRootPanel(root);
        root.setSize(PANEL_ROOT.width, PANEL_ROOT.height);

        WPlainPanel icons = new WPlainPanel();

        icons.setBackgroundPainter(BackgroundPainter.createColorful(0xFF_22aa00));
        root.add(icons, PANEL_ICON.x, PANEL_ICON.y, PANEL_ICON.width, PANEL_ICON.height);
        WButton bbutton = new WButton(new LiteralText("Prev"));
        WButton fbutton = new WButton(new LiteralText("Next"));
        root.add(bbutton, BUTT_PREV.x, BUTT_PREV.y, BUTT_PREV.width, BUTT_PREV.height);
        root.add(fbutton, BUTT_NEXT.x, BUTT_NEXT.y, BUTT_NEXT.width, BUTT_NEXT.height);
        WText text = new WText(new LiteralText("Page 1/X"));
        WTextField textField = new WTextField(new LiteralText("page#"));
        root.add(text, LABEL_PAGE.x, LABEL_PAGE.y, LABEL_PAGE.width, LABEL_PAGE.height);
        root.add(textField, FIELD_PAGE.x, FIELD_PAGE.y, FIELD_PAGE.width, FIELD_PAGE.height);

        int ix = 00;
        for (Rarity rarity : Rarity.values()) {
            
            WButton button = new WButton(new LiteralText(rarity.toString()));
            button.setOnClick(() -> {
                this.rarity = rarity;
                this.RARITY_TEXT.setText(new LiteralText(this.rarity.toString()).formatted(rarity.formatting, Formatting.BOLD));
            });
            root.add(button, BUTT_RARITY.x + (ix%2)*5, BUTT_RARITY.y + (ix/2)*2, BUTT_RARITY.width, BUTT_RARITY.height);
            ix++;
        }
        root.add(RARITY_TEXT, BUTT_RARITY.x, BUTT_RARITY.y-1, BUTT_RARITY.width+3, BUTT_RARITY.height);

        root.add(sprite, SPRITE_CARD.x, SPRITE_CARD.y, SPRITE_CARD.width, SPRITE_CARD.height);

        int i = 0;
        for (String color : new String[] {"opulent_oceans", "agonizing_abyss", "grindy_gear", "fascinating_flora", "fantastic_fauna", "mythic_misc", "tinkers_toolbox", "integral_interior", "famous_foodstuff", "typical_treassures"}) {
            WButton button = new WButton(new LiteralText(color));
            button.setOnClick(() -> {
                this.card = new Identifier(ModInit.MODID, "textures/cards/" + color + "_card.png");
                this.sprite.setImage(this.card);
            });
            root.add(button, BUTT_COLOR.x, BUTT_COLOR.y+(i*2), BUTT_COLOR.width, BUTT_COLOR.height);
            i++;
        }
        WButton SAVE = new WButton(new LiteralText("Save"));
        root.add(SAVE, BUTT_SAVE.x, BUTT_SAVE.y, BUTT_SAVE.width, BUTT_SAVE.height);

        root.validate(this);
    }

}
