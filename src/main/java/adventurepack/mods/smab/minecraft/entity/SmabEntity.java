package adventurepack.mods.smab.minecraft.entity;

import java.util.UUID;

import adventurepack.mods.smab.smab.Smab;
import adventurepack.mods.smab.smab.SmabSpecies;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class SmabEntity extends MobEntity implements IAnimatable,Tameable {

    private final AnimationFactory animationFactory;
    private final Smab smab;

    public SmabEntity(EntityType<? extends SmabEntity> entityType, World world, SmabSpecies type) {
        super(entityType, world);
        this.smab = new Smab(type);
        this.animationFactory = new AnimationFactory(this);
        this.ignoreCameraFrustum = true;
    }

    public Smab getSmab() {
        return this.smab;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    @Override
    public void registerControllers(AnimationData arg0) {
        
    }

    @Override
    public UUID getOwnerUuid() {
        return null;
    }

    @Override
    public Entity getOwner() {
        return null;
    }

}