package sidben.pogostick.proxy;

import sidben.pogostick.main.Features;


public class ClientProxy extends CommonProxy
{

    @Override
    public void initialize()
    {
        super.initialize();
        Features.registerItemModels();
    }

}
