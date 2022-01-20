package adventurepack.mods.smab.minecraft.entity;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import adventurepack.mods.smab.ModInit;
import adventurepack.mods.smab.smab.Smab;
import adventurepack.mods.smab.smab.SmabSpecies;
import biom4st3r.libs.biow0rks.autonbt.AutoNbt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class SmabEntity extends PathAwareEntity implements IAnimatable, Tameable {

    private final AnimationFactory animationFactory;
    private Smab smab;

    public SmabEntity(EntityType<? extends SmabEntity> entityType, World world, SmabSpecies type) {
        super(entityType, world);
        this.smab = new Smab(type);
        this.animationFactory = new AnimationFactory(this);
        this.ignoreCameraFrustum = true;
        this.getAttributes();
    }

    private static List<AttributeRef> REFS = Lists.newArrayList(); 

    private static record AttributeRef(EntityAttribute attribute,UUID uuid) {
        public AttributeRef(EntityAttribute attribute,UUID uuid) {
            this.attribute = attribute;
            this.uuid = uuid;
            REFS.add(this);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.smab = AutoNbt.deserialize(Smab.class, nbt.get("smab"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("smab", AutoNbt.serialize(smab));
    }

    private static final AttributeRef MAX_HEALTH = new AttributeRef(EntityAttributes.GENERIC_MAX_HEALTH, UUID.fromString("51958a36-7881-42e9-a5bf-425df1391225"));
    private static final AttributeRef KNOCKBACK_RESIST = new AttributeRef(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, UUID.fromString("7736e577-65ec-44b9-942a-68efa0828933"));
    private static final AttributeRef SPEED = new AttributeRef(EntityAttributes.GENERIC_MOVEMENT_SPEED, UUID.fromString("aafd850d-6b45-4462-bf0e-9230bf5ee183"));

    private static final AttributeRef ATK_DMG = new AttributeRef(EntityAttributes.GENERIC_ATTACK_DAMAGE, UUID.fromString("bf0e773e-d0b6-48c8-8895-db5a2d915800"));
    private static final AttributeRef ATK_KNOCKBACK = new AttributeRef(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, UUID.fromString("8d606ff0-30b6-4a39-8fc5-6b34499a874f"));
    private static final AttributeRef ATK_SPEED = new AttributeRef(EntityAttributes.GENERIC_ATTACK_SPEED, UUID.fromString("7398e774-bfd2-46fd-920d-5f0e645b2066"));

    private static final AttributeRef ARMOR = new AttributeRef(EntityAttributes.GENERIC_ARMOR, UUID.fromString("8b4039e9-b22a-4d69-9d5a-79c16b46aee7"));
    private static final AttributeRef ARMOR_TOUGHNESS = new AttributeRef(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, UUID.fromString("019251bf-75a1-49fb-9b90-18377443d66d"));


    @SuppressWarnings({"unused"})
    public void statUpdate() {
        REFS.forEach(ref -> this.getAttributeInstance(ref.attribute()).removeModifier(ref.uuid()));
        /*
            int int
            str str
            vit vit
            dex dex

            int dex
            int vit
            str dex
            str vit
            str int
            dex vit
        */
        EntityAttributeModifier mod_MAX_HEALTH = new EntityAttributeModifier(MAX_HEALTH.uuid(), "Max Health From Stats", -1, Operation.ADDITION);
        EntityAttributeModifier mod_KNOCKBACK_RESIST = new EntityAttributeModifier(KNOCKBACK_RESIST.uuid(), "Max Health From Stats", -1, Operation.ADDITION);
        EntityAttributeModifier mod_SPEED = new EntityAttributeModifier(SPEED.uuid(), "Max Health From Stats", -1, Operation.ADDITION);

        EntityAttributeModifier mod_ATK_DMG = new EntityAttributeModifier(ATK_DMG.uuid(), "Max Health From Stats", -1, Operation.ADDITION);
        EntityAttributeModifier mod_ATK_KNOCKBACK = new EntityAttributeModifier(ATK_KNOCKBACK.uuid(), "Max Health From Stats", -1, Operation.ADDITION);
        EntityAttributeModifier mod_ATK_SPEED = new EntityAttributeModifier(ATK_SPEED.uuid(), "Max Health From Stats", -1, Operation.ADDITION);

        EntityAttributeModifier mod_ARMOR = new EntityAttributeModifier(ARMOR.uuid(), "Max Health From Stats", -1, Operation.ADDITION);
        EntityAttributeModifier mod_ARMOR_TOUGHNESS = new EntityAttributeModifier(ARMOR_TOUGHNESS.uuid(), "Max Health From Stats", -1, Operation.ADDITION);

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
        if(this.world.isClient) return ActionResult.SUCCESS;
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

    private String getAnimation(String animation) {
        return String.format("animation.%s.%s.%s", this.smab.getId().getNamespace(), this.smab.getId().getPath(), animation);
    }

    @Override
    public void registerControllers(AnimationData data) {
        if (ModInit.disable_animations) return;
        data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, (event)-> {
            if (event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(getAnimation("walking"), true));
                return PlayState.CONTINUE;
            } else if (Boolean.FALSE.booleanValue()) {
                return PlayState.CONTINUE;
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(getAnimation("idle"),true));
                return PlayState.CONTINUE;
            }
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
