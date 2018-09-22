package noki.almagest.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noki.almagest.AlmagestCore;
import noki.almagest.AlmagestData;
import noki.almagest.attribute.BlockWithAttribute;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.helper.HelperTeleport;
import noki.almagest.item.ItemBlockTent;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithItemBlock;
import noki.almagest.registry.IWithRecipe;
import noki.almagest.registry.ModBlocks;
import noki.almagest.saveddata.AlmagestDataTent.TentData;
import noki.almagest.tile.TileTent;


/**********
 * @class BlockTent
 *
 * @description
 */
public class BlockTent extends BlockWithAttribute implements IWithRecipe, IWithItemBlock, ITileEntityProvider {
	

	//******************************//
	// define member variables.
	//******************************//
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	
	//******************************//
	// define member methods.
	//******************************//
	public BlockTent() {
		
		super(Material.WOOD);
		this.setHardness(2.5F);
		this.setSoundType(SoundType.WOOD);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setAttributeLevel(EStarAttribute.TOOL, 40);
		
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumHand hand, EnumFacing facing, float f1, float f2, float f3) {
		
		if(world.isRemote) {
			return true;
		}
		
		EntityPlayerMP entityPlayerMP = (EntityPlayerMP)player;
		if(world.provider.getDimension() == AlmagestData.dimensionID_tent) {
			TentData data = AlmagestCore.savedDataManager.getTentData().getData(player);
			if(data == null) {
				HelperTeleport.teleportPlayer(0, entityPlayerMP);
			}
			else {
				HelperTeleport.teleportPlayer(data.destinationId, data.destinationX, data.destinationY, data.destinationZ, entityPlayerMP);
			}
		}
		else {
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null && tile instanceof TileTent) {
				TileTent tent = (TileTent)tile;
				BlockPos tentPos = AlmagestCore.savedDataManager.getTentData()
						.updateData(entityPlayerMP, tent.getSizeLevel(), (tent.getWallStack() == null)?ItemStack.EMPTY:tent.getWallStack());
				HelperTeleport.teleportPlayer(AlmagestData.dimensionID_tent,
						(double)tentPos.getX()+0.5D, (double)tentPos.getY()+0.5D, (double)tentPos.getZ()+0.5D, entityPlayerMP);
			}
		}
		
		return true;
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		
		return new TileTent();
		
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		
		if(willHarvest == true)  {
			return true;
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
		
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state,
		@Nullable TileEntity te, @Nullable ItemStack stack) {
		
		super.harvestBlock(world, player, pos, state, te, stack);
		world.setBlockToAir(pos);
		
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof TileTent) {
			TileTent tent = (TileTent)tile;
			if(tent.getWallStack() != null) {
				stacks.add(ItemBlockTent.getStackWithWall(new ItemStack(ModBlocks.TENT), tent.getWallStack()));
			}
			else {
				stacks.add(new ItemStack(ModBlocks.TENT));
			}
		}
		
		return stacks;
		
	}
	
	@Override
	public ItemBlock getItemBlock() {
		
		return new ItemBlockTent(this);
		
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
		
		return 10000F;
		
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		
		return 0;

	}
	
	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				new StarRecipe(new ItemStack(this))
					.setAttribute(EStarAttribute.TOOL, 60).setAttribute(EStarAttribute.DECORATIVE, 50)
					.setStack(new ItemStack(Items.ENDER_PEARL)).setStack(new ItemStack(Blocks.GLASS_PANE)),
					
				new IRecipe() {
					private ResourceLocation resource;
					private ItemStack resultStack = ItemStack.EMPTY;
					
					@Override
					public IRecipe setRegistryName(ResourceLocation name) {
						this.resource = name;
						return this;
					}
					
					@Override
					public Class<IRecipe> getRegistryType() {
						return IRecipe.class;
					}
					
					@Override
					public ResourceLocation getRegistryName() {
						return this.resource;
					}
					
					@Override
					public ItemStack getRecipeOutput() {
						return new ItemStack(ModBlocks.TENT);
					}
					
					@Override
					public ItemStack getCraftingResult(InventoryCrafting inv) {
						return this.resultStack.copy();
					}
					
					@Override
					public boolean canFit(int width, int height) {
						return width*height >= 1;
					}
					
					@SuppressWarnings("deprecation")
					@Override
					public boolean matches(InventoryCrafting inventory, World worldIn) {
						this.resultStack = ItemStack.EMPTY;
						
						ItemStack tentStack = null;
						int tentStackCount = 0;
						ItemStack wallStack = null;
						int wallStackCount = 0;
						for(int i = 0; i < inventory.getSizeInventory(); i++) {
							ItemStack currentStack = inventory.getStackInSlot(i);
							if(currentStack == null || currentStack.isEmpty() || !(currentStack.getItem() instanceof ItemBlock)) {
								continue;
							}
							
							if(Block.getBlockFromItem(currentStack.getItem()) == ModBlocks.TENT) {
								tentStack = currentStack;
								tentStackCount++;
							}
							else {
								Block block = Block.getBlockFromItem(currentStack.getItem());
								if(block == null || block == Blocks.AIR) {
									continue;
								}
								try {
									IBlockState state = block.getStateFromMeta(currentStack.getMetadata());
									if(!block.isFullBlock(state) || !block.isFullCube(state) || block.canProvidePower(state)
											|| !block.getMaterial(state).blocksMovement() || block.getMaterial(state).isReplaceable()
											|| block.hasTileEntity(state)) {
										continue;
									}
								}
								catch (Exception e) {
									continue;
								}
								wallStack = currentStack;
								wallStackCount++;
							}
							
							if(tentStackCount > 1 || wallStackCount > 1) {
								return false;
							}
						}
						
						if(tentStack == null) {
							return false;
						}
						
						if(wallStack == null) {
							ItemStack removedStack = ItemBlockTent.removeWall(tentStack);
							if(removedStack == null) {
								return false;
							}
							this.resultStack = removedStack;
							return true;
						}
						
						this.resultStack = ItemBlockTent.getStackWithWall(tentStack, wallStack);
						return true;
					}
				}
		);
		
	}
	
}
