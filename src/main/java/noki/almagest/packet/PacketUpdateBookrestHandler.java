package noki.almagest.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noki.almagest.gui.ContainerBookrest;


/**********
 * @class PacketBreakMessageHandler
 *
 * @description
 * @description_en
 */
public class PacketUpdateBookrestHandler implements IMessageHandler<PacketUpdateBookrest, IMessage> {

	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public IMessage onMessage(PacketUpdateBookrest message, MessageContext ctx) {
		
		EntityPlayer player = ctx.getServerHandler().player;
		if(player.openContainer != null && player.openContainer instanceof ContainerBookrest) {
			((ContainerBookrest)player.openContainer).switchAbilitySelected(message.abilityId);
		}
		
		return null;
		
	}

}
