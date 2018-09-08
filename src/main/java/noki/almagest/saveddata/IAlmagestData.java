package noki.almagest.saveddata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

/**********
 * @class IAlmagestData
 *
 * @description セーブデータ用のクラスに実装させるinterfaceです。
 * @description_en
 */
public interface IAlmagestData {
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	abstract void readFromNBT(NBTTagCompound nbt);
	abstract NBTTagCompound createNBT();
	abstract void setSavedData(WorldSavedData data);

}
