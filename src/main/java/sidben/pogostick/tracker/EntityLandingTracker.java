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
    public int tickDelay;
    // TODO: distance
    
    
    
    public EntityLandingTracker(double motionY, ItemStack pogostickStack) {
        this._motionY = motionY;
        this._pogoStack = pogostickStack;
        this.tickDelay = 1;
    }
    
    
    
    public double getBounceMotionY() {
        return 0.9D;
    }
    
    public ItemStack getItemStack() {
        return this._pogoStack;
    }
    
    
}
