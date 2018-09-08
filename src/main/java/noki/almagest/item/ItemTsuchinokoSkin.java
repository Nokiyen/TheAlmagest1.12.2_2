package noki.almagest.item;

import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.ItemWithAttribute;


/**********
 * @class ItemTsuchinokoSkin
 *
 * @description つちのこの皮のアイテムです。へび系星座とへび系アイテムのクラフトに必要です。
 */
public class ItemTsuchinokoSkin extends ItemWithAttribute {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemTsuchinokoSkin() {
		
		this.setAttributeLevel(EStarAttribute.ANIMAL, 30);
		
	}

}
