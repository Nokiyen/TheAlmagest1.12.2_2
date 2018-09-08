package noki.almagest.saveddata.gamedata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class GameData {
	
	protected ResourceLocation name;
	protected boolean obtained;
	protected static final String key_obtained = "obtained";
	
	public boolean isObtained() {
		
		return this.obtained;
		
	}
	
	public void setObtained(boolean flag) {
		
		this.obtained = flag;
		
	}
	
	public ResourceLocation getName() {
		
		return this.name;
		
	}
	
	public String getDisplay() {
		
		return null;
		
	}
	
	public void readFromNbt(NBTTagCompound nbt) {
		
		this.obtained = nbt.getBoolean(key_obtained);
		
	}
	
	public NBTTagCompound writeToNbt(NBTTagCompound nbt) {
		
		nbt.setBoolean(key_obtained, this.obtained);
		return nbt;
		
	}

}
