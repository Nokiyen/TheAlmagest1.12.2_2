package noki.almagest.ability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


/**********
 * @class StarAbilityBuff
 *
 * @description 「星のちから」：獲得する経験値の量が増えます。
 * level 1: +10%;
 * level 2: +30%;
 * level 3: +50%;
 * level 4: +100%;
 */
public class StarAbilityXP extends StarAbility {
	
	private static float[] scale = {0.1F, 0.3F, 0.5F, 1F};
	
	public StarAbilityXP() {
		
		this.setMaxLevel(4);
		this.setEffectable(Effectable.Armor, Effectable.Amulet);
		
	}
	
	@SubscribeEvent
	public void onPotionEffectApply(PlayerPickupXpEvent event) {
		
		float totalScale = 0;
		for(ItemStack each: event.getEntityPlayer().getArmorInventoryList()) {
			int[] levels = this.getAbilityLevels(each);
			for(int eachLevel: levels) {
				totalScale += scale[eachLevel-1];
			}
		}
		
		event.getOrb().xpValue += (int)Math.floor((float)event.getOrb().xpValue * totalScale);
		
	}

}
