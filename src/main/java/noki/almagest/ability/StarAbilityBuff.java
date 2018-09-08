package noki.almagest.ability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.asm.event.PotionApplyEvent;


/**********
 * @class StarAbilityBuff
 *
 * @description 「星のちから」：状態異常を無効化します。
 * level 1: 50%;
 * level 2: 60%;
 * level 3: 70%;
 * level 4: 99%;
 */
public class StarAbilityBuff extends StarAbility {
	
	private static float[] prob = {0.5F, 0.6F, 0.7F, 0.99F};
	
	public StarAbilityBuff() {
		
		this.setMaxLevel(4);
		this.setEffectable(Effectable.Armor, Effectable.Amulet);
		
	}
	
	@SubscribeEvent
	public void onPotionEffectApply(PotionApplyEvent event) {
		
		if(!event.potionEffect.getPotion().isBadEffect()) {
			return;
		}
		
		int maxLevel = 0;
		for(ItemStack each: event.entityLivingBase.getArmorInventoryList()) {
			int[] levels = this.getAbilityLevels(each);
			for(int eachLevel: levels) {
				maxLevel = Math.max(maxLevel, eachLevel);
			}
		}
		
		if(maxLevel != 0) {
			if(event.entityLivingBase.getRNG().nextFloat() < prob[maxLevel-1]) {
				AlmagestCore.log("potion effect is canceled.");
				event.setCanceled(true);
			}
		}
		
	}
	
	

}
