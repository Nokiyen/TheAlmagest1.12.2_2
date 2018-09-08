package noki.almagest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


/**********
 * @class PacketBreakMessage
 *
 * @description
 * @description_en
 */
public class PacketUpdateMira implements IMessage {

	//******************************//
	// define member variables.
	//******************************//
	public ByteBuf data;
	public int stateId;

	
	//******************************//
	// define member methods.
	//******************************//
	public PacketUpdateMira() {
		
	}
	
	public PacketUpdateMira(int targetStateId) {
		
		this.stateId = targetStateId;
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		this.data = buf;
		this.stateId = buf.readInt();
		
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(this.stateId);
		
	}

}
