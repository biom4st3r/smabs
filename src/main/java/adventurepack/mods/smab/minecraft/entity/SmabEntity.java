package adventurepack.mods.smab.minecraft.entity;

import java.util.UUID;

import adventurepack.mods.smab.smab.Smab;
import adventurepack.mods.smab.smab.SmabSpecies;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class SmabEntity extends PathAwareEntity implements IAnimatable,Tameable {

    private final AnimationFactory animationFactory;
    private final Smab smab;

    public SmabEntity(EntityType<? extends SmabEntity> entityType, World world, SmabSpecies type) {
        super(entityType, world);
        this.smab = new Smab(type);
        this.animationFactory = new AnimationFactory(this);
        this.ignoreCameraFrustum = true;
    }

    public SmabEntity(EntityType<? extends SmabEntity> entityType, World world, Smab smab) {
        super(entityType, world);
        this.smab = smab;
        this.animationFactory = new AnimationFactory(this);
        this.ignoreCameraFrustum = true;
    }

    public Smab getSmab() {
        return this.smab;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if ((this.smab.isOT(player) || !this.smab.hasOT()) && player.getStackInHand(hand).isEmpty()) {
            if (!this.smab.hasOT()) this.smab.setOT(player);
            player.setStackInHand(hand, Smab.convertToItem(this.smab));
            this.remove(RemovalReason.DISCARDED);
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, (event)-> {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(String.format("animation.%s.%s.idle", this.smab.getId().getNamespace(),this.smab.getId().getPath()),true));
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public UUID getOwnerUuid() {
        return this.smab.getOtUUID();
    }

    @Override
    public Entity getOwner() {
        return this.smab.getOtEntity(this.world);
    }

}
