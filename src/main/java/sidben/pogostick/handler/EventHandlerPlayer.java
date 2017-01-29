package sidben.pogostick.handler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.capability.IPogostick;
import sidben.pogostick.main.Features;
import sidben.pogostick.util.LogHelper;
import sidben.pogostick.util.PogostickHelper;



// TODO: deactivate the pogostick when player enter water/lava
// TODO: deactivate the pogostick when player starts flying with elytra
// TODO: deactivate the pogostick when player changes slot (? maybe only after fall, so they can quick swap to a weapon)
// TODO: deactivate the pogostick when player lands with ender pearl
// TODO: deactivate the pogostick when player changes dimension


public class EventHandlerPlayer
{


    @SubscribeEvent
    public void onPlayerFallEvent(PlayerFlyableFallEvent event)
    {
        // Fall on creative mode
        this.handleEntityFall(event);
    }

    @SubscribeEvent
    public void onLivingFallEvent(LivingFallEvent event)
    {
        // Fall on survival mode
        this.handleEntityFall(event);
    }
    
    
    
    private void handleEntityFall(LivingEvent event)
    {
        if (event.getEntityLiving() == null) return;

        EntityLivingBase entity = event.getEntityLiving();
        float distance = 0.0F; 
        float damageMultiplier = 0.0F;
        

        // Find the correct data from valid events
        if (event instanceof LivingFallEvent) {
            distance = ((LivingFallEvent)event).getDistance();
            damageMultiplier = ((LivingFallEvent)event).getDamageMultiplier();
            
        } else if (event instanceof PlayerFlyableFallEvent) {
            distance = ((PlayerFlyableFallEvent)event).getDistance();
            damageMultiplier = ((PlayerFlyableFallEvent)event).getMultiplier();
            
        }

        
        // Handles the fall to negate damage and schedule the bounce event
        if (entity.hasCapability(CapabilityPogostick.POGOSTICK, null) && distance > 0) {
            final IPogostick pogostickStatus = entity.getCapability(CapabilityPogostick.POGOSTICK, null);

            LogHelper.debug("handleEntityFall() - %s - %s", pogostickStatus, entity);
            LogHelper.debug("    Distance: %.6f  - Damage mult: %.4f", distance, damageMultiplier);

            if (pogostickStatus.isUsingPogostick() && distance > PogostickHelper.MIN_DISTANCE_TO_BOUNCE) {

                // Negates fall damage
                if (event instanceof LivingFallEvent) {
                    ((LivingFallEvent)event).setDamageMultiplier(0.0F);
                } else if (event instanceof PlayerFlyableFallEvent) {
                    ((PlayerFlyableFallEvent)event).setMultiplier(0.0F);
                }
                
                // TODO: move to pogohelper
                if (!entity.world.isRemote && entity instanceof EntityPlayer) {
                    ((EntityPlayer) entity).addStat(Features.statTimesBounced);
                    LogHelper.debug("    adding stat");
                }

                // Bounces
                MinecraftForge.EVENT_BUS.register(new DelayedEventHandlerBounceEntity(1, entity, distance));

            }

        }
        
    }
    



    @SubscribeEvent
    public void onLivingUpdateEvent(LivingUpdateEvent event)
    {
    }



    @SubscribeEvent
    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
    {
        final EntityLivingBase entity = event.player;


        /*
         * 
         * if (entity.onGround) {
         * BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(entity.posX, entity.getEntityBoundingBox().minY - 1.0D, entity.posZ);
         * 
         * float f6 = entity.world.getBlockState(blockpos$pooledmutableblockpos).getBlock().slipperiness * 0.91F;
         * float f7 = 0.16277136F / (f6 * f6 * f6);
         * float f8 = entity.getAIMoveSpeed() * f7;
         * 
         * blockpos$pooledmutableblockpos.release();
         * 
         * //LogHelper.info(String.format("                         Block slipperness %.6f / Friction %.6f", f6, f8));
         * }
         * 
         * 
         */



        if (event.phase == Phase.END && entity != null && entity.world.isRemote) {
            PogostickHelper.removeAirFrictionIfUsingPogostick(entity);
        }

    }


}