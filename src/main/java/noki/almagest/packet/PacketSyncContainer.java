package noki.almagest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


/**********
 * @class PacketSyncContainer
 *
 * @description
 * @description_en
 */
public class PacketSyncContainer implements IMessage {

	//******************************//
	// define member variables.
	//******************************//
	public ByteBuf data;
	public int nextFlag;

	
	//******************************//
	// define member methods.
	//******************************//
	public PacketSyncContainer() {
		
	}
	
	public PacketSyncContainer(int id) {
		
		this.nextFlag = id;
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		this.data = buf;
		this.nextFlag = buf.readInt();
		
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(this.nextFlag);
		
	}

}
