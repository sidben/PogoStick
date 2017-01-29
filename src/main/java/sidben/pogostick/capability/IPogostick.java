package sidben.pogostick.capability;


public interface IPogostick
{
    void updatePogostickUsage(boolean isActive);

    void markBounced();

    boolean isUsingPogostick();

    float minDistanceToBounce();

    boolean bouncedOnce();
}
