package noki.almagest.saveddata;

public enum DataCategory {
	
	BLOCK("almagest.gamedata.category.block"),
	ITEM("almagest.gamedata.category.item"),
	LIST("almagest.gamedata.category.list"),
	RECIPE("almagest.gamedata.category.recipe"),
	MOB("almagest.gamedata.category.mob"),
	ABILITY("almagest.gamedata.category.ability"),
	CONSTELLATION("almagest.gamedata.category.const"),
	MEMO("almagest.gamedata.category.memo"),
	HELP("almagest.gamedata.category.help");
//	SEARCH("almagest.gamedata.category.search");
	
	private String displayName;
	
	private DataCategory(String displayName) {
		
		this.displayName = displayName;
		
	}
	
	public String getDisplay() {
		
		return this.displayName;
		
	}
	
}
