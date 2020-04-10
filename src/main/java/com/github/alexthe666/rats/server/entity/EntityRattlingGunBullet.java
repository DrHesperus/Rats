package com.github.alexthe666.rats.server.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityRattlingGunBullet extends AbstractArrowEntity {

    public EntityRattlingGunBullet(EntityType type, World worldIn) {
        super(type, worldIn);
        this.setDamage(2F);
    }

    public EntityRattlingGunBullet(EntityType type, World worldIn, double x, double y, double z) {
        this(type, worldIn);
        this.setPosition(x, y, z);
        this.setDamage(2F);
    }

    public EntityRattlingGunBullet(EntityType type, World worldIn, LivingEntity shooter) {
        super(type, shooter, worldIn);
        this.setDamage(2F);
    }

    public EntityRattlingGunBullet(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(RatsEntityRegistry.RATTLING_GUN_BULLET, world);
    }

    public boolean isInWater() {
        return false;
    }

    public void tick() {
        float sqrt = (float)this.getMotion().length();
        if (sqrt < 0.2F || this.inGround || this.collided) {
            this.remove();
        }
        super.tick();
    }

    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (!this.isSilent() && soundIn != SoundEvents.ENTITY_ARROW_HIT && soundIn != SoundEvents.ENTITY_ARROW_HIT_PLAYER) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, soundIn, this.getSoundCategory(), volume, pitch);
        }
    }

    protected void onHit(RayTraceResult raytraceResultIn) {
        if (raytraceResultIn instanceof EntityRayTraceResult && ((EntityRayTraceResult) raytraceResultIn).getEntity() instanceof PlayerEntity) {
            this.damageShield((PlayerEntity) ((EntityRayTraceResult) raytraceResultIn).getEntity(), (float) this.getDamage());
        }
        super.onHit(raytraceResultIn);
        this.remove();
    }

    protected void damageShield(PlayerEntity player, float damage) {
        if (damage >= 3.0F && player.getActiveItemStack().getItem().isShield(player.getActiveItemStack(), player)) {
            ItemStack copyBeforeUse = player.getActiveItemStack().copy();
            int i = 1 + MathHelper.floor(damage);
            player.getActiveItemStack().damageItem(i, player, (p_220048_0_) -> {
                p_220048_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });

            if (player.getActiveItemStack().isEmpty()) {
                Hand Hand = player.getActiveHand();
                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, Hand);

                if (Hand == Hand.MAIN_HAND) {
                    this.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemStackToSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
                }
                player.resetActiveHand();
                this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
            }
        }
    }

    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}