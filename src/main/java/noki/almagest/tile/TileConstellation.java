package noki.almagest.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import noki.almagest.AlmagestCore;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperNBTTag;


/**********
 * @class TileConstellation
 *
 * @description 星座ブロックのTileEntityです。
 * @see BlockConstellation, ItemBlockConstellation, RenderTileConstellation.
 */
public class TileConstellation extends TileEntity implements ITickable {
	
	//******************************//
	// define member variables.
	//******************************//
	private int constNumber = 0;
	private int rotation = 0;
	private boolean rotationSwitch = true;
	private int[] missingStars;
	
	private static final String NBT_constNumber = "constNumber";
	private static final String NBT_constRotation = "constRotation";
	private static final String NBT_constRotationSwitch = "constRotationSwitch";
	private static final String NBT_missingStars = "missingStars";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public void setConstNumber(int number) {
		
		number = MathHelper.clamp(number, 0, HelperConstellation.constSize);
		this.constNumber = number;
		
	}
	
	public int getConstNumber() {
		
		return this.constNumber;
		
	}
	
	public int getRotation() {
		
		return this.rotation;
		
	}
	
	public void switchRotation() {
		
		this.rotationSwitch = !this.rotationSwitch;
		
	}
	
	public void setMissingStars(int[] stars) {
		
		this.missingStars = stars;
		
	}
	
	public int[] getMissingStars() {
		
		return this.missingStars;
		
	}
	
	@Override
	public void update() {
		
		if(this.rotationSwitch == true) {
			this.rotation = (this.rotation+1)%(60*20);
		}
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		super.readFromNBT(nbt);
		HelperNBTTag helper = new HelperNBTTag(nbt);
		this.constNumber = helper.getInteger(NBT_constNumber);
		this.rotation = helper.getInteger(NBT_constRotation);
		this.rotationSwitch = helper.getBoolean(NBT_constRotationSwitch);
		this.missingStars = helper.getIntArray(NBT_missingStars);
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		
		HelperNBTTag helper = new HelperNBTTag(nbt);
		helper.setInteger(NBT_constNumber, this.constNumber);
		helper.setInteger(NBT_constRotation, this.rotation);
		helper.setBoolean(NBT_constRotationSwitch, this.rotationSwitch);
		helper.setIntArray(NBT_missingStars, this.missingStars);
		
		return super.writeToNBT(nbt);
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		
		return this.writeToNBT(new NBTTagCompound());
		
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound = this.writeToNBT(nbttagcompound);
		return new SPacketUpdateTileEntity(this.getPos(), 3, nbttagcompound);
		
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		
		AlmagestCore.log("on packet received.");
		this.readFromNBT(packet.getNbtCompound());
		
	}

}
