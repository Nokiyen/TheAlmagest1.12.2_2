package noki.almagest.packet;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noki.almagest.AlmagestCore;


/**********
 * @class PacketSyncStoryHandler
 *
 * @description
 * @description_en
 */
public class PacketSyncStoryHandler implements IMessageHandler<PacketSyncStory, IMessage> {

	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public IMessage onMessage(PacketSyncStory message, MessageContext ctx) {
		
		AlmagestCore.savedDataManager.getStoryData().markStoryFlag(message.currentFlag);;
//		AlmagestCore.log("current story flag is {}.", AlmagestCore.savedDataManager.getStoryData().getStoryFlag());
		return null;
		
	}

}
 