package noki.almagest.attribute;


/**********
 * @class EStarProperty
 *
 * @description アイテムの属性のenumです。
 */
public enum EStarAttribute {
	
	MINERAL("almagest.property.mineral.name"),
	METAL("almagest.property.metal.name"),
	JEWEL("almagest.property.jewel.name"),
	PLANT("almagest.property.plant.name"),
	WOOD("almagest.property.wood.name"),
	ANIMAL("almagest.property.animal.name"),
	MONSTER("almagest.property.monster.name"),
	TOOL("almagest.property.tool.name"),
	MACHINE("almagest.property.machine.name"),
	FUEL("almagest.property.fuel.name"),
	PAPER("almagest.property.paper.name"),
	LIQUID("almagest.property.liqiud.name"),
	FOOD("almagest.property.food.name"),
	EXPLOSIVE("almagest.property.explosive.name"),
	DECORATIVE("almagest.property.decorative.name"),
	STAR("almagest.property.star.name");
	
	
	//******************************//
	// define member variables.
	//******************************//
	private String name;
	
	
	//******************************//
	// define member methods.
	//******************************//
	private EStarAttribute(String name) {
		
		this.name = name;
		
	}
	
	public String getName() {
		
		return this.name;
		
	}

}
