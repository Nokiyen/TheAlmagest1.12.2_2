package noki.almagest.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noki.almagest.gui.ContainerBookrest;
import noki.almagest.gui.ContainerMira;
import noki.almagest.gui.MiraState;


/**********
 * @class PacketBreakMessageHandler
 *
 * @description
 * @description_en
 */
public class PacketUpdateMiraHandler implements IMessageHandler<PacketUpdateMira, IMessage> {

	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public IMessage onMessage(PacketUpdateMira message, MessageContext ctx) {
		
		EntityPlayer player = ctx.getServerHandler().player;
		if(player.openContainer != null && player.openContainer instanceof ContainerMira) {
			ContainerMira container = (ContainerMira)player.openContainer;
			switch(message.stateId) {
			case 0:
				container.setState(MiraState.CONSTELLATION);
				break;
			case 1:
				container.setState(MiraState.BOOK);
				break;
			case 2:
				container.setState(MiraState.TALK);
				break;
			}
		}
		
		return null;
		
	}

}
