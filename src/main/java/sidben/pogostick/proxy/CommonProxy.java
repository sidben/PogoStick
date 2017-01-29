package sidben.pogostick.proxy;

import net.minecraftforge.common.MinecraftForge;
import sidben.pogostick.handler.EventHandlerCapability;
import sidben.pogostick.handler.EventHandlerEntity;
import sidben.pogostick.main.Features;
import sidben.pogostick.network.NetworkManager;


/*
 * Base proxy class, here I initialize everything that must happen on both, server and client.
 */
public abstract class CommonProxy implements IProxy
{


    @Override
    public void pre_initialize()
    {
        Features.registerItems();
        Features.registerCapabilities();

        // Network messages
        NetworkManager.registerMessages();
    }


    @Override
    public void initialize()
    {
        // Event Handlers
        MinecraftForge.EVENT_BUS.register(EventHandlerEntity.class);
        MinecraftForge.EVENT_BUS.register(EventHandlerCapability.class);

        // Recipes
        Features.registerRecipes();
        Features.registerStats();
    }


    @Override
    public void post_initialize()
    {
    }


}
