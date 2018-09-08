package noki.almagest.ability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


/**********
 * @class StarAbilityHeal
 *
 * @description 「星のちから」：食べ物系のアイテムを使用した時に、回復量が増えます。
 * level 1: heal +2, saturation + 0.1;
 * level 2: heal +4, saturation + 0.2;
 * level 3: heal +8, saturation + 0.3;
 * level 4: heal +12, saturation + 0.4;
 */
public class StarAbilityHeal extends StarAbility {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private static int[] healAmount = {2, 4, 8, 12};
	private static float[] saturationAmount = {0.1F, 0.2F, 0.3F, 0.4F};
	
	
	//******************************//
	// define member methods.
	//******************************//
	public StarAbilityHeal() {
		
		this.setMaxLevel(4);
		this.setEffectable(Effectable.Heal);
		
	}
	
	@SubscribeEvent
	public void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
		
		ItemStack stack = event.getItem();
		int[] levels = this.getAbilityLevels(stack);
		
		if(levels.length == 0) {
			return;
		}
		
		if(!(stack.getItem() instanceof ItemFood)) {
			return;
		}
		
		if(event.getEntityLiving() == null || !(event.getEntityLiving() instanceof EntityPlayer)) {
			return;
		}
		
		ItemFood food = (ItemFood)stack.getItem();
		
		int healSum = 0;
		float saturationSum = 0;
		for(int each: levels) {
			int clampLevel = MathHelper.clamp(each, 1, this.getMaxLevel());
			healSum += healAmount[clampLevel-1];
			saturationSum += saturationAmount[clampLevel-1];
		}
		((EntityPlayer)event.getEntityLiving())
			.getFoodStats().addStats(food.getHealAmount(stack) + healSum, food.getSaturationModifier(stack) + saturationSum);
		
	}

}
