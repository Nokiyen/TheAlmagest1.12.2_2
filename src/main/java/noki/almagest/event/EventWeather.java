package noki.almagest.event;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;


/**********
 * @class EventWeather
 *
 * @description
 * 
 */
public class EventWeather {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	@SubscribeEvent
	public void onJoinWorld(EntityJoinWorldEvent event) {
		
		if(event.getWorld().provider.getDimension() != 0) {
			return;
		}
		
		if(event.getEntity() == null) {
			return;
		}
		
		if(!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}
		
		if(AlmagestCore.savedDataManager.getStoryData().getStoryFlag(1000) == false) {
			WorldInfo worldinfo = event.getWorld().getWorldInfo();
			worldinfo.setCleanWeatherTime(0);
			worldinfo.setRainTime(Integer.MAX_VALUE);
			worldinfo.setThunderTime(Integer.MAX_VALUE);
			worldinfo.setRaining(true);
//			worldinfo.setThundering(true);
		}
		else if(AlmagestCore.savedDataManager.getStoryData().checkStoryFlag(1001) == true) {
			int cleanTime = (300 + (new Random()).nextInt(600)) * 20;
			WorldInfo worldinfo = event.getWorld().getWorldInfo();
			worldinfo.setCleanWeatherTime(cleanTime);
			worldinfo.setRainTime(0);
			worldinfo.setThunderTime(0);
			worldinfo.setRaining(false);
			worldinfo.setThundering(false);
			
			AlmagestCore.savedDataManager.getStoryData().markStoryFlag(1001);
		}
		
	}
	
/*	@SubscribeEvent
	public void onVillagerInteract(EntityInteract event) {
		
		if(event.getTarget() == null) {
			return;
		}
		if(!(event.getTarget() instanceof EntityVillager)) {
			return;
		}
		
		event.setCanceled(true);
		event.getEntityPlayer().openGui(AlmagestCore.instance, 100, event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
//		Minecraft.getMinecraft().displayGuiScreen(new GuiContainerSample(new ContainerSample()));
		
	}*/
	
/*	@SubscribeEvent
	public void onMiraInteract(EntityInteract event) {
		
		if(event.getTarget() == null) {
			return;
		}
		if(!(event.getTarget() instanceof EntityMira)) {
			return;
		}
		
		if(!event.getWorld().isRemote) {
			event.getEntityPlayer().openGui(AlmagestCore.instance, AlmagestData.guiID_mira,
					event.getWorld(), (int)event.getEntityPlayer().posX, (int)event.getEntityPlayer().posY, (int)event.getEntityPlayer().posZ);
			switch(AlmagestCore.savedDataManager.getStoryData().getStoryFlag()) {
				case 0:
					AlmagestCore.savedDataManager.getStoryData().goToNextStory();
					break;
				case 1:
					AlmagestCore.savedDataManager.getStoryData().goToNextStory();
					break;
				case 2:
					AlmagestCore.savedDataManager.getStoryData().goToNextStory();
					break;
			}
			PacketHandler.instance.sendToAll(new PacketSyncStory(AlmagestCore.savedDataManager.getStoryData().getStoryFlag()));
		}
		
	}*/


}
