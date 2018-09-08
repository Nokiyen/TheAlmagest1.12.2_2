package noki.almagest.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtTalkingEntity extends EntityAIWatchClosest {
	
	public EntityAILookAtTalkingEntity(EntityLiving entity) {
		
		super(entity, EntityPlayer.class, 8.0F);
		this.setMutexBits(0B111);
		
    }
	
	@Override
	public boolean shouldExecute() {
		
		if(!(this.entity instanceof ITalkable)) {
			return false;
		}
		
		if(((ITalkable)this.entity).isTalking()) {
			this.closestEntity = ((ITalkable)this.entity).getTalker();
			return true;
		}
		return false;
		
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		
		if(((ITalkable)this.entity).isTalking()) {
			return true;
		}
		
		return super.shouldContinueExecuting();
		
	}

}
