package adventurepack.mods.smab.component;

import adventurepack.mods.smab.smab.Smab;
import biom4st3r.libs.biow0rks.PooledItemComponent;
import biom4st3r.libs.biow0rks.autonbt.AutoNbt;
import net.minecraft.item.ItemStack;

public class SmabItemComponent extends PooledItemComponent {
    public static final ComponentKey<SmabItemComponent> KEY = PooledItemComponent.ComponentKey.of(SmabItemComponent.class, "smabitemcomponent", SmabItemComponent::new);

    Smab smab;

    @Override
    public void fromStack(ItemStack stack) {
        if (!stack.hasNbt() || stack.getSubNbt("smab").isEmpty()) return;
        smab = AutoNbt.deserialize(Smab.class, stack.getSubNbt("smab"));
        
    }

    @Override
    public void toStack(ItemStack stack) {
        if (smab == null) return;
        stack.getOrCreateNbt().put("smab", AutoNbt.serialize(smab));
        
    }

    @Override
    public void clear() {
        smab = null;
    }

    @Override
    public ComponentKey<? extends PooledItemComponent> getKey() {
        return KEY;
    }
    
}
