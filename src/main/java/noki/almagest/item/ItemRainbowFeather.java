package noki.almagest.item;

import java.util.Arrays;
import java.util.List;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.ItemWithAttribute;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithRecipe;
import noki.almagest.registry.ModItems;


/**********
 * @class ItemRainbowFeather
 *
 * @description 
 */
public class ItemRainbowFeather extends ItemWithAttribute implements IWithRecipe {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemRainbowFeather() {
		
		this.setAttributeLevel(EStarAttribute.ANIMAL, 40);
		
	}

	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				new StarRecipe(new ItemStack(this)) {
					@Override
					public boolean matches(InventoryCrafting inv, World worldIn) {
						int featherCount = 0;
						int[] dyeType = new int[16];
						Arrays.fill(dyeType, 0);
						
						for(int i=0; i<inv.getSizeInventory(); i++) {
							ItemStack selectedStack = inv.getStackInSlot(i);
							if(selectedStack.isEmpty()) {
								continue;
							}
							if(this.isFeather(selectedStack)) {
								featherCount++;
							}
							else if(this.isDye(selectedStack)) {
								dyeType[selectedStack.getMetadata()] = 1;
							}
							else {
								return false;
							}
						}
						
						int dyeTypeSum = 0;
						for(int i=0; i<dyeType.length; i++) {
							dyeTypeSum += dyeType[i];
						}
						if(featherCount == 1 && dyeTypeSum >= 8) {
							return true;
						}
						return false;
					}
					
					private boolean isFeather(ItemStack stack) {
						Item item = stack.getItem();
						if(item == Items.FEATHER || item == ModItems.PHOENIX_FEATHER || item == ModItems.WHITE_FEATHER || item == ModItems.BLACK_FEATHER) {
							return true;
						}
						return false;
					}
					
					private boolean isDye(ItemStack stack) {
						if(stack.getItem() == Items.DYE) {
							return true;
						}
						return false;
					}
				}
			);
		
	}

}
