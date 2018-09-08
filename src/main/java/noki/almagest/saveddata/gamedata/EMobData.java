package noki.almagest.saveddata.gamedata;

public enum EMobData implements IItemData {
	
	;
	
	private EMobData(String display) {
		this.displayString = display;
	}
	
	private String displayString;
	@Override
	public String getDisplay() {
		return this.displayString;
	}

}
