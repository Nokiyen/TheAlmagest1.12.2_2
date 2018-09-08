package noki.almagest.packet;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noki.almagest.AlmagestCore;
import noki.almagest.saveddata.AlmagestDataConstellationBlock;


/**********
 * @class PacketSavedConstBlockHandler
 *
 * @description
 * @description_en
 */
public class PacketSavedConstBlockHandler implements IMessageHandler<PacketSavedConstBlock, PacketSavedConstBlockResponse> {

	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public PacketSavedConstBlockResponse onMessage(PacketSavedConstBlock message, MessageContext ctx) {
		
		AlmagestCore.log("on PacketSavedConstBlockHandler.");
		AlmagestDataConstellationBlock constData = AlmagestCore.savedDataManager.getConstData();
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimensionId);
		BlockPos pos = constData.getNearestConstellation(world, message.pos);
		if(pos == null) {
			return new PacketSavedConstBlockResponse(false, new BlockPos(0,0,0), 0); 
		}
		return new PacketSavedConstBlockResponse(true, pos, constData.getConstellation(world, pos).getId());
		
	}

}
