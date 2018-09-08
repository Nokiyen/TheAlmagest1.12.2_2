package noki.almagest.registry;

import net.minecraft.item.ItemBlock;


/**********
 * @class IWithItemBlock
 *
 * @description 独自のItemBlockを持つBlockに実装させます。登録時に自動でそのItemBlockを登録します。
 */
public interface IWithItemBlock {
	
	
	//******************************//
	// define member methods.
	//******************************//
	abstract ItemBlock getItemBlock();

}
