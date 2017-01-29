package sidben.pogostick.capability;


public interface IPogostick
{
    void updatePogostickUsage(boolean isActive);

    void markBounced();

    boolean isUsingPogostick();

    float minDistanceToBounce();

    float fallDistanceBaseLimit();

    boolean bouncedOnce();

    boolean canApplyModifiers();
}
