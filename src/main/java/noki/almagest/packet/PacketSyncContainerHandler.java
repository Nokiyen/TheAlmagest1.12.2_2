package noki.almagest.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noki.almagest.AlmagestCore;
import noki.almagest.gui.sequence.ContainerSequence;


/**********
 * @class PacketSyncContainerHandler
 *
 * @description
 * @description_en
 */
public class PacketSyncContainerHandler implements IMessageHandler<PacketSyncContainer, IMessage> {

	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public IMessage onMessage(PacketSyncContainer message, MessageContext ctx) {
		
		AlmagestCore.log("packet recieved:SyncContainer.");
		EntityPlayer player = ctx.getServerHandler().player;
		if(player.openContainer != null && player.openContainer instanceof ContainerSequence) {
			AlmagestCore.log("into if.");
			((ContainerSequence)player.openContainer).goToNextSequence(message.nextFlag);
		}
		return null;
		
	}

}
