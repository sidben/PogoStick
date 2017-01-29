package sidben.pogostick.util;

import javax.annotation.Nonnull;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.main.Features;


public class PogostickHelper
{

    private final static float MAX_MOTION_UP                    = 10F;
    private final static float MIN_DISTANCE_TO_DAMAGE_POGOSTICK = 3F;
    private final static int   MAX_DAMAGE_TO_POGOSTICK          = 10;
    private final static float MAX_DAMAGE_TO_PLAYER             = 4F;

    public final static float  MIN_DISTANCE_TO_BOUNCE           = 1.1F;
    // TODO: remove fallSpeed, not used



    /**
     * Returns true if the given entity can activate the pogostick.
     *
     * This method should be used in {@link sidben.pogostick.item.ItemPogoStick ItemPogoStick} to decide the item action.
     */
    public static boolean canEntityActivatePogostick(@Nonnull EntityLivingBase entity)
    {
        final boolean isHoldingPogostick = (entity.getHeldItemMainhand().getItem() == Features.pogoStick || entity.getHeldItemOffhand().getItem() == Features.pogoStick);

        // TODO: negate activation if the player has a food item in the other hand (or another item that is used with right-click)

        return !entity.onGround 
                && isHoldingPogostick 
                && canEntityKeepUsingPogostick(entity);
    }


    /**
     * Returns true if the given entity can keep using the pogostick. It's assumed that the entity
     * already started using it.
     *
     * This method is mainly used in {@link sidben.pogostick.handler.EventHandlerEntity#onLivingUpdateEvent() EventHandlerEntity.onLivingUpdateEvent()}
     * to disable the pogostick if certain conditions are no longer valid.
     */
    public static boolean canEntityKeepUsingPogostick(@Nonnull EntityLivingBase entity)
    {
        return entity.hasCapability(CapabilityPogostick.POGOSTICK, null) 
                && entity.isEntityAlive() 
                && !entity.isRiding() 
                && !entity.isOnLadder() 
                && !(entity.isInWater() || entity.isInLava())
                // && !(entity.isInsideOfMaterial(Material.WATER) || entity.isInsideOfMaterial(Material.LAVA)) // TODO: check with other mods liquids
                && !entity.isElytraFlying();
                // && !entity.isInWeb; TODO: make isInWeb visible

        // OBS: entity.isInsideOfMaterial(Material.WATER) DON'T detect if the player has just the feet in water, inInWater() does.
    }



    /**
     * Returns how much damage the pogostick should take to bounce from this distance.
     */
    public static int calculateItemDamage(float fallDistance)
    {
        if (fallDistance < MIN_DISTANCE_TO_DAMAGE_POGOSTICK) { return 0; }
        final float rawDamage = MathHelper.sqrt(fallDistance * 0.35F);
        return (int) MathHelper.clamp(Math.floor(rawDamage), 1, MAX_DAMAGE_TO_POGOSTICK);
    }



    /**
     * Process what should happen when an entity lands with an active pogostick.
     * Capability validation is also performed inside this method.<br/>
     * <br/>
     *
     * This method process actions for both, client and server. On client it updates
     * just the vertical motion and play the item sounds. On server it damages the item.
     * This method DO NOT negate fall damage.<br/>
     * <br/>
     *
     * This method will probably be called by {@link sidben.pogostick.handler.DelayedEventHandlerBounceEntity#execute() DelayedEventHandlerBounceEntity.execute()}.
     */
    public static void processEntityLandingWithPogostick(@Nonnull EntityLivingBase entity, ItemStack pogoStack, float fallDistance, double fallSpeed)
    {
        if (!entity.hasCapability(CapabilityPogostick.POGOSTICK, null) 
                || !entity.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick()) { return; }

        // Have a pogostick right now?
        if (pogoStack.getItem() != Features.pogoStick) { return; }


        if (entity.world.isRemote) {
            PogostickHelper.updateVerticalMotion(entity, fallDistance, fallSpeed);      // TODO: check if this should be controlled by the server
            entity.playSound(SoundEvents.ENTITY_SLIME_JUMP, 0.8F, 0.8F + entity.world.rand.nextFloat() * 0.4F);

        } else {
            if (entity instanceof EntityPlayer) {
                final EntityPlayer player = ((EntityPlayer) entity);

                // Item damage
                final int damageToItem = PogostickHelper.calculateItemDamage(fallDistance);
                final float damageToPlayer = Math.min(MAX_DAMAGE_TO_PLAYER, damageToItem - pogoStack.getMaxDamage() + pogoStack.getItemDamage());

                if (damageToItem > 0) {
                    pogoStack.damageItem(damageToItem, entity);
                }


                // Exceeding damage goes to the player, up to a limit
                if (damageToPlayer > 0) {
                    // TODO: prevents bouncing if the item breaks
                    // TODO: deactivate pogostick if breaks
                    // entity.fallDistance = 0.0F;
                    entity.attackEntityFrom(DamageSource.FALL, damageToPlayer);
                }

                // TODO: check -> (entityplayermp.connection.getNetworkManager().isChannelOpen() && entityplayermp.world == this.world)


                // Stats
                player.addStat(Features.Stats.TIMES_BOUNCED);

                if (fallDistance > Features.StatsParams.DISTANCE_FOR_BIG_FALL) {
                    player.addStat(Features.Stats.BIG_FALL);
                }
                if (damageToPlayer > 0.0F) {
                    player.addStat(Features.Stats.BREAK_POGOSTICK);
                }


                LogHelper.trace("A fall from %.4f will cause %d damage to the pogostick and %.2f to the player", fallDistance, damageToItem, damageToPlayer);
            }

        }
    }



    private static void updateVerticalMotion(@Nonnull EntityLivingBase entity, float fallDistance, double fallSpeed)
    {
        float adjustedFallDistance = fallDistance * 0.8F;
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
                // (horizontal speed should be increased naturally)
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
        if (!entity.hasCapability(CapabilityPogostick.POGOSTICK, null) 
                || !entity.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick() 
                || entity.onGround) { return; }

        /**
         * Ref:
         *
         * {@link net.minecraft.entity.EntityLivingBase#moveEntityWithHeading(float strafe, float forward)}
         * {@link net.minecraft.entity.Entity#moveRelative(float strafe, float forward, float friction)}
         * {@link net.minecraft.entity.Entity#move(MoverType type, double x, double y, double z)}
         */

        // LogHelper.debug("+ Air motion(pre): %.4f, %.4f", entity.motionX, entity.motionZ);
        entity.moveRelative(entity.moveStrafing, entity.moveForward, 0.03F);
        // LogHelper.debug("+ Air motion(pos): %.4f, %.4f", entity.motionX, entity.motionZ);
        // LogHelper.debug("+---------------------------------+");
    }

}
