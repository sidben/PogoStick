package sidben.pogostick.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import sidben.pogostick.item.ItemPogoStick;


/**
 * Handles blocks, items, commands and other features from this mod.
 */
public class Features
{


    // -----------------------------------------------------------------------
    // Items
    // -----------------------------------------------------------------------

    public static final ItemPogoStick pogoStick = new ItemPogoStick();



    public static void registerItems()
    {
        GameRegistry.register(pogoStick);
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModels()
    {
        final ItemModelMesher itemMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

        itemMesher.register(pogoStick, 0, new ModelResourceLocation("pogostick:pogo_stick", "inventory"));
    }



    // -----------------------------------------------------------------------
    // Blocks
    // -----------------------------------------------------------------------



    // -----------------------------------------------------------------------
    // Recipes
    // -----------------------------------------------------------------------

    private static String OREDIC_STICK      = "stickWood";
    private static String OREDIC_IRON_BLOCK = "blockIron";


    public static void registerRecipes()
    {
        final ItemStack slimeBlocks = new ItemStack(Blocks.SLIME_BLOCK);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Features.pogoStick, 1), "ttt", " i ", " s ", 't', OREDIC_STICK, 'i', OREDIC_IRON_BLOCK, 's', slimeBlocks));
    }



    // -----------------------------------------------------------------------
    // Commands
    // -----------------------------------------------------------------------

}
