package sidben.pogostick.handler;

import javax.annotation.Nonnull;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;


/**
 * Event handler that should only execute once, after a certain time.
 *
 * Works for EntityEvent and PlayerTickEvent.
 */
public abstract class DelayedEventHandler<T extends Event>
{

    private final int      _tickOfExecution;
    private final Entity   _entityAffected;
    private final Class<T> _eventClass;



    public DelayedEventHandler(int ticksToWait, @Nonnull Entity entityAffected, Class<T> eventClass) {
        this._tickOfExecution = entityAffected.ticksExisted + ticksToWait;
        this._entityAffected = entityAffected;
        this._eventClass = eventClass;
    }



    protected Entity getEntityAffected()
    {
        return this._entityAffected;
    }


    protected Entity getEventEntity(T event)
    {
        if (event instanceof EntityEvent) { return ((EntityEvent) event).getEntity(); }
        if (event instanceof PlayerTickEvent) { return ((PlayerTickEvent) event).phase == Phase.END ? ((PlayerTickEvent) event).player : null; }
        return null;
    }



    // @SubscribeEvent
    /**
     * This method must be called from onEvent() method from the inherited class.
     */
    protected void onEventInternal(T event)
    {
        final Entity eventEntity = this.getEventEntity(event);
        if (eventEntity == null) { return; }

        /*
         * Since T extends EntityEvent, this class will subscribe to all 'EntityEvent', not just
         * the one implemented in the concrete class. Because of that, I need to make sure this
         * method will only work for the correct event.
         *
         * UPDATE: the base class no longer subscribe to event, I'll leave it to the inherited class.
         */
        if (this.isAffectedEntity(eventEntity) && event.getClass().equals(this._eventClass)) {

            if (eventEntity.ticksExisted >= this._tickOfExecution) {
                this.execute(event);
                MinecraftForge.EVENT_BUS.unregister(this);
            }

        }
    }



    private boolean isAffectedEntity(Entity entity)
    {
        /*
         * When you compare entities, the game will just look for the same entityId and ignore if
         * the entities are not on the same side, so I need to also compare their worlds.
         */
        return _entityAffected.equals(entity) && _entityAffected.getEntityWorld().equals(entity.getEntityWorld());
    }



    public abstract void onEvent(T event);

    public abstract void execute(T event);

}
