package noki.almagest.registry;

import java.util.List;
import net.minecraft.item.ItemStack;


/**********
 * @class IWithSubtypes
 *
 * @description メタデータで種類があるブロック・アイテムにこれを実装させます。実装させると、jsonなどを自動で登録します。
 */
public interface IWithSubTypes {
	
	
	//******************************//
	// define member methods.
	//******************************//
	abstract int getSubtypeCount();
	abstract List<ItemStack> getSubItems();
	abstract boolean registerToAlmagest();

}
