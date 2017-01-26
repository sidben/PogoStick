package sidben.pogostick.handler;

import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import sidben.pogostick.ModPogoStick;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.capability.IPogostick;
import sidben.pogostick.main.Features;
import sidben.pogostick.util.LogHelper;
import sidben.pogostick.util.tracker.EntityLandingTracker;



public class PlayerEventHandler
{
    


    @SubscribeEvent
    public void onPlayerFallEvent(PlayerFlyableFallEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        
        // Fall on creative mode
        if (entity instanceof EntityPlayer) {
            LogHelper.debug("onPlayerFallEvent()");
        }
        
    }


    
    @SubscribeEvent
    public void onLivingFallEvent(LivingFallEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();

        // Fall on survival mode
        if (entity instanceof EntityPlayer) {
            LogHelper.debug("onLivingFallEvent()");

            
            if (entity.hasCapability(CapabilityPogostick.POGOSTICK, null)) {
                IPogostick pogostickStatus = entity.getCapability(CapabilityPogostick.POGOSTICK, null);
                LogHelper.debug("    with pogostick capability - %s", pogostickStatus);
                
                
                if (!entity.world.isRemote) {
                    ((EntityPlayer) entity).addStat(Features.statTimesBounced);
                    LogHelper.debug("    adding stat");
                }
                
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
    }    
    
    
    
}