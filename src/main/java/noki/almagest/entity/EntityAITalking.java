package noki.almagest.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;


public class EntityAITalking extends EntityAIBase {
	
	private final EntityLiving entity;
	
	public EntityAITalking(EntityLiving entity) {
		
		this.entity = entity;
		this.setMutexBits(0B111);
		
	}
	
	public boolean shouldExecute() {
		
		if((this.entity instanceof ITalkable)) {
			return false;
		}
		if(!this.entity.isEntityAlive()) {
			return false;
		}
		if(this.entity.isInWater()) {
			return false;
		}
		if(!this.entity.onGround) {
			return false;
		}
		if(this.entity.velocityChanged) {
			return false;
		}
		
		EntityPlayer player = ((ITalkable)this.entity).getTalker();
		if(player == null) {
			return false;
		}
		if(this.entity.getDistanceSq(player) > 16.0D) {
			return false;
		}
		
		return player.openContainer != null;
		
	}
	
	public void startExecuting() {
		
		this.entity.getNavigator().clearPath();
		
	}
	
	public void resetTask() {
		
		((ITalkable)this.entity).setTalker(null);
		
	}
	
}
