package noki.almagest.saveddata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;


/**********
 * @class AlmagestDataAriadne
 *
 * @description
 * @description_en
 */
public class AlmagestDataAriadne implements IAlmagestData {
	
	//******************************//
	// define member variables.
	//******************************//
	private Map<Integer, ArrayList<BlockPos>> blockMap = new HashMap<Integer, ArrayList<BlockPos>>();
	private WorldSavedData almagestData;
	
	private static final String nbt_xPos = "xpos";
	private static final String nbt_yPos = "ypos";
	private static final String nbt_zPos = "zpos";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public boolean isBlockPlacedAt(World world, BlockPos pos) {
		
		ArrayList<BlockPos> eachBlockList = this.blockMap.get(world.provider.getDimension());
		if(eachBlockList == null) {
			return false;
		}
		if(!eachBlockList.contains(pos)) {
			return false;
		}
		return true;
		
	}
	
	public void addBlock(World world, BlockPos pos) {
		
		if(this.isBlockPlacedAt(world, pos)) {
			this.blockMap.get(world.provider.getDimension()).remove(pos);
		}
		
		ArrayList<BlockPos> eachBlockList = this.blockMap.get(world.provider.getDimension());
		if(eachBlockList == null) {
			eachBlockList = new ArrayList<BlockPos>();
			this.blockMap.put(world.provider.getDimension(), eachBlockList);
		}
		
		eachBlockList.add(pos);
		this.almagestData.markDirty();
		
	}
	
	public void removeBlock(World world, BlockPos pos) {
		
		if(this.isBlockPlacedAt(world, pos)) {
			this.blockMap.get(world.provider.getDimension()).remove(pos);
			this.almagestData.markDirty();
		}
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		for(String dimensionId: nbt.getKeySet()) {
			NBTTagCompound eachNbt = nbt.getCompoundTag(dimensionId);
			ArrayList<BlockPos> eachBlockList = new ArrayList<BlockPos>();
			
			for(String eachBlockKey: eachNbt.getKeySet()) {
				NBTTagCompound eachBlock = eachNbt.getCompoundTag(eachBlockKey);
				BlockPos pos = new BlockPos(eachBlock.getInteger(nbt_xPos), eachBlock.getInteger(nbt_yPos), eachBlock.getInteger(nbt_zPos));
				eachBlockList.add(pos);
			}
			
			this.blockMap.put(Integer.valueOf(dimensionId), eachBlockList);
		}
		
	}
	
	@Override
	public NBTTagCompound createNBT() {
		
		NBTTagCompound nbt = new NBTTagCompound();
		
		for(Map.Entry<Integer, ArrayList<BlockPos>> entry : this.blockMap.entrySet()) {
			NBTTagCompound eachNbt = new NBTTagCompound();
			
			int count = 1;
			for(BlockPos eachPos : entry.getValue()) {
				NBTTagCompound eachBlock = new NBTTagCompound();
				eachBlock.setInteger(nbt_xPos, eachPos.getX());
				eachBlock.setInteger(nbt_yPos, eachPos.getY());
				eachBlock.setInteger(nbt_zPos, eachPos.getZ());
				
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
	
	@Override
	public void reset() {
		
		this.blockMap.clear();
		
	}

}
