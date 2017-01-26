package sidben.pogostick.util;

import javax.annotation.Nonnull;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.main.Features;


public class PogostickHelper
{
    
    
    public final static float MIN_DISTANCE_TO_BOUNCE = 1.1F; 
    
    
    

    
    public static boolean canEntityActivatePogostick(@Nonnull EntityLivingBase entity)
    {
        // return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
        // shouldEnablePogostick = (playerIn.onGround || (!playerIn.isInWater() && !playerIn.isInLava() && !playerIn.isElytraFlying())) && !playerIn.isPlayerSleeping();
        
        ItemStack heldPogostick = getHeldPogostick(entity);
     
        return entity.hasCapability(CapabilityPogostick.POGOSTICK, null) 
                && !entity.onGround 
                && entity.isEntityAlive()
                && !(entity.isInWater() || entity.isInLava())
                && !entity.isElytraFlying()
                && heldPogostick != ItemStack.EMPTY;
    }
    
    
    
    /**
     * Returns the ItemStack with the pogostick being held by the given entity.
     * Return ItemStack.EMPTY if invalid.
     */
    public static ItemStack getHeldPogostick(@Nonnull EntityLivingBase entity)
    {
        if (entity.getHeldItemMainhand().getItem() == Features.pogoStick && entity.getHeldItemMainhand().getItemDamage() < entity.getHeldItemMainhand().getMaxDamage()) return entity.getHeldItemMainhand();  
        if (entity.getHeldItemOffhand().getItem() == Features.pogoStick && entity.getHeldItemOffhand().getItemDamage() < entity.getHeldItemOffhand().getMaxDamage())  return entity.getHeldItemOffhand();  
        return ItemStack.EMPTY;
    }
    
    
}
