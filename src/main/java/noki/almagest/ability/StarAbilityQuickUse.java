package noki.almagest.ability;

import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


/**********
 * @class StarAbilityQuickUse
 *
 * @description 「星のちから」：アイテムの使用時間が減ります。
 * level 1: -10%;
 * level 2: -20%;
 * level 3: -30%;
 * level 4: -50%;
 */
public class StarAbilityQuickUse extends StarAbility {
	
	private static float[] scale = {0.1F, 0.2F, 0.3F, 0.5F};
	
	public StarAbilityQuickUse() {
		
		this.setMaxLevel(4);
		this.setEffectable(Effectable.Attack, Effectable.Heal, Effectable.Support);
		
	}
	
	@SubscribeEvent
	public void onItemUseStart(LivingEntityUseItemEvent.Start event) {
		
		float totalScale = 1F;
		int[] levels = this.getAbilityLevels(event.getItem());
		for(int eachLevel: levels) {
			totalScale = Math.max(totalScale - scale[eachLevel-1], 0F);
		}
		
		event.setDuration((int)Math.max(Math.floor((float)event.getDuration() * totalScale), 1));
		
	}

}
