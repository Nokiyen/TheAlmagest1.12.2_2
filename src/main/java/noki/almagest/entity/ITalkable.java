package noki.almagest.entity;

import net.minecraft.entity.player.EntityPlayer;

public interface ITalkable {
	
	abstract boolean isTalking();
	abstract EntityPlayer getTalker();
	abstract void setTalker(EntityPlayer entity);
	
}
