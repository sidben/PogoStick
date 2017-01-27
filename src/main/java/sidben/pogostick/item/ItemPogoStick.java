package sidben.pogostick.item;

import javax.annotation.Nullable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import sidben.pogostick.ModPogoStick;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.capability.IPogostick;
import sidben.pogostick.reference.Reference;
import sidben.pogostick.util.LogHelper;
import sidben.pogostick.util.PogostickHelper;


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
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {

                if (entityIn != null && entityIn.hasCapability(CapabilityPogostick.POGOSTICK, null)) {
                    final IPogostick pogostickStatus = entityIn.getCapability(CapabilityPogostick.POGOSTICK, null);
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
        final ItemStack material = new ItemStack(Items.SLIME_BALL, 1, OreDictionary.WILDCARD_VALUE);
        if (OreDictionary.itemMatches(material, repair, false)) { return true; }
        return repair.getItem() == Items.SLIME_BALL;
    }



    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        LogHelper.trace("onItemRightClick(world, %s, %s)", playerIn, handIn);


        // TODO: add some tiny delay before activating the pogostick. Food items have getMaxItemUseDuration() of 32
        // TODO: BUG - food items on the off-hand are consumed without the animation



        // TODO: test if I can trust that the client won't desync if I update the capability here
        // I think that the Elytra updates client-side and send to server


        if (!worldIn.isRemote && playerIn.hasCapability(CapabilityPogostick.POGOSTICK, null)) {
            final IPogostick pogostickStatus = playerIn.getCapability(CapabilityPogostick.POGOSTICK, null);
            boolean shouldEnablePogostick = false;

            if (!pogostickStatus.isUsingPogostick()) {
                shouldEnablePogostick = PogostickHelper.canEntityActivatePogostick(playerIn);
                if (!shouldEnablePogostick) {
                    LogHelper.debug("    pogostick denied");
                    // playerIn.playSound(SoundEvents.BLOCK_SLIME_HIT, 1.8F, 1.8F); // don't work, must be on client
                }
            }

            LogHelper.debug("> capability - isUsing (pre, fresh) == %s", playerIn.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick());
            LogHelper.debug("> should enable: %s (changed %s)", shouldEnablePogostick, (shouldEnablePogostick != pogostickStatus.isUsingPogostick()));

            if (shouldEnablePogostick != pogostickStatus.isUsingPogostick()) {
                pogostickStatus.updatePogostickUsage(shouldEnablePogostick);
                ModPogoStick.instance.getNetworkManager().sendPogoStatusUpdate(shouldEnablePogostick, playerIn);
            }

            LogHelper.debug("> capability - isUsing (pos, fresh) == %s", playerIn.getCapability(CapabilityPogostick.POGOSTICK, null).isUsingPogostick());


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
