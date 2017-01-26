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
        
        if (entity instanceof EntityPlayer) {
            /*
            LogHelper.info("onPlayerFallEvent()");
            LogHelper.info("    distance:      " + event.getDistance());
            LogHelper.info("    dmg mult:      " + event.getMultiplier());
            LogHelper.info("    player:        " + event.getEntityPlayer());
            LogHelper.info("    motion Y:      " + event.getEntityLiving().motionY);
            */
            /*
            LogHelper.info("    main hand:     " + event.getEntityPlayer().getHeldItemMainhand());
            LogHelper.info("    off hand:      " + event.getEntityPlayer().getHeldItemOffhand());
            LogHelper.info("    hand active:   " + event.getEntityPlayer().isHandActive());                 // <---
            LogHelper.info("    active item:   " + event.getEntityPlayer().getActiveItemStack());           // <---
            */
        }
        

        /*
        // Fall on creative mode
        if (isEntityUsingPogoStick(entity) && entity instanceof EntityPlayer) {
            LogHelper.info("** Player that can fly was falling, but landed using a pogo stick. **");
            
            // Negates fall damage
            event.setMultiplier(0.0F);
            
            // Bounces
            ModPogoStick.bounceManager.pushEntity(entity, entity.motionY, entity.getActiveItemStack());
        }
        */
    }


    
    @SubscribeEvent
    public void onLivingFallEvent(LivingFallEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();

        if (entity instanceof EntityPlayer) {
            /*
            LogHelper.info("onLivingFallEvent()");
            LogHelper.info("    is remote:     " + entity.world.isRemote);
            LogHelper.info("    using pogo:    " + isEntityUsingPogoStick(entity));
            LogHelper.info("    distance:      " + event.getDistance());
            LogHelper.info("    dmg mult:      " + event.getDamageMultiplier());
            LogHelper.info("    entity:        " + event.getEntityLiving());
            LogHelper.info("    motion Y:      " + event.getEntityLiving().motionY);
            */
            
            /*
            LogHelper.info("    main hand:     " + event.getEntityLiving().getHeldItemMainhand());
            LogHelper.info("    off hand:      " + event.getEntityLiving().getHeldItemOffhand());
            LogHelper.info("    hand active:   " + event.getEntityLiving().isHandActive());                 // <---
            LogHelper.info("    active item:   " + event.getEntityLiving().getActiveItemStack());           // <---
            LogHelper.info("    entity player? " + (event.getEntityLiving() instanceof EntityPlayer));
            */
        }
        

        
        
        /*
        if (isEntityUsingPogoStick(entity) && entity instanceof EntityPlayer) {
            LogHelper.info("** Player was falling, but landed using a pogo stick. **");
            
            // Negates fall damage
            event.setDamageMultiplier(0.0F);
            
            // Bounces
            ModPogoStick.bounceManager.pushEntity(entity, entity.motionY, entity.getActiveItemStack());
        }
        */


        /*
        LogHelper.info("onLivingFallEvent()");
        LogHelper.info("    main hand:     " + event.getEntityLiving().getHeldItemMainhand());
        LogHelper.info("    off hand:      " + event.getEntityLiving().getHeldItemOffhand());
        LogHelper.info("    active item:   " + event.getEntityLiving().getActiveItemStack());           // <---
        */


        /*
        if (ModPogoStick.isBouncingTempVar < 0) {
            
            // Negates fall damage
            event.setDamageMultiplier(0.0F);

            if (!entity.world.isRemote) {
                LogHelper.info("  damaging item");
                entity.getHeldItemMainhand().damageItem(1, entity);
            }
        
        }
        */
        
        
        
        if (entity.hasCapability(CapabilityPogostick.POGOSTICK, null)) {
            LogHelper.info("+   PlayerFalling (with pogostick capability)");
            LogHelper.info("+     " + entity);
            
            IPogostick pogostickStatus = entity.getCapability(CapabilityPogostick.POGOSTICK, null);
            LogHelper.info("+     isActive: " + pogostickStatus.isUsingPogostick());
        }
        


        
    }
    

    @SubscribeEvent
    public void onLivingUpdateEvent(LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();

        if (entity instanceof EntityPlayerMP) 
        {
            // LogHelper.info(String.format("onLivingUpdateEvent() - Tick %f - %s", Minecraft.getMinecraft().getRenderPartialTicks(), entity));
        }
        
        
        if (isEntityUsingPogoStick(entity) && entity instanceof EntityPlayerSP) {
            //LogHelper.info(String.format("onLivingUpdateEvent()  - forward %.4f (%.2f) / strafe %.4f / motionX %.4f / motionZ %.4f", entity.moveForward, ((EntityPlayerSP)entity).movementInput.moveForward, entity.moveStrafing, entity.motionX, entity.motionZ));
        }
        
    }
    
 
    
    
    @SubscribeEvent
    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
    {
        EntityLivingBase entity = event.player;
        EntityLandingTracker tracker = EntityLandingTracker.EMPTY;

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
    
    
    
    
    
    private boolean isEntityUsingPogoStick(EntityLivingBase entity) {
        return entity != null && entity.isHandActive() && entity.getActiveItemStack().getItem() == Features.pogoStick;
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