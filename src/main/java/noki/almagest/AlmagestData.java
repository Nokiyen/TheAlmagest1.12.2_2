package noki.almagest;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome.BiomeProperties;
import noki.almagest.attribute.AttributeHelper;
import noki.almagest.entity.EntityMira;
import noki.almagest.event.EventConstellation;
import noki.almagest.event.EventFishing;
import noki.almagest.event.EventObtained;
import noki.almagest.event.EventProperty;
import noki.almagest.event.EventRender;
import noki.almagest.event.EventSleep;
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
import noki.almagest.world.PlaniBiome;
import noki.almagest.world.PlaniWorldProvider;


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
	public static int dimensionID;
	public static DimensionType dimensionType;
	public static BiomeDictionary.Type biomeType;
	
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
		dimensionID = 88;
		dimensionType = DimensionType.register("Planisphere", "_planisphere", dimensionID, PlaniWorldProvider.class, false);
		biomeType = BiomeDictionary.Type.getType("STAR");
		ForgeRegistries.BIOMES.register(
				new PlaniBiome(
						new BiomeProperties("Planisphere").setTemperature(0.8F).setRainfall(0.0F).setRainDisabled()
				).setRegistryName(new ResourceLocation(ModInfo.ID.toLowerCase(), "planisphere"))
			);
		BiomeDictionary.addTypes(ForgeRegistries.BIOMES.getValue(new ResourceLocation(ModInfo.ID.toLowerCase(), "planisphere")), biomeType);
		DimensionManager.registerDimension(dimensionID, dimensionType);

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
		MinecraftForge.EVENT_BUS.register(new EventVersionCheck());
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			MinecraftForge.EVENT_BUS.register(new EventRender());
			MinecraftForge.EVENT_BUS.register(new EventToolTip());
		}
		
		//entity.
		ResourceLocation location = new ResourceLocation(ModInfo.ID.toLowerCase(), "almagest.entity.mira");
		EntityMira.setResource(location);
		EntityRegistry.registerModEntity(location, EntityMira.class, "Mira", 0, AlmagestCore.instance, 250, 1, false, 0xAAAAAA, 0xCCCCCC);
		AlmagestCore.savedDataManager.getFlagData().registerMob(new EntityMira(null), location);
/*		EntityRegistry.registerModEntity(new ResourceLocation(ModInfo.ID.toLowerCase(), "almagest.entity.tsuchinoko"),
				EntityTsuchinoko.class, "Tsuchinoko", 1, AlmagestCore.instance, 250, 1, false, 0xDDDDDD, 0xEEEEEE);*/
//		EntityRegistry.addSpawn(EntityMira.class, 20, 1, 4, EnumCreatureType.CREATURE, Biomes.PLAINS);
		
	}
	
	public static void registerPost() {
		
		AttributeHelper.register();
		
	}
	
}
