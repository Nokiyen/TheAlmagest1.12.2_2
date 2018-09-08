package noki.almagest.event;

import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import noki.almagest.AlmagestCore;

public class EventPlayerTick {
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		
		if(event.phase != Phase.END) {
			return;
		}
		
		FoodStats foodStats = event.player.getFoodStats();
		if(foodStats.getSaturationLevel() <= 0) {
			return;
		}
		
		AlmagestCore.log("food level: {} / saturation: {}.", foodStats.getFoodLevel(), foodStats.getSaturationLevel());
		
	}

}
