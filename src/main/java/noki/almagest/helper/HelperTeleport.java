package noki.almagest.helper;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import noki.almagest.AlmagestData;
import noki.almagest.world.planisphere.PlaniTeleporter;


/**********
 * @class HelperTeleport
 *
 * @description
 */
public class HelperTeleport {
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	//----------
	//Static Method.
	//----------
	public static void teleportPlayer(int dimensionID, EntityPlayerMP player) {
		
		BlockPos pos = player.mcServer.getWorld(dimensionID).getSpawnPoint();
		teleportPlayer(dimensionID, pos.getX()+0.5D, pos.getY(), pos.getZ()+0.5D, player);		
		
	}
	
	public static void teleportPlayer(int dimensionID, double posX, double posY, double posZ, EntityPlayerMP player) {
		
		PlayerList playerList = player.mcServer.getPlayerList();
		WorldServer worldServer = player.mcServer.getWorld(AlmagestData.dimensionID_planisphere);
		
		// travel to dimension.
//		player.forceSpawn = true;
		playerList.transferPlayerToDimension(player, dimensionID, new PlaniTeleporter(worldServer));
		player.connection.setPlayerLocation(posX, posY, posZ, player.rotationYaw, player.rotationPitch);
		
	}

}
