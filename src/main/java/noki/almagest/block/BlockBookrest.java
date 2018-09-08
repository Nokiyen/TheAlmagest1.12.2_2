package noki.almagest.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.AlmagestCore;
import noki.almagest.AlmagestData;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.attribute.AttributeHelper;
import noki.almagest.attribute.BlockWithAttribute;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.event.post.AttributeLevelEvent;
import noki.almagest.item.ItemBlockBookrest;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithEvent;
import noki.almagest.registry.IWithItemBlock;
import noki.almagest.registry.IWithRecipe;
import noki.almagest.saveddata.PropertySet;


/**********
 * @class BlockBookrest
 *
 * @description
 */
public class BlockBookrest extends BlockWithAttribute implements IWithRecipe, IWithItemBlock, IWithEvent {
	

	//******************************//
	// define member variables.
	//******************************//
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	
	//******************************//
	// define member methods.
	//******************************//
	public BlockBookrest() {
		
		super(Material.WOOD);
		this.setHardness(2.5F);
		this.setSoundType(SoundType.WOOD);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setAttributeLevel(EStarAttribute.WOOD, 20);
		this.setAttributeLevel(EStarAttribute.TOOL, 20);
		this.setAttributeLevel(EStarAttribute.STAR, 20);
		
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		
		return EnumBlockRenderType.MODEL;
		
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		
		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
		
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		
		return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
		
	}
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing,
			float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
		
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
		
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		
		return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
		
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		
		return new BlockStateContainer(this, new IProperty[] {FACING});
		
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumHand hand, EnumFacing facing, float f1, float f2, float f3) {
		
		player.openGui(AlmagestCore.instance, AlmagestData.guiID_bookrest, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
		
	}
	
	@Override
	public ItemBlock getItemBlock() {
		
		return new ItemBlockBookrest(this);
		
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
				new StarRecipe(new ItemStack(this)) {
					@Override
					public boolean matches(InventoryCrafting inv, World worldIn) {
						int plankCount = 0;
						int attributeSum = 0;
						for(int i=0; i<inv.getSizeInventory(); i++) {
							ItemStack selectedStack = inv.getStackInSlot(i);
							if(selectedStack.isEmpty()) {
								continue;
							}

							boolean plankFlag = false;
							boolean attributeFlag = false;
							
							Block block = Block.getBlockFromItem(selectedStack.getItem());
							if(this.isPlank(block)) {
								plankCount++;
								plankFlag = true;
							}
							int level = AttributeHelper.getAttrributeLevel(selectedStack, EStarAttribute.STAR);
							attributeSum += level;
							if(level != 0) {
								attributeFlag = true;
							}
							
							if(plankFlag == false && attributeFlag == false) {
								return false;
							}
						}
						
						if(plankCount == 2 && attributeSum >= 10) {
							return true;
						}
						return false;
					}
					
					private boolean isPlank(Block block) {
						if(block == Blocks.PLANKS) {
							return true;
						}
						return false;
					}
					
					@Override
					public boolean isSpecial() {
						return true;
					}
				}.setMaxStack(4)
		);
		
	}
	
}
