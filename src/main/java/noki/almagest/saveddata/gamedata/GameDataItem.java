package noki.almagest.saveddata.gamedata;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class GameDataItem extends GameDataBlock {
	
	private Item dataItem;

	public GameDataItem(ItemStack stack, boolean hasRecipe) {
		
		super(stack, hasRecipe);
		this.dataItem = stack.getItem();
		
	}
	
	public Item getItem() {
		
		return this.dataItem;
		
	}
	
/*	@Override
	public ResourceLocation getName() {
		
		ResourceLocation location = this.dataItem.getRegistryName();
		if(location == null){
			return new ResourceLocation("");
		}
		return location;
//		return new ResourceLocation(this.dataBlock.getUnlocalizedName());
		
	}*/
	
/*	@Override
	public String getDisplay() {
		
		return this.dataItem.getUnlocalizedName()+".name";
		
	}*/

}
