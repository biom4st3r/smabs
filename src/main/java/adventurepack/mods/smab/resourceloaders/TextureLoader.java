package adventurepack.mods.smab.resourceloaders;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import adventurepack.mods.smab.ModInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class TextureLoader {
    private static final Identifier CARD_ATLAS_ID = new Identifier(ModInit.MODID, "textures/atlas/cards.png");
    private static List<Identifier> SPRITES = Lists.newArrayList();
    private static final RenderLayer CARD_LAYER = RenderLayer.getEntityCutout(CARD_ATLAS_ID);

    private static SpriteAtlasTexture.Data DATA = null;
    private static final SpriteAtlasTexture CARD_ATLAS = new SpriteAtlasTexture(CARD_ATLAS_ID);
    
    private static void init(ResourceManager manager, Collection<Identifier> ids) {
        // get a collections of texture ids
        SPRITES.addAll(ids); 
        // Register the atlas
        MinecraftClient.getInstance().getTextureManager().registerTexture(TextureLoader.CARD_ATLAS.getId(), TextureLoader.CARD_ATLAS);
        // Compile the texture data into atlas
        DATA = CARD_ATLAS.stitch(manager, SPRITES.stream(), MinecraftClient.getInstance().getProfiler(), 4);
        // upload texture data to GPU
        CARD_ATLAS.upload(DATA);
        // ...?? i shouldn't need this??
        // ClientSpriteRegistryCallback.event(CardTextures.CARD_ATLAS_ID).register((atlas,reg) -> {
        //     ids.forEach(reg::register);
        // });
        // We don't need it, because we have a list of SPRITES
    }
}
