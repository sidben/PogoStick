package sidben.pogostick.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.enchantment.EnchantmentSpring;
import sidben.pogostick.item.ItemPogoStick;


/**
 * Handles blocks, items, commands and other features from this mod.
 */
public class Features
{


    // -----------------------------------------------------------------------
    // Items
    // -----------------------------------------------------------------------

    public static class Items
    {
        public static final ItemPogoStick POGOSTICK = new ItemPogoStick();
    }


    public static void registerItems()
    {
        GameRegistry.register(Items.POGOSTICK);
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModels()
    {
        final ItemModelMesher itemMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

        itemMesher.register(Items.POGOSTICK, 0, new ModelResourceLocation("pogostick:pogo_stick", "inventory"));
    }



    // -----------------------------------------------------------------------
    // Blocks
    // -----------------------------------------------------------------------



    // -----------------------------------------------------------------------
    // Recipes
    // -----------------------------------------------------------------------

    private static String OREDIC_STICK = "stickWood";
    // private static String OREDIC_IRON_BLOCK = "blockIron";


    public static void registerRecipes()
    {
        final ItemStack slimeBlocks = new ItemStack(Blocks.SLIME_BLOCK);
        final ItemStack pistons = new ItemStack(Blocks.PISTON);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.POGOSTICK, 1), "ttt", " p ", " s ", 't', OREDIC_STICK, 'p', pistons, 's', slimeBlocks));
    }



    // -----------------------------------------------------------------------
    // Achievements and Stats
    // -----------------------------------------------------------------------

    public static class Stats
    {
        public static final StatBase TIMES_BOUNCED = new StatBasic("stat.timesBounced", new TextComponentTranslation("pogostick:stat.times_bounced", new Object[0]));
    }



    public static void registerStats()
    {
        Stats.TIMES_BOUNCED.registerStat();
    }



    // -----------------------------------------------------------------------
    // Capabilities
    // -----------------------------------------------------------------------

    public static void registerCapabilities()
    {
        CapabilityPogostick.register();
    }



    // -----------------------------------------------------------------------
    // Commands
    // -----------------------------------------------------------------------



    // -----------------------------------------------------------------------
    // Enchantments
    // -----------------------------------------------------------------------

    public static class Enchantments
    {
        public static final Enchantment SPRING = new EnchantmentSpring(Enchantment.Rarity.COMMON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
    }


    public static void registerEnchantments()
    {
        GameRegistry.register(Enchantments.SPRING);
    }


}
