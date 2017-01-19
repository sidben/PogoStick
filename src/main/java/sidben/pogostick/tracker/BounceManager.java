package sidben.pogostick.tracker;

import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import sidben.pogostick.helper.LogHelper;


/**
 * Handles all bounciness.
 *
 */
public class BounceManager
{

    private final HashMap<Entity, EntityLandingTracker> _fallenEntities = Maps.newHashMap();
    private EntityLandingTracker _localFallenEntities = EntityLandingTracker.EMPTY;     // TODO: can be removed? The client controls movement

    
       
    
    
    
    /**
     * Stores info about entities that are using pogo sticks and should bounce on the next tick. 
     */
    public void pushEntity(Entity entity, double motionY, ItemStack pogostickStack) {
        LogHelper.info("pushEntity(" + entity + ", " + motionY + ", " +pogostickStack+ ") - #" + _fallenEntities.size());
        
        if (motionY < 0D) {
            EntityLandingTracker tracker = new EntityLandingTracker(motionY, pogostickStack);
            if (!entity.world.isRemote) {
                _fallenEntities.put(entity, tracker);
            } else {
                _localFallenEntities = tracker;
            }
        }
    }

    
    /**
     * Looks for the given entity on the internal list and return the tracker value. The entity is
     * removed from the list (if found).
     * 
     *  Returns EntityLandingTracker.EMPTY if the entity was not found.
     */
    public EntityLandingTracker popEntity(Entity entity) {
        EntityLandingTracker tracker = EntityLandingTracker.EMPTY;

        if (!entity.world.isRemote) {
            if (_fallenEntities.containsKey(entity)) {
                LogHelper.info("popEntity(" + entity + ") - #" + _fallenEntities.size());
                
                tracker = _fallenEntities.get(entity);
                _fallenEntities.remove(entity);
            }
        } else {
            // LogHelper.info("popEntity(" + entity + ") - local");
            tracker = _localFallenEntities;
            _localFallenEntities = EntityLandingTracker.EMPTY;
        }

        
        return tracker;
    }

}
