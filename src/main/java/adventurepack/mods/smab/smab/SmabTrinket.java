package adventurepack.mods.smab.smab;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class SmabTrinket implements Trinket {
    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return true;
        // return Trinket.super.canEquip(stack, slot, entity);
    }
}
