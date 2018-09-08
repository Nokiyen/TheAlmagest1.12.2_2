package noki.almagest.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import noki.almagest.AlmagestCore;
import noki.almagest.ModInfo;

public class RecipeRegistry {
	
	private static Map<String, Block> recipesForBlock = new HashMap<>();
	private static Map<String, Item> recipesForItem = new HashMap<>();
	private static ArrayList<IRecipe> additionalRecipe = new ArrayList<>();
	
	public static void setBlockRecipe(Block block, String name) {
		
		recipesForBlock.put(name, block);
		
	}
	
	public static void setItemRecipe(Item item, String name) {
		
		recipesForItem.put(name, item);
		
	}
	
	public static void setAdditionalRecipe(IRecipe recipe) {
		
		additionalRecipe.add(recipe);
		
	}
	
	public static void register() {
		
		for(Map.Entry<String, Block> entry: recipesForBlock.entrySet()) {
			if(!(entry.getValue() instanceof IWithRecipe)) {
				break;
			}
			
			IWithRecipe withRecipe = (IWithRecipe)entry.getValue();
			if(withRecipe.getRecipe().size() == 1) {
				ForgeRegistries.RECIPES.register(
						withRecipe.getRecipe().get(0).setRegistryName(new ResourceLocation(ModInfo.ID.toLowerCase(), entry.getKey())));
			}
			else {
				for(int i=0; i<withRecipe.getRecipe().size(); i++) {
					ForgeRegistries.RECIPES.register(
							withRecipe.getRecipe().get(i).setRegistryName(new ResourceLocation(ModInfo.ID.toLowerCase(), entry.getKey()+"_"+i)));
				}
			}
		}
		
		for(Map.Entry<String, Item> entry: recipesForItem.entrySet()) {
			if(!(entry.getValue() instanceof IWithRecipe)) {
				break;
			}
			
			IWithRecipe withRecipe = (IWithRecipe)entry.getValue();
			if(withRecipe.getRecipe().size() == 1) {
				ForgeRegistries.RECIPES.register(
						withRecipe.getRecipe().get(0).setRegistryName(new ResourceLocation(ModInfo.ID.toLowerCase(), entry.getKey())));
			}
			else {
				for(int i=0; i<withRecipe.getRecipe().size(); i++) {
					ForgeRegistries.RECIPES.register(
							withRecipe.getRecipe().get(i).setRegistryName(new ResourceLocation(ModInfo.ID.toLowerCase(), entry.getKey()+"_"+i)));
				}
			}
		}
		
		for(int i=0; i<additionalRecipe.size(); i++) {
			ForgeRegistries.RECIPES.register(
					additionalRecipe.get(i).setRegistryName(
							new ResourceLocation(ModInfo.ID.toLowerCase(), "recipe.additional."+String.format("%03d", i))));
			AlmagestCore.savedDataManager.getFlagData().registerRecipe(additionalRecipe.get(i));
		}
		
		recipesForBlock.clear();
		recipesForItem.clear();
		additionalRecipe.clear();
		
	}

}
