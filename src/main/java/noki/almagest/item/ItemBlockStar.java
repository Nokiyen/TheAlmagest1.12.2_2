package noki.almagest.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;


/**********
 * @class ItemBlockStar
 *
 * @description
 */
public class ItemBlockStar extends ItemBlock {
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemBlockStar(Block block) {
		
		super(block);
		this.setHasSubtypes(true);
		
	}
	
	@Override
	public int getMetadata(int metadata) {
		
		return MathHelper.clamp(metadata, 0, 7);
		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		
		return this.getUnlocalizedName() + "." + stack.getMetadata();
		
	}
	
}
