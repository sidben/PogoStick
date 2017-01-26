package sidben.pogostick.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sidben.pogostick.ModPogoStick;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.util.LogHelper;
import sidben.pogostick.util.PogostickHelper;


public class DelayedEventHandlerUpdateNewPlayer extends DelayedEventHandler<LivingUpdateEvent>
{

    public DelayedEventHandlerUpdateNewPlayer(int ticksToWait, Entity entityAffected) {
        super(ticksToWait, entityAffected, LivingUpdateEvent.class);
    }


    @Override
    public void execute(LivingUpdateEvent event)
    {
        LogHelper.debug("DelayedEventHandlerUpdateNewPlayer.execute()");
        LogHelper.debug(">   entity %s has capability: %s", event.getEntityLiving(), event.getEntityLiving().hasCapability(CapabilityPogostick.POGOSTICK, null));

        // Sync clients on load
        final EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayerMP && entity.hasCapability(CapabilityPogostick.POGOSTICK, null)) {
            // Updates the server and send to client
            boolean isActiveNow = entity.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick();
            boolean canBeActive = PogostickHelper.canEntityActivatePogostick(entity);

            entity.getCapability(CapabilityPogostick.POGOSTICK, null).updatePogostickUsage(isActiveNow && canBeActive);
            ModPogoStick.instance.getNetworkManager().sendPogoStatusUpdate(isActiveNow && canBeActive, (EntityPlayer) entity);
        }

    }


    
    @Override
    @SubscribeEvent
    public void onEvent(LivingUpdateEvent event)
    {
        super.onEventInternal(event);
    }

}
