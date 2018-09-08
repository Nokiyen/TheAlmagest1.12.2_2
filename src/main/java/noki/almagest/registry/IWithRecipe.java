package noki.almagest.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;


/**********
 * @class IWithRecipe
 *
 * @description レシピを持っているブロック・アイテムに対して実装させます。これを実装させると、登録時に自動でレシピも登録します。
 * IRecipeで登録する必要のあるブロック・アイテムにだけ必要です(このmodではほとんどがそう)。
 */
public interface IWithRecipe {
	
	
	//******************************//
	// define member methods.
	//******************************//
	abstract List<IRecipe> getRecipe();
	
	public default IRecipe getRecipe(ItemStack stack) {
		
		return this.getRecipe().get(0);
				
	}
	
	public default List<IRecipe> makeRecipeList(IRecipe... recipes) {
		
		List<IRecipe> list = new ArrayList<IRecipe>();
		for(IRecipe each: recipes) {
			list.add(each);
		}
		return list;
		
	}

}
