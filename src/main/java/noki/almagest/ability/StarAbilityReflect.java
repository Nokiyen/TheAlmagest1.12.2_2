package noki.almagest.ability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;


/**********
 * @class StarAbilityBuff
 *
 * @description 「星のちから」：飛び道具を跳ね返します。
 * level 1: 10%;
 * level 2: 20%;
 * level 3: 30%;
 * level 4: 50%;
 */
public class StarAbilityReflect extends StarAbility {
	
	private static float[] prob = {0.1F, 0.2F, 0.3F, 1.0F};
	
	public StarAbilityReflect() {
		
		this.setMaxLevel(4);
		
	}
	
	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		
		if(!event.getSource().isProjectile()) {
			return;
		}
		if(event.getSource().getImmediateSource() == null) {
			return;
		}
		Entity entity = event.getSource().getImmediateSource();
		if(!(entity.world instanceof WorldServer)) {
			return;
		}
		
		int maxLevel = 0;
		for(ItemStack each: event.getEntityLiving().getArmorInventoryList()) {
			int[] levels = this.getAbilityLevels(each);
			for(int eachLevel: levels) {
				maxLevel = Math.max(maxLevel, eachLevel);
			}
		}
		
		if(maxLevel == 0) {
			return;
		}
		
		if(event.getEntityLiving().getRNG().nextFloat() < prob[maxLevel-1]) {
			AlmagestCore.log("projectile is reflected.");
			event.setCanceled(true);
			if(entity instanceof EntityFireball) {
				EntityFireball fireball = (EntityFireball)entity;
				fireball.motionX = -1 * fireball.motionX;
				fireball.motionY = -1 * fireball.motionY;
				fireball.motionZ = -1 * fireball.motionZ;
				fireball.accelerationX = fireball.motionX * 0.1D;
				fireball.accelerationY = fireball.motionY * 0.1D;
				fireball.accelerationZ = fireball.motionZ * 0.1D;
			}
			else {
				double difX = entity.posX = entity.prevPosX;
				double difY = entity.posY = entity.prevPosY;
				double difZ = entity.posZ = entity.prevPosZ;
				double scale = 5/Math.sqrt(difX*difX + difY*difY + difZ*difZ);
				entity.motionX = -1 * difX*scale;
				entity.motionY = -1 * difY*scale;
				entity.motionZ = -1 * difZ*scale;
			}
		}
		
	}
	
	

}
