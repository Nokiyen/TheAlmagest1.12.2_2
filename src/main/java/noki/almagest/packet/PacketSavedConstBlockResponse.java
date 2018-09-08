package noki.almagest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


/**********
 * @class PacketSavedConstBlockResponse
 *
 * @description
 * @description_en
 */
public class PacketSavedConstBlockResponse implements IMessage {

	//******************************//
	// define member variables.
	//******************************//
	public ByteBuf data;
	public boolean success;
	public int constId;
	public BlockPos pos;

	
	//******************************//
	// define member methods.
	//******************************//
	public PacketSavedConstBlockResponse() {
		
	}
	
	public PacketSavedConstBlockResponse(boolean success, BlockPos pos, int constId) {
		
		this.success = success;
		this.pos = pos;
		this.constId = constId;
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		this.data = buf;
		this.success = buf.readBoolean();
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.constId = buf.readInt();
		
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeBoolean(this.success);
		buf.writeInt(this.pos.getX());
		buf.writeInt(this.pos.getY());
		buf.writeInt(this.pos.getZ());
		buf.writeInt(this.constId);
		
	}

}
