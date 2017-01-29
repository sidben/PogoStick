package sidben.pogostick.util;

import javax.annotation.Nonnull;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.enchantment.EnchantmentSpring;
import sidben.pogostick.main.Features;


public class PogostickHelper
{

    private final static float MIN_MOTION_UP                    = 0.5F;
    private final static float MAX_MOTION_UP                    = 10F;
    private final static float MIN_DISTANCE_TO_DAMAGE_POGOSTICK = 3F;
    private final static int   MAX_DAMAGE_TO_POGOSTICK          = 10;



    /**
     * Returns true if the given entity can activate the pogostick.
     *
     * This method should be used in {@link sidben.pogostick.item.ItemPogoStick ItemPogoStick} to decide the item action.
     */
    public static boolean canEntityActivatePogostick(@Nonnull EntityLivingBase entity)
    {
        final boolean isHoldingPogostick = isPogoStack(entity.getHeldItemMainhand()) || isPogoStack(entity.getHeldItemOffhand());

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
     * Is this ItemStack a valid pogostick?
     */
    public static boolean isPogoStack(ItemStack stack)
    {
        return stack.getItem() == Features.Items.POGOSTICK;
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
    public static void processEntityLandingWithPogostick(@Nonnull EntityLivingBase entity, ItemStack pogoStack, float fallDistance)
    {
        if (!entity.hasCapability(CapabilityPogostick.POGOSTICK, null) 
                || !entity.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick()) { return; }

        // Have a pogostick right now?
        if (pogoStack.getItem() != Features.Items.POGOSTICK) { return; }

        // Mark that the entity bounced once. This is not synced via packets, but this method is called by
        // client and server so I assume they will be synced.
        entity.getCapability(CapabilityPogostick.POGOSTICK, null).markBounced();

        if (entity.world.isRemote) {
            final int springLevel = EnchantmentHelper.getEnchantmentLevel(Features.Enchantments.SPRING, pogoStack);
            PogostickHelper.updateVerticalMotion(entity, fallDistance, springLevel);
            entity.playSound(SoundEvents.ENTITY_SLIME_JUMP, 0.8F, 0.8F + entity.world.rand.nextFloat() * 0.4F);

        } else {
            // Item damage
            final int damageAmount = PogostickHelper.calculateItemDamage(fallDistance);
            LogHelper.trace("A fall from %.4f will cause %d damage to the pogostick", fallDistance, damageAmount);
            if (damageAmount > 0) {
                pogoStack.damageItem(damageAmount, entity); // TODO: if the damage breaks the pogostick, the remaining damage goes to the player (secret achievement?)
            }

            // Stats
            if (entity instanceof EntityPlayer) {
                ((EntityPlayer) entity).addStat(Features.Stats.TIMES_BOUNCED);
            }

        }
    }



    private static void updateVerticalMotion(@Nonnull EntityLivingBase entity, float fallDistance, int springLevel)
    {
        float minMotion = MIN_MOTION_UP;
        float accelModifier = 0.5F;
        final float springBonus = EnchantmentSpring.getBouncingModifier(springLevel);
        final float distanceLimiter = 2.0F * Math.max(springBonus, 1F);
        final float adjustedFallDistance = Math.min(distanceLimiter, fallDistance);


        if (entity instanceof EntityPlayer) {
            if (entity.isSneaking()) {
                // If the player is sneaking the bounce limit is much lower
                LogHelper.debug("  Applying sneaking modifier");
                accelModifier = 0.1F;

            } else if (entity.isSprinting()) {
                // If the player is sprinting the bounce height limit is lower, but accelerates faster
                // (horizontal speed should be increased naturally)
                LogHelper.debug("  Applying sprinting modifier");
                minMotion = 0.7F;
                accelModifier = 0.25F;

            }
        }


        // LogHelper.debug(">>> Fall distance %.4f (from %.4f)", adjustedFallDistance, fallDistance);
        // LogHelper.debug(">>> Old motionY %.4f", entity.motionY);

        final double newSpeed = minMotion + Math.log(Math.max(adjustedFallDistance, 1)) * accelModifier;
        entity.motionY = MathHelper.clamp(newSpeed, MIN_MOTION_UP, MAX_MOTION_UP);

        // LogHelper.debug(">>> New motionY %.4f (min %.4f, max %.4f)", newSpeed, MIN_MOTION_UP, MAX_MOTION_UP);
        // LogHelper.debug(" ");

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
                || !entity.getCapability(CapabilityPogostick.POGOSTICK, null).bouncedOnce() 
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



    /**
     * Try to apply the Frost Walker effect on the block below the given entity.
     * This method DO NOT perform capability validations.
     */
    public static void tryFrostBounce(EntityLivingBase entity)
    {
        if (entity.world.isRemote) { return; }


        // TODO: BUG - not working well after branch rebase to get bouncing implementation

        final ItemStack playerItemStack = entity.getHeldItemMainhand();

        if (isPogoStack(playerItemStack)) {
            final int freezeLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FROST_WALKER, playerItemStack);
            // LogHelper.trace("$$ Testing for frost walker - level: %d", freezeLevel);


            if (freezeLevel > 0) {
                final World world = entity.world;
                final int checkingDistance = 2;
                boolean effectApplied = false;

                // Check up to 2 blocks below the player
                for (int i = checkingDistance; i >= 0; i--) {
                    final BlockPos blockpos = new BlockPos(entity).down(i);
                    final IBlockState blockstateBelow = world.getBlockState(blockpos.down());
                    final IBlockState blockstate = world.getBlockState(blockpos);

                    LogHelper.trace("  Testing #%d, %s == %s, %s == %s (player Y %.2f), already applied %s", i, blockpos, blockstate.getBlock().getLocalizedName(), blockpos.down(),
                            blockstateBelow.getBlock().getLocalizedName(), entity.posY, effectApplied);
                    if (effectApplied) {
                        break;
                    }


                    if (blockstate.getMaterial() == Material.AIR && (blockstateBelow.getMaterial() == Material.WATER && blockstateBelow.getValue(BlockLiquid.LEVEL).equals(0))) {

                        LogHelper.trace("$$  Freeeeeze!");
                        LogHelper.trace("  Accepted %s - %s, attempt %d", blockpos, blockstateBelow.getBlock().getLocalizedName(), i);
                        // TODO: achievement

                        /**
                         * Tweaked implementation of {@link net.minecraft.enchantment.EnchantmentFrostWalker#freezeNearby()}
                         */
                        final double f = Math.min(16, freezeLevel + 2);
                        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(0, 0, 0);


                        LogHelper.debug("  %d, f = %.2f", freezeLevel, f);

                        for (final BlockPos.MutableBlockPos blockpos$mutableblockpos1 : BlockPos.getAllInBoxMutable(blockpos.add((-f), -1.0D, (-f)), blockpos.add(f, -1.0D, f))) {
                            if (blockpos$mutableblockpos1.distanceSqToCenter(entity.posX, blockpos.getY() + 1 + i, entity.posZ) <= f * f) {
                                // if (blockpos$mutableblockpos1.distanceSqToCenter(entity.posX, entity.posY, entity.posZ) <= f * f) {
                                LogHelper.debug("    +-- ", freezeLevel, f);

                                blockpos$mutableblockpos.setPos(blockpos$mutableblockpos1.getX(), blockpos$mutableblockpos1.getY() + 1, blockpos$mutableblockpos1.getZ());
                                final IBlockState iblockstate = world.getBlockState(blockpos$mutableblockpos);
                                /*
                                 * LogHelper.debug("  -- " + blockpos$mutableblockpos);
                                 * LogHelper.debug("     " + iblockstate);
                                 * LogHelper.debug("     " + (iblockstate.getMaterial()== Material.AIR));
                                 */


                                if (iblockstate.getMaterial() == Material.AIR) {
                                    final IBlockState iblockstate1 = world.getBlockState(blockpos$mutableblockpos1);

                                    if (iblockstate1.getMaterial() == Material.WATER && iblockstate1.getValue(BlockLiquid.LEVEL).intValue() == 0
                                            && world.mayPlace(Blocks.FROSTED_ICE, blockpos$mutableblockpos1, false, EnumFacing.DOWN, (Entity) null)) {
                                        world.setBlockState(blockpos$mutableblockpos1, Blocks.FROSTED_ICE.getDefaultState());
                                        world.scheduleUpdate(blockpos$mutableblockpos1.toImmutable(), Blocks.FROSTED_ICE, MathHelper.getInt(entity.getRNG(), 60, 120));
                                        effectApplied = true;
                                    }
                                }
                            }

                        }

                    }

                }

            }

        }

    }


}
