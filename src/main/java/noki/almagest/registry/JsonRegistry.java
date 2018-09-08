package noki.almagest.registry;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import noki.almagest.ModInfo;


/**********
 * @class JsonRegistry
 *
 * @description Block, Itemのjsonを登録するクラスです。登録するタイミングの違いの問題で別クラスに。
 * 
 */
public class JsonRegistry {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private static Map<Block, String> jsonForBlock = new HashMap<>();
	private static Map<Item, String> jsonForItem = new HashMap<>();
	
	
	//******************************//
	// define member methods.
	//******************************//
	public static void setBlock(Block block, String name) {
		
		jsonForBlock.put(block, name);
		
	}
	
	public static void setItem(Item item, String name) {
		
		jsonForItem.put(item, name);
		
	}
	
	public static void register() {
		
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			
			for(Map.Entry<Block, String> entry: jsonForBlock.entrySet()) {
				registerEach(entry.getKey(), entry.getValue());
			}
			for(Map.Entry<Item, String> entry: jsonForItem.entrySet()) {
				registerEach(entry.getKey(), entry.getValue());
			}
			
		}
		
		//never use these instances.
		jsonForBlock.clear();
		jsonForItem.clear();
		
	}
	
	private static void registerEach(Item item, String name) {
		
		if(item instanceof IWithSubTypes) {
			registerJson(item, name, ((IWithSubTypes)item).getSubtypeCount());
		}
		else {
			registerJson(item, name);
		}
		
	}
	
	private static void registerEach(Block block, String name) {
		
		Item itemBlock = Item.getItemFromBlock(block);
		if(block instanceof IWithSubTypes) {
			registerJson(itemBlock, name, ((IWithSubTypes)block).getSubtypeCount());
		}
		else {
			registerJson(itemBlock, name);
		}
		
	}
	
	private static void registerJson(Item item, String name) {
		
		registerJson(item, name, 1);
		
	}
	
	private static void registerJson(Item item, String name, int length) {
		
		if(length > 1) {
//			ModelResourceLocation[] locations = new ModelResourceLocation[length];
			for(int i=0; i < length; i++) {
//				ModelBakery.registerItemVariants(item, new ResourceLocation(ModInfo.ID.toLowerCase(), name+"_"+i));
//				locations[i] = new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + name + "_" + i, "inventory");
//				ModelLoader.registerItemVariants(item, new ResourceLocation(ModInfo.ID.toLowerCase(), name+"_"+i));
//				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, i, 
//						new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + name + "_" + i, "variants"));
				ModelLoader.setCustomModelResourceLocation(item, i,
						new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + name + "_" + i, "inventory"));
			}
		}
		else {
//			ModelBakery.registerItemVariants(item, new ResourceLocation(ModInfo.ID.toLowerCase(), name));
//			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, 
//					new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + name, "inventory"));
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(ModInfo.ID.toLowerCase() + ":" + name, "inventory"));
		}
		
	}

}
