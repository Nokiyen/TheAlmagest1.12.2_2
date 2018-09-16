package noki.almagest.helper;

import net.minecraft.nbt.NBTTagCompound;
import noki.almagest.AlmagestData;


/**********
 * @class HelperNBTTag
 *
 * @description
 */
public class HelperNBTTag {
	
	//******************************//
	// define member variables.
	//******************************//
	private NBTTagCompound parentNBT;
	private NBTTagCompound childNBT;
		
	private static final String NBT_prefix = AlmagestData.NBT_prefix;

	
	//******************************//
	// define member methods.
	//******************************//
	public HelperNBTTag(NBTTagCompound nbt) {
		
		this.parentNBT = nbt;
		this.childNBT = this.parentNBT.getCompoundTag(NBT_prefix);
		
		this.parentNBT.setTag(NBT_prefix, this.childNBT);
		
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
	
	public HelperNBTTag setBoolean(String key, boolean value) {
		
		this.childNBT.setBoolean(key, value);
		return this;
		
	}
	
	public HelperNBTTag setInteger(String key, int value) {
		
		this.childNBT.setInteger(key, value);
		return this;
	}
	
	public HelperNBTTag setString(String key, String value) {
		
		this.childNBT.setString(key, value);
		return this;
		
	}
	
	public HelperNBTTag setIntArray(String key, int[] value) {
		
		this.childNBT.setIntArray(key, value);
		return this;
		
	}
	
	public HelperNBTTag setTag(String key, NBTTagCompound tag) {
		
		this.childNBT.setTag(key, tag);
		return this;
		
	}
	
	public boolean hasKey(String key) {
		
		return this.childNBT.hasKey(key);
		
	}
	
	public NBTTagCompound getNBT() {
		
		return this.parentNBT;
		
	}
	
}
