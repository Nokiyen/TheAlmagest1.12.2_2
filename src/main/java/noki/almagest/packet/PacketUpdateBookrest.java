package noki.almagest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


/**********
 * @class PacketBreakMessage
 *
 * @description
 * @description_en
 */
public class PacketUpdateBookrest implements IMessage {

	//******************************//
	// define member variables.
	//******************************//
	public ByteBuf data;
	public int abilityId;

	
	//******************************//
	// define member methods.
	//******************************//
	public PacketUpdateBookrest() {
		
	}
	
	public PacketUpdateBookrest(int targetAbilityId) {
		
		this.abilityId = targetAbilityId;
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		this.data = buf;
		this.abilityId = buf.readInt();
		
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(this.abilityId);
		
	}

}
