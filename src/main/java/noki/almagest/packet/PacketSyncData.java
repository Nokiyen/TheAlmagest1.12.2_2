package noki.almagest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


/**********
 * @class PacketSyncData
 *
 * @description
 * @description_en
 */
public class PacketSyncData implements IMessage {

	//******************************//
	// define member variables.
	//******************************//
	public ByteBuf data;
	public NBTTagCompound nbt;

	
	//******************************//
	// define member methods.
	//******************************//
	public PacketSyncData() {
		
	}
	
	public PacketSyncData(NBTTagCompound nbt) {
		
		this.nbt = nbt;
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		this.data = buf;
		this.nbt = ByteBufUtils.readTag(this.data);
		
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		
		ByteBufUtils.writeTag(this.data, this.nbt);
		
	}

}
