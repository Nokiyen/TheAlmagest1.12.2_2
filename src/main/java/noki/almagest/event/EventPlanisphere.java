package noki.almagest.event;

import java.util.HashMap;

import net.minecraft.block.BlockContainer;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.world.BlockEvent.CropGrowEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.AlmagestData;
import noki.almagest.entity.EntityMira;

public class EventPlanisphere {
	
	//0=unchecked, 1=having mira, 2=no mira.
	private HashMap<ChunkPos, Integer> chunkFlag = new HashMap<ChunkPos, Integer>();
	
	public EventPlanisphere() {
		
		//init.
		if(this.chunkFlag == null) {
			this.chunkFlag = new HashMap<ChunkPos, Integer>();
		}
		else {
			this.chunkFlag.clear();
		}
		
		for(int i=-3; i<=3; i++) {
			for(int j=-3; j<=3; j++) {
				this.chunkFlag.put(new ChunkPos(i, j), 0);
			}
		}
		
	}
	
	//ミラがワールドに存在しないときにスポーンさせる
/*	@SubscribeEvent
	public void onWorldJoin(EntityJoinWorldEvent event) {
		
		if(event.getWorld().isRemote) {
			return;
		}
		
		if(event.getWorld().provider.getDimension() != AlmagestData.dimensionID) {
			return;
		}
		
		if(!AlmagestCore.savedDataManager.getStoryData().getStoryFlag(2000)) {
			return;
		}
		
		if(event.getEntity() == null) {
			return;
		}
		
		if(!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}
		
		AlmagestCore.log("on world join by player.");
		
		
		
//		if(event.getWorld().countEntities(EntityMira.class) != 0) {
//			return;
//		}
		
		boolean found = false;
		for(Entity each: event.getWorld().loadedEntityList) {
			if(each instanceof EntityMira) {
				found = true;
				AlmagestCore.log("{}", each.getDisplayName().toString());
			}
		}
		if(found) {
			return;
		}
		
		AlmagestCore.log("try to spawn mira.");
		EntityMira entity = new EntityMira(event.getWorld());
		entity.setPosition(2, 90, 0);
		entity.forceSpawn = true;
		boolean result = event.getWorld().spawnEntity(entity);		
		AlmagestCore.log("spawnEntity() / {}.", String.valueOf(result));
		
	}*/
	
/*	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		
		if(event.getWorld().provider.getDimension() != AlmagestData.dimensionID) {
			return;
		}
		AlmagestCore.log("load planisphere.");
		
	}*/
	
/*	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event) {
		
		if(event.getEntityLiving().world == null || event.getEntityLiving().world.isRemote) {
			return;
		}
		if(!(event.getEntityLiving() instanceof EntityMira)) {
			return;
		}
		AlmagestCore.savedDataManager.getStoryData().markStoryFlagOnServer(2000);
		
	}*/
	
	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load event) {
		
		if(event.getWorld().isRemote) {
			return;
		}
		if(event.getWorld().provider.getDimension() != AlmagestData.dimensionID_planisphere) {
			return;
		}
/*		if(!AlmagestCore.savedDataManager.getStoryData().getStoryFlag(2000)) {
			return;
		}*/
		
		ChunkPos chunkPos = event.getChunk().getPos();
		if(chunkPos.x<-3 || chunkPos.x>3 || chunkPos.z<-3 || chunkPos.z>3) {
			return;
		}
		
		//update flag.
		if(event.getWorld().countEntities(EntityMira.class) == 0) {
			this.chunkFlag.put(chunkPos, 2);
		}
		else {
			this.chunkFlag.put(chunkPos, 1);
		}
		
		//check finish.
		boolean miraFlag = false;
		for(int flagNum: this.chunkFlag.values()) {
			if(flagNum == 0) {
				return;
			}
			else if(flagNum == 1) {
				miraFlag = true;
			}
		}
		
		//clear.
		this.chunkFlag.clear();
		for(int i=-3; i<=3; i++) {
			for(int j=-3; j<=3; j++) {
				this.chunkFlag.put(new ChunkPos(i, j), 0);
			}
		}
		
		//finish.
		if(miraFlag) {
			return;
		}
		
		AlmagestCore.log("try to spawn mira.");
		EntityMira entity = new EntityMira(event.getWorld());
		entity.setPosition(2, 90, 0);
		entity.forceSpawn = true;
		event.getWorld().spawnEntity(entity);
		
	}
	
	//コンテナ付きアイテムは置けない。チェストなどを置かれると困る。
	@SubscribeEvent
	public void onBlockPlace(PlaceEvent event) {
		
		if(event.getWorld().provider.getDimension() != AlmagestData.dimensionID_planisphere) {
			return;
		}
		
/*		if(event.getWorld().isRemote) {
			return;
		}*/
		
		if(event.getPlayer() == null) {
			return;
		}
		
		if(event.getPlacedBlock().getBlock() instanceof BlockContainer) {
			event.setCanceled(true);
		}
		
	}
	
	//植林場にされると困る。
	@SubscribeEvent
	public void onSaplingGrow(SaplingGrowTreeEvent event) {
		
		if(event.getWorld().provider.getDimension() != AlmagestData.dimensionID_planisphere) {
			return;
		}
		event.setResult(Result.DENY);
		
	}
	
	//農場にされると困る。
	@SubscribeEvent
	public void onCropGrow(CropGrowEvent event) {
		
		if(event.getWorld().provider.getDimension() != AlmagestData.dimensionID_planisphere) {
			return;
		}
		event.setResult(Result.DENY);
		
	}
	
}
