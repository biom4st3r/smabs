package adventurepack.mods.smab.minecraft.client.itemodel;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

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

public class CardModel extends AbstractModel implements UnbakedModel {

    private JsonItemDefinition def;
    private static BakedModel model = null;

    public CardModel(JsonItemDefinition def) {
        this.def = def;
    }

    Mesh mesh = null;
    Sprite CARD_BACKGROUND;
    Sprite ICON;

    @Override
    public void emitItemQuads(ItemStack stack, java.util.function.Supplier<Random> randomSupplier,
            RenderContext context) {
        attemptInitMesh(randomSupplier);
        // 0 back
        // 1 front

        /*
        0 top left 0,0
        1 bottom right 1,1
        2 top right 1,0
        3 bottom left 0,1

        */

        context.meshConsumer().accept(mesh);
        mesh = null;
    }


    public static final RenderMaterial MATERIAL = RendererAccess.INSTANCE.getRenderer().materialFinder().find();

    private void attemptInitMesh(java.util.function.Supplier<Random> randomSupplier) {
        if (mesh == null) {
            Identifier card_id = new Identifier(def.card_icon().getNamespace(), def.card_icon().getPath().replace("textures/", "").replace(".png", ""));
            // Identifier card_id = new Identifier("ap_smabs:cards/integral_interior_card");
            CARD_BACKGROUND = ModInitClient.getCardSprite(card_id);
            ICON = ModInitClient.getCardSprite(def.sprite_icon());
            MeshBuilder builder = RendererAccess.INSTANCE.getRenderer().meshBuilder();
            QuadEmitter emitter = builder.getEmitter();
            for (Direction dir : Direction.values()) {
                model.getQuads(null, dir, randomSupplier.get()).forEach(quad -> emitter.fromVanilla(quad, MATERIAL, quad.getFace()));
            }
            int[] i = {0};
            model.getQuads(null, null, randomSupplier.get()).forEach(quad -> {
                if (i[0] == 1) {
                    emitter.fromVanilla(quad, MATERIAL, quad.getFace());
                    createCardBackground(CARD_BACKGROUND, emitter);
                    emitter.tag(i[0]++);
                    emitter.emit();

                    emitter.fromVanilla(quad, MATERIAL, quad.getFace());
                    createIcon(ICON, 5, 4, 16, 14, 0.0001F, emitter);
                    emitter.tag(i[0]++);
                    emitter.emit();

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
            mesh = builder.build();
        }
    }

    private void createIcon(Sprite sprite, int x, int y, int width, int height, float depth_mod, QuadEmitter emitter) {
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

    private void setColor(int color, QuadEmitter emitter) {
        for (int i = 0; i < 4; i++) {
            emitter.spriteColor(i, 0, color);
        }
    }

    private void createCardBackground(Sprite sprite, QuadEmitter emitter) {
        emitter.sprite(0, 0, sprite.getMinU(), sprite.getMinV());
        emitter.sprite(1, 0, sprite.getMinU(), sprite.getMaxV());
        emitter.sprite(2, 0, sprite.getMaxU(), sprite.getMaxV());
        emitter.sprite(3, 0, sprite.getMaxU(), sprite.getMinV());
   
        fixCardBackground(emitter,0);
    }

    private void fixCardBackground(QuadEmitter emitter, float depth_mod)  {
        emitter.pos(0, 0, 0.041667F + depth_mod, 0);
        emitter.pos(1, 0, 0.041667F + depth_mod, 1.25F);
        emitter.pos(2, 1, 0.041667F + depth_mod, 1.25F);
        emitter.pos(3, 1, 0.041667F + depth_mod, 0);
        // If i don't do surgery here the the width is 1 unit to narrow????
    }

    @Override
    public Sprite getParticleSprite() {
        return model.getParticleSprite();
    }

    @Override
    public ModelTransformation getTransformation() {
        return model.getTransformation();
    }

    @Override
    public ModelOverrideList getOverrides() {
        return model.getOverrides();
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

    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter,
            ModelBakeSettings rotationContainer, Identifier modelId) {
        if (model == null) {
            model = ModInitClient.CARD_MODEL.bake(loader, textureGetter, rotationContainer, modelId);
        }
        return this;
    }
    
}
