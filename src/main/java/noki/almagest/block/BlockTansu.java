package noki.almagest.block;

import java.util.List;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noki.almagest.AlmagestCore;
import noki.almagest.AlmagestData;
import noki.almagest.attribute.BlockContainerWithAttribute;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithRecipe;
import noki.almagest.tile.TileTansu;


/**********
 * @class BlockTansu
 *
 * @description
 */
public class BlockTansu extends BlockContainerWithAttribute implements IWithRecipe {

	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public BlockTansu() {
		
		super(Material.WOOD);
		this.setHardness(2.5F);
		this.setSoundType(SoundType.WOOD);
		
		this.setAttributeLevel(EStarAttribute.WOOD, 30);
		this.setAttributeLevel(EStarAttribute.TOOL, 30);
		
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumHand hand, EnumFacing facing, float f1, float f2, float f3) {
		
		player.openGui(AlmagestCore.instance, AlmagestData.guiID_tansu, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
		
	}

	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				new StarRecipe(new ItemStack(this)).setAttribute(EStarAttribute.WOOD, 50));
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		
//		return new TileTansu();
		
		return null;
		
	}
	
	public EnumBlockRenderType getRenderType(IBlockState state) {
		
		return EnumBlockRenderType.MODEL;
		
	}

}
