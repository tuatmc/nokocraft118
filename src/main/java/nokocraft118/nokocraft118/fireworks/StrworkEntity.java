package nokocraft118.nokocraft118.fireworks;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nokocraft118.nokocraft118.setup.Registration;

import javax.annotation.Nullable;
import java.util.OptionalInt;

//TUAT花火エンティティの設定，呼び出すパーティクルの設定などはここ
//長いけどほとんどはバニラ花火と同じ挙動
public class StrworkEntity extends Projectile implements ItemSupplier {
    private static StrworkParticle particle = new StrworkParticle();

    private static final EntityDataAccessor<ItemStack> DATA_ID_FIREWORKS_ITEM = SynchedEntityData.defineId(StrworkEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<OptionalInt> DATA_ATTACHED_TO_TARGET = SynchedEntityData.defineId(StrworkEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
    private static final EntityDataAccessor<Boolean> DATA_SHOT_AT_ANGLE = SynchedEntityData.defineId(StrworkEntity.class, EntityDataSerializers.BOOLEAN);
    private int life;
    private int lifetime;
    @javax.annotation.Nullable
    private LivingEntity attachedToEntity;


    public StrworkEntity(Level world, double x, double y, double z, ItemStack item) {
        super(Registration.STRWORK.get(), world);
        //ここでスーパークラスにわたすEntityTypeが地味に重要，ここが違うとイベントハンドラーが呼ばれない
        this.life = 0;
        this.setPos(x, y, z);
        int i = 1;
        if(!item.isEmpty() && item.hasTag()) {
            this.entityData.set(DATA_ID_FIREWORKS_ITEM, item.copy());
            i += item.getOrCreateTagElement("Fireworks").getByte("Flight");
        }

        this.setDeltaMovement(this.random.nextGaussian() * 0.001D, 0.05D, this.random.nextGaussian() * 0.001D);
        this.lifetime = 10 * i + this.random.nextInt(6) + this.random.nextInt(7);

        this.setXRot(0.0f);
        this.setYRot(0.0f);

    }

    public StrworkEntity(EntityType<StrworkEntity> strworkEntityEntityType, Level level) {
        super(Registration.STRWORK.get(), level);
    }

    public StrworkEntity(Level world, @Nullable Entity entity, double x, double y, double z, ItemStack item) {
        this(world, x, y, z, item);
        this.setOwner(entity);
    }

    public StrworkEntity(Level world, ItemStack item, LivingEntity entity) {
        this(world, entity, entity.getX(), entity.getY(), entity.getZ(), item);
        this.entityData.set(DATA_ATTACHED_TO_TARGET, OptionalInt.of(entity.getId()));
        this.attachedToEntity = entity;
    }

    public StrworkEntity(Level world, ItemStack item, double x, double y, double z, boolean gravity) {
        this(world, x, y, z, item);
        this.entityData.set(DATA_SHOT_AT_ANGLE, gravity);
    }


    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_ID_FIREWORKS_ITEM, ItemStack.EMPTY);
        this.entityData.define(DATA_ATTACHED_TO_TARGET, OptionalInt.empty());
        this.entityData.define(DATA_SHOT_AT_ANGLE, false);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return pDistance < 4096.0D && !this.isAttachedToEntity();
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return super.shouldRender(pX, pY, pZ) && !this.isAttachedToEntity();
    }

    @Override
    public void tick() {
        super.tick();
        if(this.isAttachedToEntity()) {
            if(this.attachedToEntity == null) {
                this.entityData.get(DATA_ATTACHED_TO_TARGET).ifPresent((val) -> {
                    Entity entity = this.level.getEntity(val);
                    if(entity instanceof LivingEntity) {
                        this.attachedToEntity = (LivingEntity) entity;
                    }

                });
            }

            if(this.attachedToEntity != null) {
                Vec3 vec3;
                if(this.attachedToEntity.isFallFlying()) {
                    Vec3 vec31 = this.attachedToEntity.getLookAngle();
                    double d0 = 1.5D;
                    double d1 = 0.1D;
                    Vec3 vec32 = this.attachedToEntity.getDeltaMovement();
                    this.attachedToEntity.setDeltaMovement(vec32.add(vec31.x * 0.1D + (vec31.x * 1.5D - vec32.x) * 0.5D, vec31.y * 0.1D + (vec31.y * 1.5D - vec32.y) * 0.5D, vec31.z * 0.1D + (vec31.z * 1.5D - vec32.z) * 0.5D));
                    vec3 = this.attachedToEntity.getHandHoldingItemAngle(Items.FIREWORK_ROCKET);
                } else {
                    vec3 = Vec3.ZERO;
                }

                this.setPos(this.attachedToEntity.getX() + vec3.x, this.attachedToEntity.getY() + vec3.y, this.attachedToEntity.getZ() + vec3.z);
                this.setDeltaMovement(this.attachedToEntity.getDeltaMovement());
            }
        } else {
            if(!this.isShotAtAngle()) {
                double d2 = this.horizontalCollision ? 1.0D : 1.15D;
                this.setDeltaMovement(this.getDeltaMovement().multiply(d2, 1.0D, d2).add(0.0D, 0.04D, 0.0D));
            }

            Vec3 vec33 = this.getDeltaMovement();
            this.move(MoverType.SELF, vec33);
            this.setDeltaMovement(vec33);
        }

        HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        if(!this.noPhysics) {
            this.onHit(hitresult);
            this.hasImpulse = true;
        }

        this.updateRotation();
        if(this.life == 0 && !this.isSilent()) {
            this.level.playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.FIRE_AMBIENT, SoundSource.AMBIENT, 3.0F, 1.0F);
        }

        ++this.life;
        if(this.level.isClientSide && this.life % 2 < 2) {
            this.level.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);
        }

        if(!this.level.isClientSide && this.life > this.lifetime) {
            this.explode();
        }


    }

    @Override
    protected void onHit(HitResult result) {
        if(result.getType() == HitResult.Type.MISS || !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, result)) {
            super.onHit(result);
        }
    }

    private void explode() {
        this.setYRot(0);
        this.level.broadcastEntityEvent(this, (byte) 17);
        this.gameEvent(GameEvent.EXPLODE, this.getOwner());
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if(!this.level.isClientSide) {
            this.explode();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        BlockPos blockpos = new BlockPos(result.getBlockPos());
        this.level.getBlockState(blockpos).entityInside(this.level, blockpos, this);
        if(!this.level.isClientSide() && this.hasExplosion()) {
            //this.explode();
        }

        super.onHitBlock(result);
    }

    private boolean hasExplosion() {
        ItemStack itemstack = this.entityData.get(DATA_ID_FIREWORKS_ITEM);
        CompoundTag compoundtag = itemstack.isEmpty() ? null : itemstack.getTagElement("Fireworks");
        ListTag listtag = compoundtag != null ? compoundtag.getList("Explosions", 10) : null;
        return listtag != null && !listtag.isEmpty();
    }


    private boolean isAttachedToEntity() {
        return this.entityData.get(DATA_ATTACHED_TO_TARGET).isPresent();
    }

    public boolean isShotAtAngle() {
        return this.entityData.get(DATA_SHOT_AT_ANGLE);
    }

    @Override
    @SubscribeEvent
    public void handleEntityEvent(byte id) {
        if(id == 17 && this.level.isClientSide) {
            if(!this.hasExplosion()) {
                for(int i = 0; i < this.random.nextInt(3) + 2; ++i) {
                    this.level.addParticle(ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, 0.005D, this.random.nextGaussian() * 0.05D);
                }
            } else {
                ItemStack itemstack = this.entityData.get(DATA_ID_FIREWORKS_ITEM);
                CompoundTag compoundtag = itemstack.isEmpty() ? null : itemstack.getTagElement("Fireworks");
                Vec3 vec3 = this.getDeltaMovement();
                particle.createStrworks((ClientLevel) level, this.getX(), this.getY(), this.getZ(), vec3.x, vec3.y, vec3.z, compoundtag);
            }
        }

        super.handleEntityEvent(id);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Life", this.life);
        tag.putInt("LifeTime", this.lifetime);
        ItemStack itemstack = this.entityData.get(DATA_ID_FIREWORKS_ITEM);
        if(!itemstack.isEmpty()) {
            tag.put("FireworksItem", itemstack.save(new CompoundTag()));
        }

        tag.putBoolean("ShotAtAngle", this.entityData.get(DATA_SHOT_AT_ANGLE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.life = tag.getInt("Life");
        this.lifetime = tag.getInt("LifeTime");
        ItemStack itemstack = ItemStack.of(tag.getCompound("FireworksItem"));
        if(!itemstack.isEmpty()) {
            this.entityData.set(DATA_ID_FIREWORKS_ITEM, itemstack);
        }

        if(tag.contains("ShotAtAngle")) {
            this.entityData.set(DATA_SHOT_AT_ANGLE, tag.getBoolean("ShotAtAngle"));
        }
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemstack = this.entityData.get(DATA_ID_FIREWORKS_ITEM);
        return itemstack.isEmpty() ? new ItemStack(Registration.STRWORK_ITEM.get()) : itemstack;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }
}
