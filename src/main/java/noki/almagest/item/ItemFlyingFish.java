package noki.almagest.item;

import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.ItemFoodWithAttribute;


/**********
 * @class IItemFlyingFish
 *
 * @description とびうおのアイテムです。とびうお座のクラフトに必要です。
 */
public class ItemFlyingFish extends ItemFoodWithAttribute {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemFlyingFish() {
		
		super(2, 0.1F, false);
		this.setAttributeLevel(EStarAttribute.ANIMAL, 10);
		this.setAttributeLevel(EStarAttribute.LIQUID, 5);
		
	}

}
