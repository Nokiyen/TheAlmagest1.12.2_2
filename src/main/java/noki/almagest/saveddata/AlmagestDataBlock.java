package noki.almagest.saveddata;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;


/**********
 * @class AlmagestDataBlock
 *
 * @description playerにより設置されたブロックの位置を保存するクラスです。大丈夫か？
 * @description_en
 */
public class AlmagestDataBlock implements IAlmagestData {
	
	//******************************//
	// define member variables.
	//******************************//
	private Map<Integer, Map<BlockPos, PropertySet>> blockMap = new HashMap<Integer, Map<BlockPos,PropertySet>>();
	private WorldSavedData almagestData;
	
	private static final String nbt_xPos = "xpos";
	private static final String nbt_yPos = "ypos";
	private static final String nbt_zPos = "zpos";
	private static final String nbt_propertySet = "property_set";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public boolean isBlockPlacedAt(World world, BlockPos pos) {
		
//		AlmagestCore.log2("%s/%s/%s.", pos.getX(), pos.getZ(), pos.getZ());
		
		Map<BlockPos, PropertySet> eachBlockMap = this.blockMap.get(world.provider.getDimension());
		if(eachBlockMap == null) {
			return false;
		}
		if(eachBlockMap.get(pos) == null) {
			return false;
		}
		return true;
		
	}
	
	//always check by isBlockPlacedAt().
	public ItemStack addProperty(World world, BlockPos pos, ItemStack stack) {
		
		Map<BlockPos, PropertySet> eachBlockMap = this.blockMap.get(world.provider.getDimension());
		ItemStack newStack = eachBlockMap.get(pos).addProperty(stack);
		eachBlockMap.remove(pos);
		return newStack;
		
	}
	
	public PropertySet getProperty(World world, BlockPos pos) {
		
		Map<BlockPos, PropertySet> eachBlockMap = this.blockMap.get(world.provider.getDimension());
		if(eachBlockMap == null) {
			return null;
		}
		return eachBlockMap.get(pos);
		
	}
	
	public void blockPlaced(World world, BlockPos pos, ItemStack stack) {
		
		if(this.isBlockPlacedAt(world, pos)) {
			this.blockMap.get(world.provider.getDimension()).remove(pos);
		}
		
		Map<BlockPos, PropertySet> eachBlockMap = this.blockMap.get(world.provider.getDimension());
		if(eachBlockMap == null) {
			eachBlockMap = new HashMap<BlockPos, PropertySet>();
			this.blockMap.put(world.provider.getDimension(), eachBlockMap);
		}
		
		eachBlockMap.put(pos, new PropertySet(stack));
		this.almagestData.markDirty();
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		for(String dimensionId: nbt.getKeySet()) {
			NBTTagCompound eachNbt = nbt.getCompoundTag(dimensionId);
			Map<BlockPos, PropertySet> eachBlockMap = new HashMap<BlockPos, PropertySet>();
			
			for(String eachBlockKey: eachNbt.getKeySet()) {
				NBTTagCompound eachBlock = eachNbt.getCompoundTag(eachBlockKey);
				BlockPos pos = new BlockPos(eachBlock.getInteger(nbt_xPos), eachBlock.getInteger(nbt_yPos), eachBlock.getInteger(nbt_zPos));
				eachBlockMap.put(pos, new PropertySet(eachBlock.getCompoundTag(nbt_propertySet)));
			}
			
			this.blockMap.put(Integer.valueOf(dimensionId), eachBlockMap);
		}
		
	}
	
	@Override
	public NBTTagCompound createNBT() {
		
		NBTTagCompound nbt = new NBTTagCompound();
		
		for(Map.Entry<Integer, Map<BlockPos,PropertySet>> entry : this.blockMap.entrySet()) {
			NBTTagCompound eachNbt = new NBTTagCompound();
			
			int count = 1;
			for(Map.Entry<BlockPos, PropertySet> eachEntry : entry.getValue().entrySet()) {
				NBTTagCompound eachBlock = new NBTTagCompound();
				eachBlock.setInteger(nbt_xPos, eachEntry.getKey().getX());
				eachBlock.setInteger(nbt_yPos, eachEntry.getKey().getY());
				eachBlock.setInteger(nbt_zPos, eachEntry.getKey().getZ());
				eachBlock.setTag(nbt_propertySet, eachEntry.getValue().createNbt());
				
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
