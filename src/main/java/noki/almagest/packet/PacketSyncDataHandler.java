package noki.almagest.packet;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noki.almagest.AlmagestCore;


/**********
 * @class PacketSyncDataHandler
 *
 * @description
 * @description_en
 */
public class PacketSyncDataHandler implements IMessageHandler<PacketSyncData, IMessage> {

	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public IMessage onMessage(PacketSyncData message, MessageContext ctx) {
		
//		AlmagestCore.savedDataManager.getNbt().readFromNBT(message.nbt);
		return null;
		
	}

}
