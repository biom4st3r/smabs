// package adventurepack.mods.smab;

// import org.apache.commons.compress.archivers.dump.DumpArchiveEntry.TYPE;

// import net.fabricmc.api.EnvType;
// import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
// import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
// import net.fabricmc.loader.api.FabricLoader;
// import net.minecraft.entity.Entity;
// import net.minecraft.entity.EntityDimensions;
// import net.minecraft.entity.EntityType;
// import net.minecraft.entity.vehicle.BoatEntity;
// import net.minecraft.nbt.NbtCompound;
// import net.minecraft.network.Packet;
// import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
// import net.minecraft.util.Identifier;
// import net.minecraft.util.registry.Registry;
// import net.minecraft.world.World;

// public class MiscEntity extends Entity {
//     public static final EntityType<MiscEntity> TYPE = FabricEntityTypeBuilder
//         .create()
//         .fireImmune()
//         .entityFactory(MiscEntity::new)
//         .dimensions(EntityDimensions.fixed(2, 2))
//         .build();
//     static {
//         Registry.register(Registry.ENTITY_TYPE, new Identifier("MODID", "MISCENTITY"), TYPE);
//         if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
//             EntityRendererRegistry.register(TYPE, entityRendererFactory);
//         }
//     }

//     public MiscEntity(EntityType<?> type, World world) {
//         super(type, world);
//         this.inanimate = true;
//     }

//     @Override
//     public boolean collidesWith(Entity other) {
//         return BoatEntity.canCollide(this, other);
//     }

//     @Override
//     public boolean isCollidable() {
//         return true;
//     }

//     @Override
//     public boolean isPushable() {
//         return true;
//     }

//     @Override
//     public boolean collides() {
//         return !this.isRemoved();
//     }

//     @Override
//     protected void initDataTracker() {}

//     @Override
//     protected void readCustomDataFromNbt(NbtCompound nbt) {}

//     @Override
//     protected void writeCustomDataToNbt(NbtCompound nbt) {}

//     @Override
//     public Packet<?> createSpawnPacket() {
//         return new EntitySpawnS2CPacket(this);
//     }
    
// }
