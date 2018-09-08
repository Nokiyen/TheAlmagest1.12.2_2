package noki.almagest.packet;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noki.almagest.AlmagestCore;
import noki.almagest.event.EventRender;


/**********
 * @class PacketSavedConstBlockHandlerResponse
 *
 * @description
 * @description_en
 */
public class PacketSavedConstBlockHandlerResponse implements IMessageHandler<PacketSavedConstBlockResponse, IMessage> {

	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public IMessage onMessage(PacketSavedConstBlockResponse message, MessageContext ctx) {
		
		AlmagestCore.log("on PacketSavedConstBlockHandlerResponse.");
		if(message.success) {
			EventRender.setTarget(message.pos, message.constId);
		}
		return null;
		
	}

}
