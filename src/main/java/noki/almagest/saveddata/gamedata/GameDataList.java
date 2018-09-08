package noki.almagest.saveddata.gamedata;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;


public class GameDataList extends GameData {
	
	protected ItemStack stack;
	
	
	public GameDataList(ItemStack stack) {
		
		this.stack = stack;
		
	}
	
	@Override
	public ResourceLocation getName() {
		
		return new ResourceLocation("minecraft", this.stack.getUnlocalizedName());
		
	}
	
	public ItemStack getStack() {
		
		return this.stack;
		
	}
	
	@Override
	public String getDisplay() {
		
		return this.stack.getUnlocalizedName()+".name";
		
	}
	
}
