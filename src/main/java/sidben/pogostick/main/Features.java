package sidben.pogostick.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
    // Commands
    // -----------------------------------------------------------------------

}
