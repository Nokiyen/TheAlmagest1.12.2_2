package noki.almagest.saveddata.gamedata;

import noki.almagest.ability.StarAbilityCreator.StarAbility;

public class GameDataAbility extends GameData {
	
	private StarAbility abitlity;
	
	public GameDataAbility(StarAbility ability) {
		
		this.abitlity = ability;
		
	}
	
	public StarAbility getAbility() {
		
		return this.abitlity;
		
	}
	
	@Override
	public String getDisplay() {
		
		return this.abitlity.getLocalizedName();
		
	}
	

}
