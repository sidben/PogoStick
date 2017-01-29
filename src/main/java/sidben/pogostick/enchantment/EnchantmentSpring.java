package sidben.pogostick.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import sidben.pogostick.reference.Reference;
import sidben.pogostick.util.PogostickHelper;


// TODO: effect
public class EnchantmentSpring extends Enchantment
{

    public EnchantmentSpring(Rarity rarityIn, EntityEquipmentSlot[] slots) {
        super(rarityIn, EnumEnchantmentType.ALL, slots);
        this.setRegistryName("spring");
    }


    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return 1 + (enchantmentLevel - 1) * 20;
    }

    /**
     * Returns the maximum value of enchantability needed on the enchantment level passed.
     */
    @Override
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    @Override
    public int getMaxLevel()
    {
        return 2;
    }

    /**
     * Determines if this enchantment can be applied to a specific ItemStack.
     */
    @Override
    public boolean canApply(ItemStack stack)
    {
        return PogostickHelper.isPogoStack(stack);
    }

    /**
     * Is this enchantment allowed to be enchanted on books via Enchantment Table
     *
     * @return false to disable the vanilla feature
     */
    @Override
    public boolean isAllowedOnBooks()
    {
        return false;
    }


    /**
     * Return the name of key in translation table of this enchantment.
     */
    @Override
    public String getName()
    {
        return Reference.ModID + ":enchantment.spring";
    }



    public static float getBouncingModifier(int level)
    {
        return level * 1.75F;
    }


}
