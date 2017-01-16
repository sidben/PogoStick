package sidben.pogostick.proxy;

import net.minecraftforge.common.MinecraftForge;
import sidben.pogostick.helper.ItemsHelper;


/*
 * Base proxy class, here I initialize everything that must happen on both, server and client.
 */
public abstract class CommonProxy implements IProxy
{


    @Override
    public void pre_initialize()
    {
    }


    @Override
    public void initialize()
    {
        // Register items
        ItemsHelper.register();
    }


    @Override
    public void post_initialize()
    {
    }


}
