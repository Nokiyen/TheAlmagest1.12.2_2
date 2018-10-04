package noki.almagest.saveddata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import noki.almagest.AlmagestCore;


/**********
 * @class AlmagestNBT
 *
 * @description このmodの各種情報を保存するためのNBTです。もうちょっとこの辺うまく書けないものか。WorldSavedDataの生成のされ方が……。
 * @description_en
 */
public class AlmagestNBT extends WorldSavedData {
	
	//******************************//
	// define member variables.
	//******************************//
	private AlmagestDataFlag flagData;
	private AlmagestDataChunk chunkData;
	private AlmagestDataBlock blockData;
	private AlmagestDataStory storyData;
	private AlmagestDataConstellationBlock constData;
	private AlmagestDataTent tentData;
	private AlmagestDataAriadne ariadneData;
	private AlmagestDataCrete creteData;
	
	private NBTTagCompound nbtForReading;
	
	private static final String key_flagData = "gamedata";
	private static final String key_chunkData = "chunkdata";
	private static final String key_blockData = "blockdata";
	private static final String key_storyData = "storydata";
	private static final String key_constData = "constdata";
	private static final String key_tentData = "tentdata";
	private static final String key_ariadneData = "ariadnedata";
	private static final String key_creteData = "cretedata";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public AlmagestNBT(String name) {
		
		super(name);
		
	}
	
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		this.nbtForReading = nbt;

	}
	
	public void setAlmagestData(IAlmagestData... data) {
		
		for(int i = 0; i < data.length; i++) {
			switch(i) {
				case 0:
					this.flagData = (AlmagestDataFlag)data[i];
					break;
				case 1:
					this.chunkData = (AlmagestDataChunk)data[i];
					break;
				case 2:
					this.blockData = (AlmagestDataBlock)data[i];
					break;
				case 3:
					this.storyData = (AlmagestDataStory)data[i];
					break;
				case 4:
					this.constData = (AlmagestDataConstellationBlock)data[i];
					break;
				case 5:
					this.tentData = (AlmagestDataTent)data[i];
					break;
				case 6:
					this.ariadneData = (AlmagestDataAriadne)data[i];
					break;
				case 7:
					this.creteData = (AlmagestDataCrete)data[i];
					break;
			}
		}
		
	}
	
	public void retryReadFromNBT() {
		
		if(this.nbtForReading != null) {
			this.flagData.readFromNBT(this.nbtForReading.getCompoundTag(key_flagData));
			this.chunkData.readFromNBT(this.nbtForReading.getCompoundTag(key_chunkData));
			this.blockData.readFromNBT(this.nbtForReading.getCompoundTag(key_blockData));
			this.storyData.readFromNBT(this.nbtForReading.getCompoundTag(key_storyData));
			this.constData.readFromNBT(this.nbtForReading.getCompoundTag(key_constData));
			this.tentData.readFromNBT(this.nbtForReading.getCompoundTag(key_tentData));
			this.ariadneData.readFromNBT(this.nbtForReading.getCompoundTag(key_ariadneData));
			this.creteData.readFromNBT(this.nbtForReading.getCompoundTag(key_creteData));
		}
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		
		AlmagestCore.log("AlmagestNBT.writeToNBT().");
		
		nbt.setTag(key_flagData, this.flagData.createNBT());
		nbt.setTag(key_chunkData, this.chunkData.createNBT());
		nbt.setTag(key_blockData, this.blockData.createNBT());
		nbt.setTag(key_storyData, this.storyData.createNBT());
		nbt.setTag(key_constData, this.constData.createNBT());
		nbt.setTag(key_tentData, this.tentData.createNBT());
		nbt.setTag(key_ariadneData, this.ariadneData.createNBT());
		nbt.setTag(key_creteData, this.creteData.createNBT());
		
		return nbt;
		
	}

}
