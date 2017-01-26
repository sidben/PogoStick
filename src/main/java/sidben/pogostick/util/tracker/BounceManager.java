package sidben.pogostick.util.tracker;

import java.util.HashMap;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import sidben.pogostick.util.LogHelper;


/**
 * Handles all bounciness.
 *
 */
public class BounceManager
{

    private final HashMap<Entity, EntityLandingTracker> _fallenEntities      = Maps.newHashMap();
    private EntityLandingTracker                        _localFallenEntities = EntityLandingTracker.EMPTY;     // TODO: can be removed? The client controls movement



    /**
     * Stores info about entities that are using pogo sticks and should bounce on the next tick.
     */
    public void pushEntity(Entity entity, double motionY, ItemStack pogostickStack)
    {
        LogHelper.debug("pushEntity(%s, %d, %s) - # entities on list %d", entity, motionY, pogostickStack, _fallenEntities.size());

        if (motionY < 0D) {
            final EntityLandingTracker tracker = new EntityLandingTracker(motionY, pogostickStack);
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
     * Returns EntityLandingTracker.EMPTY if the entity was not found.
     */
    public EntityLandingTracker popEntity(Entity entity)
    {
        EntityLandingTracker tracker = EntityLandingTracker.EMPTY;

        if (!entity.world.isRemote) {
            if (_fallenEntities.containsKey(entity)) {
                LogHelper.debug("popEntity(%s) - # entities on list %d", entity, _fallenEntities.size());

                final EntityLandingTracker peekTracker = _fallenEntities.get(entity);
                if (peekTracker.tickDelay <= 0) {
                    _fallenEntities.remove(entity);
                    tracker = peekTracker;
                } else {
                    peekTracker.tickDelay--;
                }
            }
        } else {
            // LogHelper.info("popEntity(" + entity + ") - local");
            final EntityLandingTracker peekTracker = _localFallenEntities;
            if (peekTracker.tickDelay <= 0) {
                _localFallenEntities = EntityLandingTracker.EMPTY;
                tracker = peekTracker;
            } else {
                peekTracker.tickDelay--;
            }

        }


        // TODO: refactor and optimize
        return tracker;
    }

}
