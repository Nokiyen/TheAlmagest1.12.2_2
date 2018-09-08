package noki.almagest.registry;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noki.almagest.AlmagestCore;


public class AlmagestRegistry {
	
	private static List<Block> almagestForBlock = new ArrayList<>();
	private static List<Item> almagestForItem = new ArrayList<>();
	
	public static void setBlockToAlmagest(Block block) {
		
		almagestForBlock.add(block);
		
	}
	
	public static void setItemToAlmagest(Item item) {
		
		almagestForItem.add(item);
		
	}
	
	public static void register() {
		
		for(Block block: almagestForBlock) {
			boolean hasRecipe = block instanceof IWithRecipe;
			boolean hasSubItems = block instanceof IWithSubTypes;
			if(hasSubItems && ((IWithSubTypes)block).registerToAlmagest()) {
				List<ItemStack> items = ((IWithSubTypes)block).getSubItems();
				for(ItemStack each: items) {
					AlmagestCore.savedDataManager.getFlagData().registerBlock(each, hasRecipe);
				}
			}
			else {
				AlmagestCore.savedDataManager.getFlagData().registerBlock(new ItemStack(block), hasRecipe);
			}
		}
		
		for(Item item: almagestForItem) {
			boolean hasRecipe = item instanceof IWithRecipe;
			boolean hasSubItems = item instanceof IWithSubTypes;
			if(hasSubItems && ((IWithSubTypes)item).registerToAlmagest()) {
				List<ItemStack> items = ((IWithSubTypes)item).getSubItems();
				for(ItemStack each: items) {
					AlmagestCore.savedDataManager.getFlagData().registerItem(each, hasRecipe);
				}
			}
			else {
				AlmagestCore.savedDataManager.getFlagData().registerItem(new ItemStack(item), hasRecipe);
			}
		}
		
		almagestForBlock.clear();
		almagestForItem.clear();
		
	}

}
