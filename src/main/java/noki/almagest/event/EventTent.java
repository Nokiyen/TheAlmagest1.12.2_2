package noki.almagest.event;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.AlmagestData;
import noki.almagest.saveddata.AlmagestDataTent.TentData;

public class EventTent {
	
	//dimensionに来た時に、テント内をupdateする。
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public void onJoinWorld(EntityJoinWorldEvent event) {
		
		if(event.getWorld().provider.getDimension() != AlmagestData.dimensionID_tent) {
			return;
		}
		
		if(event.getWorld().isRemote == true) {
			return;
		}
		
		if(!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}
		
		TentData data = AlmagestCore.savedDataManager.getTentData().getData((EntityPlayer)event.getEntity());
		if(data == null) {
			return;
		}
		
		if(!data.needUpdateSpace && !data.needUpdateWall) {
			return;
		}
		
		//can't assure in case of minus.
		int chunkX = (int)Math.floor((double)data.tentPos.getX()/16D);
		int chunkY = (int)Math.floor((double)data.tentPos.getY()/16D);
		int chunkZ = (int)Math.floor((double)data.tentPos.getZ()/16D);
		
		//extend space.
		boolean doUpdateWall = false;
		if(data.needUpdateSpace) {
			IBlockState state = Blocks.AIR.getDefaultState();
			for(int currentY = data.prevSizeLevel*2+4; currentY <= data.sizeLevel*2+3; currentY++) {
				for(int currentX = 6-(data.sizeLevel-1); currentX <= 8+(data.sizeLevel-1); currentX++) {
					for(int currentZ = 6-(data.sizeLevel-1); currentZ <= 8+(data.sizeLevel-1); currentZ++) {
						event.getWorld().setBlockState(new BlockPos(chunkX*16+currentX, chunkY*16+currentY, chunkZ*16+currentZ), state);
					}
				}
			}
			for(int currentY = 3; currentY <= data.prevSizeLevel*2+3; currentY++) {
				for(int currentX = 6-(data.sizeLevel-1); currentX <= 8+(data.sizeLevel-1); currentX++) {
					for(int currentZ = 8+(data.prevSizeLevel-1)+1; currentZ <= 8+(data.sizeLevel-1); currentZ++) {
						event.getWorld().setBlockState(new BlockPos(chunkX*16+currentX, chunkY*16+currentY, chunkZ*16+currentZ), state);
					}
					for(int currentZ = 6-(data.prevSizeLevel-1)-1; currentZ >= 6-(data.sizeLevel-1); currentZ--) {
						event.getWorld().setBlockState(new BlockPos(chunkX*16+currentX, chunkY*16+currentY, chunkZ*16+currentZ), state);
					}
				}
				for(int currentZ = 6-(data.prevSizeLevel-1); currentZ <= 8+(data.prevSizeLevel-1); currentZ++) {
					for(int currentX = 8+(data.prevSizeLevel-1)+1; currentX <= 8+(data.sizeLevel-1); currentX++) {
						event.getWorld().setBlockState(new BlockPos(chunkX*16+currentX, chunkY*16+currentY, chunkZ*16+currentZ), state);
					}
					for(int currentX = 6-(data.prevSizeLevel-1)-1; currentX >= 6-(data.sizeLevel-1); currentX--) {
						event.getWorld().setBlockState(new BlockPos(chunkX*16+currentX, chunkY*16+currentY, chunkZ*16+currentZ), state);
					}
				}
			}
			
			data.finishUpdateSpace();
			doUpdateWall = true;
		}
		
		//change wall.
		if(data.needUpdateWall || doUpdateWall) {
			IBlockState state = Block.getBlockFromItem(data.wallStack.getItem()).getStateFromMeta(data.wallStack.getMetadata());
			
			for(int currentX = 6-data.sizeLevel; currentX <= 8+data.sizeLevel; currentX++) {
				for(int currentZ = 6-data.sizeLevel; currentZ <= 8+data.sizeLevel; currentZ++) {
					event.getWorld().setBlockState(new BlockPos(chunkX*16+currentX, chunkY*16+data.sizeLevel*2+4, chunkZ*16+currentZ), state);
					event.getWorld().setBlockState(new BlockPos(chunkX*16+currentX, chunkY*16+2, chunkZ*16+currentZ), state);
				}
			}
			for(int currentY = 3; currentY <= data.sizeLevel*2+3; currentY++) {
				for(int currentX = 6-data.sizeLevel; currentX <= 8+data.sizeLevel; currentX++) {
					event.getWorld().setBlockState(new BlockPos(chunkX*16+currentX, chunkY*16+currentY, chunkZ*16+(6-data.sizeLevel)), state);
					event.getWorld().setBlockState(new BlockPos(chunkX*16+currentX, chunkY*16+currentY, chunkZ*16+(8+data.sizeLevel)), state);
				}
				for(int currentZ = 6-data.sizeLevel-1; currentZ <= 8+data.sizeLevel-1; currentZ++) {
					event.getWorld().setBlockState(new BlockPos(chunkX*16+(6-data.sizeLevel), chunkY*16+currentY, chunkZ*16+currentZ), state);
					event.getWorld().setBlockState(new BlockPos(chunkX*16+(8+data.sizeLevel), chunkY*16+currentY, chunkZ*16+currentZ), state);
				}
			}
			
			data.finishUpdateWall();
		}
		
	}
	
	//指定の範囲外のブロック破壊をキャンセルする。
	@SubscribeEvent
	public void onBreakBlock(BreakEvent event) {
		
		if(event.getWorld().provider.getDimension() != AlmagestData.dimensionID_tent) {
			return;
		}
		
		if(event.getWorld().isRemote) {
			return;
		}
		
		if(event.getPlayer() == null) {
			return;
		}
		
		if(((EntityPlayerMP)event.getPlayer()).isCreative()) {
			return;
		}
		
		TentData data = AlmagestCore.savedDataManager.getTentData().getData(event.getPlayer());
		if(data == null) {
			return;
		}
		
		//can't assure in case of minus.
		int chunkX = (int)Math.floor((double)data.tentPos.getX()/16D);
		int chunkY = (int)Math.floor((double)data.tentPos.getY()/16D);
		int chunkZ = (int)Math.floor((double)data.tentPos.getZ()/16D);
		int relativeX = event.getPos().getX()-chunkX*16;
		int relativeY = event.getPos().getY()-chunkY*16;
		int relativeZ = event.getPos().getZ()-chunkZ*16;
		int sizeOffset = data.sizeLevel-1;
		
		if(relativeX < 6-sizeOffset || 8+sizeOffset < relativeX || relativeZ < 6-sizeOffset || 8+sizeOffset < relativeZ
				|| relativeY < 3 || 5+sizeOffset*2 < relativeY) {
			event.setCanceled(true);
		}
		
		if(relativeX == 7 && relativeY == 3 && relativeZ == 7) {
			event.setCanceled(true);			
		}
		
	}
	
	//指定の範囲外のアイテムドロップをクリアする。
	@SubscribeEvent
	public void onHarvestDrops(HarvestDropsEvent event) {
		
		if(event.getWorld().provider.getDimension() != AlmagestData.dimensionID_tent) {
			return;
		}
		
		if(event.getWorld().isRemote) {
			return;
		}
		
		if(event.getHarvester() == null) {
			return;
		}
		
		if(((EntityPlayerMP)event.getHarvester()).isCreative()) {
			return;
		}
		
		TentData data = AlmagestCore.savedDataManager.getTentData().getData(event.getHarvester());
		if(data == null) {
			return;
		}
		
		//can't assure in case of minus.
		int chunkX = (int)Math.floor((double)data.tentPos.getX()/16D);
		int chunkY = (int)Math.floor((double)data.tentPos.getY()/16D);
		int chunkZ = (int)Math.floor((double)data.tentPos.getZ()/16D);
		int relativeX = event.getPos().getX()-chunkX*16;
		int relativeY = event.getPos().getY()-chunkY*16;
		int relativeZ = event.getPos().getZ()-chunkZ*16;
		int sizeOffset = data.sizeLevel-1;
		
		if(relativeX < 6-sizeOffset || 8+sizeOffset < relativeX || relativeZ < 6-sizeOffset || 8+sizeOffset < relativeZ
				|| relativeY < 3 || 5+sizeOffset*2 < relativeY) {
			event.getDrops().clear();
		}
		
	}
	
}
