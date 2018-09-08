package noki.almagest.item;

import java.util.List;

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
 * @class ItemOil
 *
 * @description 
 */
public class ItemOil extends ItemWithAttribute implements IWithRecipe, IWithEvent {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemOil() {
		
		this.setHasSubtypes(true);
		this.setAttributeLevel(EStarAttribute.FUEL, 30);
		
	}
	
	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				new StarRecipe(new ItemStack(this,4))
					.setAttribute(EStarAttribute.ANIMAL, 20).setStack(new ItemStack(ModItems.COCKTAIL,1,0)).setSpecial(true),
				new StarRecipe(new ItemStack(this,4))
					.setAttribute(EStarAttribute.ANIMAL, 20).setStack(new ItemStack(ModItems.COCKTAIL,1,1)).setSpecial(true),
				new StarRecipe(new ItemStack(this,4))
					.setAttribute(EStarAttribute.ANIMAL, 20).setStack(new ItemStack(ModItems.COCKTAIL,1,2)).setSpecial(true),
				new StarRecipe(new ItemStack(this,4))
					.setAttribute(EStarAttribute.ANIMAL, 20).setStack(new ItemStack(ModItems.COCKTAIL_RAINBOW,1)).setSpecial(true),
				new StarRecipe(new ItemStack(this,4))
					.setAttribute(EStarAttribute.MONSTER, 20).setStack(new ItemStack(ModItems.COCKTAIL,1,0)).setSpecial(true),
				new StarRecipe(new ItemStack(this,4))
					.setAttribute(EStarAttribute.MONSTER, 20).setStack(new ItemStack(ModItems.COCKTAIL,1,1)).setSpecial(true),
				new StarRecipe(new ItemStack(this,4))
					.setAttribute(EStarAttribute.MONSTER, 20).setStack(new ItemStack(ModItems.COCKTAIL,1,2)).setSpecial(true),
				new StarRecipe(new ItemStack(this,4))
					.setAttribute(EStarAttribute.MONSTER, 20).setStack(new ItemStack(ModItems.COCKTAIL_RAINBOW,1)).setSpecial(true)
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
			case 4:
				if(event.getAttribute() == EStarAttribute.FUEL) event.setLevel(event.getLevel()+10);
			case 5:
				if(event.getAttribute() == EStarAttribute.FOOD) event.setLevel(event.getLevel()+10);
			case 6:
				break;
		}
		
	}

}
