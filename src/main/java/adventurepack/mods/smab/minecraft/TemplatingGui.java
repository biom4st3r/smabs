package adventurepack.mods.smab.minecraft;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import adventurepack.mods.smab.ModInit;
import adventurepack.mods.smab.minecraft.client.itemodel.JsonItemDefinition;
import biom4st3r.libs.biow0rks.autojson.AutoJson;
import biom4st3r.libs.biow0rks.reflection.FieldHandler;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.WText;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class TemplatingGui extends LightweightGuiDescription {
    // TO BE EXPORTED //
    public Identifier card = new Identifier(ModInit.MODID, "textures/cards/opulent_oceans_card.png");
    public Rarity rarity = Rarity.COMMON;
    public Identifier ITEM_ICON;
    // Reactive Elements //
    private WText RARITY_TEXT = new WText(new LiteralText(rarity.toString()).formatted(rarity.formatting, Formatting.BOLD));
    private WGridPanel icons = new WGridPanel(5);
    private WText page_text_indicator = new WText(new LiteralText("Page 1/X"));
    private WSprite ITEM_SPRITE = new WSprite(new Identifier(""));
    private WSprite CARD_SPRITE = new WSprite(card) {
        @Override
        public void setSize(int x, int y) {
            int size = x/24;
            super.setSize(size * 24, size * 30);
        }
    };
    public int page = 1;


    private static final FieldHandler<Map<Identifier,Sprite>> SpriteAtlasTexture$SPRITES = FieldHandler.get(SpriteAtlasTexture.class, f->f.getType()==Map.class);
    private Map<Identifier,Sprite> SPRITES = SpriteAtlasTexture$SPRITES.get(MinecraftClient.getInstance().getBakedModelManager().getAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE));

    public record SizePos(int x, int y, int width, int height){}

    private SizePos PANEL_ROOT = new SizePos    (-1 ,-1, 420, 230);
    private SizePos PANEL_ICON = new SizePos    (4, 1, 10, 18);
    private SizePos BUTT_PREV = new SizePos     (1, 10, 3, 1);
    private SizePos BUTT_NEXT = new SizePos     (14, 10, 3, 1);
    private SizePos LABEL_PAGE = new SizePos    (1, 20, 10, 1);
    private SizePos FIELD_PAGE = new SizePos    (11, 20, 4, 1);
    private SizePos BUTT_RARITY = new SizePos   (17, 18, 5, 2);
    private SizePos SPRITE_CARD = new SizePos   (19, 1, 9, 0);

    private SizePos BUTT_COLOR = new SizePos    (31, 1, 10, 2);

    private SizePos BUTT_SAVE = new SizePos     (21, 11, 4, 2);


    private void reset() {
        // this.rarity = Rarity.COMMON;
        // this.RARITY_TEXT.setText(new LiteralText(rarity.toString()).formatted(rarity.formatting, Formatting.BOLD));
        // this.card = new Identifier(ModInit.MODID, "textures/cards/opulent_oceans_card.png");
        // this.CARD_SPRITE.setImage(this.card);
        // this.ITEM_ICON = null;
        // this.ITEM_SPRITE.setImage(new Identifier(""));
    }

    private static int sort(Identifier a, Identifier b) {
        // if(a.getNamespace().equals("minecraft") && b.getNamespace().equals("minecraft")) {
        //     return 0;
        // } else 
        if(!a.getNamespace().equals("minecraft")) {
            return 1;
        } else if(!b.getNamespace().equals("minecraft")) {
            return -1;
        }
        return 0;
    }
 

    public void updatePage(int i) {
        if (i < 1) return;
        this.page = i;

        int count = i-1;
        Iterable<Identifier> ids = ()->SPRITES.keySet().stream().sorted(TemplatingGui::sort).skip(count * 28).limit(28).iterator();
        for (WWidget w : this.icons.streamChildren().toArray(WWidget[]::new)) {
            icons.remove(w);
        }

        this.page_text_indicator.setText(new LiteralText(String.format("Page %s/%s", this.page,SPRITES.size()/28F)));

        int iconx = 0;
        for(Identifier id : ids) {
            Sprite sprite = SPRITES.get(id);
            Texture texture = new Texture(sprite.getAtlas().getId(), sprite.getMinU(), sprite.getMinV(), sprite.getMaxU(), sprite.getMaxV());
            this.icons.add(new WClickableSprite(texture, ()-> {
                // System.out.println("Hello");
                this.ITEM_ICON = id;
                this.ITEM_SPRITE.setImage(texture);
            }), 1+ ((0 + iconx%4) * 5), 1+((0 + iconx/4)*5), 3, 3);
            iconx++;
        }
    }

    private static class WClickableSprite extends WSprite {

        private Runnable onClick;

        public WClickableSprite(Texture texture, Runnable onClick) {
            super(texture);
            this.onClick = onClick;
        }

        public WClickableSprite(Identifier image, Runnable onClick) {
            super(image);
            this.onClick = onClick;
        }

        @Override
        public InputResult onClick(int x, int y, int button) {
            this.onClick.run();
            return InputResult.PROCESSED;
        }

    }

    public TemplatingGui() {
        
        System.out.println(SPRITES.size());
        WGridPanel root = new WGridPanel(10);
        setRootPanel(root);
        root.setSize(PANEL_ROOT.width, PANEL_ROOT.height);

        // SPRITES.keySet().stream().limit(10).forEach(sprite -> {
        //     icons.add(new WSprite(sprite), 0, 0);
        // });
        updatePage(1);

        icons.setBackgroundPainter(BackgroundPainter.createColorful(0xFF_22aa00));
        root.add(icons, PANEL_ICON.x, PANEL_ICON.y, PANEL_ICON.width, PANEL_ICON.height);
        WButton prev_button = new WButton(new LiteralText("Prev"));
        prev_button.setOnClick(()-> {
            updatePage(this.page - 1);
        });
        WButton next_button = new WButton(new LiteralText("Next"));
        next_button.setOnClick(()-> {
            updatePage(this.page + 1);
        });
        root.add(prev_button, BUTT_PREV.x, BUTT_PREV.y, BUTT_PREV.width, BUTT_PREV.height);
        root.add(next_button, BUTT_NEXT.x, BUTT_NEXT.y, BUTT_NEXT.width, BUTT_NEXT.height);
        WTextField textField = new WTextField(new LiteralText("page#"));
        textField.setChangedListener(string-> {
            try {
                this.updatePage(Integer.parseInt(string));
            } catch (Throwable t) {}
        });
        root.add(page_text_indicator, LABEL_PAGE.x, LABEL_PAGE.y, LABEL_PAGE.width, LABEL_PAGE.height);
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
        root.add(RARITY_TEXT, BUTT_RARITY.x, BUTT_RARITY.y-1, BUTT_RARITY.width+3, 1);

        root.add(CARD_SPRITE, SPRITE_CARD.x, SPRITE_CARD.y, SPRITE_CARD.width, SPRITE_CARD.height);
        root.add(ITEM_SPRITE, SPRITE_CARD.x+2, SPRITE_CARD.y+1, 3, 3);

        int i = 0;
        for (String color : new String[] {"opulent_oceans", "agonizing_abyss", "grindy_gear", "fascinating_flora", "fantastic_fauna", "mythic_misc", "tinkers_toolbox", "integral_interior", "famous_foodstuff", "typical_treassures"}) {
            WButton button = new WButton(new LiteralText(color));
            button.setOnClick(() -> {
                this.card = new Identifier(ModInit.MODID, "textures/cards/" + color + "_card.png");
                System.out.println(this.card);
                this.CARD_SPRITE.setImage(this.card);
            });
            root.add(button, BUTT_COLOR.x, BUTT_COLOR.y+(i*2), BUTT_COLOR.width, BUTT_COLOR.height);
            i++;
        }
        WButton SAVE = new WButton(new LiteralText("Save"));
        SAVE.setOnClick(()-> {
            WGridPanel savePanel = new WGridPanel(10);
            savePanel.setSize(200, 100);
            // root.add(savePanel, 10, 10, 10, 10);
            this.setRootPanel(savePanel);
            WTextField field = new WTextField();
            field.setSuggestion("snake_case_name");
            savePanel.add(field, 1, 1, 10, 2);
            WButton export = new WButton(new LiteralText("Export"));
            export.setOnClick(()-> {
                if (field.getText().isBlank()) return;
                root.remove(savePanel);
                this.setRootPanel(root);
                root.validate(this);
                Identifier item_name = new Identifier(ModInit.MODID, field.getText().toLowerCase().replace(" ", "_"));
                JsonItemDefinition item = new JsonItemDefinition(item_name, this.rarity, this.card, this.ITEM_ICON);
                new File("config/smab_card_items/").mkdirs();
                File file = new File("config/smab_card_items/" + item_name.toString().replace(":", "__") + ".json");

                try (FileOutputStream stream = new FileOutputStream(file)) {
                    stream.write(AutoJson.serialize(item).toString().getBytes());
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reset();
            });
            savePanel.add(export, 1, 3, 5, 2);
            savePanel.validate(this);
        });
        root.add(SAVE, BUTT_SAVE.x, BUTT_SAVE.y, BUTT_SAVE.width, BUTT_SAVE.height);

        root.validate(this);
    }

}
