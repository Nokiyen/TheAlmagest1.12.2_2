package noki.almagest.ability;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.event.post.AttributeLevelEvent;


/**********
 * @class StarAbilityAttribute
 *
 * @description 「星のちから」：組成の値を上げます。
 * level 1: +10;
 * level 2: +20;
 * level 3: +30;
 * level 4: +40;
 */
public class StarAbilityAttribute extends StarAbility {
	
	private static int[] add = {10, 20, 30, 40};
	
	public StarAbilityAttribute() {
		
		this.setMaxLevel(4);
		this.setEffectable(Effectable.Weapon, Effectable.Armor, Effectable.Amulet, Effectable.Tool,
				Effectable.Attack, Effectable.Heal, Effectable.Support, Effectable.Block);
		
	}
	
	@SubscribeEvent
	public void onAttributeLevel(AttributeLevelEvent event) {
		
		if(event.getLevel() == 0) {
			return;
		}
		
		int totalAdd = event.getLevel();
		int[] levels = this.getAbilityLevels(event.getStack());
		for(int eachLevel: levels) {
			totalAdd += add[eachLevel-1];
		}
		
		event.setLevel(totalAdd);
		
	}

}
