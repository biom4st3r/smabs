package adventurepack.mods.smab.minecraft.client.itemodel;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import javax.swing.Renderer;

import com.mojang.datafixers.util.Pair;

import adventurepack.mods.smab.ModInitClient;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

@SuppressWarnings({"unused"})
public class CardModel extends AbstractModel implements UnbakedModel {

    /**
     * When enabled: this recreates the mesh every frame. 
     * DBUGGING ONLY
     */
    private static boolean invalidateMesh = true;

    /**
     * Back face of card
     */
    private static final int CARD_BACK_QUAD = 0;
    /**
     * Front face of card
     */
    private static final int CARD_FACE_QUAD = 1;
    /**
     * item icon
     */
    private static final int ICON_QUAD = 2;
    /**
     * Foil overlay on front face
     */
    private static final int FOIL_QUAD = 3;
    
    /**
     * From FRAPI: MaterialFinder#find "Resulting instances can and should be re-used to prevent needless memory allocation"
     */
    private static final RenderMaterial MATERIAL = RendererAccess.INSTANCE.getRenderer().materialFinder().find();

    private static interface ItemQuadEmitter {
        void emitItemQuads(CardModel model, ItemStack stack, java.util.function.Supplier<Random> randomSupplier,
            RenderContext context);
    }

    /**
     * Initializes the mesh for any CardModel instance.
     */
    private static final ItemQuadEmitter MESH_INIT_EMITTER = (model,stack,random,ctx) -> {
        Identifier card_id = new Identifier(model.def.card_icon().getNamespace(), model.def.card_icon().getPath().replace("textures/", "").replace(".png", ""));
        model.CARD_FACE_SPRITE = ModInitClient.getCardSprite(card_id);
        model.ICON_SPRITE = ModInitClient.getCardSprite(model.def.sprite_icon());
        MeshBuilder builder = RendererAccess.INSTANCE.getRenderer().meshBuilder();
        QuadEmitter emitter = builder.getEmitter();
        for (Direction dir : Direction.values()) {
            CardModel.BAKED_CARD_MODEL.getQuads(null, dir, random.get()).forEach(quad -> emitter.fromVanilla(quad, MATERIAL, quad.getFace()));
        }
        
        int[] i = {0};
        CardModel.BAKED_CARD_MODEL.getQuads(null, null, random.get()).forEach(quad -> {
            // tag 1 corresponds to the front face of the card
            if (i[0] == CARD_FACE_QUAD) {
                // swap the default card out
                emitter.fromVanilla(quad, MATERIAL, quad.getFace());
                createCardBackground(model.CARD_FACE_SPRITE, emitter);
                emitter.tag(i[0]++);
                emitter.emit();
                // create item icon on card
                emitter.fromVanilla(quad, MATERIAL, quad.getFace());
                createIcon(model.ICON_SPRITE, 5, 4, 16, 14, 0.0001F, emitter);
                emitter.tag(i[0]++);
                emitter.emit();
                // // Add overlay between icon and face
                // emitter.fromVanilla(quad, MATERIAL, quad.getFace());
                // createCardBackground(
                //     ModInitClient.getCardTexture(new Identifier(ModInit.MODID, "foils/land_and_sea_foil")), 
                //     emitter);
                // fixCardBackground(emitter,0.00005F);
                // setColor(0x88FFFFFF, emitter);
                // emitter.tag(i[0]++);
                // emitter.emit();

                return;
            } else {
                emitter.fromVanilla(quad, MATERIAL, quad.getFace());
                emitter.tag(i[0]++);
                emitter.emit();
                return;
            }
        });
        model.mesh = builder.build();
        if (invalidateMesh) {
            CardModel.RENDER.emitItemQuads(model, stack, random, ctx);
        } else {
            model.emitter = CardModel.RENDER;
        }
    };

    private static final ItemQuadEmitter RENDER = (model,stack,random,ctx) -> {
        ctx.meshConsumer().accept(model.mesh);
    };

    private static void createIcon(Sprite sprite, int x, int y, int width, int height, float depth_mod, QuadEmitter emitter) {
        Vec3f pos0 = emitter.copyPos(0, null);
        Vec3f pos2 = emitter.copyPos(2, null);
        float xunit = (pos2.getX() - pos0.getX()) / 24F;
        float ypos = pos0.getY() + depth_mod;
        float zunit = pos2.getZ()/30F;
   
        emitter.pos(0, xunit*x, ypos, zunit*y);
        emitter.pos(3, xunit*(x+width), ypos, zunit*y);
        emitter.pos(1, xunit*x, ypos, zunit*(y+height));
        emitter.pos(2, xunit*(x+width), ypos, zunit*(y+height));
   
   
        emitter.sprite(0, 0, sprite.getMinU(), sprite.getMinV());
        emitter.sprite(1, 0, sprite.getMinU(), sprite.getMaxV());
        emitter.sprite(2, 0, sprite.getMaxU(), sprite.getMaxV());
        emitter.sprite(3, 0, sprite.getMaxU(), sprite.getMinV());
    }

    @SuppressWarnings({"unused"})
    private static void setColor(int color, QuadEmitter emitter) {
        for (int i = 0; i < 4; i++) {
            emitter.spriteColor(i, 0, color);
        }
    }

    private static void createCardBackground(Sprite sprite, QuadEmitter emitter) {
        emitter.sprite(0, 0, sprite.getMinU(), sprite.getMinV());
        emitter.sprite(1, 0, sprite.getMinU(), sprite.getMaxV());
        emitter.sprite(2, 0, sprite.getMaxU(), sprite.getMaxV());
        emitter.sprite(3, 0, sprite.getMaxU(), sprite.getMinV());
   
        fixCardBackground(emitter,0);
    }

    private static void fixCardBackground(QuadEmitter emitter, float depth_mod)  {
        emitter.pos(0, 0, 0.041667F + depth_mod, 0);
        emitter.pos(1, 0, 0.041667F + depth_mod, 1.25F);
        emitter.pos(2, 1, 0.041667F + depth_mod, 1.25F);
        emitter.pos(3, 1, 0.041667F + depth_mod, 0);
        // If i don't do surgery here the the width is 1 unit to narrow????
    }
    // END OF STATIC


    /**
     * Provides the icon and front face sprites
     */
    private final JsonItemDefinition def;
    /**
     * cached mesh of modified card
     */
    private Mesh mesh = null;
    private Sprite CARD_FACE_SPRITE;
    private Sprite ICON_SPRITE;

    public CardModel(JsonItemDefinition def) {
        this.def = def;
    }

    /**
     * Over engineering in progress<p>
     * this is the actual method/lambda that handles emitting quads.
     * MESH_INIT_EMITTER automatically swap `this.emitter`  RENDER
     * emitter when the mesh is generated.<p>
     * 
     * This avoids branching once the mesh is created
     */
    private ItemQuadEmitter emitter = MESH_INIT_EMITTER;

    @Override
    public void emitItemQuads(ItemStack stack, java.util.function.Supplier<Random> randomSupplier,
            RenderContext context) {
        // System.out.println("hello0");
        this.emitter.emitItemQuads(this, stack, randomSupplier, context);
    }

    @Override
    public Sprite getParticleSprite() {
        return BAKED_CARD_MODEL.getParticleSprite();
    }

    @Override
    public ModelTransformation getTransformation() {
        return BAKED_CARD_MODEL.getTransformation();
    }

    @Override
    public ModelOverrideList getOverrides() {
        return BAKED_CARD_MODEL.getOverrides();
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter,
            Set<Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    /**
     * Initlized during the first bake of ANY CardModel. 
     * The same model is used for all cards, then processed during
     * the individual instances first render
     */
    private static BakedModel BAKED_CARD_MODEL = null;

    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter,
            ModelBakeSettings rotationContainer, Identifier modelId) {
        if (BAKED_CARD_MODEL == null) {
            BAKED_CARD_MODEL = ModInitClient.CARD_MODEL.bake(loader, textureGetter, rotationContainer, modelId);
        }
        return this;
    }
    
}
