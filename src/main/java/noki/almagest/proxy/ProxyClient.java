package noki.almagest.proxy;

import java.util.Formatter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noki.almagest.AlmagestCore;
import noki.almagest.AlmagestData;
import noki.almagest.client.render.RenderResource;
import noki.almagest.client.render.entity.RenderFactoryMira;
import noki.almagest.client.render.entity.TsuchinokoRenderFactory;
import noki.almagest.client.render.model.BakedModelConstellation;
import noki.almagest.client.render.model.BakedModelConstellationAll;
import noki.almagest.client.render.tile.RenderTileConstellation;
import noki.almagest.client.render.tile.RenderTileStarCompass;
import noki.almagest.entity.EntityMira;
import noki.almagest.entity.EntityTsuchinoko;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.packet.PacketHandler;
import noki.almagest.packet.PacketSyncStory;
import noki.almagest.registry.ModBlocks;
import noki.almagest.registry.ModItems;
import noki.almagest.tile.TileConstellation;
import noki.almagest.tile.TileStarCompass;
import noki.almagest.tile.TileTent;



/**********
 * @class ProxyClient
 *
 * @description クライアント用proxyクラス。
 * @description_en Proxy class for Client.
 */
public class ProxyClient implements ProxyCommon {
	
	//******************************//
	// define member variables.
	//******************************//
	public static ModelResourceLocation locationConstellation = new ModelResourceLocation("almagest:model_dynamic", "inventory");


	//******************************//
	// define member methods.
	//******************************//
	@Override
	public void registerTileEntities() {
		
		GameRegistry.registerTileEntity(TileConstellation.class, new ResourceLocation("almagest", "tile_constellation"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileConstellation.class, new RenderTileConstellation());
		GameRegistry.registerTileEntity(TileStarCompass.class, new ResourceLocation("almagest", "star_compass"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileStarCompass.class, new RenderTileStarCompass());
		GameRegistry.registerTileEntity(TileTent.class, new ResourceLocation("almagest", "tile_tent"));
/*		MinecraftForgeClient.registerItemRenderer(
				Item.getItemFromBlock(AlmagestData.blockConstellation), new RenderItemBlockConstellation());*/
		
	}
	
	@Override
	public void registerEntities() {
		
		RenderingRegistry.registerEntityRenderingHandler(EntityMira.class, new RenderFactoryMira());
		RenderingRegistry.registerEntityRenderingHandler(EntityTsuchinoko.class, new TsuchinokoRenderFactory());
		
	}
	
	@Override
	public void registerRenderers() {
		
		ItemMeshDefinition meshDefinition = new ItemMeshDefinition() {
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return locationConstellation;
			}
		};
		Item itemConstellation = Item.getItemFromBlock(ModBlocks.CONSTELLATION_BLOCK);
		
		ModelBakery.registerItemVariants(itemConstellation,locationConstellation);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemConstellation, meshDefinition);
		
	}
	@Override
	public void registerPre() {
		
		MinecraftForge.EVENT_BUS.register(new ForgeHook());
		
	}
	
	public class ForgeHook {
				
		@SubscribeEvent
		public void onModelBakeEvent(ModelBakeEvent event) {
			
			event.getModelRegistry().putObject(locationConstellation, new BakedModelConstellationAll());
			
			for(Constellation constellation: Constellation.values()) {
				ModelResourceLocation location = 
						new ModelResourceLocation("almagest", "constellation_"+constellation.getName().toLowerCase());
				event.getModelRegistry().putObject(location, new BakedModelConstellation(constellation, false));
				
				if(constellation.isIncomplete()) {
					location = 
							new ModelResourceLocation("almagest", "constellation_"+constellation.getName().toLowerCase()+"_incomplete");
					event.getModelRegistry().putObject(location, new BakedModelConstellation(constellation, true));
				}
			}
			
		}
		
		@SubscribeEvent
		public void onTextureStichEvent(TextureStitchEvent.Pre event) {
			
			RenderResource.registerTexture(event);

		}
		
	}
	
	@Override
	public boolean hasPlanisphere() {
		
		EntityPlayer player = Minecraft.getMinecraft().player;
		if(player == null) {
			return false;
		}
		
		ItemStack mainStack = player.getHeldItem(EnumHand.MAIN_HAND);
		if(mainStack != null && mainStack.getItem() == ModItems.PLANISPHERE) {
			return true;
		}
		
		ItemStack offStack = player.getHeldItem(EnumHand.OFF_HAND);
		if(offStack != null && offStack.getItem() == ModItems.PLANISPHERE) {
			return true;
		}
		
		return false;
		
	}
	
	@SuppressWarnings("resource")
	@Override
	public void log(String message, Object... data) {
		
		Minecraft.getMinecraft().player.sendMessage(
				new TextComponentTranslation(new Formatter().format(message, data).toString()));
		
	}
	
	@Override
	public EntityPlayer getPlayer() {
		
		return Minecraft.getMinecraft().player;
		
	}
	
	@Override
	public void syncData(NBTTagCompound nbt) {
		
	}
	
	@Override
	public void registerFixers() {
		
		EntityLiving.registerFixesMob(Minecraft.getMinecraft().getDataFixer(), EntityTsuchinoko.class);
		
	}
	
	@Override
	public boolean guiOpening() {
		
		if(Minecraft.getMinecraft().currentScreen != null) {
			return true;
		}
		return false;
		
	}
	
	@Override
	public void goToNextStory() {
		
	}
	
	@Override
	public void syncStory() {
		
	}

	
}
