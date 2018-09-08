package noki.almagest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


/**********
 * @class PacketStarCompassSync
 *
 * @description
 * @description_en
 */
public class PacketStarCompassSync implements IMessage {

	//******************************//
	// define member variables.
	//******************************//
	public ByteBuf data;
	public int dimensionId;
	public BlockPos tilePos;
	public int constId;
	public BlockPos targetPos;

	
	//******************************//
	// define member methods.
	//******************************//
	public PacketStarCompassSync() {
		
	}
	
	public PacketStarCompassSync(int dimensionId, BlockPos tilePos, BlockPos targetPos, int constId) {
		
		this.dimensionId = dimensionId;
		this.tilePos = tilePos;
		this.targetPos = targetPos;
		this.constId = constId;
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		this.data = buf;
		this.dimensionId = buf.readInt();
		this.tilePos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.targetPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.constId = buf.readInt();
		
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(this.dimensionId);
		buf.writeInt(this.tilePos.getX());
		buf.writeInt(this.tilePos.getY());
		buf.writeInt(this.tilePos.getZ());
		buf.writeInt(this.targetPos.getX());
		buf.writeInt(this.targetPos.getY());
		buf.writeInt(this.targetPos.getZ());
		buf.writeInt(this.constId);
		
	}

}
