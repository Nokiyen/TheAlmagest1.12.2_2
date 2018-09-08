package noki.almagest.registry;

import net.minecraft.block.Block;
import noki.almagest.block.BlockBookrest;
import noki.almagest.block.BlockConstellation;
import noki.almagest.block.BlockStar;
import noki.almagest.block.BlockStarCompass;
import noki.almagest.block.BlockTansu;


/**********
 * @class ModItems
 *
 * @description このmodのブロックを登録し、インスタンスを保持しておくクラスです。
 * 
 */
public class ModBlocks {
	
	
	//******************************//
	// define member variables.
	//******************************//
	public static Block CONSTELLATION_BLOCK;
	public static final String CONSTELLATION_BLOCK_name = "constellation_block";
	
	public static Block ATLAS_STAR;
	public static final String ATLAS_STAR_name ="atlas_star";
	
	public static Block BOOKREST;
	public static final String BOOKREST_name = "bookrest";

	public static Block STAR_COMPASS;
	public static final String STAR_COMPASS_name = "star_compass";
	
	public static Block TANSU;
	public static final String TANSU_name = "tansu";

	
	//******************************//
	// define member methods.
	//******************************//
	public static void register() {
		
		CONSTELLATION_BLOCK = RegistryHelper.registerBlock(new BlockConstellation(), CONSTELLATION_BLOCK_name, false);
		ATLAS_STAR = RegistryHelper.registerBlock(new BlockStar(), ATLAS_STAR_name);
		BOOKREST = RegistryHelper.registerBlock(new BlockBookrest(), BOOKREST_name);
		STAR_COMPASS = RegistryHelper.registerBlock(new BlockStarCompass(), STAR_COMPASS_name);
//		TANSU = RegistryHelper.registerBlock(new BlockTansu(), TANSU_name);
		
	}

}
