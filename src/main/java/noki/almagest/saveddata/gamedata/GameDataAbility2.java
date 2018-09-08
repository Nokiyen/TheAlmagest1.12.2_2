package noki.almagest.saveddata.gamedata;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import noki.almagest.ability.StarAbility;

public class GameDataAbility2 extends GameData {
	
	private StarAbility abitlity;
	private int level;
	
	public GameDataAbility2(StarAbility ability, int level) {
		
		this.abitlity = ability;
		this.level = level;
		this.name = new ResourceLocation("almagest", "ability."+this.abitlity.getId()+"."+this.level);
		
	}
	
	public StarAbility getAbility() {
		
		return this.abitlity;
		
	}
	
	public int getLevel() {
		
		return this.level;
		
	}
	
	@Override
	public String getDisplay() {
		
		return new TextComponentTranslation(this.abitlity.getName(this.level)).getFormattedText();
		
	}

}
