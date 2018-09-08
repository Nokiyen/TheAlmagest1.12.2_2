package noki.almagest.saveddata.gamedata;

import net.minecraft.util.ResourceLocation;
import noki.almagest.ModInfo;


public class GameDataHelp extends GameData {
	
	protected EHelpData data;
	
	
	public GameDataHelp(EHelpData data) {
		
		this.data = data;
		
	}
	
	@Override
	public ResourceLocation getName() {
		
		return new ResourceLocation(ModInfo.ID.toLowerCase(), this.data.getDisplay());
		
	}
	
	@Override
	public String getDisplay() {
		
		return this.data.getDisplay();
		
	}
	
	public boolean hasImg() {
		
		return this.data.hasImg();
		
	}
	
	public ResourceLocation getImg() {
		
		return this.data.getImg();
		
	}
	
}
