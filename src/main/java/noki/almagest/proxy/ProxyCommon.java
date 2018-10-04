package noki.almagest.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;


/**********
 * @class ProxyCommon
 *
 * @description 共通proxyクラス。
 * @description_en Interface of proxy classes.
 */
public interface ProxyCommon {
	
	//******************************//
	// define member variables.
	//******************************//

	
	//******************************//
	// define member methods.
	//******************************//
	abstract void registerTileEntities();
	abstract void registerEntities();
	abstract void registerRenderers();
	abstract void registerPre();
	abstract boolean hasPlanisphere();
	abstract void log(String message, Object... data);
	abstract EntityPlayer getPlayer();
	abstract void sendMessage(ITextComponent text);
	// no need. please delete!!!
	abstract void syncData(NBTTagCompound nbt);
	abstract void registerFixers();
	abstract boolean guiOpening();
	abstract void syncStory();
	abstract void goToNextStory();
	
}
