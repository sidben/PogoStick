package sidben.pogostick.util;

import javax.annotation.Nonnull;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.main.Features;


public class PogostickHelper
{

    private final static float MAX_MOTION_UP                    = 10F;
    private final static float MIN_DISTANCE_TO_DAMAGE_POGOSTICK = 3F;
    private final static int   MAX_DAMAGE_TO_POGOSTICK          = 10;

    public final static float  MIN_DISTANCE_TO_BOUNCE           = 1.1F;



    public static boolean canEntityActivatePogostick(@Nonnull EntityLivingBase entity)
    {
        final ItemStack heldPogostick = getHeldPogostick(entity);

        // TODO: negate activation if the player has a food item in the other hand (or another item that is used with right-click)
        // TODO: test sleeping

        return entity.hasCapability(CapabilityPogostick.POGOSTICK, null) 
                && !entity.onGround && entity.isEntityAlive() 
                && !(entity.isInWater() || entity.isInLava()) 
                && !entity.isElytraFlying()
                && heldPogostick != ItemStack.EMPTY;
    }



    /**
     * Returns the ItemStack with the pogostick being held by the given entity.
     * Return ItemStack.EMPTY if invalid.
     * This method DO NOT realize capability validation;
     */
    @Deprecated // TODO: better implementation, this method may be removed
    private static ItemStack getHeldPogostick(@Nonnull EntityLivingBase entity)
    {
        if (entity.getHeldItemMainhand().getItem() == Features.pogoStick
                && entity.getHeldItemMainhand().getItemDamage() < entity.getHeldItemMainhand().getMaxDamage()) { return entity.getHeldItemMainhand(); }
        if (entity.getHeldItemOffhand().getItem() == Features.pogoStick
                && entity.getHeldItemOffhand().getItemDamage() < entity.getHeldItemOffhand().getMaxDamage()) { return entity.getHeldItemOffhand(); }
        return ItemStack.EMPTY;
    }



    /**
     * Returns how much damage the pogostick should take to bounce from this distance.
     */
    public static int calculateItemDamage(float fallDistance)
    {
        if (fallDistance < MIN_DISTANCE_TO_DAMAGE_POGOSTICK) { return 0; }
        final float rawDamage = MathHelper.sqrt(fallDistance * 0.3F);
        return (int) MathHelper.clamp(Math.floor(rawDamage), 1, MAX_DAMAGE_TO_POGOSTICK);
    }



    /**
     * Process what should happen when an entity lands with an active pogostick.
     * Capability validation is also performed inside this method.
     *
     * This method process actions for both, client and server. On client it updates
     * just the vertical motion and play the item sounds. On server it damages the item.
     *
     * This method DO NOT negate fall damage.
     */
    public static void processEntityLandingWithPogostick(@Nonnull EntityLivingBase entity, float fallDistance, double fallSpeed)
    {
        if (!entity.hasCapability(CapabilityPogostick.POGOSTICK, null) || !entity.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick()) { return; }

        // Have a pogostick right now?
        ItemStack pogoStack = ItemStack.EMPTY;
        if (entity.getHeldItemMainhand().getItem() == Features.pogoStick) {
            pogoStack = entity.getHeldItemMainhand();
        } else if (entity.getHeldItemOffhand().getItem() == Features.pogoStick) {
            pogoStack = entity.getHeldItemOffhand();
        } else {
            return;
        }


        if (entity.world.isRemote) {
            PogostickHelper.updateVerticalMotion(entity, fallDistance, fallSpeed);
            entity.playSound(SoundEvents.ENTITY_SLIME_JUMP, 0.8F, 0.8F + entity.world.rand.nextFloat() * 0.4F);

        } else {
            final int damageAmount = PogostickHelper.calculateItemDamage(fallDistance);
            LogHelper.trace("A fall from %.4f will cause %d damage to the pogostick", fallDistance, damageAmount);
            if (damageAmount > 0) {
                pogoStack.damageItem(damageAmount, entity);
            }

        }
    }



    private static void updateVerticalMotion(@Nonnull EntityLivingBase entity, float fallDistance, double fallSpeed)
    {
        // TODO: extra increase if jumping (will cause problems with elytra?)
        // TODO: sprinting lower the max jump but increases the horizontal speed

        float adjustedFallDistance = fallDistance;
        float minMotion = 0.6F;
        float lastModifier = 0.4F;

        if (entity instanceof EntityPlayer) {
            if (entity.isSneaking()) {
                // If the player is sneaking the bounce limit is much lower
                LogHelper.debug("  Applying sneaking modifier");
                adjustedFallDistance = fallDistance * 0.25F;
                minMotion = 0.75F;

            } else if (entity.isSprinting()) {
                // If the player is sprinting the bounce height limit is lower, but accelerates faster 
                // (horizontal speed should be increased)
                LogHelper.debug("  Applying sprinting modifier");
                lastModifier = 0.25F;
                
            }
        }


        LogHelper.debug(">>> Fall distance %.4f (from %.4f)", adjustedFallDistance, fallDistance);
        LogHelper.debug(">>> Old motionY %.4f", entity.motionY);

        final double newSpeed = minMotion + Math.log(Math.max(adjustedFallDistance, 1)) * lastModifier;
        entity.motionY = Math.min(newSpeed, MAX_MOTION_UP);

        LogHelper.debug(">>> New motionY %.4f (max %.4f)", newSpeed, MAX_MOTION_UP);
        LogHelper.debug("    ");

        entity.isAirBorne = true;
        entity.onGround = false;
    }



    /**
     * Removes the air friction for horizontal movement when the entity
     * is using the pogostick.
     */
    public static void removeAirFrictionIfUsingPogostick(@Nonnull EntityLivingBase entity)
    {
        if (!entity.hasCapability(CapabilityPogostick.POGOSTICK, null) || !entity.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick() || entity.onGround) { return; }

        // TODO: better formula, I want much slower acceleration

        final float s1 = entity.moveStrafing * entity.moveStrafing + entity.moveForward * entity.moveForward;
        final float s2 = Math.max(MathHelper.sqrt(s1), 1F);
        final float factor = 0.05F / s2;
        final float strafe = entity.moveStrafing * factor;
        final float forward = entity.moveForward * factor;

        final float xa1 = MathHelper.sin(entity.rotationYaw * 0.017453292F);
        final float ya1 = MathHelper.cos(entity.rotationYaw * 0.017453292F);
        final double xa2 = strafe * ya1 - forward * xa1;
        final double ya2 = forward * ya1 + strafe * xa1;


        // LogHelper.debug("+ Air motion(pre): %.4f, %.4f", entity.motionX, entity.motionZ);
        entity.motionX += xa2;
        entity.motionZ += ya2;
        // LogHelper.debug("+ Air motion(pos): %.4f, %.4f", entity.motionX, entity.motionZ);
        // LogHelper.debug("+---------------------------------+");

    }

}
