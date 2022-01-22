package adventurepack.mods.smab.minecraft;

import adventurepack.mods.smab.ModInit;
import adventurepack.mods.smab.ModInitClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GuiItem extends Item {

    public GuiItem() {
        super(new Item.Settings().maxCount(1).group(ModInit.SMABS_GROUP));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        
        if (world.isClient) {
            ModInitClient.openGui();
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
    
}
