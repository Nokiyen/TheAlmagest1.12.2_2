package noki.almagest.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import noki.almagest.AlmagestCore;
import noki.almagest.ModInfo;
import noki.almagest.block.BlockConstellation;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperObtainedMessage;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.saveddata.gamedata.GameDataBlock;
import noki.almagest.saveddata.gamedata.GameDataRecipe;


public class RecipeRegistry {
	
	private static Map<String, Block> recipesForBlock = new HashMap<>();
	private static Map<String, Item> recipesForItem = new HashMap<>();
	private static ArrayList<IRecipe> additionalRecipe = new ArrayList<>();
	
	private static Map<ItemStack, ArrayList<ItemStack>> recipeHints = new HashMap<>();
	
	
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
			IRecipe targetRecipe;
			if(withRecipe.getRecipe().size() == 1) {
				targetRecipe = withRecipe.getRecipe().get(0);
				ForgeRegistries.RECIPES.register(
						targetRecipe.setRegistryName(new ResourceLocation(ModInfo.ID.toLowerCase(), entry.getKey())));
				if(targetRecipe instanceof StarRecipe) {
					registerRecipeHint((StarRecipe)targetRecipe);
				}
			}
			else {
				for(int i=0; i<withRecipe.getRecipe().size(); i++) {
					targetRecipe = withRecipe.getRecipe().get(i);
					ForgeRegistries.RECIPES.register(targetRecipe
							.setRegistryName(new ResourceLocation(ModInfo.ID.toLowerCase(), entry.getKey()+"_"+i)));
					if(targetRecipe instanceof StarRecipe) {
						registerRecipeHint((StarRecipe)targetRecipe);
					}
				}
			}
		}
		
		for(Map.Entry<String, Item> entry: recipesForItem.entrySet()) {
			if(!(entry.getValue() instanceof IWithRecipe)) {
				break;
			}
			
			IWithRecipe withRecipe = (IWithRecipe)entry.getValue();
			IRecipe targetRecipe;
			if(withRecipe.getRecipe().size() == 1) {
				targetRecipe = withRecipe.getRecipe().get(0);
				ForgeRegistries.RECIPES.register(
						withRecipe.getRecipe().get(0).setRegistryName(new ResourceLocation(ModInfo.ID.toLowerCase(), entry.getKey())));
				if(targetRecipe instanceof StarRecipe) {
					registerRecipeHint((StarRecipe)targetRecipe);
				}
			}
			else {
				for(int i=0; i<withRecipe.getRecipe().size(); i++) {
					targetRecipe = withRecipe.getRecipe().get(i);
					ForgeRegistries.RECIPES.register(
							withRecipe.getRecipe().get(i).setRegistryName(new ResourceLocation(ModInfo.ID.toLowerCase(), entry.getKey()+"_"+i)));
					if(targetRecipe instanceof StarRecipe) {
						registerRecipeHint((StarRecipe)targetRecipe);
					}
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
//		additionalRecipe.clear();
		
	}
	
	public static void registerRecipeHint(StarRecipe recipe) {
		
		AlmagestCore.log("try to add recipe hint / {}.", recipe.getRecipeOutput().getDisplayName());
		
		if(!recipe.hasHint() || recipe.getHintItems() == null) {
			return;
		}
		
		ArrayList<ItemStack> hints = recipe.getHintItems();
		for(ItemStack eachHint: hints) {
			ArrayList<ItemStack> registeredRecipe = null;
			for(ItemStack eachKey: recipeHints.keySet()) {
				if(ItemStack.areItemsEqual(eachHint, eachKey)) {
					registeredRecipe = recipeHints.get(eachKey);
					break;
				}
			}
			if(registeredRecipe == null) {
				registeredRecipe = new ArrayList<ItemStack>();
				recipeHints.put(eachHint, registeredRecipe);
			}
			boolean alreadyRegistered = false;
			for(ItemStack eachRegistered: registeredRecipe) {
				if(ItemStack.areItemsEqual(recipe.getRecipeOutput(), eachRegistered)) {
					alreadyRegistered = true;
					break;
				}
			}
			if(!alreadyRegistered) {
				AlmagestCore.log("hint: {} for {}.", eachHint.getDisplayName(), recipe.getRecipeOutput().getDisplayName());
				registeredRecipe.add(recipe.getRecipeOutput());
			}
		}
		
	}
	
	public static void checkRecipeHint(ItemStack stack) {
		
		for(IRecipe eachRecipe: additionalRecipe) {
			if(ItemStack.areItemsEqual(stack, eachRecipe.getRecipeOutput())) {
				GameDataRecipe recipeData = AlmagestCore.savedDataManager.getFlagData().getRecipe(eachRecipe);
				if(recipeData != null && !recipeData.isObtained()) {
					AlmagestCore.savedDataManager.getFlagData().setObtained(recipeData);
					HelperObtainedMessage.sendRecipeMessage(recipeData);
					return;
				}
			}
		}
		
		ArrayList<ItemStack> registeredRecipe = null;
		for(ItemStack eachKey: recipeHints.keySet()) {
			if(eachKey.getItem() instanceof ItemBlock && Block.getBlockFromItem(eachKey.getItem()) instanceof BlockConstellation
					&& stack.getItem() instanceof ItemBlock && Block.getBlockFromItem(stack.getItem()) instanceof BlockConstellation) {
				if(HelperConstellation.getConstNumber(eachKey) == HelperConstellation.getConstNumber(stack)) {
					registeredRecipe = recipeHints.get(eachKey);
				}
			}
			else if(ItemStack.areItemsEqual(eachKey, stack)) {
				registeredRecipe = recipeHints.get(eachKey);
				break;
			}
		}
		
		if(registeredRecipe == null) {
			return;
		}
		
		for(ItemStack eachRecipe: registeredRecipe) {
			GameDataBlock targetData;
			if(eachRecipe.getItem() instanceof ItemBlock) {
				targetData = AlmagestCore.savedDataManager.getFlagData().getBlock(eachRecipe);
			}
			else {
				targetData = AlmagestCore.savedDataManager.getFlagData().getItem(eachRecipe);
			}
			if(targetData == null) {
				continue;
			}
			
			if(targetData.hasRecipe() && !targetData.recipeObtained()) {
				AlmagestCore.savedDataManager.getFlagData().setRecipeObtained(targetData);
				HelperObtainedMessage.sendRecipeMessage(targetData);
			}
		}
		
	}
	
}
