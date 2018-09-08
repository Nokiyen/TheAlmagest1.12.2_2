package noki.almagest.saveddata.gamedata;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;


public class GameDataMob extends GameData {
	
	private EntityLiving entity;
	
	public GameDataMob(EntityLiving entity, ResourceLocation location) {
		
		this.entity = entity;
		this.name = location;
		
	}
	
	public EntityLiving getEntity() {
		
		return this.entity;
		
	}
	
	@Override
	public String getDisplay() {
		
		return this.entity.getName();
		
	}

}
