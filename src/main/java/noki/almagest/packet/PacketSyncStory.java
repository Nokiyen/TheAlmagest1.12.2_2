package noki.almagest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


/**********
 * @class PacketSyncStory
 *
 * @description
 * @description_en
 */
public class PacketSyncStory implements IMessage {

	//******************************//
	// define member variables.
	//******************************//
	public ByteBuf data;
	public int currentFlag;

	
	//******************************//
	// define member methods.
	//******************************//
	public PacketSyncStory() {
		
	}
	
	public PacketSyncStory(int currentFlag) {
		
		this.currentFlag = currentFlag;
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		this.data = buf;
		this.currentFlag = buf.readInt();
		
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(this.currentFlag);
		
	}

}
