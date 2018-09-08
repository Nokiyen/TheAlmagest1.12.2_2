package noki.almagest.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import noki.almagest.AlmagestData;
import noki.almagest.ModInfo;


/**********
 * @class RegistryHelper
 *
 * @description
 * 
 */
public class RegistryHelper {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public static Block registerBlock(Block block, String name) {
		
		return registerBlock(block, name, true);
		
	}
	
	public static Block registerBlock(Block block, String name, boolean json) {
		
		//register item itself.
		block.setRegistryName(ModInfo.ID.toLowerCase(), name).setUnlocalizedName(name).setCreativeTab(AlmagestData.tab);
		ForgeRegistries.BLOCKS.register(block);
		
		Item itemBlock;
		if(block instanceof IWithItemBlock) {
			itemBlock = ((IWithItemBlock)block).getItemBlock();
		}
		else {
			itemBlock = new ItemBlock(block);
		}
		itemBlock.setRegistryName(ModInfo.ID.toLowerCase(), name).setUnlocalizedName(name).setCreativeTab(AlmagestData.tab);
		ForgeRegistries.ITEMS.register(itemBlock);
		
		
		//register recipe if it having.
		boolean hasRecipe = block instanceof IWithRecipe;
		if(hasRecipe) {
			RecipeRegistry.setBlockRecipe(block, name);
		}
		
		if(block instanceof IWithEvent) {
			MinecraftForge.EVENT_BUS.register(block);
		}
		
		if(json == true) {
			JsonRegistry.setBlock(block, name);
		}
		
		AlmagestRegistry.setBlockToAlmagest(block);
		
		
		return block;
		
	}
	
	public static Item registerItem(Item item, String name) {
		
		return registerItem(item, name, true);
		
	}
	
	public static Item registerItem(Item item, String name, boolean json) {
		
		//register item itself.
		item.setRegistryName(ModInfo.ID.toLowerCase(), name).setUnlocalizedName(name).setCreativeTab(AlmagestData.tab);
		ForgeRegistries.ITEMS.register(item);
		
		//register recipe if it having.
		//register recipe if it having.
		boolean hasRecipe = item instanceof IWithRecipe;
		if(hasRecipe) {
			RecipeRegistry.setItemRecipe(item, name);
		}
		
		if(item instanceof IWithEvent) {
			MinecraftForge.EVENT_BUS.register(item);
		}
		
		if(json == true) {
			JsonRegistry.setItem(item, name);
		}
		
		AlmagestRegistry.setItemToAlmagest(item);
		
		return item;
		
	}

}
