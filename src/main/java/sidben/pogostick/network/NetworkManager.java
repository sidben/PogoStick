package sidben.pogostick.network;

import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import sidben.pogostick.util.LogHelper;


public class NetworkManager
{

    private static final String  MOD_CHANNEL = "CH_SIDBEN_POGO";
    private SimpleNetworkWrapper _networkWrapper;



    public void registerMessages()
    {
        this._networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_CHANNEL);

        int packetdId = 0;
        this._networkWrapper.registerMessage(MessagePogostickStatusUpdate.Handler.class, MessagePogostickStatusUpdate.class, packetdId++, Side.CLIENT);
    }



    public void sendPogoStatusUpdate(boolean isUsingPogostick, @Nonnull EntityPlayer player)
    {
        final MessagePogostickStatusUpdate message = new MessagePogostickStatusUpdate(isUsingPogostick);
        LogHelper.trace("Sending MessagePogostickStatusUpdate() - Message: %s to %s", message, player);

        this._networkWrapper.sendTo(message, (EntityPlayerMP) player);
    }


}