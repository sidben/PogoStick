package sidben.pogostick.proxy;

import sidben.pogostick.helper.ItemsHelper;

public class ClientProxy extends CommonProxy
{
    
    @Override
    public void initialize()
    {
        super.initialize();
        
        // Register items
        ItemsHelper.registerRender();
    }
    
}
