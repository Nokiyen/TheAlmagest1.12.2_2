package noki.almagest.saveddata;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import noki.almagest.helper.HelperConstellation.Constellation;


/**********
 * @class AlmagestDataConstellationBlock
 *
 * @description worldに生成された星座ブロックの位置を記憶するクラスです。
 * @description_en
 */
public class AlmagestDataConstellationBlock implements IAlmagestData {
	
	//******************************//
	// define member variables.
	//******************************//
	private Map<Integer, Map<BlockPos, Constellation>> blockMap = new HashMap<Integer, Map<BlockPos,Constellation>>();
	private WorldSavedData almagestData;
	
	private static final String nbt_xPos = "xpos";
	private static final String nbt_yPos = "ypos";
	private static final String nbt_zPos = "zpos";
	private static final String nbt_constId = "const_id";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public boolean isBlockPlacedAt(World world, BlockPos pos) {
		
		Map<BlockPos, Constellation> eachBlockMap = this.blockMap.get(world.provider.getDimension());
		if(eachBlockMap == null) {
			return false;
		}
		if(eachBlockMap.get(pos) == null) {
			return false;
		}
		return true;
		
	}
	
	public void addBlock(World world, BlockPos pos, Constellation constellation) {
		
		if(this.isBlockPlacedAt(world, pos)) {
			this.blockMap.get(world.provider.getDimension()).remove(pos);
		}

		Map<BlockPos, Constellation> eachBlockMap = this.blockMap.get(world.provider.getDimension());
		if(eachBlockMap == null) {
			eachBlockMap = new HashMap<BlockPos, Constellation>();
			this.blockMap.put(world.provider.getDimension(), eachBlockMap);
		}
		
		eachBlockMap.put(pos, constellation);
		this.almagestData.markDirty();
		
	}
	
	public void removeBlock(World world, BlockPos pos) {
		
		if(this.isBlockPlacedAt(world, pos)) {
			this.blockMap.get(world.provider.getDimension()).remove(pos);
			this.almagestData.markDirty();
		}
		
	}
	
	public Constellation getConstellation(World world, BlockPos pos) {
		
		if(this.isBlockPlacedAt(world, pos)) {
			return this.blockMap.get(world.provider.getDimension()).get(pos);
		}
		return null;
		
	}
	
	public BlockPos getNearestConstellation(World world, BlockPos pos) {
		
		Map<BlockPos, Constellation> eachBlockMap = this.blockMap.get(world.provider.getDimension());
		if(eachBlockMap == null) {
			return null;
		}

		BlockPos nearest = null;
		double distance = Double.MAX_VALUE;
		for(BlockPos each: eachBlockMap.keySet()) {
			double eachDistance = each.getDistance(pos.getX(), pos.getY(), pos.getZ());
			if(distance > eachDistance) {
				nearest = each;
				distance = eachDistance;
			}
		}
		return nearest;
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		for(String dimensionId: nbt.getKeySet()) {
			NBTTagCompound eachNbt = nbt.getCompoundTag(dimensionId);
			Map<BlockPos, Constellation> eachBlockMap = new HashMap<BlockPos, Constellation>();
			
			for(String eachBlockKey: eachNbt.getKeySet()) {
				NBTTagCompound eachBlock = eachNbt.getCompoundTag(eachBlockKey);
				BlockPos pos = new BlockPos(eachBlock.getInteger(nbt_xPos), eachBlock.getInteger(nbt_yPos), eachBlock.getInteger(nbt_zPos));
				eachBlockMap.put(pos, Constellation.getConstFromNumber(eachBlock.getInteger(nbt_constId)));
			}
			
			this.blockMap.put(Integer.valueOf(dimensionId), eachBlockMap);
		}
		
	}
	
	@Override
	public NBTTagCompound createNBT() {
		
		NBTTagCompound nbt = new NBTTagCompound();
		
		for(Map.Entry<Integer, Map<BlockPos,Constellation>> entry : this.blockMap.entrySet()) {
			NBTTagCompound eachNbt = new NBTTagCompound();
			
			int count = 1;
			for(Map.Entry<BlockPos, Constellation> eachEntry : entry.getValue().entrySet()) {
				NBTTagCompound eachBlock = new NBTTagCompound();
				eachBlock.setInteger(nbt_xPos, eachEntry.getKey().getX());
				eachBlock.setInteger(nbt_yPos, eachEntry.getKey().getY());
				eachBlock.setInteger(nbt_zPos, eachEntry.getKey().getZ());
				eachBlock.setInteger(nbt_constId, eachEntry.getValue().getId());
				
				eachNbt.setTag(String.valueOf(count), eachBlock);
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
