package noki.almagest;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome.BiomeProperties;
import noki.almagest.attribute.AttributeHelper;
import noki.almagest.entity.EntityCallistoArrow;
import noki.almagest.entity.EntityMira;
import noki.almagest.event.EventConstellation;
import noki.almagest.event.EventFishing;
import noki.almagest.event.EventKeyInput;
import noki.almagest.event.EventObtained;
import noki.almagest.event.EventPlanisphere;
import noki.almagest.event.EventProperty;
import noki.almagest.event.EventRender;
import noki.almagest.event.EventSleep;
import noki.almagest.event.EventTent;
import noki.almagest.event.EventToolTip;
import noki.almagest.event.EventVersionCheck;
import noki.almagest.event.EventWeather;
import noki.almagest.gui.GuiHandler;
import noki.almagest.helper.HelperReward;
import noki.almagest.recipe.RecipeHelper;
import noki.almagest.registry.AlmagestRegistry;
import noki.almagest.registry.JsonRegistry;
import noki.almagest.registry.ModBlocks;
import noki.almagest.registry.ModItems;
import noki.almagest.registry.RecipeRegistry;
import noki.almagest.world.planisphere.PlaniBiome;
import noki.almagest.world.planisphere.PlaniWorldProvider;
import noki.almagest.world.tent.TentBiome;
import noki.almagest.world.tent.TentWorldProvider;


/**********
 * @class AlmagestData
 *
 * @description このModの各種データの格納、登録をするクラスです。
 */
public class AlmagestData {
	
	
	//******************************//
	// define member variables.
	//******************************//
	//--------------------
	// Creative Tab.
	//--------------------
	public static CreativeTabs tab;
	
	//--------------------
	// Tags of NBT.
	//--------------------
	public static final String NBT_prefix = "8fcd6c65_Almagest";
	
	//--------------------
	// Dimension.
	//--------------------
	public static DimensionType dimensionType_planisphere;
	public static BiomeDictionary.Type biomeType_planisphere;

	public static DimensionType dimensionType_tent;
	public static BiomeDictionary.Type biomeType_tent;
	
	//--------------------
	// Cofiguration.
	//--------------------
	public static File configFile;
	public static Configuration config;
	public static int dimensionID_planisphere;
	public static int dimensionID_tent;
	public static int almagestKeyNum;
	public static boolean tooltipFlag;
	
	public static KeyBinding almagestKey;
	
	//--------------------
	// GUI.
	//--------------------
	public static int guiID_almagest = 0;
	public static int guiID_bookrest = 1;
	public static int guiID_mira = 2;
	public static int guiID_tansu = 3;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public static void registerPre() {
		
		// creative tab.
		tab = new TabAlmagest();
		
		// register.
		ModBlocks.register();
		ModItems.register();
		JsonRegistry.register();
		
		// world.
//		dimensionID_planisphere = 88;
		dimensionType_planisphere = DimensionType.register("Planisphere", "_planisphere", dimensionID_planisphere, PlaniWorldProvider.class, false);
		biomeType_planisphere = BiomeDictionary.Type.getType("STAR");
		ForgeRegistries.BIOMES.register(
				new PlaniBiome(
						new BiomeProperties("Planisphere").setTemperature(0.8F).setRainfall(0.0F).setRainDisabled()
				).setRegistryName(new ResourceLocation(ModInfo.ID.toLowerCase(), "planisphere"))
			);
		BiomeDictionary.addTypes(ForgeRegistries.BIOMES.getValue(new ResourceLocation(ModInfo.ID.toLowerCase(), "planisphere")), biomeType_planisphere);
		DimensionManager.registerDimension(dimensionID_planisphere, dimensionType_planisphere);
		
//		dimensionID_tent = 89;
		dimensionType_tent = DimensionType.register("Tent", "_tent", dimensionID_tent, TentWorldProvider.class, false);
		biomeType_tent = BiomeDictionary.Type.getType("TENT");
		ForgeRegistries.BIOMES.register(
				new TentBiome(
						new BiomeProperties("Tent").setTemperature(0.8F).setRainfall(0.0F).setRainDisabled()
				).setRegistryName(new ResourceLocation(ModInfo.ID.toLowerCase(), "tent"))
			);
		BiomeDictionary.addTypes(ForgeRegistries.BIOMES.getValue(new ResourceLocation(ModInfo.ID.toLowerCase(), "tent")), biomeType_tent);
		DimensionManager.registerDimension(dimensionID_tent, dimensionType_tent);
		
	}
	
	public static void loadConigulation(File file) {
		
		configFile = file;
		config = new Configuration(configFile);
		config.load();
		
		dimensionID_planisphere = config.getInt("dimension_id_planisphere", "Dimension", 88, 2, 9999, "");
		dimensionID_tent = config.getInt("dimension_id_tent", "Dimension", 89, 2, 9999, "");
		almagestKeyNum = config.getInt("almagest_key_num", "Settings", Keyboard.KEY_O, 1, 300, "");
		tooltipFlag = config.getBoolean("tooltip_flag", "Settings", false, "");
		
		config.save();
		
	}
	
	public static void register() {
		
		//recipe.
		RecipeHelper.register();
		RecipeRegistry.register();
		AlmagestRegistry.register();
		
		//rewards.
		HelperReward.registerRewards();
		
		//json.
//		JsonRegistry.register();
		
		//gui.
		NetworkRegistry.INSTANCE.registerGuiHandler(AlmagestCore.instance, new GuiHandler());
		
		//event.
		MinecraftForge.EVENT_BUS.register(new EventConstellation());
		MinecraftForge.EVENT_BUS.register(new EventProperty());
		MinecraftForge.EVENT_BUS.register(new EventFishing());
		MinecraftForge.EVENT_BUS.register(new EventWeather());
		MinecraftForge.EVENT_BUS.register(new EventSleep());
		MinecraftForge.EVENT_BUS.register(new EventObtained());
		MinecraftForge.EVENT_BUS.register(new EventPlanisphere());
		MinecraftForge.EVENT_BUS.register(new EventTent());
		MinecraftForge.EVENT_BUS.register(new EventVersionCheck());
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			MinecraftForge.EVENT_BUS.register(new EventRender());
			MinecraftForge.EVENT_BUS.register(new EventToolTip());
			MinecraftForge.EVENT_BUS.register(new EventKeyInput());
		}
		
		//entity.
		ResourceLocation location = new ResourceLocation(ModInfo.ID.toLowerCase(), "almagest.entity.mira");
		EntityMira.setResource(location);
		EntityRegistry.registerModEntity(location, EntityMira.class, "Mira", 0, AlmagestCore.instance, 250, 1, false, 0xAAAAAA, 0xCCCCCC);
		AlmagestCore.savedDataManager.getFlagData().registerMob(new EntityMira(null), location);
		
		location = new ResourceLocation(ModInfo.ID.toLowerCase(), "almagest.entity.callisto");
		EntityRegistry.registerModEntity(location, EntityCallistoArrow.class, "callisto", 1, AlmagestCore.instance, 250, 1, false);
/*		EntityRegistry.registerModEntity(new ResourceLocation(ModInfo.ID.toLowerCase(), "almagest.entity.tsuchinoko"),
				EntityTsuchinoko.class, "Tsuchinoko", 1, AlmagestCore.instance, 250, 1, false, 0xDDDDDD, 0xEEEEEE);*/
//		EntityRegistry.addSpawn(EntityMira.class, 20, 1, 4, EnumCreatureType.CREATURE, Biomes.PLAINS);
		
	}
	
	public static void registerPost() {
		
		AttributeHelper.register();
		
	}
	
}
