package noki.almagest.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.attribute.BlockWithAttribute;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.item.ItemBlockAriadne;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithItemBlock;
import noki.almagest.registry.IWithRecipe;
import noki.almagest.registry.IWithSubTypes;
import noki.almagest.registry.ModItems;
import noki.almagest.tile.TileAriadne;


/**********
 * @class BlockAriadne
 *
 * @description
 */
public class BlockAriadne extends BlockWithAttribute implements IWithRecipe, IWithItemBlock, ITileEntityProvider, IWithSubTypes {
	

	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public BlockAriadne() {
		
		super(Material.CARPET);
		this.setHardness(0.8F);
		this.setSoundType(SoundType.CLOTH);
		this.setAttributeLevel(EStarAttribute.TOOL, 40);
		this.setAttributeLevel(EStarAttribute.MONSTER, 20);
		this.setMemory(50);
		
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		
		return EnumBlockRenderType.MODEL;
		
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		
		return true;
		
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		
		return false;
		
	}
	
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		
		return BlockRenderLayer.TRANSLUCENT;
		
	}
	
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos){
		
		return NULL_AABB;
		
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		stacks.add(new ItemStack(this, 1, 2));
		return stacks;
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		
		return new TileAriadne();
		
	}
	
	@Override
	public ItemBlock getItemBlock() {
		
		return new ItemBlockAriadne(this);
		
	}
	
	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				new StarRecipe(new ItemStack(this)).setAttribute(EStarAttribute.MONSTER, 30)
					.setStack(new ItemStack(Items.ENDER_PEARL)).setStack(new ItemStack(ModItems.COCKTAIL, 1, 0)),
				new StarRecipe(new ItemStack(this)).setAttribute(EStarAttribute.MONSTER, 30)
					.setStack(new ItemStack(Items.ENDER_PEARL)).setStack(new ItemStack(ModItems.COCKTAIL, 1, 1)),
				new StarRecipe(new ItemStack(this)).setAttribute(EStarAttribute.MONSTER, 30)
					.setStack(new ItemStack(Items.ENDER_PEARL)).setStack(new ItemStack(ModItems.COCKTAIL, 1, 2)),
				new StarRecipe(new ItemStack(this)).setAttribute(EStarAttribute.MONSTER, 30)
					.setStack(new ItemStack(Items.ENDER_PEARL)).setStack(new ItemStack(ModItems.COCKTAIL_RAINBOW)),
				new StarRecipe(new ItemStack(this))
					.setStack(new ItemStack(this, 1, 1)).setStack(new ItemStack(this, 1, 2))
		);
		
	}

	@Override
	public int getSubtypeCount() {
		
		return 3;
		
	}

	@Override
	public List<ItemStack> getSubItems() {
		
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(new ItemStack(this, 1, 0));
		return list;
		
	}

	@Override
	public boolean registerToAlmagest() {
		
		return false;
		
	}
	
}
