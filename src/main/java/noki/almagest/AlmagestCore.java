package noki.almagest;

import noki.almagest.helper.HelperConstellation;
import noki.almagest.packet.PacketHandler;
import noki.almagest.proxy.ProxyCommon;
import noki.almagest.saveddata.SavedDataManager;
import noki.almagest.saveddata.TempDataManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;


/**********
 * @Mod Almagest
 *
 * @author Nokiyen
 * 
 * @description 失われた星図を求めて。
 */
@Mod(modid = ModInfo.ID, version = ModInfo.VERSION, name = ModInfo.NAME, useMetadata = true,
	dependencies=ModInfo.DEPENDENCIES, acceptedMinecraftVersions=ModInfo.MC_VERSIONS, updateJSON = ModInfo.UPDATE_JSON)
public class AlmagestCore {
	
	//******************************//
	// define member variables.
	//******************************//
	@Instance(value = ModInfo.ID)
	public static AlmagestCore instance;
	@Metadata
	public static ModMetadata metadata;	//	extract from mcmod.info file, not java internal coding.
	@SidedProxy(
			clientSide = ModInfo.PROXY_LOCATION+"ProxyClient",
			serverSide = ModInfo.PROXY_LOCATION+"ProxyServer"
	)
	public static ProxyCommon proxy;
	
	public static boolean DEBUG = true;
	public static Logger logger;
	
	public static SavedDataManager savedDataManager = new SavedDataManager();

	
	//******************************//
	// define member methods.
	//******************************//
	//----------
	//Core Event Methods.
	//----------
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		logger = event.getModLog();
		
		// for mod's specific data.
		HelperConstellation.registerStarData(event);
		PacketHandler.registerPre();
		
		// for items and blocks.
		AlmagestData.registerPre();
		proxy.registerPre();
		proxy.registerEntities();
		
		MinecraftForge.EVENT_BUS.register(savedDataManager);
		
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event) {
		
		AlmagestData.register();
		proxy.registerTileEntities();
//		proxy.registerEntities();
		proxy.registerRenderers();
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
//		RecipeRegistry.register();
		
		AlmagestData.registerPost();
		
		TempDataManager.tempRegister();
		
		proxy.registerFixers();
			
	}
	
	@EventHandler
	public void serverAboutToStart(FMLServerAboutToStartEvent event) {
		
		AlmagestCore.log("server about to start.");
//		savedDataManager.loadStorage(event);
		savedDataManager.resetNbt();
			
	}
	
	
	//----------
	//Static Method.
	//----------
	public static void log(String message, Object... data) {
		
		if(DEBUG) {
			logger.info(message, data);
		}
		
	}
	
	public static void log2(String message, Object... data) {
		
		if(DEBUG) {
			proxy.log(message, data);
		}
		
	}
	
}
