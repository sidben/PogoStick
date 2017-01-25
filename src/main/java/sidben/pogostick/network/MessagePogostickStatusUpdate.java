package sidben.pogostick.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sidben.pogostick.capability.CapabilityPogostick;
import sidben.pogostick.capability.IPogostick;
import sidben.pogostick.util.LogHelper;


public class MessagePogostickStatusUpdate implements IMessage
{
    
    private boolean _isActive;
    
        
    
    
    public MessagePogostickStatusUpdate() {
    }

    public MessagePogostickStatusUpdate(boolean isPogostickActive) {        // TODO: make all parameters follow this name convention
        this._isActive = isPogostickActive;
    }
    
    
    
    public boolean getIsPogostickActive() {
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
            LogHelper.info("Handling MessagePogostickStatusUpdate() - " + message);

            final EntityPlayer player = Minecraft.getMinecraft().player; 
            LogHelper.info("    " + player);
            if (player == null) { return null; }
            

            // Updates the player capabilities client-side
            if (player.hasCapability(CapabilityPogostick.POGOSTICK, null))
            {
                IPogostick pogostickStatus = player.getCapability(CapabilityPogostick.POGOSTICK, null);
                pogostickStatus.updatePogostickUsage(message.getIsPogostickActive());
            }

            return null;
        }

    }

}
