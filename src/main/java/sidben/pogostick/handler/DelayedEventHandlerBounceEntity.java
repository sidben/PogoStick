package sidben.pogostick.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import sidben.pogostick.util.LogHelper;
import sidben.pogostick.util.PogostickHelper;


public class DelayedEventHandlerBounceEntity extends DelayedEventHandler<PlayerTickEvent>
{

    private final float  _originalDistance;
    private final double _originalMotionY;



    public DelayedEventHandlerBounceEntity(int ticksToWait, Entity entityAffected, float fallDistance) {
        super(ticksToWait, entityAffected, PlayerTickEvent.class);
        this._originalDistance = fallDistance;
        this._originalMotionY = entityAffected.motionY;
    }


    @Override
    public void execute(PlayerTickEvent event)
    {
        LogHelper.debug("DelayedEventHandlerBounceEntity.execute()");
        LogHelper.debug(">   entity %s has fallen %.4f at speed %.4f", event.player, this._originalDistance, this._originalMotionY);
        LogHelper.debug(">   MotionY - Current %.4f, original: %.4f", event.player.motionY, this._originalMotionY);
        LogHelper.debug(">   Player pos -  X: %.3f, Y: %.3f, Z: %.3f", event.player.posX, event.player.posY, event.player.posZ);

        final EntityPlayer entity = event.player;
        if (entity == null || entity.isDead) { return; }

        PogostickHelper.processEntityLandingWithPogostick(entity, this._originalDistance, this._originalMotionY);
    }


    @Override
    @SubscribeEvent
    public void onEvent(PlayerTickEvent event)
    {
        super.onEventInternal(event);
    }

}
