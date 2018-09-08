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


/**********
 * @class ItemOrigamiCrane
 *
 * @description 
 */
public class ItemPureWater extends ItemWithAttribute implements IWithRecipe, IWithEvent {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemPureWater() {
		
		this.setHasSubtypes(true);
		this.setAttributeLevel(EStarAttribute.LIQUID, 30);
		
	}
	
	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				new StarRecipe(new ItemStack(this,4)).setAttribute(EStarAttribute.FUEL, 20).setAttribute(EStarAttribute.LIQUID, 40)
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
				if(event.getAttribute() == EStarAttribute.LIQUID) event.setLevel(event.getLevel()+10);
			case 4:
			case 5:
			case 6:
				break;
		}
		
	}

}
