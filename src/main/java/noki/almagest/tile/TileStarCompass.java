package noki.almagest.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import noki.almagest.AlmagestCore;
import noki.almagest.helper.HelperNBTTag;
import noki.almagest.helper.HelperConstellation.Constellation;


/**********
 * @class TileStarCompass
 *
 * @description
 */
public class TileStarCompass extends TileEntity {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private BlockPos targetPos;
	private Constellation targetConst;
	
	private int stackMetadata;
	private int stackTick;
	
	private static final String NBT_posX = "posx";
	private static final String NBT_posY = "posy";
	private static final String NBT_posZ = "posz";
	private static final String NBT_const = "const";
	private static final String NBT_metadata = "stack_metadata";
	private static final String NBT_tick = "stack_tick";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public TileStarCompass() {
		
	}
	
	public void setTarget(BlockPos pos, Constellation constellation) {
		
		this.targetPos = pos;
		this.targetConst = constellation;
		AlmagestCore.log("target: {} / {} / {}.", this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
		
	}
	
	public void setStackData(int metadata, int tick) {
		
		this.stackMetadata = metadata;
		this.stackTick = tick;
		
	}
	
	public BlockPos getTargetPos() {
		
		return this.targetPos;
		
	}
	
	public int getStackMetadata() {
		
		return this.stackMetadata;
		
	}
	
	public int getStackTick() {
		
		return this.stackTick;
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		super.readFromNBT(nbt);
		HelperNBTTag helper = new HelperNBTTag(nbt);
		if(helper.hasKey(NBT_posX)) {
			this.targetPos = new BlockPos(helper.getInteger(NBT_posX), helper.getInteger(NBT_posY), helper.getInteger(NBT_posZ));
		}
		if(helper.hasKey(NBT_const)) {
			this.targetConst = Constellation.getConstFromNumber(helper.getInteger(NBT_const));
		}
		this.stackMetadata = helper.getInteger(NBT_metadata);
		this.stackTick = helper.getInteger(NBT_tick);
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		
		HelperNBTTag helper = new HelperNBTTag(nbt);
		if(this.targetPos != null) {
			helper.setInteger(NBT_posX, this.targetPos.getX());
			helper.setInteger(NBT_posY, this.targetPos.getY());
			helper.setInteger(NBT_posZ, this.targetPos.getZ());
		}
		if(this.targetConst != null) {
			helper.setInteger(NBT_const, this.targetConst.getId());
		}
		helper.setInteger(NBT_metadata, this.stackMetadata);
		helper.setInteger(NBT_tick, this.stackTick);
		
		return super.writeToNBT(nbt);
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		
		return this.writeToNBT(new NBTTagCompound());
		
	}

}
