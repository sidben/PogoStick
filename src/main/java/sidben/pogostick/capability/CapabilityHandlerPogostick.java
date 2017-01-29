package sidben.pogostick.capability;

public class CapabilityHandlerPogostick implements IPogostick
{

    private final static float MIN_DISTANCE_TO_START_BOUNCING = 1.1F;
    private final static float MIN_DISTANCE_TO_KEEP_BOUNCING  = 0.3F;

    private boolean            _isActive;
    private boolean            _bouncedOnce;


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
        return this.bouncedOnce() ? MIN_DISTANCE_TO_KEEP_BOUNCING : MIN_DISTANCE_TO_START_BOUNCING;
    }


    @Override
    public boolean bouncedOnce()
    {
        return this._bouncedOnce;
    }


    @Override
    public String toString()
    {
        return "CapabilityHandlerPogostick [isActive=" + _isActive + ", bouncedOnce=" + _bouncedOnce + "]";
    }


}
