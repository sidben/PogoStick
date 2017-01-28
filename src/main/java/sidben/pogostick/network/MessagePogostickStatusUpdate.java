package sidben.pogostick.network;

import javax.annotation.concurrent.Immutable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.capability.IPogostick;
import sidben.pogostick.util.LogHelper;


@Immutable
public class MessagePogostickStatusUpdate implements IMessage
{

    private boolean _isActive;



    public MessagePogostickStatusUpdate() {
    }

    public MessagePogostickStatusUpdate(boolean isPogostickActive) {        // TODO: make all parameters follow this name convention
        this._isActive = isPogostickActive;
    }



    public boolean getIsPogostickActive()
    {
        return this._isActive;
    }


    @Override
    public void fromBytes(ByteBuf buf)
    {
        this._isActive = buf.readBoolean();
    }


    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(this._isActive);
    }


    @Override
    public String toString()
    {
        return String.format("MessagePogostickStatusUpdate [isActive: %s]", this.getIsPogostickActive());
    }



    public static class Handler implements IMessageHandler<MessagePogostickStatusUpdate, IMessage>
    {

        @Override
        public IMessage onMessage(MessagePogostickStatusUpdate message, MessageContext ctx)
        {
            LogHelper.trace("Handling MessagePogostickStatusUpdate() - Message: %s", message);

            final EntityPlayer player = Minecraft.getMinecraft().player;
            if (player == null) {
                LogHelper.trace("    WARNING: Target player (client) not found");
                return null;
            }


            // Updates the player capabilities client-side
            if (player.hasCapability(CapabilityPogostick.POGOSTICK, null)) {
                final IPogostick pogostickStatus = player.getCapability(CapabilityPogostick.POGOSTICK, null);
                pogostickStatus.updatePogostickUsage(message.getIsPogostickActive());
            }

            return null;
        }

    }

}
