package sidben.pogostick.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.util.LogHelper;
import sidben.pogostick.util.PogostickHelper;


public class DelayedEventHandlerBounceEntity extends DelayedEventHandler<PlayerTickEvent>
{
    
    private final float _originalDistance;
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

        EntityLivingBase entity = event.player;
        ItemStack pogoStack = PogostickHelper.getHeldPogostick(entity);
        if (pogoStack == ItemStack.EMPTY) return;
        
        
        float maxMotion = 2F;       // TODO: make const

        if (entity.world.isRemote) {
            entity.motionY = (this._originalMotionY * -1) * 1.25;
            LogHelper.debug(">>> New motionY %.4f (max %.4f)", entity.motionY, maxMotion);
            LogHelper.debug(">>> Fall distance %.4f", this._originalDistance);
            
            
            entity.motionY = Math.min(maxMotion, entity.motionY);
            entity.motionX *= 2;
            entity.motionZ *= 2;

            entity.isAirBorne = true;
            entity.onGround = false;

            entity.playSound(SoundEvents.ENTITY_SLIME_JUMP, 0.8F, 0.8F + entity.world.rand.nextFloat() * 0.4F);
            
        } else {
            pogoStack.damageItem(1, entity);       // TODO: dmg item based on distance fallen (max 3 dmg?)
            
        } 
        
    }


    @Override
    @SubscribeEvent
    public void onEvent(PlayerTickEvent event)
    {
        super.onEventInternal(event);
    }

}
