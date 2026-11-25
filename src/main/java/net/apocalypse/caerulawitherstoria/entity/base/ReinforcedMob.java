package net.apocalypse.caerulawitherstoria.entity.base;

import net.apocalypse.caerulawitherstoria.mixins.EntityAccessor;
import net.apocalypse.caerulawitherstoria.mixins.LivingAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public abstract class ReinforcedMob extends Mob {
    public ReinforcedMob(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    private final EntityDataAccessor<Float> DATA_HEALTH_GOTTEN = LivingAccessor.getHealthID();

    private static final EntityDataAccessor<Float> DURABILITY = SynchedEntityData.defineId(ReinforcedMob.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DURABILITY_RE = SynchedEntityData.defineId(ReinforcedMob.class, EntityDataSerializers.FLOAT);

    @Override
    public void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(DURABILITY, getMaxDura());
        this.entityData.define(DURABILITY_RE, 0f);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag){
        super.readAdditionalSaveData(tag);
        float h = tag.getFloat("durability");
        this.entityData.set(DURABILITY, h);
        this.entityData.set(DURABILITY_RE, getMaxDura() - h);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag){
        super.readAdditionalSaveData(tag);
        tag.putFloat("durability", getDurability());
    }
    public abstract float getMaxDura();
    private void setDurability(float db){
        this.entityData.set(DURABILITY, Math.min(db, this.getMaxDura()));
        this.entityData.set(DURABILITY_RE, Math.max(0, this.getMaxDura() - db));
    }
    private float getDurability(){
        float dura1 = this.entityData.get(DURABILITY);
        float dura2 = getMaxDura() - this.entityData.get(DURABILITY_RE);
        return Math.max(dura1, dura2);
    }
    private void refreshDurability(){
        float dura1 = this.entityData.get(DURABILITY);
        float dura2 = getMaxDura() - this.entityData.get(DURABILITY_RE);
        if (dura1 != dura2){
            setDurability(getDurability());
        }
    }
    private void setNotAnnihilated(){
        this.dead = false;
        if(this.getRemovalReason() == RemovalReason.DISCARDED || this.getRemovalReason() == RemovalReason.KILLED)
            ((EntityAccessor)this).setRemovalReasonRaw(null);
    }
    private boolean shouldAnnihilate(){
        return this.getDurability() <= 0;
    }
    public  boolean isNoAi(){
        return false;
    }

    @Override
    public void tick(){
        super.tick();
        if(!shouldAnnihilate()) this.setNotAnnihilated();
        AttributeInstance instance = this.getAttribute(Attributes.MAX_HEALTH);
        if(instance != null){
            if(instance.getBaseValue() < getMaxDura()) {
                instance.setBaseValue(getMaxDura());
                instance.getModifiers().forEach(am -> {
                    if (am.getAmount() < 0) instance.removeModifier(am);
                });
            }
        }
        List<MobEffectInstance> effects = this.getActiveEffects().stream().toList();
        for (MobEffectInstance effectInstance: effects){
            removeBadEffect(Attributes.MAX_HEALTH, effectInstance);
            removeBadEffect(Attributes.ATTACK_DAMAGE, effectInstance);
        }
        if (this.getY() < -999) this.setPosRaw(this.getX(), 256, this.getZ());
    }
    public void removeBadEffect(Attribute attr, MobEffectInstance effectInstance){
        AttributeModifier modifier = effectInstance.getEffect().getAttributeModifiers().get(attr);
        if(modifier != null && modifier.getAmount() < 0) this.removeEffect(effectInstance.getEffect());
    }
    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance instance){
        Map<Attribute, AttributeModifier> map = instance.getEffect().getAttributeModifiers();
        AttributeModifier healModifier = map.get(Attributes.MAX_HEALTH), atkModifier = map.get(Attributes.ATTACK_DAMAGE);
        if(healModifier != null && healModifier.getAmount() < 0) return false;
        return atkModifier == null || !(atkModifier.getAmount() < 0);
    }
    @Override
    public float getHealth(){
        return getDurability();
    }
    @Override
    public void setHealth(float pHealth){
        this.entityData.set(DATA_HEALTH_GOTTEN, getDurability());
        refreshDurability();
    }
    @Override
    public void tickDeath(){
        if(!this.shouldAnnihilate()){
            this.deathTime = 0;
            this.setNotAnnihilated();
            return;
        }
        super.tickDeath();
    }
    @Override
    public void actuallyHurt(@NotNull DamageSource source, float amount){
        if (this.isInvulnerableTo(source)) return;
        amount = Math.min(amount, ForgeHooks.onLivingHurt(this, source, amount));
        amount = this.getDamageAfterArmorAbsorb(source, amount);
        amount = this.getDamageAfterMagicAbsorb(source, amount);
        float reduction = Math.min(amount, this.getMaxHealth() * 0.33f);
        if (reduction > 0){
            this.getCombatTracker().recordDamage(source, reduction);
            this.setDurability(this.getDurability() - reduction);
            this.gameEvent(GameEvent.ENTITY_DAMAGE);
        }
    }
    @Override
    public void heal(float amount){
        if(this.shouldAnnihilate()) return;
        this.setDurability(this.getDurability() + amount);
    }
    @Override
    public boolean isDeadOrDying(){
        return shouldAnnihilate();
    }
    @Override
    public void die(@NotNull DamageSource source){
        if(shouldAnnihilate()) super.die(source);
    }
    @Override
    public void remove(@NotNull RemovalReason reason){
        if (reason == RemovalReason.DISCARDED || (!this.shouldAnnihilate() && reason == RemovalReason.KILLED)) return;
        super.remove(reason);
    }
    @Override
    public void knockback(double d1, double d2, double d3){}
    @Override
    public void push(double d1, double d2, double d3){}
    public int MAX_DIST = 16777216;
    @Override
    public void setPos(double x, double y, double z){
        if (this.distanceToSqr(x,y,z) > MAX_DIST) return;
        super.setPos(x,y,z);
    }
    @Override
    public void moveTo(double x, double y, double z, float pYRot, float pXRot){
        if (this.distanceToSqr(x,y,z) > MAX_DIST) return;
        super.moveTo(x,y,z,pYRot,pXRot);
    }
}
