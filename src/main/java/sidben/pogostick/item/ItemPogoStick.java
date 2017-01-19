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
import sidben.pogostick.helper.LogHelper;
import sidben.pogostick.reference.Reference;


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
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
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
        return EnumAction.NONE;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;       // Same as shield, but may be limited
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        /*
        LogHelper.info("onItemRightClick()");
        LogHelper.info("  hand: " + handIn);
        LogHelper.info("  player: " + playerIn);
        */
        
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }


}
