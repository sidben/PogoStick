package sidben.pogostick.capability;

import javax.annotation.Nonnull;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import sidben.pogostick.util.LogHelper;


public class CapabilitySerializerPogostick implements ICapabilitySerializable<NBTTagCompound>
{

    private static String          NBT_ACTIVE = "pogostickActive";

    private final EntityLivingBase _entity;
    private final IPogostick       _capabilityInstance;



    public CapabilitySerializerPogostick(@Nonnull EntityLivingBase entity) {
        this._entity = entity;
        this._capabilityInstance = new CapabilityHandlerPogostick();
    }



    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        LogHelper.debug(" ** hasCapability [v2] - %s", this._entity);

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
        LogHelper.debug(" ** serializeNBT [v2] - %s", this._entity);

        final NBTTagCompound nbtCompound = new NBTTagCompound();
        nbtCompound.setBoolean(NBT_ACTIVE, this._capabilityInstance.isUsingPogostick());
        return nbtCompound;
    }


    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        LogHelper.info(" ** deserializeNBT [v2] - %s", this._entity);

        final NBTTagCompound nbtCompound = nbt;
        this._capabilityInstance.updatePogostickUsage(nbtCompound.getBoolean(NBT_ACTIVE));
    }

}
