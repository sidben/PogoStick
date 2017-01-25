package sidben.pogostick.item;

import javax.annotation.Nullable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sidben.pogostick.ModPogoStick;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.capability.IPogostick;
import sidben.pogostick.handler.PlayerEventHandler;
import sidben.pogostick.reference.Reference;
import sidben.pogostick.util.LogHelper;


public class ItemPogoStick extends Item
{

    public ItemPogoStick() {
        this.maxStackSize = 1;
        this.setUnlocalizedName("pogo_stick");
        this.setRegistryName("pogo_stick");
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        this.setMaxDamage(720);

        this.addPropertyOverride(new ResourceLocation("using"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                // TODO: use the isEntityUsingPogoStick(), that should be moved elsewhere
                // return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
                
                if (entityIn.hasCapability(CapabilityPogostick.POGOSTICK, entityIn.getAdjustedHorizontalFacing())) {
                    IPogostick pogostickStatus = entityIn.getCapability(CapabilityPogostick.POGOSTICK, entityIn.getAdjustedHorizontalFacing());
                    return pogostickStatus.isUsingPogostick() ? 1F : 0F;
                }

                return 0F;
            }
        });

    }



    @Override
    public String getUnlocalizedName()
    {
        return Reference.ModID + ":" + super.getUnlocalizedName();
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return Reference.ModID + ":" + super.getUnlocalizedName();
    }
    


    /**
     * Return whether this item is reparable in an anvil.
     */
    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return repair.getItem() == Items.SLIME_BALL;
    }


    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.NONE;     // TODO: remove
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;       // Same as shield, but may be limited - TODO: after capabilities, remove
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        LogHelper.info(String.format("onItemRightClick(world, player, %s)", handIn));
        /*
        LogHelper.info("onItemRightClick()");
        LogHelper.info("  hand: " + handIn);
        LogHelper.info("  player: " + playerIn);
        */

        
        
        // TODO: validation - add to the interface canEnablePogostick? - not flying, not swiming, etc
        // TODO: a way to pass the player to the capability, so I can apply the business rule and deactivate when the player enters water, etc
        


        // TODO: test if I can trust that the client won't desync if I update the capability here
        // I think that the Elytra updates client-side and send to server
        
        if (!worldIn.isRemote && playerIn.hasCapability(CapabilityPogostick.POGOSTICK, null)) 
        {
            IPogostick pogostickStatus = playerIn.getCapability(CapabilityPogostick.POGOSTICK, null);
            boolean shouldEnablePogostick = false;
            
            if (!pogostickStatus.isUsingPogostick()) {
                // TODO: util method: canEnablePogostick
                shouldEnablePogostick = (playerIn.onGround || (!playerIn.isInWater() && !playerIn.isInLava() && !playerIn.isElytraFlying())) && !playerIn.isPlayerSleeping();
            }

            LogHelper.info("> capability - isUsing (pre, fresh) " + playerIn.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick());
            LogHelper.info("> should enable: " + shouldEnablePogostick + " (changed " + (shouldEnablePogostick != pogostickStatus.isUsingPogostick()) + ")");
            
            if (shouldEnablePogostick != pogostickStatus.isUsingPogostick()) 
            {
                pogostickStatus.updatePogostickUsage(shouldEnablePogostick);
                ModPogoStick.instance.getNetworkManager().sendPogoStatusUpdate(shouldEnablePogostick, playerIn);
            }

            LogHelper.info("> capability - isUsing (pos, fresh) " + playerIn.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick());
            

            // TODO: check interaction with chests, etc
            // return new ActionResult(EnumActionResult.PASS, playerIn.getHeldItem(handIn)); 
        }
        
        
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }



    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        // Avoids re-equipping the item when it takes damage
        return !oldStack.getItem().equals(newStack.getItem());      // TODO: check if a slot change would cause problems. I want to ignore animation just for the same slot
    }

    
}
