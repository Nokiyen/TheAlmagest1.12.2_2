package noki.almagest.saveddata;

import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.ModInfo;


/**********
 * @class SavedDataManager
 *
 * @description
 * @description_en
 */
public class SavedDataManager {
	
	//******************************//
	// define member variables.
	//******************************//
	private AlmagestDataFlag flagData = new AlmagestDataFlag();
	private AlmagestDataChunk chunkData = new AlmagestDataChunk();
	private AlmagestDataBlock blockData = new AlmagestDataBlock();
	private AlmagestDataStory storyData = new AlmagestDataStory();
	private AlmagestDataConstellationBlock constData = new AlmagestDataConstellationBlock();
	private AlmagestNBT nbt;
	
	private static final String DATA_NAME = ModInfo.NAME.toLowerCase()+"_saved_data";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public SavedDataManager() {
		
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		
		if(this.nbt != null) {
			return;
		}
		
		MapStorage storage = event.getWorld().getMapStorage();
		AlmagestNBT instance = (AlmagestNBT)storage.getOrLoadData(AlmagestNBT.class, DATA_NAME);
		
		if(instance == null) {
			instance = new AlmagestNBT(DATA_NAME);
			storage.setData(DATA_NAME, instance);
		}
		
		this.nbt = instance;
		
		this.nbt.setAlmagestData(this.flagData, this.chunkData, this.blockData, this.storyData, this.constData);
		
		this.flagData.setSavedData(this.nbt);
		this.chunkData.setSavedData(this.nbt);
		this.blockData.setSavedData(this.nbt);
		this.storyData.setSavedData(this.nbt);
		this.constData.setSavedData(this.nbt);
		
		this.nbt.retryReadFromNBT();
		
	}
	
	public AlmagestDataFlag getFlagData() {
		
		return this.flagData;
		
	}
	
	public AlmagestDataChunk getChunkData() {
		
		return this.chunkData;
		
	}
	
	public AlmagestDataBlock getBlockData() {
		
		return this.blockData;
		
	}
	
	public AlmagestDataStory getStoryData() {
		
		return this.storyData;
		
	}

	public AlmagestDataConstellationBlock getConstData() {
		
		return this.constData;
		
	}

}
