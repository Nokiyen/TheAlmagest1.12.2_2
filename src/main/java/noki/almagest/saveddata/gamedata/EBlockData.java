package noki.almagest.saveddata.gamedata;

public enum EBlockData implements IItemData {
	
	CONSTELLATION("almagest.gui.block.constellation.display"),
	CONSTELLATOIN_IN("almagest.gui.block.constellation_in.display"),
	STAR("almagest.gui.block.star.display"),
	BOOKREST("almagest.gui.block.bookrest.display"),
	PLANETARIUM("almagest.gui.block.planetarium.display"),
	ARMILLARY_SPHERE("almagest.gui.block.sphere.display");
	
	private EBlockData(String display) {
		this.displayString = display;
	}
	
	private String displayString;
	@Override
	public String getDisplay() {
		return this.displayString;
	}

}
