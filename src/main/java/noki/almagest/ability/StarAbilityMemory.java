package noki.almagest.ability;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.event.post.MemoryEvent;


/**********
 * @class StarAbilityMemory
 *
 * @description 「星のちから」：星のきおくの数値を上げます。
 * level 1: +10%;
 * level 2: +20%;
 * level 3: +30%;
 */
public class StarAbilityMemory extends StarAbility {
	
	private static float[] scale = {0.1F, 0.2F, 0.3F, 0.5F};
	
	public StarAbilityMemory() {
		
		this.setMaxLevel(4);
		this.setEffectable(Effectable.Weapon, Effectable.Armor, Effectable.Amulet, Effectable.Tool,
				Effectable.Attack, Effectable.Heal, Effectable.Support, Effectable.Block);
		
	}
	
	@SubscribeEvent
	public void onMemory(MemoryEvent event) {
		
		if(event.getMemory() == 0) {
			return;
		}
		
		float totalScale = 1F;
		int[] levels = this.getAbilityLevels(event.getStack());
		for(int eachLevel: levels) {
			totalScale += scale[eachLevel-1];
		}
		
		event.setMemory((int)Math.floor((float)event.getMemory()*totalScale));
		
	}

}
