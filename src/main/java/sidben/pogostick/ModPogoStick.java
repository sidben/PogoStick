package sidben.pogostick;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import sidben.pogostick.handler.EventHandlerConfig;
import sidben.pogostick.main.ModConfig;
import sidben.pogostick.proxy.IProxy;
import sidben.pogostick.reference.Reference;


/*
 *  Features/magic branch goals:
 * 
 * x Allow the pogostick to be enchanted on enchanting table. Valid enchantments: Unbreaking (partially vanilla behavior)
 * 
 * x Allow the pogostick to get Mending via books (anvil) - vanilla behavior
 * x Allow the pogostick to get Frost Walker via books (anvil)
 * x Allow the pogostick to get Curse of Vanishing via books (anvil) - vanilla behavior
 * 
 * - Custom implementation of Frost Walker - freeze water where the pogostick lands
 * 
 * - Check if potion of speed works as expected
 * - Check if potion of leaping works as expected
 */


@Mod(modid = Reference.ModID, name = Reference.ModName, version = Reference.ModVersion)
public class ModPogoStick
{

    @Mod.Instance(Reference.ModID)
    public static ModPogoStick instance;

    @SidedProxy(clientSide = Reference.ClientProxyClass, serverSide = Reference.ServerProxyClass)
    public static IProxy       proxy;



    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        // Config
        ModConfig.init(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(EventHandlerConfig.class);

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
