package noki.almagest.proxy;

import noki.almagest.AlmagestCore;
import noki.almagest.entity.EntityTsuchinoko;
import noki.almagest.packet.PacketHandler;
import noki.almagest.packet.PacketSyncData;
import noki.almagest.packet.PacketSyncStory;
import noki.almagest.tile.TileAriadne;
import noki.almagest.tile.TileConstellation;
import noki.almagest.tile.TileStarCompass;
import noki.almagest.tile.TileTent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;


/**********
 * @class ProxyServer
 *
 * @description サーバ用proxyクラス。
 * @description_en Proxy class for Server.
 */
public class ProxyServer implements ProxyCommon {
	
	//******************************//
	// define member variables.
	//******************************//

	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public void registerTileEntities() {
		
//		GameRegistry.registerTileEntity(TileConstellation.class, "TileConstellation");
		GameRegistry.registerTileEntity(TileConstellation.class, new ResourceLocation("almagest", "tile_constellation"));
		GameRegistry.registerTileEntity(TileStarCompass.class, new ResourceLocation("almagest", "tile_star_compass"));
		GameRegistry.registerTileEntity(TileTent.class, new ResourceLocation("almagest", "tile_tent"));
		GameRegistry.registerTileEntity(TileAriadne.class, new ResourceLocation("almagest", "tile_ariadne"));
		
	}
	
	@Override
	public void registerEntities() {
		
	}
	
	@Override
	public void registerRenderers() {
		
	}
	
	@Override
	public void registerPre() {
		
	}
	
	@Override
	public boolean hasPlanisphere() {
		
		return false;
		
	}
	
	@Override
	public void log(String message, Object... data) {
		
	}
	
	@Override
	public EntityPlayer getPlayer() {
		
		return null;
		
	}
	
	@Override
	public void sendMessage(ITextComponent text) {
		
	}
	
	@Override
	public void syncData(NBTTagCompound nbt) {
		
//		PacketHandler.instance.sendToAll(new PacketSyncData(nbt));
		
	}
	
	@Override
	public void registerFixers() {
		
		EntityLiving.registerFixesMob(FMLCommonHandler.instance().getMinecraftServerInstance().getDataFixer(), EntityTsuchinoko.class);
		
	}
	
	@Override
	public boolean guiOpening() {
		
		return false;
		
	}
	
	@Override
	public void goToNextStory() {
		
//		AlmagestCore.savedDataManager.getStoryData().goToNextStory();
		
	}
	
	@Override
	public void syncStory() {
		
//		PacketHandler.instance.sendToAll(new PacketSyncStory(AlmagestCore.savedDataManager.getStoryData().getStoryFlag()));
		
	}

}
