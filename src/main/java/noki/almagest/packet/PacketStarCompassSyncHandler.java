package noki.almagest.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noki.almagest.AlmagestCore;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.tile.TileStarCompass;


/**********
 * @class PacketStarCompassSyncHandler
 *
 * @description
 * @description_en
 */
public class PacketStarCompassSyncHandler implements IMessageHandler<PacketStarCompassSync, IMessage> {

	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public IMessage onMessage(PacketStarCompassSync message, MessageContext ctx) {
		
		AlmagestCore.log("on star compass sync.");
		
		World world = Minecraft.getMinecraft().world;
		if(world == null || world.provider.getDimension() != message.dimensionId) {
			return null;
		}
		
		TileEntity tile = world.getTileEntity(message.tilePos);
		if(tile == null || !(tile instanceof TileStarCompass)) {
			return null;
		}
		
		((TileStarCompass)tile).setTarget(message.targetPos, Constellation.getConstFromNumber(message.constId));
		return null;
		
	}

}
