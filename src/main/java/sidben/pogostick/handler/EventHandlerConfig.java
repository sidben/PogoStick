package sidben.pogostick.handler;

import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sidben.pogostick.main.ModConfig;
import sidben.pogostick.reference.Reference;


public class EventHandlerConfig
{

    @SubscribeEvent
    public static void onConfigurationChangedEvent(OnConfigChangedEvent event)
    {
        if (event.getModID().equalsIgnoreCase(Reference.ModID)) {
            ModConfig.refreshConfig();
        }
    }

}
