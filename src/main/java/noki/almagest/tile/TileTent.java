package noki.almagest.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import noki.almagest.helper.HelperNBTTag;


/**********
 * @class TileTent
 *
 * @description
 */
public class TileTent extends TileEntity {
	
	//******************************//
	// define member variables.
	//******************************//
	private int sizeLevel;
	private ItemStack wallStack;
	
	private static final String NBT_sizeLevel = "size_level";
	private static final String NBT_wallStack = "wall_stack";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public void setInfo(int sizeLevel, ItemStack wallStack) {
		
		this.sizeLevel = sizeLevel;
		this.wallStack = wallStack;
		
	}
	
	public int getSizeLevel() {
		
		return this.sizeLevel;
		
	}
	
	public ItemStack getWallStack() {
		
		return this.wallStack;
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		super.readFromNBT(nbt);
		HelperNBTTag helper = new HelperNBTTag(nbt);
		this.sizeLevel = helper.getInteger(NBT_sizeLevel);
		this.wallStack = new ItemStack(helper.getTag(NBT_wallStack));
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		
		HelperNBTTag helper = new HelperNBTTag(nbt);
		helper.setInteger(NBT_sizeLevel, this.sizeLevel);
		if(this.wallStack != null) {
			helper.setTag(NBT_wallStack, this.wallStack.serializeNBT());
		}
		
		return super.writeToNBT(nbt);
		
	}

}
