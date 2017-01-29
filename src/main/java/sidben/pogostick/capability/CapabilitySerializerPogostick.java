package sidben.pogostick.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;


public class CapabilitySerializerPogostick implements ICapabilitySerializable<NBTTagCompound>
{

    private static String    NBT_ACTIVE = "pogostickActive";

    private final IPogostick _capabilityInstance;



    public CapabilitySerializerPogostick() {
        this._capabilityInstance = new CapabilityHandlerPogostick();
    }



    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityPogostick.POGOSTICK;
    }


    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityPogostick.POGOSTICK) { return (T) this._capabilityInstance; }
        return null;
    }


    @Override
    public NBTTagCompound serializeNBT()
    {
        final NBTTagCompound nbtCompound = new NBTTagCompound();
        nbtCompound.setBoolean(NBT_ACTIVE, this._capabilityInstance.isUsingPogostick());
        return nbtCompound;
    }


    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        final NBTTagCompound nbtCompound = nbt;
        this._capabilityInstance.updatePogostickUsage(nbtCompound.getBoolean(NBT_ACTIVE));
    }

}
