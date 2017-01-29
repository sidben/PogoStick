package sidben.pogostick.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sidben.pogostick.capability.CapabilitySerializerPogostick;
import sidben.pogostick.main.ModConfig;
import sidben.pogostick.reference.Reference;


public class EventHandlerCapability
{

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent.Entity event)
    {

        // TODO: Allow zombies / baby zombies to use pogosticks
        if (event.getEntity() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(Reference.ModID, "pogo"), new CapabilitySerializerPogostick(ModConfig.DEFAULT_DISTANCE_LIMITER_FOR_PLAYERS, true));
        }

    }

}
