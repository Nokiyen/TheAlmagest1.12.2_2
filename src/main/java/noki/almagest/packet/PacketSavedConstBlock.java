package noki.almagest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


/**********
 * @class PacketSavedConstBlock
 *
 * @description
 * @description_en
 */
public class PacketSavedConstBlock implements IMessage {

	//******************************//
	// define member variables.
	//******************************//
	public ByteBuf data;
	public BlockPos pos;
	public int dimensionId;

	
	//******************************//
	// define member methods.
	//******************************//
	public PacketSavedConstBlock() {
		
	}
	
	public PacketSavedConstBlock(int dimensionId, BlockPos pos) {
		
		this.dimensionId = dimensionId;
		this.pos = pos;
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		this.data = buf;
		this.dimensionId = buf.readInt();
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(this.dimensionId);
		buf.writeInt(this.pos.getX());
		buf.writeInt(this.pos.getY());
		buf.writeInt(this.pos.getZ());
		
	}

}
