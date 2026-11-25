package net.apocalypse.caerulawitherstoria.entity;

import net.apocalypse.caerulawitherstoria.CaerulaWitherstoriaMod;
import net.apocalypse.caerulawitherstoria.entity.base.SyncableGeo;
import net.apocalypse.caerulawitherstoria.entity.goal.FlyingStrollGoal;
import net.apocalypse.caerulawitherstoria.init.CWModEntities;
import net.apocalypse.caerulawitherstoria.util.Tools;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class ChitinGhastEntity extends Monster implements SyncableGeo, RangedAttackMob {
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(ChitinGhastEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Boolean> IS_PHASE_TWO = SynchedEntityData.defineId(ChitinGhastEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> CHARGE_TICK = SynchedEntityData.defineId(ChitinGhastEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> COOLDOWN = SynchedEntityData.defineId(ChitinGhastEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> BITE_T_MINUS = SynchedEntityData.defineId(ChitinGhastEntity.class, EntityDataSerializers.INT);

    public final ServerBossEvent bossEvent = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS);

    public ChitinGhastEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(CWModEntities.CHITIN_GHAST.get(), world);
    }

    public ChitinGhastEntity(@NotNull EntityType<ChitinGhastEntity> type, Level world) {
        super(type, world);
        xpReward = 999;
        this.moveControl = new FlyingMoveControl(this, 16, true);
        setNoAi(false);
        setMaxUpStep(2f);
        setPersistenceRequired();
    }

    @Override
    public void tick(){
        super.tick();
        this.clearFire();
        bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        int charge = this.entityData.get(CHARGE_TICK);
        int bite = this.entityData.get(BITE_T_MINUS);
        int cooldown = this.entityData.get(COOLDOWN);
        LivingEntity enemy = Tools.validTarget(this);
        if(cooldown > 0){
            this.entityData.set(COOLDOWN, cooldown - 1);
        } else if(enemy != null){
            if (!this.entityData.get(IS_PHASE_TWO)) this.entityData.set(CHARGE_TICK, 200);
            else{
                this.setAnimation("animation.chitin_ghast.bite");
                this.entityData.set(COOLDOWN, 200);
                this.entityData.set(BITE_T_MINUS, 30);
                CaerulaWitherstoriaMod.queueServerWork(30, this::biteEnemies);
            }
        }
        if(charge > 0){
            if(enemy != null) {
                this.setDeltaMovement(this.position().vectorTo(enemy.position()).normalize().scale(0.64));
            }
            destroyBlocks();
            this.entityData.set(CHARGE_TICK, charge - 1);
            this.entityData.set(COOLDOWN, 200);
        }
        if(bite > 0){
            if(enemy != null){
                this.setDeltaMovement(this.position().vectorTo(enemy.position()).normalize().scale(0.5));
            }
            this.entityData.set(BITE_T_MINUS, bite - 1);
        }
    }

    @Override
    public void die(DamageSource source){
        if(this.entityData.get(IS_PHASE_TWO)) super.die(source);
        else {
            this.setHealth(this.getMaxHealth());
            this.entityData.set(IS_PHASE_TWO, true);
            this.setAnimation("animation.chitin_ghast.to_phase_2");
            this.entityData.set(COOLDOWN, 20);
            this.entityData.set(CHARGE_TICK, 0);
        }
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity){
        this.lookAt(pTarget, 45, 45);
        shootFireballTo(pTarget);
    }

    public void destroyBlocks(){
        double x = this.getX(), y = this.getY(), z = this.getZ();
        Iterable<BlockPos> poses = BlockPos.betweenClosed(BlockPos.containing(x - 2, y, z-2), BlockPos.containing(x+2, y+4, z+2));
        Level level = this.level();
        for (BlockPos pos: poses){
            BlockState bs = level.getBlockState(pos);
            double h = bs.getDestroySpeed(level, pos);
            if(h>=0 && h<5){
                Block.dropResources(bs, level, pos);
                level.destroyBlock(pos, false);
            }
        }
    }

    public void shootFireballTo(LivingEntity pTarget){
        if(pTarget == null) return;
        if(!this.entityData.get(IS_PHASE_TWO))return;
        if(this.level() instanceof ServerLevel serverLevel) {
            serverLevel.playSound(this, this.blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.HOSTILE, 1, Mth.nextFloat(serverLevel.getRandom(), 0.9f, 1.1f));
            Vec3 myEye = this.position().add(0, this.getBbHeight() / 2, 0);
            Vec3 offset = myEye.vectorTo(pTarget.position().add(0, pTarget.getBbHeight() / 2, 0));
            float pitch = -15 + 30 * serverLevel.getRandom().nextFloat();
            float yaw = -15 + 30 * serverLevel.getRandom().nextFloat();
            Vec3 newOffset = offset.xRot((float) Math.toRadians(pitch)).yRot((float) Math.toRadians(yaw));
            Fireball fireball = new LargeFireball(this.level(), this, 0, 0, 0, 1);
            fireball.setPos(myEye.add(this.getLookAngle().normalize().scale(2)));
            fireball.shoot(newOffset.x, newOffset.y, newOffset.z, 1f, 0);
            serverLevel.addFreshEntity(fireball);
        }
    }

    public void biteEnemies(){
        if(this.level() instanceof ServerLevel serverLevel){
            Vec3 pos = this.position();
            List<LivingEntity> entities = serverLevel.getEntitiesOfClass(LivingEntity.class,
                    new AABB(pos.add(-3, -2, -3), pos.add(3,5,3)),
                    living -> {
                return living.isAlive() && living != this;
                    });
            entities.forEach(
                    e ->{
                        e.hurt(this.level().damageSources().mobAttack(this), 325);
                    }
            );
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount){
        if(source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypes.EXPLOSION)) return false;
        if(source.getEntity() == this) return false;
        if(this.entityData.get(CHARGE_TICK) > 0) amount = Math.min(amount, 1);
        return super.hurt(source, amount);
    }

    @Override
    public void tickDeath(){
        this.setNoGravity(false);
        ++this.deathTime;
        if (this.deathTime >= 60 && !this.level().isClientSide() && !this.isRemoved()) {
            this.level().broadcastEntityEvent(this, (byte)60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public void push(@NotNull Entity pEntity){
        super.push(pEntity);
        if(pEntity instanceof ChitinGhastEntity && pEntity != this.getTarget()) return;
        if(this.entityData.get(CHARGE_TICK)>0) {
            pEntity.hurt(this.level().damageSources().mobAttack(this), 7);
            Vec3 vec3 = this.position().vectorTo(pEntity.position()).normalize().scale(0.5);
            pEntity.push(vec3.x, vec3.y, vec3.z);
            pEntity.invulnerableTime = 0;
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.64);
        builder = builder.add(Attributes.MAX_HEALTH, 199);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 9);
        builder = builder.add(Attributes.FOLLOW_RANGE, 72);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
        builder = builder.add(Attributes.FLYING_SPEED, 0.64);
        return builder;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }


    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new ConditionalRangedAttack(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true, false));
        this.goalSelector.addGoal(4, new FlyingStrollGoal(this, 1));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new FloatGoal(this));
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 2F;
    }


    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("DataAnimation", this.entityData.get(ANIMATION));
        compound.putBoolean("DataPhase", this.entityData.get(IS_PHASE_TWO));
        compound.putInt("DataChargeTick", this.entityData.get(CHARGE_TICK));
        compound.putInt("DataCooldown", this.entityData.get(COOLDOWN));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("DataAnimation")) this.entityData.set(ANIMATION, compound.getString("DataAnimation"));
        if (compound.contains("DataPhase")) this.entityData.set(IS_PHASE_TWO, compound.getBoolean("DataPhase"));
        if (compound.contains("DataChargeTick")) this.entityData.set(CHARGE_TICK, compound.getInt("DataChargeTick"));
        if (compound.contains("DataCooldown")) this.entityData.set(COOLDOWN, compound.getInt("DataCooldown"));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION, "undefined");
        this.entityData.define(IS_PHASE_TWO, false);
        this.entityData.define(CHARGE_TICK, 0);
        this.entityData.define(COOLDOWN, 0);
        this.entityData.define(BITE_T_MINUS, 0);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source){
        return SoundEvents.GHAST_HURT;
    }
    @Override
    public SoundEvent getAmbientSound(){
        return SoundEvents.GHAST_AMBIENT;
    }
    @Override
    public SoundEvent getDeathSound(){
        return SoundEvents.GHAST_DEATH;
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new FlyingPathNavigation(this, world);
    }
    @Override
    public void setNoGravity(boolean noGravity) {
        if(this.isDeadOrDying()) super.setNoGravity(noGravity);
        else super.setNoGravity(true);
    }
    @Override
    public boolean causeFallDamage(float l, float d, DamageSource source) {
        return false;
    }
    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    private String ongoingAnimation = "empty";
    private String previousAnimation = "empty";

    @Override
    public String getOngoingAnimation() {
        return this.entityData.get(ANIMATION);
    }
    @Override
    public void setAnimation(String animation){
        this.entityData.set(ANIMATION, animation);
    }
    @Override
    public void setOngoingAnimation(String animation){
        this.ongoingAnimation = animation;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 0, this::movementPredicate));
        data.add(new AnimationController<>(this, "procedure", 0, this::procedurePredicate));
    }
    private PlayState movementPredicate(AnimationState event) {
        if (this.isDeadOrDying()) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.chitin_ghast.die"));
        }
        if (this.ongoingAnimation.equals("empty")) {
            if(this.entityData.get(CHARGE_TICK) > 0)
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.chitin_ghast.attack_1"));
            if(this.entityData.get(IS_PHASE_TWO))
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.chitin_ghast.idle_no_mask"));
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.chitin_ghast.idle"));
        }
        return PlayState.STOP;
    }

    public long lastSwing;

    private PlayState procedurePredicate(AnimationState event) {
        if (!ongoingAnimation.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED
                || (!this.ongoingAnimation.equals(previousAnimation) && !this.ongoingAnimation.equals("empty"))) {
            if (!this.ongoingAnimation.equals(previousAnimation))
                event.getController().forceAnimationReset();
            event.getController().setAnimation(RawAnimation.begin().thenPlay(this.ongoingAnimation));
            if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                this.ongoingAnimation = "empty";
                event.getController().forceAnimationReset();
            }
        } else if (ongoingAnimation.equals("empty")) {
            previousAnimation = "empty";
            return PlayState.STOP;
        }
        previousAnimation = this.ongoingAnimation;
        return PlayState.CONTINUE;
    }
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static class ConditionalRangedAttack extends RangedAttackGoal{
        private final ChitinGhastEntity me;
        public ConditionalRangedAttack(ChitinGhastEntity pRangedAttackMob) {
            super(pRangedAttackMob, 1, 10, 64);
            me = pRangedAttackMob;
        }
        @Override
        public boolean canUse(){return super.canUse() && me.entityData.get(IS_PHASE_TWO) && me.entityData.get(BITE_T_MINUS) <= 0;}
        @Override
        public boolean canContinueToUse(){return this.canUse() && me.entityData.get(IS_PHASE_TWO) && me.entityData.get(BITE_T_MINUS) <= 0;}
    }
}
