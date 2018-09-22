package noki.almagest.saveddata;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.WorldSavedData;


/**********
 * @class AlmagestDataTent
 *
 * @description 追加ディメンションのテントについての情報を保持するクラスです。
 * @description_en
 */
public class AlmagestDataTent implements IAlmagestData {
	
	//******************************//
	// define member variables.
	//******************************//
	private Map<UUID, TentData> tentMap = new HashMap<UUID, TentData>();
	private WorldSavedData almagestData;	
	
	
	//******************************//
	// define member methods.
	//******************************//
	public BlockPos updateData(EntityPlayer player, int sizeLevel, ItemStack wallStack) {
		
		TentData data = this.tentMap.get(player.getGameProfile().getId());
		if(data == null) {
			//really loose generating.
			data = new TentData(this.tentMap.size(), 7, this.tentMap.size());
			this.tentMap.put(player.getGameProfile().getId(), data);
		}
		data.setSizeLevel(sizeLevel);
		data.setStack(wallStack);
		data.setDestination(player.world.provider.getDimension(), player.posX, player.posY, player.posZ);
		
		return data.getPos();
		
	}
	
	public TentData getData(EntityPlayer player) {
		
		return this.tentMap.get(player.getGameProfile().getId());
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		for(String uuid: nbt.getKeySet()) {
			NBTTagCompound eachNbt = nbt.getCompoundTag(uuid);
			this.tentMap.put(UUID.fromString(uuid), new TentData(eachNbt));
		}
		
	}
	
	@Override
	public NBTTagCompound createNBT() {
		
		NBTTagCompound nbt = new NBTTagCompound();
		
		for(Map.Entry<UUID, TentData> entry : this.tentMap.entrySet()) {
			nbt.setTag(entry.getKey().toString(), entry.getValue().createNbt());
		}
		
		return nbt;
		
	}
	
	@Override
	public void setSavedData(WorldSavedData data) {
		
		this.almagestData = data;
		
	}
	
	@Override
	public void reset() {
		
		this.tentMap.clear();
		
	}
	
	public class TentData {
		
		public BlockPos tentPos;
		public ItemStack wallStack;
		public int sizeLevel;
		public int prevSizeLevel;
		public boolean needUpdateSpace;
		public boolean needUpdateWall;
		
		public int destinationId;
		public double destinationX;
		public double destinationY;
		public double destinationZ;
		
		private static final String NBT_tentPosX = "tent_pos_x";
		private static final String NBT_tentPosY = "tent_pos_y";
		private static final String NBT_tentPosZ = "tent_pos_z";
		private static final String NBT_wallStack = "wall_stack";
		private static final String NBT_sizeLevel = "size_level";
		private static final String NBT_prevSizeLevel = "prev_size_level";
		private static final String NBT_needUpdateSpace = "need_update_space";
		private static final String NBT_needUpdateWall = "need_update_wall";
		private static final String NBT_desitinationId = "destination_id";
		private static final String NBT_desitinationX = "destination_x";
		private static final String NBT_desitinationY = "destination_y";
		private static final String NBT_desitinationZ = "destination_z";
		
		public TentData(int chunkX, int chunkY, int chunkZ) {
			
			this.tentPos = new BlockPos(chunkX*16+7, chunkY*16+3, chunkZ*16+6);
			this.wallStack = new ItemStack(Blocks.BEDROCK);
			this.sizeLevel = 1;
			this.prevSizeLevel = 1;
			this.needUpdateSpace = false;
			this.needUpdateWall = false;
			AlmagestDataTent.this.almagestData.markDirty();
			
		}
		
		public TentData(NBTTagCompound nbt) {
			
			this.tentPos = new BlockPos(nbt.getInteger(NBT_tentPosX), nbt.getInteger(NBT_tentPosY), nbt.getInteger(NBT_tentPosZ));
			if(nbt.hasKey(NBT_wallStack)) {
				this.wallStack = new ItemStack((NBTTagCompound)nbt.getTag(NBT_wallStack));
			}
			else {
				this.wallStack = ItemStack.EMPTY;
			}
			this.sizeLevel = nbt.getInteger(NBT_sizeLevel);
			this.prevSizeLevel = nbt.getInteger(NBT_prevSizeLevel);
			this.needUpdateSpace = nbt.getBoolean(NBT_needUpdateSpace);
			this.needUpdateWall = nbt.getBoolean(NBT_needUpdateWall);
			
			this.destinationId = nbt.getInteger(NBT_desitinationId);
			this.destinationX = nbt.getDouble(NBT_desitinationX);
			this.destinationY = nbt.getDouble(NBT_desitinationY);
			this.destinationZ = nbt.getDouble(NBT_desitinationZ);
			
		}
		
		public void setStack(ItemStack stack) {
			
			if(stack == null || stack.isEmpty()) {
				stack = new ItemStack(Blocks.BEDROCK);
			}
			if(!ItemStack.areItemsEqual(stack, this.wallStack)) {
				this.wallStack = stack;
				this.needUpdateWall = true;
				AlmagestDataTent.this.almagestData.markDirty();
			}
			
		}
		
		public void setSizeLevel(int size) {
			
			size = MathHelper.clamp(size, 1, 4);
			if(this.sizeLevel < size) {
				this.prevSizeLevel = this.sizeLevel;
				this.sizeLevel = size;
				this.needUpdateSpace = true;
				AlmagestDataTent.this.almagestData.markDirty();
			}
			
		}
		
		public void setDestination(int dimensionId, double playerX, double playerY, double playerZ) {
			
			this.destinationId = dimensionId;
			this.destinationX = playerX;
			this.destinationY = playerY;
			this.destinationZ = playerZ;
			AlmagestDataTent.this.almagestData.markDirty();
			
		}
		
		public BlockPos getPos() {
			
			return this.tentPos;
			
		}
		
		public void finishUpdateSpace() {
			
			this.needUpdateSpace = false;
			AlmagestDataTent.this.almagestData.markDirty();
			
		}
		public void finishUpdateWall() {
			
			this.needUpdateWall = false;
			AlmagestDataTent.this.almagestData.markDirty();
			
		}
		
		public NBTTagCompound createNbt() {
			
			NBTTagCompound nbt = new NBTTagCompound();
			if(this.tentPos != null) {
				nbt.setInteger(NBT_tentPosX, this.tentPos.getX());
				nbt.setInteger(NBT_tentPosY, this.tentPos.getY());
				nbt.setInteger(NBT_tentPosZ, this.tentPos.getZ());
			}
			if(this.wallStack != null) {
				nbt.setTag(NBT_wallStack, this.wallStack.serializeNBT());
			}
			nbt.setInteger(NBT_sizeLevel, this.sizeLevel);
			nbt.setInteger(NBT_prevSizeLevel, this.prevSizeLevel);
			nbt.setBoolean(NBT_needUpdateSpace, this.needUpdateSpace);
			nbt.setBoolean(NBT_needUpdateWall, this.needUpdateWall);
			
			nbt.setInteger(NBT_desitinationId, this.destinationId);
			nbt.setDouble(NBT_desitinationX, this.destinationX);
			nbt.setDouble(NBT_desitinationY, this.destinationY);
			nbt.setDouble(NBT_desitinationZ, this.destinationZ);
			
			return nbt;
			
		}
		
	}

}
