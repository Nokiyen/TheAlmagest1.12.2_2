package noki.almagest.world.planisphere;

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
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.AlmagestData;
import noki.almagest.ModInfo;
import noki.almagest.client.render.world.PlaniSkyRenderer;


public class PlaniWorldProvider extends WorldProvider {
	
	@SideOnly(Side.CLIENT)
	private IRenderHandler skyRenderer;
	@SideOnly(Side.CLIENT)
	private IRenderHandler weatherRenderer;
	
	
	@Override
	public void init() {
		
		this.biomeProvider = new BiomeProviderSingle(ForgeRegistries.BIOMES.getValue(new ResourceLocation(ModInfo.ID.toLowerCase(), "planisphere")));
		this.hasSkyLight = true;
//		NBTTagCompound nbttagcompound = this.worldObj.getWorldInfo().getDimensionData(AlmagestData.dimensionType);
		
	}
	
	@Override
	public IChunkGenerator createChunkGenerator() {
		
		return new PlaniChunkGenerator(this.world, this.world.getSeed());
		
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
	
	// if return false, minecraft will get crash!!
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
		
		return new BlockPos(0, 90, 0);
		
	}
	
	@Override
	public int getAverageGroundLevel() {
		
		return 88;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
		
		return false;
		
	}
	
	@Override
	public DimensionType getDimensionType() {
		
		return DimensionType.getById(AlmagestData.dimensionID_planisphere);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getSkyColor(net.minecraft.entity.Entity cameraEntity, float partialTicks) {
		
		return new Vec3d(0.0D, 0.0D, 0.0D);
		
	}
	
	@Override
	public float getSunBrightnessFactor(float par1) {
		
		return 1.0F;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getSkyRenderer() {
		
		if(this.skyRenderer == null) {
			this.skyRenderer = new PlaniSkyRenderer();
		}
		return this.skyRenderer;
		
	}
	
/*	@Override
	@SideOnly(Side.CLIENT)
    public IRenderHandler getWeatherRenderer() {
		
		if(this.weatherRenderer == null) {
			this.weatherRenderer = new IRenderHandler() {
				@Override
				public void render(float partialTicks, WorldClient world, Minecraft mc) {
					//no weather.
				}
			};
		}
		
		return this.weatherRenderer;
		
	}*/
	
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
