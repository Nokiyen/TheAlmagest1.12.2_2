package noki.almagest.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.AlmagestCore;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.attribute.BlockWithAttribute;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.event.post.AttributeLevelEvent;
import noki.almagest.item.ItemBlockStarCompass;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithItemBlock;
import noki.almagest.registry.IWithRecipe;
import noki.almagest.saveddata.PropertySet;
import noki.almagest.tile.TileStarCompass;


/**********
 * @class BlockStarCompass
 *
 * @description
 */
public class BlockStarCompass extends BlockWithAttribute implements IWithRecipe, IWithItemBlock, ITileEntityProvider {
	

	//******************************//
	// define member variables.
	//******************************//
	protected static final AxisAlignedBB box = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 3.0D/16.0D, 1.0D);
	
	
	//******************************//
	// define member methods.
	//******************************//
	public BlockStarCompass() {
		
		super(Material.IRON);
		this.setHardness(3.0F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.METAL);
		this.setAttributeLevel(EStarAttribute.TOOL, 20);
		this.setAttributeLevel(EStarAttribute.METAL, 20);
		this.setAttributeLevel(EStarAttribute.STAR, 20);
		this.setMemory(50);
		
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		
		return EnumBlockRenderType.MODEL;
		
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		
		return false;
		
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		
		return false;
		
	}
	
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		
		return BlockRenderLayer.CUTOUT;
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		
		return new TileStarCompass();
		
	}
	
	@Override
	public ItemBlock getItemBlock() {
		
		return new ItemBlockStarCompass(this);
		
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		
		return box;
		
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
		
		PropertySet set = AlmagestCore.savedDataManager.getBlockData().getProperty(world, pos);
		if(set == null) {
			return super.getExplosionResistance(world, pos, exploder, explosion);
		}
		if(!StarPropertyCreator.isMagnitude(set.getMemory(), 3)) {
			return super.getExplosionResistance(world, pos, exploder, explosion);
		}
		return 10000F;
		
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

		PropertySet set = AlmagestCore.savedDataManager.getBlockData().getProperty((World)world, pos);
		if(set == null) {
			return super.getFlammability(world, pos, face);
		}
		if(!StarPropertyCreator.isMagnitude(set.getMemory(), 3)) {
			return super.getFlammability(world, pos, face);
		}
		return 0;

	}
	
	@SubscribeEvent
	public void onAttributeLevel(AttributeLevelEvent event) {
		
		if(Block.getBlockFromItem(event.getStack().getItem()) == this && event.getAttribute() == EStarAttribute.STAR &&
				StarPropertyCreator.isMagnitude(event.getStack(), 5)) {
			event.setLevel(event.getLevel()+10);
		}
		
	}
	
	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				new StarRecipe(new ItemStack(this)).setAttribute(EStarAttribute.STAR, 30).setStack(new ItemStack(Items.COMPASS)));
		
	}
	
}
