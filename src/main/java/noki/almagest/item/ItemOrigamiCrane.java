package noki.almagest.item;

import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.ItemWithAttribute;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithRecipe;


/**********
 * @class ItemOrigamiCrane
 *
 * @description 
 */
public class ItemOrigamiCrane extends ItemWithAttribute implements IWithRecipe {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemOrigamiCrane() {
		
		this.setAttributeLevel(EStarAttribute.TOOL, 10);
		this.setAttributeLevel(EStarAttribute.ANIMAL, 5);
		
	}

	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(new StarRecipe(new ItemStack(this)).setStack(new ItemStack(Items.PAPER)));
		
	}

}
