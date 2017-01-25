package sidben.pogostick.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import sidben.pogostick.util.LogHelper;


public class CapabilityHandlerPogostick implements IPogostick
{

    private boolean _isActive;
    

    @Override
    public boolean isUsingPogostick()
    {
        return this._isActive;
    }

    @Override
    public void updatePogostickUsage(boolean isActive)
    {
        LogHelper.info(" ** Updating isActive " + isActive + " on " + super.toString());
        this._isActive = isActive;
    }

    
    @Override
    public String toString()
    {
        return "CapabilityHandlerPogostick [isActive=" + _isActive + "]";
    }
    
}
