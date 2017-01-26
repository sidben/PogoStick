package sidben.pogostick.capability;

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
        LogHelper.debug(" ** Updating isActive %s on %s", isActive, super.toString());
        this._isActive = isActive;
    }


    @Override
    public String toString()
    {
        return "CapabilityHandlerPogostick [isActive=" + _isActive + "]";
    }

}
