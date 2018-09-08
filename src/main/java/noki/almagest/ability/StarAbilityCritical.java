package noki.almagest.ability;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


/**********
 * @class StarAbilityCritical
 *
 * @description 「星のちから」：一定の確率でクリティカルします。
 * level 1: +20%;
 * level 2: +30%;
 * level 3: +50%;
 * level 4: +100%;
 */
public class StarAbilityCritical extends StarAbility {
	
	private static float[] scale = {0.2F, 0.3F, 0.5F, 1F};
	
	public StarAbilityCritical() {
		
		this.setMaxLevel(4);
		this.setEffectable(Effectable.Weapon, Effectable.Amulet);
		
	}
	
	@SubscribeEvent
	public void onCritical(CriticalHitEvent event) {
		
		if(event.isVanillaCritical()) {
			return;
		}
		
		float totalScale = 0;
		int[] levels = this.getAbilityLevels(event.getEntityPlayer().getActiveItemStack());
		for(int eachLevel: levels) {
			totalScale += scale[eachLevel-1];
		}
		totalScale = MathHelper.clamp(totalScale, 0.0F, 1.0F);
		if(event.getEntityPlayer().world.rand.nextFloat() <= totalScale) {
			event.setResult(Result.ALLOW);
		}
		
	}

}
