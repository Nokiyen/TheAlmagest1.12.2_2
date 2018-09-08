package noki.almagest.item;

import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.ItemWithAttribute;
import noki.almagest.event.post.AttributeLevelEvent;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithEvent;
import noki.almagest.registry.IWithRecipe;
import noki.almagest.registry.ModItems;


/**********
 * @class ItemCocktail
 *
 * @description 
 */
public class ItemCocktailRainbow extends ItemWithAttribute implements IWithRecipe, IWithEvent {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemCocktailRainbow() {
		
		this.setHasSubtypes(true);
		this.setAttributeLevel(EStarAttribute.TOOL, 40);
		this.setAttributeLevel(EStarAttribute.STAR, 40);
		
	}
	
	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				new StarRecipe(new ItemStack(this,4))
					.setStack(new ItemStack(Items.REDSTONE,6)).setStack(new ItemStack(Items.DYE,6,4))
					.setStack(new ItemStack(ModItems.PURE_WATER)).setAttribute(EStarAttribute.STAR, 40)
		);
		
	}
	
	@SubscribeEvent
	public void onAttributeLevel(AttributeLevelEvent event) {
		
		if(event.getStack() == null || event.getStack().getItem() != this) {
			return;
		}
		
		switch(StarPropertyCreator.getMagnitude(event.getStack())) {
			case 1:
			case 2:
			case 3:
				if(event.getAttribute() == EStarAttribute.EXPLOSIVE) event.setLevel(event.getLevel()+40);
			case 4:
				if(event.getAttribute() == EStarAttribute.TOOL) event.setLevel(event.getLevel()+20);
			case 5:
			case 6:
				break;
		}
		
	}

}
