package sidben.pogostick.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import sidben.pogostick.ModPogoStick;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.util.LogHelper;


public class DelayedEventHandlerLivingUpdate extends DelayedEventHandler<LivingUpdateEvent>
{

    public DelayedEventHandlerLivingUpdate(int ticksToWait, Entity entityAffected)  
    {
        super(ticksToWait, entityAffected, LivingUpdateEvent.class);
    }

    
    @Override
    public void execute(LivingUpdateEvent event)
    {
        LogHelper.info("DelayedEventHandlerLivingUpdate.execute()");
        LogHelper.info(">   " + event.getEntityLiving());
        LogHelper.info(">   " + event.getEntityLiving().hasCapability(CapabilityPogostick.POGOSTICK, null));

        // Sync clients on load
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayerMP && entity.hasCapability(CapabilityPogostick.POGOSTICK, null)) 
        {
            ModPogoStick.instance.getNetworkManager().sendPogoStatusUpdate(entity.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick(), (EntityPlayer) entity);
        }

    }

}
