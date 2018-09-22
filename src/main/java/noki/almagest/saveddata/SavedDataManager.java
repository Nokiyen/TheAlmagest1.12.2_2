package noki.almagest.saveddata;

import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.ModInfo;


/**********
 * @class SavedDataManager
 *
 * @description このmodがNBTとして保存しているセーブデータを読み込むクラスです。
 * 		AlmagestCoreにstaticでインスタンス生成。
 * 		onWorldLoad()でNBT読み込み。
 * 		AlmagestCore.serverAboutToStart()でセーブデータ再読み込みのためのリセット。異なるセーブデータのワールドに入るときに保持していたデータをリセットする。
 * @description_en class to read NBT save data of this mod.
 * 		At AlmagestCore: instantiation into static field.
 * 		At this.onWorldLoad(): read from NBT via WorldEvent.Load event.
 * 		At AlmagestCore.serverAboutToStart(): reset all data before entering another save data world and re-read its save data.
 */
public class SavedDataManager {
	
	//******************************//
	// define member variables.
	//******************************//
	private AlmagestNBT nbt;
	
	private AlmagestDataFlag flagData = new AlmagestDataFlag();
	private AlmagestDataChunk chunkData = new AlmagestDataChunk();
	private AlmagestDataBlock blockData = new AlmagestDataBlock();
	private AlmagestDataStory storyData = new AlmagestDataStory();
	private AlmagestDataConstellationBlock constData = new AlmagestDataConstellationBlock();
	private AlmagestDataTent tentData = new AlmagestDataTent();
	private AlmagestDataAriadne ariadneData = new AlmagestDataAriadne();
	
	private static final String DATA_NAME = ModInfo.NAME.toLowerCase()+"_saved_data";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public SavedDataManager() {
		
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
		
		this.nbt.setAlmagestData(this.flagData, this.chunkData, this.blockData, this.storyData, this.constData, this.tentData, this.ariadneData);
		
		this.flagData.setSavedData(this.nbt);
		this.chunkData.setSavedData(this.nbt);
		this.blockData.setSavedData(this.nbt);
		this.storyData.setSavedData(this.nbt);
		this.constData.setSavedData(this.nbt);
		this.tentData.setSavedData(this.nbt);
		this.ariadneData.setSavedData(this.nbt);
		
		this.nbt.retryReadFromNBT();
		
	}
	
	public void resetNbt() {
		
		this.nbt = null;
		this.flagData.reset();
		this.chunkData.reset();
		this.blockData.reset();
		this.storyData.reset();
		this.constData.reset();
		this.tentData.reset();
		this.ariadneData.reset();
		
	}
	
	//----------
	//Getter Method.
	//----------
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
	
	public AlmagestDataAriadne getAriadneData() {
		
		return this.ariadneData;
		
	}
	
}
