package noki.almagest.world.tent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.AlmagestData;
import noki.almagest.ModInfo;


public class TentWorldProvider extends WorldProvider {
	
	@Override
	public void init() {
		
		this.biomeProvider = new BiomeProviderSingle(ForgeRegistries.BIOMES.getValue(new ResourceLocation(ModInfo.ID.toLowerCase(), "tent")));
		this.hasSkyLight = true;
		
	}
	
	@Override
	public IChunkGenerator createChunkGenerator() {
		
		return new TentChunkGenerator(this.world, this.world.getSeed());
		
	}
	
	@Override
	public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
		
		return 0.0F;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float[] calcSunriseSunsetColors(float p_76560_1_, float p_76560_2_) {
		
		return null;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
		
		int i = 10518688;
		float f2 = MathHelper.cos(p_76562_1_ * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		f2 = MathHelper.clamp(f2, 0.0F, 1.0F);
		float f3 = (float)(i >> 16 & 255) / 255.0F;
		float f4 = (float)(i >> 8 & 255) / 255.0F;
		float f5 = (float)(i & 255) / 255.0F;
		f3 *= f2 * 0.0F + 0.15F;
		f4 *= f2 * 0.0F + 0.15F;
		f5 *= f2 * 0.0F + 0.15F;
		
		return new Vec3d((double)f3, (double)f4, (double)f5);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isSkyColored() {
		
		return true;
		
	}
	
	@Override
	public boolean canRespawnHere() {
		
		return false;
		
	}
	
	@Override
	public boolean isSurfaceWorld(){
		
		return false;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getCloudHeight() {
		
		return 8.0F;
		
	}
	
	@Override
	public boolean canCoordinateBeSpawn(int x, int z) {
		
		return this.world.getGroundAboveSeaLevel(new BlockPos(x, 0, z)).getMaterial().blocksMovement();
		
	}
	
	@Override
	public BlockPos getSpawnPoint() {
		
		return new BlockPos(0, 0, 0);
		
	}
	
	@Override
	public int getAverageGroundLevel() {
		
		return 0;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
		
		return false;
		
	}
	
	@Override
	public DimensionType getDimensionType() {
		
		return DimensionType.getById(AlmagestData.dimensionID_tent);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getSkyColor(net.minecraft.entity.Entity cameraEntity, float partialTicks) {
		
		return new Vec3d(0.0D, 0.0D, 0.0D);
		
	}
	
	@Override
	public boolean isDaytime() {
		
		return false;
		
	}
	
	@Override
	public Biome getBiomeForCoords(BlockPos pos) {
		
		return this.biomeProvider.getBiome(pos);
		
	}
	
	@Override
	public WorldSleepResult canSleepAt(EntityPlayer player, BlockPos pos) {
		
		return WorldSleepResult.DENY;
		
	}

}
