package sidben.pogostick.handler;

import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sidben.pogostick.util.LogHelper;


/**
 * Event handler that should only execute once, after a certain time. 
 */
public abstract class DelayedEventHandler<T extends EntityEvent>
{

    private int _tickOfExecution;
    private final Entity _entityAffected;
    private final Class<T> _eventClass;
    

    
    public DelayedEventHandler(int ticksToWait, @Nonnull Entity entityAffected, Class<T> eventClass) 
    {
        this._tickOfExecution = entityAffected.ticksExisted + ticksToWait;
        this._entityAffected = entityAffected;
        this._eventClass = eventClass;
    }
    
    
    
    
    protected Entity getEntityAffected()
    {
        return this._entityAffected;
    }
    
    @SubscribeEvent
    protected void onEvent(T event)
    {
        if (event.getEntity() == null) return;
        
        /*
         * Since T extends EntityEvent, this class will subscribe to all 'EntityEvent', not just
         * the one implemented in the concrete class. Because of that, I need to make sure this 
         * method will only work for the correct event.
         */
        if (this.isAffectedEntity(event.getEntity()) && event.getClass().equals(this._eventClass)) 
        {
            
            if (event.getEntity().ticksExisted >= this._tickOfExecution) {
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

    

    public abstract void execute(T event);
    
}
