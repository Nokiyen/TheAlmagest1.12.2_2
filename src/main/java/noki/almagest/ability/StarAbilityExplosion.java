package noki.almagest.ability;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


/**********
 * @class StarAbilityExplosion
 *
 * @description 「星のちから」：アイテム使用後に爆発します。
 * level 1: 爆発する;
 */
public class StarAbilityExplosion extends StarAbility {
	
	public StarAbilityExplosion() {
		
		this.setMaxLevel(1);
		this.setEffectable(Effectable.Weapon, Effectable.Tool, Effectable.Attack, Effectable.Heal, Effectable.Support);
		
	}
	
	@SubscribeEvent
	public void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
		
		EntityLivingBase entity = event.getEntityLiving();
		if(entity.world.isRemote) {
			return;
		}
		
		int[] levels = this.getAbilityLevels(event.getItem());
		if(levels.length == 0) {
			return;
		}
		
		entity.world.createExplosion(null, entity.posX, entity.posY, entity.posZ, 5F, true);
		
	}

}
