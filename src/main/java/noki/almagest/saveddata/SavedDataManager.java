package noki.almagest.saveddata;

import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
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
	private AlmagestDataTent tentData = new AlmagestDataTent();
	private AlmagestNBT nbt;
	
	private static final String DATA_NAME = ModInfo.NAME.toLowerCase()+"_saved_data";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public SavedDataManager() {
		
	}
	
	public void resetNbt() {
		
		this.nbt = null;
		this.flagData.reset();
		this.chunkData.reset();
		this.blockData.reset();
		this.storyData.reset();
		this.constData.reset();
		this.tentData.reset();
		
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		
		AlmagestCore.log("handle save data.");
		
		if(this.nbt != null) {
			AlmagestCore.log("already having nbt.");
			return;
		}
		
		AlmagestCore.log("try to load nbt.");
		
		MapStorage storage = event.getWorld().getMapStorage();
		AlmagestNBT instance = (AlmagestNBT)storage.getOrLoadData(AlmagestNBT.class, DATA_NAME);
		
		if(instance == null) {
			instance = new AlmagestNBT(DATA_NAME);
			storage.setData(DATA_NAME, instance);
		}
		
		this.nbt = instance;
		
		this.nbt.setAlmagestData(this.flagData, this.chunkData, this.blockData, this.storyData, this.constData, this.tentData);
		
		this.flagData.setSavedData(this.nbt);
		this.chunkData.setSavedData(this.nbt);
		this.blockData.setSavedData(this.nbt);
		this.storyData.setSavedData(this.nbt);
		this.constData.setSavedData(this.nbt);
		this.tentData.setSavedData(this.nbt);
		
		this.nbt.retryReadFromNBT();
		
	}
	
/*	public void loadStorage(FMLServerAboutToStartEvent event) {
		
		AlmagestCore.log("try to load storage.");
		
		AlmagestCore.log("{}.", event.getServer().getFolderName());
		MapStorage storage = new MapStorage(
				event.getServer().getActiveAnvilConverter().getSaveLoader(event.getServer().getFolderName(), true));
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
		
	}*/
	
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
	
	public AlmagestDataTent getTentData() {
		
		return this.tentData;
		
	}

}
