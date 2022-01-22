package adventurepack.mods.smab.minecraft.client.itemodel;

import java.util.Random;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;

public class DeckModel extends AbstractModel {

    public DeckModel(Sprite sprite, ModelTransformation tranformation, ModelOverrideList overridelist) {
        super(sprite, tranformation, overridelist);
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        
        
    }
    
}
