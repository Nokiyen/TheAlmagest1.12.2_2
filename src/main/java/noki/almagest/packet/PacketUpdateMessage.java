package noki.almagest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


/**********
 * @class PacketBreakMessage
 *
 * @description
 * @description_en
 */
public class PacketUpdateMessage implements IMessage {

	//******************************//
	// define member variables.
	//******************************//
	public ByteBuf data;
	public int type;
	public BlockPos pos;

	
	//******************************//
	// define member methods.
	//******************************//
	public PacketUpdateMessage() {
		
	}
	
	public PacketUpdateMessage(int type, BlockPos pos) {
		
		this.type = type;
		this.pos = pos;
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		this.data = buf;
		this.type = buf.readInt();
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(this.type);
		buf.writeInt(this.pos.getX());
		buf.writeInt(this.pos.getY());
		buf.writeInt(this.pos.getZ());
		
	}

}
