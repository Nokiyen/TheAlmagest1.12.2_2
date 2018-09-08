package noki.almagest.saveddata.gamedata;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;


public class GameDataRecipe extends GameData {
	
	protected IRecipe recipe;
	
	
	public GameDataRecipe(IRecipe recipe) {
		
		this.recipe = recipe;
		
	}
	
	@Override
	public ResourceLocation getName() {
		
		return this.recipe.getRegistryName();
		
	}
	
	@Override
	public String getDisplay() {
		
		return this.recipe.getRecipeOutput().getDisplayName();
		
	}
	
	public IRecipe getRecipe() {
		
		return this.recipe;
		
	}
	
}
