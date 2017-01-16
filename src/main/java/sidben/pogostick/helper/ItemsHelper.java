package sidben.pogostick.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sidben.pogostick.item.ItemPogoStick;

public class ItemsHelper
{

    public static final ItemPogoStick  pogoStick = new ItemPogoStick();

    
    public static void register()
    {
        GameRegistry.register(pogoStick);
    }

    
    @SideOnly(Side.CLIENT)
    public static void registerRender()
    {
        ItemModelMesher itemMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        
        itemMesher.register(pogoStick, 0, new ModelResourceLocation("pogostick:pogo_stick", "inventory"));
    }

    
}
