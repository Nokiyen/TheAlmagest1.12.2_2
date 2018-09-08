package noki.almagest.saveddata;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;


/**********
 * @class AlmagestDataChunk
 *
 * @description チャンク全部のpropertyを管理するクラスです。
 * @description_en
 */
public class AlmagestDataChunk implements IAlmagestData {
	
	//******************************//
	// define member variables.
	//******************************//
	private Map<Integer, Map<ChunkPos,ChunkProperty>> chunkMap = new HashMap<Integer, Map<ChunkPos,ChunkProperty>>();
	private WorldSavedData almagestData;

	private static final String nbt_xPos = "xpos";
	private static final String nbt_zPos = "zpos";
	private static final String nbt_chunkProperty = "chunk_property";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemStack addProperty(World world, int chunkX, int chunkZ, ItemStack stack) {
		
		Map<ChunkPos, ChunkProperty> eachChunkMap = this.chunkMap.get(world.provider.getDimension());
		if(eachChunkMap == null) {
			eachChunkMap = new HashMap<ChunkPos, ChunkProperty>();
			this.chunkMap.put(world.provider.getDimension(), eachChunkMap);
			this.almagestData.markDirty();
		}
		
		ChunkPos pos = new ChunkPos(chunkX, chunkZ);
		ChunkProperty property = eachChunkMap.get(pos);
		if(property == null) {
			property = new ChunkProperty(this.almagestData);
			eachChunkMap.put(pos, property);
			this.almagestData.markDirty();
		}
		
		return property.addProperty(stack);
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		for(String dimensionId: nbt.getKeySet()) {
			NBTTagCompound eachNbt = nbt.getCompoundTag(dimensionId);
			Map<ChunkPos,ChunkProperty> eachChunkMap = new HashMap<ChunkPos, ChunkProperty>();
			
			for(String eachChunkKey: eachNbt.getKeySet()) {
				NBTTagCompound eachChunk = eachNbt.getCompoundTag(eachChunkKey);
				ChunkPos pos = new ChunkPos(eachChunk.getInteger(nbt_xPos), eachChunk.getInteger(nbt_zPos));
				eachChunkMap.put(pos, new ChunkProperty(eachChunk.getCompoundTag(nbt_chunkProperty), this.almagestData));
			}
			
			this.chunkMap.put(Integer.valueOf(dimensionId), eachChunkMap);
		}
		
	}
	
	@Override
	public NBTTagCompound createNBT() {
		
		NBTTagCompound nbt = new NBTTagCompound();
		
		for(Map.Entry<Integer, Map<ChunkPos,ChunkProperty>> entry : this.chunkMap.entrySet()) {
			NBTTagCompound eachNbt = new NBTTagCompound();
			
			int count = 1;
			for(Map.Entry<ChunkPos,ChunkProperty> eachEntry : entry.getValue().entrySet()) {
				NBTTagCompound eachChunk = new NBTTagCompound();
				eachChunk.setInteger(nbt_xPos, eachEntry.getKey().x);
				eachChunk.setInteger(nbt_zPos, eachEntry.getKey().z);
				eachChunk.setTag(nbt_chunkProperty, eachEntry.getValue().createNbt());
				eachNbt.setTag(String.valueOf(count), eachChunk);
				count++;
			}
			
			nbt.setTag(String.valueOf(entry.getKey()), eachNbt);
		}
		
		return nbt;
		
	}
	
	@Override
	public void setSavedData(WorldSavedData data) {
		
		this.almagestData = data;
		
	}

}
