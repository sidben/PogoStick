package sidben.pogostick.capability;

import sidben.pogostick.main.ModConfig;


public class CapabilityHandlerPogostick implements IPogostick
{

    private boolean       _isActive;
    private boolean       _bouncedOnce;
    private final float   _distanceLimiterBase;
    private final boolean _canApplyModifiers;



    public CapabilityHandlerPogostick(float distanceLimiterBase, boolean canApplySpeedModifiers) {
        this._distanceLimiterBase = distanceLimiterBase;
        this._canApplyModifiers = canApplySpeedModifiers;
    }



    @Override
    public boolean isUsingPogostick()
    {
        return this._isActive;
    }


    @Override
    public void updatePogostickUsage(boolean isActive)
    {
        // Resets bounce status on state change
        if (this._isActive != isActive) {
            this._bouncedOnce = false;
        }

        this._isActive = isActive;
    }


    @Override
    public void markBounced()
    {
        if (this.isUsingPogostick()) {
            this._bouncedOnce = true;
        }
    }


    @Override
    public float minDistanceToBounce()
    {
        return this.bouncedOnce() ? ModConfig.MIN_DISTANCE_TO_KEEP_BOUNCING : ModConfig.MIN_DISTANCE_TO_START_BOUNCING;
    }


    @Override
    public boolean bouncedOnce()
    {
        return this._bouncedOnce;
    }


    @Override
    public float fallDistanceBaseLimit()
    {
        // NOTE: for future use, when implementing pogosticks use on mobs
        return this._distanceLimiterBase;
    }


    @Override
    public boolean canApplyModifiers()
    {
        // NOTE: for future use, when implementing pogosticks use on mobs
        return this._canApplyModifiers;
    }


    @Override
    public String toString()
    {
        return "CapabilityHandlerPogostick [isActive=" + _isActive + ", bouncedOnce=" + _bouncedOnce + ", distanceLimiterBase=" + _distanceLimiterBase + ", canApplyModifiers=" + _canApplyModifiers
                + "]";
    }


}