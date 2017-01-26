package sidben.pogostick.capability;

import java.util.concurrent.Callable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import sidben.pogostick.util.LogHelper;


public class CapabilityPogostick
{

    @CapabilityInject(IPogostick.class)
    public static Capability<IPogostick> POGOSTICK = null;



    public static void register()
    {
        LogHelper.debug(" ** Registering capabilities");

        CapabilityManager.INSTANCE.register(IPogostick.class, new CapabilityPogostick.StoragePogostick(), new Callable<IPogostick>()
        {
            @Override
            public IPogostick call() throws Exception
            {
                return new CapabilityHandlerPogostick();
            }
        });
    }



    static class StoragePogostick implements IStorage<IPogostick>
    {

        @Override
        public NBTBase writeNBT(Capability<IPogostick> capability, IPogostick instance, EnumFacing side)
        {
            LogHelper.debug(" @@ write NBT");
            return null;
        }

        @Override
        public void readNBT(Capability<IPogostick> capability, IPogostick instance, EnumFacing side, NBTBase nbt)
        {
            LogHelper.debug(" @@ Read NBT");
        }

    }


}
