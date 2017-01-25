package sidben.pogostick.proxy;

import net.minecraftforge.common.MinecraftForge;
import sidben.pogostick.ModPogoStick;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.handler.EventHandlerCapability;
import sidben.pogostick.handler.EventHandlerEntity;
import sidben.pogostick.handler.PlayerEventHandler;
import sidben.pogostick.main.Features;


/*
 * Base proxy class, here I initialize everything that must happen on both, server and client.
 */
public abstract class CommonProxy implements IProxy
{


    @Override
    public void pre_initialize()
    {
        // Register items
        Features.registerItems();
        
        // Network messages
        ModPogoStick.instance.getNetworkManager().registerMessages();

        // Capabilities
        CapabilityPogostick.register();
    }


    @Override
    public void initialize()
    {
        // Event Handlers
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        MinecraftForge.EVENT_BUS.register(new EventHandlerCapability());
        MinecraftForge.EVENT_BUS.register(new EventHandlerEntity());
    }


    @Override
    public void post_initialize()
    {
    }


}
