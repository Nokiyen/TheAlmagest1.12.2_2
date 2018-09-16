package noki.almagest.event;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.AlmagestData;
import noki.almagest.helper.HelperTeleport;


/**********
 * @class EventSleep
 *
 * @description
 * @description_en
 */
public class EventSleep {
	
	//******************************//
	// define member variables.
	//******************************//
	public static BlockPos posForPlani;
	
	
	
	//******************************//
	// define member methods.
	//******************************//
	@SubscribeEvent
	public void onSleepInBed(PlayerSleepInBedEvent event) {
		
		if(event.getEntityPlayer().world.provider.getDimension() == 0 && AlmagestCore.savedDataManager.getStoryData().getCurrentStory() == 0) {
			posForPlani = event.getEntityPlayer().getPosition();
			if(!event.getEntityPlayer().world.isRemote) {
				HelperTeleport.teleportPlayer(AlmagestData.dimensionID_planisphere, 0.5D, 90, 39.5D, (EntityPlayerMP)event.getEntityPlayer());
			}
		}
		
	}

}
