package adventurepack.mods.smab.resourceloaders;

import java.util.List;

import com.google.common.collect.Lists;

import adventurepack.mods.smab.ModInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class CardTextures {
    public static final Identifier _CARD_ATLAS_ID = new Identifier(ModInit.MODID, "card_atlas");
    public static final Identifier CARD_ATLAS_ID = new Identifier(ModInit.MODID, "textures/atlas/cards.png");
    public static List<Identifier> SPRITES = Lists.newArrayList();
    public static final RenderLayer CARD_LAYER = RenderLayer.getEntityCutout(CARD_ATLAS_ID);

    public static SpriteAtlasTexture.Data DATA = null;
    public static final SpriteAtlasTexture CARD_ATLAS = new SpriteAtlasTexture(CARD_ATLAS_ID);
    
    public static void init(ResourceManager manager) {
        MinecraftClient.getInstance().getTextureManager().registerTexture(CardTextures.CARD_ATLAS.getId(), CardTextures.CARD_ATLAS);
        DATA = CARD_ATLAS.stitch(manager, SPRITES.stream(), MinecraftClient.getInstance().getProfiler(), 4);
        CARD_ATLAS.upload(DATA);
    }   
}
