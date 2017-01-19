package sidben.pogostick.tracker;

import net.minecraft.item.ItemStack;

/**
 * Tracks info about the player fall so they can bounce with the pogo stick. 
 */
public class EntityLandingTracker
{

    public static final EntityLandingTracker EMPTY = new EntityLandingTracker(0D, ItemStack.EMPTY); 
    
    
    private final double _motionY;
    private final ItemStack _pogoStack;
    // TODO: distance
    
    
    
    public EntityLandingTracker(double motionY, ItemStack pogostickStack) {
        this._motionY = motionY;
        this._pogoStack = pogostickStack;
    }
    
    
    
    public double getBounceMotionY() {
        return 0.7D;
    }
    
    
}
