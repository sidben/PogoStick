package sidben.pogostick.handler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sidben.pogostick.capability.CapabilitySerializerPogostick;
import sidben.pogostick.reference.Reference;
import sidben.pogostick.util.LogHelper;


public class EventHandlerCapability
{

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent.Entity event)
    {

        // TODO: Allow zombies / baby zombies to use pogosticks
        if (event.getEntity() instanceof EntityPlayer) {
            LogHelper.trace("Adding capabilities to player");

            event.addCapability(new ResourceLocation(Reference.ModID, "pogo"), new CapabilitySerializerPogostick((EntityLivingBase) event.getEntity()));
        }

    }


}
