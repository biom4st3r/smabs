package adventurepack.mods.smab.minecraft.client.itemodel;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

public abstract class AbstractModel implements BakedModel, FabricBakedModel {

    protected final Sprite sprite;
    protected final ModelTransformation tranformation;
    protected final ModelOverrideList overridelist;

    public AbstractModel(Sprite sprite, ModelTransformation tranformation, ModelOverrideList overridelist) {
        this.sprite = sprite;
        this.tranformation = tranformation;
        this.overridelist = overridelist;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos,
            Supplier<Random> randomSupplier, RenderContext context) {
        
    }

    @Override
    public abstract void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context);

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return this.sprite;
    }

    @Override
    public ModelTransformation getTransformation() {
        return this.tranformation;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return this.overridelist;
    }
    
}
