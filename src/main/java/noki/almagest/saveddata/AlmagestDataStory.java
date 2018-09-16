package noki.almagest.saveddata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import noki.almagest.packet.PacketHandler;
import noki.almagest.packet.PacketSyncStory;

public class AlmagestDataStory implements IAlmagestData {
	
	private WorldSavedData almagestData;
	//main story < 1000.
	//sub flags >= 1000.
	private Map<Integer, Boolean> storyFlags = new HashMap<Integer, Boolean>();
	
	private static final String nbt_storyFlag = "storyflag";
	
	public int getCurrentStory() {
		
		ArrayList<Integer> trueList = new ArrayList<Integer>();
		for(Map.Entry<Integer, Boolean> each: this.storyFlags.entrySet()) {
			if(each.getValue() == true && each.getKey() < 1000) {
				trueList.add(each.getKey());
			}
		}
		if(trueList.size() != 0) {
			return Collections.max(trueList);
		}
		
		return 0;
		
	}
	
	public boolean getStoryFlag(int flagId) {
		
		Boolean flag = this.storyFlags.get(flagId);
		if(flag != null) {
			return flag;
		}
		return false;
		
	}
	
	public boolean checkStoryFlag(int flagId) {
		
		return this.checkStoryFlag(flagId, flagId-1);
		
	}
	
	public boolean checkStoryFlag(int targetId, int regId) {
		
		Boolean targetFlag = this.storyFlags.get(targetId);
		if(targetFlag == null) {
			targetFlag = false;
		}
		Boolean regFlag = this.storyFlags.get(regId);
		if(regFlag == null) {
			regFlag = false;
		}
		
		//if prerequisite flag is OK, and target flag is still not done, return true. (it's time to ignite the scene!)
		if(regFlag == true && targetFlag == false) {
			return true;
		}
		
		return false;
		
	}
	
	public void markStoryFlagOnServer(int flagId) {
		
		this.markStoryFlag(flagId);
		PacketHandler.instance.sendToAll(new PacketSyncStory(flagId));
		
	}
	
	public void markStoryFlag(int flagId) {
		
		this.storyFlags.put(flagId, true);
		this.almagestData.markDirty();
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		NBTTagCompound child = nbt.getCompoundTag(nbt_storyFlag);
		for(String key: child.getKeySet()) {
			this.storyFlags.put(new Integer(key), child.getBoolean(key));
		}
		
	}
	
	@Override
	public NBTTagCompound createNBT() {
		
		NBTTagCompound child = new NBTTagCompound();
		for(Map.Entry<Integer, Boolean> each: this.storyFlags.entrySet()) {
			child.setBoolean(each.getKey().toString(), each.getValue());
		}
		
		NBTTagCompound parent = new NBTTagCompound();
		parent.setTag(nbt_storyFlag, child);
		return parent;
		
	}
	
	@Override
	public void setSavedData(WorldSavedData data) {
		
		this.almagestData = data;
		
	}
	
	public void syncStoryData(int flagId, boolean flag) {
		
		this.storyFlags.put(flagId, flag);
		
	}
	
	@Override
	public void reset() {
		
		this.storyFlags.clear();
		
	}

}
