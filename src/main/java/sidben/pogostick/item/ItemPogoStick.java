package sidben.pogostick.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import sidben.pogostick.helper.LogHelper;
import sidben.pogostick.reference.Reference;


public class ItemPogoStick extends Item
{

    public ItemPogoStick() {
        this.maxStackSize = 1;
        this.setUnlocalizedName("pogo_stick");
        this.setRegistryName("pogo_stick");
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        this.setMaxDamage(300);
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
     * Return whether this item is repairable in an anvil.
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
        return EnumAction.BLOCK;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        LogHelper.info("onItemRightClick()");
        LogHelper.info("  hand: " + handIn);
        LogHelper.info("  player: " + playerIn);
        
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }


}
