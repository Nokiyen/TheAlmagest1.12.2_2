package noki.almagest.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noki.almagest.AlmagestData;


/**********
 * @class HelperNBTStack
 *
 * @description
 */
public class HelperNBTStack {
	
	//******************************//
	// define member variables.
	//******************************//
	private ItemStack stack;

	private NBTTagCompound parentNBT;
	private NBTTagCompound childNBT;
	private boolean hasChild;
	private boolean setNewTag;
		
	private static final String NBT_prefix = AlmagestData.NBT_prefix;


	//******************************//
	// define member methods.
	//******************************//
	public HelperNBTStack(ItemStack stack) {
		
		this.stack = stack;
		
		if(stack.getTagCompound() != null) {
			this.parentNBT = stack.getTagCompound();
		}
		else {
			this.parentNBT = new NBTTagCompound();
		}
		
		if(this.parentNBT.hasKey(NBT_prefix)) {
			this.hasChild = true;
		}
		else {
			this.hasChild = false;
		}
		
		this.childNBT = this.parentNBT.getCompoundTag(NBT_prefix);
		
		this.parentNBT.setTag(NBT_prefix, this.childNBT);
		this.setNewTag = false;
//		this.stack.setTagCompound(this.parentNBT);
		
	}
	
	public boolean getBoolean(String key) {
		
		return this.childNBT.getBoolean(key);
		
	}
	
	public int getInteger(String key) {
		
		return this.childNBT.getInteger(key);
		
	}
	
	public String getString(String key) {
		
		return this.childNBT.getString(key);
		
	}
	
	public int[] getIntArray(String key) {
		
		return this.childNBT.getIntArray(key);
		
	}
	
	public NBTTagCompound getTag(String key) {
		
		return this.childNBT.getCompoundTag(key);
		
	}
	
	public HelperNBTStack setBoolean(String key, boolean value) {
		
		this.childNBT.setBoolean(key, value);
		if(!this.setNewTag) {
			this.stack.setTagCompound(this.parentNBT);
			this.setNewTag = true;
		}
		return this;
		
	}
	
	public HelperNBTStack setInteger(String key, int value) {
		
		this.childNBT.setInteger(key, value);
		if(!this.setNewTag) {
			this.stack.setTagCompound(this.parentNBT);
			this.setNewTag = true;
		}
		return this;
	}
	
	public HelperNBTStack setString(String key, String value) {
		
		this.childNBT.setString(key, value);
		if(!this.setNewTag) {
			this.stack.setTagCompound(this.parentNBT);
			this.setNewTag = true;
		}
		return this;
		
	}
	
	public HelperNBTStack setIntArray(String key, int[] value) {
		
		this.childNBT.setIntArray(key, value);
		if(!this.setNewTag) {
			this.stack.setTagCompound(this.parentNBT);
			this.setNewTag = true;
		}
		return this;
		
	}
	
	public HelperNBTStack setTag(String key, NBTTagCompound tag) {
		
		this.childNBT.setTag(key, tag);
		if(!this.setNewTag) {
			this.stack.setTagCompound(this.parentNBT);
			this.setNewTag = true;
		}
		return this;
		
	}
	
	public HelperNBTStack removeTag(String key) {
		
		this.childNBT.removeTag(key);
		return this;
		
	}
	
	public boolean hasKey(String key) {
		
		return this.childNBT.hasKey(key);
		
	}
	
	public NBTTagCompound getNBT() {
		
		return this.parentNBT;
		
	}
	
	public ItemStack getStack() {
		
		return this.stack;
		
	}
	
	public boolean hasChild() {
		
		return this.hasChild;
		
	}
	
}
