package sidben.pogostick.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sidben.pogostick.ModPogoStick;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.util.LogHelper;

public class EventHandlerEntity
{

    
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) 
    {
        if (event.getEntity() instanceof EntityPlayer) {
            LogHelper.info("onEntityJoinWorld() " + event.getEntity());
            LogHelper.info("    UUID " + event.getEntity().getUniqueID());
            LogHelper.info("    Tick " + Minecraft.getMinecraft().getRenderPartialTicks());
        }

        // Sync the capabilities
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayerMP && entity.hasCapability(CapabilityPogostick.POGOSTICK, null)) {
            
            LogHelper.info("    onGround: " + entity.onGround);
            
            // OBS: client may not be there yet
            // ModPogoStick.instance.getNetworkManager().sendPogoStatusUpdate(entity.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick(), (EntityPlayerMP) entity);
            
            // TODO: find better way to sync a client on world load (why capabilities don't sync on load?)
            // idea - use the onEntityJoinWorld() on the client to request an update to the server
            MinecraftForge.EVENT_BUS.register(new DelayedEventHandlerLivingUpdate(2, entity));
        }
        
    }
    
}