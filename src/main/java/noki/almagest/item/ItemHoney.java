package noki.almagest.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.ItemFoodWithAttribute;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithRecipe;


/**********
 * @class ItemHoney
 *
 * @description はちみつのアイテムです。ふうちょう座のクラフトに必要です。
 * @description_en
 */
public class ItemHoney extends ItemFoodWithAttribute implements IWithRecipe {
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemHoney() {
		
		super(4, 0.3F, false);
		this.setAttributeLevel(EStarAttribute.FOOD, 10);
		this.setAttributeLevel(EStarAttribute.LIQUID, 10);
		
	}
	
	@Override
	public int modifyAttribute(EStarAttribute attribute, ItemStack stack) {
		
		if(attribute == EStarAttribute.LIQUID && StarPropertyCreator.isMagnitude(stack, 4)) {
			return super.modifyAttribute(attribute, stack) + 10;
		}
		return super.modifyAttribute(attribute, stack);
		
	}
	
	@Override
	public int getHealAmount(ItemStack stack) {
		
		if(StarPropertyCreator.isMagnitude(stack, 5)) {
			return super.getHealAmount(stack) + 2;
		}
		
		return super.getHealAmount(stack);
		
	}
	
	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
			new StarRecipe(new ItemStack(this)) {
				@Override
				public boolean matches(InventoryCrafting inv, World worldIn) {
					int flowerCount = 0;
					for(int i=0; i<inv.getSizeInventory(); i++) {
						ItemStack selectedStack = inv.getStackInSlot(i);
						if(selectedStack.isEmpty()) {
							continue;
						}
						if(this.isFlower(selectedStack)) {
							flowerCount++;
						}
						else {
							return false;
						}
					}
					
					if(flowerCount == 4) {
						return true;
					}
					return false;
				}
				
				private boolean isFlower(ItemStack stack) {
					Block block = Block.getBlockFromItem(stack.getItem());
					if(block == Blocks.RED_FLOWER || block == Blocks.YELLOW_FLOWER) {
						return true;
					}
					return false;
				}
			}
		);
		
	}

}
