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
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
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
import sidben.pogostick.util.PogostickHelper;
import sidben.pogostick.util.tracker.EntityLandingTracker;



public class EventHandlerPlayer
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
                
                
                // TODO: move to pogohelper
                if (!entity.world.isRemote) {
                    ((EntityPlayer) entity).addStat(Features.statTimesBounced);
                    LogHelper.debug("    adding stat");
                }
                
            }
        
        }
        
        
        if (entity instanceof EntityLivingBase && entity.hasCapability(CapabilityPogostick.POGOSTICK, null)) 
        {
            IPogostick pogostickStatus = entity.getCapability(CapabilityPogostick.POGOSTICK, null);

            LogHelper.debug("onLivingFallEvent() - %s - %s", pogostickStatus, entity);
            LogHelper.debug("    Distance: %.6f  - Damage mult: %.4f", event.getDistance(), event.getDamageMultiplier());
            
            
            
            if (pogostickStatus.isUsingPogostick() && event.getDistance() > PogostickHelper.MIN_DISTANCE_TO_BOUNCE) {

                // Negates fall damage
                event.setDamageMultiplier(0.0F);
                
                // Bounces
                LogHelper.debug("  Original motionY " + entity.motionY);
                MinecraftForge.EVENT_BUS.register(new DelayedEventHandlerBounceEntity(1, entity, event.getDistance()));

                
                
                /*
                if (!entity.world.isRemote) {
                    ItemStack pogoStack = PogostickHelper.getHeldPogostick(entity);
                    pogoStack.damageItem(1, entity);       // TODO: dmg item based on distance fallen (max 3 dmg?)
                } else {
                    if (entity instanceof EntityPlayer) {
                        entity.playSound(SoundEvents.ENTITY_SLIME_JUMP, 0.8F, 0.8F + entity.world.rand.nextFloat() * 0.4F);                
                    }
                }
                */
                
                // ModPogoStick.bounceManager.pushEntity(entity, entity.motionY, entity.getActiveItemStack());
                
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
    
    
    
    private void bouncesEntity(EntityLivingBase entity, EntityLandingTracker tracker) {
        if (tracker != EntityLandingTracker.EMPTY && tracker.getBounceMotionY() > 0.0D) {
            //entity.fallDistance = 0.0F;
            entity.motionY = tracker.getBounceMotionY();
            entity.isAirBorne = true;
            entity.onGround = false;

            if (!entity.world.isRemote) {
                tracker.getItemStack().damageItem(1, entity);       // TODO: dmg item based on distance fallen (max 3 dmg?)
            } else {
                entity.playSound(SoundEvents.ENTITY_SLIME_JUMP, 0.8F, 0.8F + entity.world.rand.nextFloat() * 0.4F);                
            }
        }
    }
    
}