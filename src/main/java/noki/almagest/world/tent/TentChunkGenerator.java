package noki.almagest.world.tent;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import noki.almagest.registry.ModBlocks;


public class TentChunkGenerator implements IChunkGenerator {
	
	private World world;

	public TentChunkGenerator(World world, long seed) {
		
		this.world = world;
				
	}
	
	@Override
	public Chunk generateChunk(int chunkX, int chunkZ) {
				
		ChunkPrimer chunkPrimer = new ChunkPrimer();
		
		for(int yChunk=0; yChunk<16; yChunk++) {
			for(int relativeX=0; relativeX<16; relativeX++) {
				for(int relativeY=0; relativeY<16; relativeY++) {
					for(int relativeZ=0; relativeZ<16; relativeZ++) {
						int currentY = yChunk*16+relativeY;
						if(relativeX < 6 || 8 < relativeX || relativeZ < 6 || 8 < relativeZ || relativeY < 3 || 5 < relativeY) {
							chunkPrimer.setBlockState(relativeX, currentY, relativeZ, Blocks.BEDROCK.getDefaultState());
						}
					}
				}
			}
		}
				
		Chunk chunk = new Chunk(this.world, chunkPrimer, chunkX, chunkZ);
		byte[] abyte = chunk.getBiomeArray();
		for(int i = 0; i < abyte.length; ++i) {
			abyte[i] = (byte)89;
		}
		chunk.generateSkylightMap();
		
		return chunk;
		
	}
	
	@Override
	public void populate(int chunkX, int chunkZ) {
		
		for(int yChunk=0; yChunk<16; yChunk++) {
			this.world.setBlockState(new BlockPos(7+chunkX*16, 3+yChunk*16, 7+chunkZ*16), ModBlocks.TENT.getDefaultState());
		}
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getPossibleCreatures(EnumCreatureType type, BlockPos pos) {
		
        return this.world.getBiome(pos).getSpawnableList(type);
        
	}
	
	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		
		return false;
		
	}
	
	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) {
		
		// nothing to do.
		
	}
	
	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {

		return null;
		
	}

	@Override
	public boolean isInsideStructure(World p_193414_1_, String p_193414_2_, BlockPos p_193414_3_) {
		
		return false;
		
	}
	
}