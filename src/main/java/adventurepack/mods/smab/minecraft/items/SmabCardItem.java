package adventurepack.mods.smab.minecraft.items;

import java.util.Optional;

import adventurepack.mods.smab.Registries;
import adventurepack.mods.smab.component.SmabItemComponent;
import adventurepack.mods.smab.minecraft.entity.SmabEntity;
import adventurepack.mods.smab.smab.Smab;
import adventurepack.mods.smab.smab.SmabSpecies;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SmabCardItem extends CardItem {
    public final SmabSpecies species;

    public SmabCardItem(SmabSpecies species, Settings settings) {
        super(species, settings);
        this.species = species;
    }

    @Override
    @SuppressWarnings({"deprecation"})
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) return TypedActionResult.success(user.getStackInHand(hand), true);
        boolean serial = false;
        Optional<SmabItemComponent> component = SmabItemComponent.KEY.getComponent(user.getStackInHand(hand));
        if (component.isPresent()) {
            if (component.get().smab == null) {
                component.get().smab = new Smab(this.species);
                component.get().smab.setOT(user);
                serial = true;
            }
            if (user.isSneaking()) {
                SmabEntity entity = new SmabEntity(Registries.SMABS.get(this.species.id()).type(), world, component.get().smab);
                user.getStackInHand(hand).decrement(1);
                entity.updatePositionAndAngles(user.getX(), user.getY(), user.getZ(), user.getYaw(), user.getPitch());
                world.spawnEntity(entity);
                
            }
            component.get().release(serial);
        }

        if (user.isSneaking()) {
            return TypedActionResult.success(user.getStackInHand(hand), true);
        } else {
            return super.use(world, user, hand);
        }
    }
}
