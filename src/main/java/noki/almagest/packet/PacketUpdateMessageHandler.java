package noki.almagest.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noki.almagest.tile.TileConstellation;


/**********
 * @class PacketBreakMessageHandler
 *
 * @description
 * @description_en
 */
public class PacketUpdateMessageHandler implements IMessageHandler<PacketUpdateMessage, IMessage> {

	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public IMessage onMessage(PacketUpdateMessage message, MessageContext ctx) {
		
		if(message.type == 1) {
			TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(message.pos);
			if(tile instanceof TileConstellation) {
				((TileConstellation)tile).switchRotation();
			}
		}
		return null;
		
	}

}
