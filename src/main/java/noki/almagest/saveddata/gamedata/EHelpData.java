package noki.almagest.saveddata.gamedata;

import net.minecraft.util.ResourceLocation;
import noki.almagest.ModInfo;

public enum EHelpData implements IItemData {
	
	HELP001(1, true),
	HELP002(2, true),
	HELP003(3, true),
	HELP004(4, true),
	HELP005(5, true),
	HELP006(6, true),
	HELP007(7, true),
	HELP008(8, true),
	HELP009(9, true),
	HELP010(10, true),
	HELP011(11, true),
	HELP012(12, true),
	;
	
	private int id;
	private boolean hasImg;
	private ResourceLocation imgResouce;
	
	private EHelpData(int id, boolean hasImg) {
		
		this.id = id;
		this.hasImg = hasImg;
		this.imgResouce = new ResourceLocation(ModInfo.ID.toLowerCase(), "help/"+String.format("%03d", this.id)+".jpg");
		
	}
	
	@Override
	public String getDisplay() {
		
		return "almagest.gui.almagest.help."+String.format("%03d", this.id);
		
	}
	
	public boolean hasImg() {
		
		return this.hasImg;
		
	}
	
	public ResourceLocation getImg() {
		
		return this.imgResouce;
		
	}

}
