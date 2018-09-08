package noki.almagest.saveddata.gamedata;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noki.almagest.ModInfo;


public class GameDataBlock extends GameData {
	
	protected static final String key_recipeObtained = "recipe_obtained";
	
	protected ItemStack stack;
	private Block dataBlock;
	protected boolean hasRecipe;
	protected boolean recipeObtained;
	
	
	public GameDataBlock(ItemStack stack, boolean hasRecipe) {
		
		this.stack = stack;
		this.hasRecipe = hasRecipe;
		this.dataBlock = Block.getBlockFromItem(stack.getItem());
		
	}
	
	public boolean hasRecipe() {
		
		return this.hasRecipe;
		
	}
	
	public boolean reciepObtained() {
		
		return this.recipeObtained;
		
	}
	
	@Override
	public ResourceLocation getName() {
		
/*		ResourceLocation location = this.dataBlock.getRegistryName();
		if(location == null){
			return new ResourceLocation("");
		}*/
		return new ResourceLocation(ModInfo.ID.toLowerCase(), this.stack.getUnlocalizedName());
//		return new ResourceLocation(this.dataBlock.getUnlocalizedName());
		
	}
	
	public ItemStack getStack() {
		
		return this.stack;
		
	}
	
	public Block getBlock() {
		
		return this.dataBlock;
		
	}
	
	@Override
	public String getDisplay() {
		
		return this.stack.getUnlocalizedName()+".name";
		
	}
	
	@Override
	public void readFromNbt(NBTTagCompound nbt) {
		
		this.recipeObtained = nbt.getBoolean(key_obtained);
		super.readFromNbt(nbt);
		
	}
	
	@Override
	public NBTTagCompound writeToNbt(NBTTagCompound nbt) {
		
		nbt.setBoolean(key_recipeObtained, this.recipeObtained);
		return super.writeToNbt(nbt);
		
	}

}
