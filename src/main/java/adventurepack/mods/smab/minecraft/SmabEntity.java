package adventurepack.mods.smab.minecraft;

import adventurepack.mods.smab.smab.Smab;
import adventurepack.mods.smab.smab.SmabSpecies;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public abstract class SmabEntity extends TameableEntity {

    protected SmabEntity(EntityType<? extends TameableEntity> entityType, World world, SmabSpecies type) {
        super(entityType, world);
        this.smab = null;
    }

    public final Smab smab;

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

}
