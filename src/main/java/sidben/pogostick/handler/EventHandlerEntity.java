package sidben.pogostick.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.capability.IPogostick;
import sidben.pogostick.main.Features;
import sidben.pogostick.network.NetworkManager;
import sidben.pogostick.util.LogHelper;
import sidben.pogostick.util.PogostickHelper;



// TODO: deactivate the pogostick when player lands with ender pearl
// TODO: deactivate the pogostick when player changes dimension


public class EventHandlerEntity
{


    @SubscribeEvent
    public static void onPlayerFallEvent(PlayerFlyableFallEvent event)
    {
        // Fall on creative mode
        handleEntityFall(event);
    }

    @SubscribeEvent
    public static void onLivingFallEvent(LivingFallEvent event)
    {
        // Fall on survival mode
        handleEntityFall(event);
    }



    private static void handleEntityFall(LivingEvent event)
    {
        if (event.getEntityLiving() == null) { return; }


        final EntityLivingBase entity = event.getEntityLiving();
        float distance = 0.0F;
        // Find the correct data from valid events
        if (event instanceof LivingFallEvent) {
            distance = ((LivingFallEvent) event).getDistance();
            // damageMultiplier = ((LivingFallEvent) event).getDamageMultiplier();

        } else if (event instanceof PlayerFlyableFallEvent) {
            distance = ((PlayerFlyableFallEvent) event).getDistance();
            // damageMultiplier = ((PlayerFlyableFallEvent) event).getMultiplier();

        }


        // Handles the fall to negate damage and schedule the bounce event
        if (entity.hasCapability(CapabilityPogostick.POGOSTICK, null) && entity.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick() && distance > 0) {
            final IPogostick pogostickStatus = entity.getCapability(CapabilityPogostick.POGOSTICK, null);


            // Check if the player is holding the pogostick right now
            ItemStack pogoStack = ItemStack.EMPTY;
            if (entity.getHeldItemMainhand().getItem() == Features.Items.POGOSTICK) {
                pogoStack = entity.getHeldItemMainhand();
            } else if (entity.getHeldItemOffhand().getItem() == Features.Items.POGOSTICK) {
                pogoStack = entity.getHeldItemOffhand();
            }


            // Check if the bounce is valid
            if (distance > PogostickHelper.MIN_DISTANCE_TO_BOUNCE && pogoStack != ItemStack.EMPTY) {

                // Negates fall damage (client and server)
                if (event instanceof LivingFallEvent) {
                    ((LivingFallEvent) event).setDamageMultiplier(0.0F);
                } else if (event instanceof PlayerFlyableFallEvent) {
                    ((PlayerFlyableFallEvent) event).setMultiplier(0.0F);
                }

                // Bounces (client and server)
                MinecraftForge.EVENT_BUS.register(new DelayedEventHandlerBounceEntity(1, entity, pogoStack, distance));

            } else {

                // Invalid distance, disables pogostick (server-only, just to avoid chance of desync)
                if (!event.getEntityLiving().world.isRemote) {
                    pogostickStatus.updatePogostickUsage(false);
                    if (entity instanceof EntityPlayer) {
                        NetworkManager.sendPogoStatusUpdate(false, (EntityPlayer) entity);
                    }
                }


            }

        }

    }



    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        /*
         * if (event.getEntity() instanceof EntityPlayer) {
         * LogHelper.debug("onEntityJoinWorld()");
         * LogHelper.debug("    Entity %s, UUID %s, Partial tick %f", event.getEntity(), event.getEntity().getUniqueID(), Minecraft.getMinecraft().getRenderPartialTicks());
         * }
         */

        // Sync the capabilities (necessary if the player activated the pogostick and logged out mid-air)
        final Entity entity = event.getEntity();
        if (entity instanceof EntityPlayerMP && entity.hasCapability(CapabilityPogostick.POGOSTICK, null)) {

            // OBS: client may not be there yet
            // ModPogoStick.instance.getNetworkManager().sendPogoStatusUpdate(entity.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick(), (EntityPlayerMP) entity);

            // TODO: find better way to sync a client on world load (why capabilities don't sync on load?)
            // NOTE: The network thread runs on another thread, check addScheduledTask


            // Delay an update event, so the client have time to load the player
            MinecraftForge.EVENT_BUS.register(new DelayedEventHandlerUpdateNewPlayer(2, entity));
        }

    }



    @SubscribeEvent
    public static void onLivingUpdateEvent(LivingUpdateEvent event)
    {
        if (event.getEntityLiving() == null) { return; }
        if (event.getEntityLiving().world.isRemote) { return; }


        final EntityLivingBase entity = event.getEntityLiving();

        if (entity.hasCapability(CapabilityPogostick.POGOSTICK, null)) {
            final IPogostick pogostickStatus = entity.getCapability(CapabilityPogostick.POGOSTICK, null);

            // Disables the pogostick if needed.
            if (pogostickStatus.isUsingPogostick() && !PogostickHelper.canEntityKeepUsingPogostick(entity)) {
                LogHelper.trace("Disabling pogostick for entity %s", entity);
                pogostickStatus.updatePogostickUsage(false);

                // If it's a player, send the status update to client
                if (entity instanceof EntityPlayer) {
                    NetworkManager.sendPogoStatusUpdate(false, (EntityPlayer) entity);
                }

            } else {
                // Special rule for Frost Walking. Only test when the player is falling
                // OBS: motionY on ground will be -0.07840 due to 'gravity', but I can just check for zero since I also check onGround.
                if (!entity.onGround && entity.motionY < 0F && entity.hasCapability(CapabilityPogostick.POGOSTICK, null) && entity.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick()) {
                    PogostickHelper.tryfrostBounce(entity);
                }
                
            }

        }

    }



    @SubscribeEvent
    public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
    {
        final EntityLivingBase entity = event.player;

        if (event.phase == Phase.END && entity != null && entity.world.isRemote) {
            PogostickHelper.removeAirFrictionIfUsingPogostick(entity);
        }
    }



}