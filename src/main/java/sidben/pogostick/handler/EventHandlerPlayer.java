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



// TODO: deactivate the pogostick when player enter water/lava
// TODO: deactivate the pogostick when player starts flying with elytra
// TODO: deactivate the pogostick when player changes slot (? maybe only after fall, so they can quick swap to a weapon)
// TODO: deactivate the pogostick when player lands with ender pearl
// TODO: deactivate the pogostick when player changes dimension
// TODO: remove air friction when pogostick is active


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
        EntityLivingBase entity = event.player;

        // if (!entity.world.isRemote) return;
        
        
        // LogHelper.info(String.format("  temp var %d, phase %s", ModPogoStick.isBouncingTempVar, event.phase));
        

        
        /*
        if (event.phase == Phase.START && entity != null) {
            //LogHelper.info(String.format("onPlayerTickEvent() S  - forward %.4f / strafe %.4f / motionX %.4f / motionZ %.4f", entity.moveForward, entity.moveStrafing, entity.motionX, entity.motionZ));
            
            if (isEntityUsingPogoStick(entity) && entity instanceof EntityPlayer) {
            }
            
        }
        
        
        if (event.phase == Phase.END && entity != null) {
            //LogHelper.info(String.format("onPlayerTickEvent() E  - forward %.4f / strafe %.4f / motionX %.4f / motionZ %.4f", entity.moveForward, entity.moveStrafing, entity.motionX, entity.motionZ));

            
            
            if (entity.onGround) {
                BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(entity.posX, entity.getEntityBoundingBox().minY - 1.0D, entity.posZ);

                float f6 = entity.world.getBlockState(blockpos$pooledmutableblockpos).getBlock().slipperiness * 0.91F;
                float f7 = 0.16277136F / (f6 * f6 * f6);
                float f8 = entity.getAIMoveSpeed() * f7;
                
                blockpos$pooledmutableblockpos.release();
                
                //LogHelper.info(String.format("                         Block slipperness %.6f / Friction %.6f", f6, f8));
            }
            
            
            
            float strafeBase = entity.moveStrafing / 0.2F;
            float forwardBase = entity.moveForward / 0.2F;
            float g = strafeBase * strafeBase + forwardBase * forwardBase;
            float gA = MathHelper.sqrt(g);

            if (gA < 1.0F)
            {
                gA = 1.0F;
            }


            float gB = 0.09999999F / gA;
            if (!entity.onGround) {
                gB = 0.07F / gA;
            }

            float strafe = strafeBase * gB;
            float forward = forwardBase * gB;
            float g1 = MathHelper.sin(entity.rotationYaw * 0.017453292F);
            float g2 = MathHelper.cos(entity.rotationYaw * 0.017453292F);
            double gx1 = (double)(strafe * g2 - forward * g1);
            double gz1 = (double)(forward * g2 + strafe * g1);

            
            
            
            
            float strafeBase2 = entity.moveStrafing;
            float forwardBase2 = entity.moveForward;
            float h = strafeBase2 * strafeBase2 + forwardBase2 * forwardBase2;
            float hA = MathHelper.sqrt(h);

            if (hA < 1.0F)
            {
                hA = 1.0F;
            }

            
            float hB = 0.09F / hA;
            if (!entity.onGround) {
                hB = 0.07F / hA;
            }
            
            float strafe2 = strafeBase2 * hB;
            float forward2 = forwardBase2 * hB;
            float h1 = MathHelper.sin(entity.rotationYaw * 0.017453292F);
            float h2 = MathHelper.cos(entity.rotationYaw * 0.017453292F);
            double hx1 = (double)(strafe2 * h2 - forward2 * h1);
            double hz1 = (double)(forward2 * h2 + strafe2 * h1);

            
            //LogHelper.info(String.format("                         g %.6f / %b / gA %.6f / gB %.6f / g1 %.6f / g2 %.6f / gx1 %.3f / gz1 %.3f", g, (g >= 1.0E-4F), gA, gB, g1, g2, gx1, gz1));
            //LogHelper.info(String.format("                         g %.6f / gx %.6f / gz %.6f", g, gx1, gz1));
            //LogHelper.info(String.format("                         h %.6f / hx %.6f / hz %.6f", h, hx1, hz1));

            
            
            if (isEntityUsingPogoStick(entity) && entity instanceof EntityPlayer) {
                entity.motionX += gx1 - hx1;
                entity.motionZ += gz1 - hz1;
            }



            
            tracker = ModPogoStick.bounceManager.popEntity(entity);
            bouncesEntity(entity, tracker);
        }
        */
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