package sidben.pogostick.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.util.LogHelper;


public class EventHandlerEntity
{


    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        /*
        if (event.getEntity() instanceof EntityPlayer) {
            LogHelper.debug("onEntityJoinWorld()");
            LogHelper.debug("    Entity %s, UUID %s, Partial tick %f", event.getEntity(), event.getEntity().getUniqueID(), Minecraft.getMinecraft().getRenderPartialTicks());
        }
        */

        // Sync the capabilities
        final Entity entity = event.getEntity();
        if (entity instanceof EntityPlayerMP && entity.hasCapability(CapabilityPogostick.POGOSTICK, null)) {

            // OBS: client may not be there yet
            // ModPogoStick.instance.getNetworkManager().sendPogoStatusUpdate(entity.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick(), (EntityPlayerMP) entity);

            // TODO: find better way to sync a client on world load (why capabilities don't sync on load?)
            // idea - use the onEntityJoinWorld() on the client to request an update to the server
            
            // Delay an update event, so the client have time to load the player
            MinecraftForge.EVENT_BUS.register(new DelayedEventHandlerUpdateNewPlayer(2, entity));
        }

    }

}
