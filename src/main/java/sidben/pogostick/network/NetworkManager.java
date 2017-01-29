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

    private static final String         MOD_CHANNEL = "ch_sidben_pogo";
    private static int                  packetdId   = 0;
    private static SimpleNetworkWrapper _networkWrapper;



    public static void registerMessages()
    {
        _networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_CHANNEL);

        _networkWrapper.registerMessage(MessagePogostickStatusUpdate.Handler.class, MessagePogostickStatusUpdate.class, packetdId++, Side.CLIENT);
    }



    public static void sendPogoStatusUpdate(boolean isUsingPogostick, @Nonnull EntityPlayer player)
    {
        final MessagePogostickStatusUpdate message = new MessagePogostickStatusUpdate(isUsingPogostick);
        LogHelper.trace("Sending MessagePogostickStatusUpdate() - Message: %s to %s", message, player);

        _networkWrapper.sendTo(message, (EntityPlayerMP) player);
    }


}