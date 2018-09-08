package noki.almagest.saveddata.gamedata;

import net.minecraft.util.ResourceLocation;

public class GameDataMemo extends GameData {
	
	private EMemoData memo;
	
	public GameDataMemo(EMemoData memo) {
		
		this.memo = memo;
		this.name = new ResourceLocation("almagest", this.memo.name());
		
	}
	
	public EMemoData getMemo() {
		
		return this.memo;
		
	}
	
	@Override
	public String getDisplay() {
		
		return this.memo.getDisplay();
		
	}

}
