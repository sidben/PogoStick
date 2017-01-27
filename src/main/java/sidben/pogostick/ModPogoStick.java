package sidben.pogostick;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import sidben.pogostick.handler.ConfigurationHandler;
import sidben.pogostick.network.NetworkManager;
import sidben.pogostick.proxy.IProxy;
import sidben.pogostick.reference.Reference;


@Mod(modid = Reference.ModID, name = Reference.ModName, version = Reference.ModVersion)
public class ModPogoStick
{

    @Mod.Instance(Reference.ModID)
    public static ModPogoStick  instance;

    @SidedProxy(clientSide = Reference.ClientProxyClass, serverSide = Reference.ServerProxyClass)
    public static IProxy        proxy;

    private NetworkManager      _networkManager;



    public NetworkManager getNetworkManager()
    {
        if (_networkManager == null) {
            _networkManager = new NetworkManager();
        }
        return _networkManager;
    }



    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        // Loads config
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());

        // Sided pre-initialization
        proxy.pre_initialize();
    }


    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        // Sided initializations
        proxy.initialize();
    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        // Sided post-initialization
        proxy.post_initialize();
    }

}
