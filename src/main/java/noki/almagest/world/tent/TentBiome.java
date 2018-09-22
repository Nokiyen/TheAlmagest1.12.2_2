package noki.almagest.world.tent;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class TentBiome extends Biome {
	
	public TentBiome(Biome.BiomeProperties properties) {
		
		super(properties);
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
		this.decorator = new TentBiomeDecorator();
		
	}
	
	@SideOnly(Side.CLIENT)
	public int getSkyColorByTemp(float par1) {
		
		return 0;
	
	}
	
	@Override
	public boolean canRain() {
		
		return false;
		
	}
	
}
