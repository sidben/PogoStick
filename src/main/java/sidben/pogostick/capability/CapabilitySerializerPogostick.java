package sidben.pogostick.capability;

import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import sidben.pogostick.ModPogoStick;
import sidben.pogostick.util.LogHelper;


public class CapabilitySerializerPogostick implements ICapabilitySerializable<NBTTagCompound>
{

    private static String NBT_ACTIVE = "pogostickActive";

    private final EntityLivingBase _entity;
    private IPogostick _capabilityInstance;

    
    
    public CapabilitySerializerPogostick(@Nonnull EntityLivingBase entity) 
    {
        this._entity = entity;
        this._capabilityInstance = new CapabilityHandlerPogostick();
    }

    
    
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        LogHelper.info(" ** hasCapability [v2] - " + this._entity);
        
        return capability == CapabilityPogostick.POGOSTICK;
    }

    
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if(capability == CapabilityPogostick.POGOSTICK) {
            return (T) this._capabilityInstance;
        }
        return null;
    }

    
    @Override
    public NBTTagCompound serializeNBT()
    {
        LogHelper.info(" ** serializeNBT [v2] - " + this._entity);
        // LogHelper.info("    Tick " + Minecraft.getMinecraft().getRenderPartialTicks());

        NBTTagCompound nbtCompound = new NBTTagCompound();
        nbtCompound.setBoolean(NBT_ACTIVE, this._capabilityInstance.isUsingPogostick());
        return nbtCompound;
    }

    
    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        LogHelper.info(" ** deserializeNBT [v2] - " + this._entity);
        // LogHelper.info("    Tick " + Minecraft.getMinecraft().getRenderPartialTicks());
        
        NBTTagCompound nbtCompound = (NBTTagCompound) nbt;
        this._capabilityInstance.updatePogostickUsage(nbtCompound.getBoolean(NBT_ACTIVE));
    }

}
