package noki.almagest.block;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noki.almagest.AlmagestCore;
import noki.almagest.attribute.BlockContainerWithAttribute;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.item.ItemBlockConstellation;
import noki.almagest.packet.PacketHandler;
import noki.almagest.packet.PacketUpdateMessage;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithItemBlock;
import noki.almagest.registry.IWithRecipe;
import noki.almagest.registry.IWithSubTypes;
import noki.almagest.registry.ModItems;
import noki.almagest.tile.TileConstellation;


/**********
 * @class BlockConstellation
 *
 * @description 88の星図を表すブロックです。種類はNBTで管理。
 * @see ItemBlockConstellation, TileConstellation, RenderTileConstellation.
 */
public class BlockConstellation extends BlockContainerWithAttribute implements IWithItemBlock, IWithSubTypes {

	//******************************//
	// define member variables.
	//******************************//
	// indicating whether it is ecliptical constellation or not.
	public static final PropertyBool ECLIPTICAL = PropertyBool.create("ecliptical");
	// indicating whether the constellation is lacking stars or not.
	public static final PropertyBool COMPLETE = PropertyBool.create("complete");
	
	
	//******************************//
	// define member methods.
	//******************************//
	public BlockConstellation() {
		
		super(Material.GLASS);
		this.setHardness(0.3F);
		this.setSoundType(SoundType.GLASS);
		this.setLightLevel(1.0F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(ECLIPTICAL, false).withProperty(COMPLETE, true));
		this.setAttributeLevel(EStarAttribute.STAR, 40);
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		
		return new TileConstellation();
		
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
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		
		if((Boolean)state.getValue(COMPLETE) == true) {
			return 15;
		}
		return 0;
		
	}
	
/*	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos) {
		
		if((Boolean)world.getBlockState(pos).getValue(COMPLETE) == true) {
			return 15;
		}
		else {
			return 8;
		}
		
	}*/
	
	@Override
	public int getMetaFromState(IBlockState state) {
		
		if((Boolean)state.getValue(ECLIPTICAL) == false) {
			return (Boolean)state.getValue(COMPLETE) == true ? 0 : 2;
		}
		else {
			return (Boolean)state.getValue(COMPLETE) == true ? 1 : 3;
		}
		
	}
	
	@Override
	public IBlockState getStateFromMeta(int metadata) {
		
		switch(metadata) {
			case 0:
				return this.getDefaultState().withProperty(ECLIPTICAL, false).withProperty(COMPLETE, true);
			case 1:
				return this.getDefaultState().withProperty(ECLIPTICAL, true).withProperty(COMPLETE, true);
			case 2:
				return this.getDefaultState().withProperty(ECLIPTICAL, false).withProperty(COMPLETE, false);
			case 3:
				return this.getDefaultState().withProperty(ECLIPTICAL, true).withProperty(COMPLETE, false);
			default:
				return this.getDefaultState().withProperty(ECLIPTICAL, false).withProperty(COMPLETE, true);		
		}
		
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		
		return new BlockStateContainer(this, ECLIPTICAL, COMPLETE);
		
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		if(!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null && tile instanceof TileConstellation) {
				AlmagestCore.log("switch rotation.");
				((TileConstellation)tile).switchRotation();
				world.markBlockRangeForRenderUpdate(pos, pos);
				tile.markDirty();
				
				PacketHandler.instance.sendToAll(new PacketUpdateMessage(1,pos));
			}
		}
		
		return true;
		
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof TileConstellation) {
			int constNumber = ((TileConstellation)tile).getConstNumber();
			int[] missingStars = ((TileConstellation)tile).getMissingStars();
			if(missingStars.length == 0) {
				stacks.add(HelperConstellation.getConstStack(constNumber, 1));
			}
			else {
				stacks.add(HelperConstellation.getConstStack(constNumber, missingStars, 1));
			}
		}
		
		return stacks;
		
	}
	
	// NBT付スタックをドロップさせるには、removedByPlayer()とharvesBlock()のオーバライドが必要。
	// setBlockToAir()のタイミングを遅らせる。
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
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		
		list.addAll(this.getSubItems());
		
	}
	
/*	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				//ふうちょう座
				new StarRecipe(ItemBlockConstellation.getConstStack(3, 1))
					.setAttribute(EStarAttribute.STAR, 20).setStack(new ItemStack(ModItems.HONEY)),
				//インディアン座
				new StarRecipe(ItemBlockConstellation.getConstStack(44, 1))
					.setAttribute(EStarAttribute.STAR, 20).setStack(new ItemStack(Items.EMERALD)),
				//はえ座
				new StarRecipe(ItemBlockConstellation.getConstStack(56, 1))
					.setAttribute(EStarAttribute.STAR, 20).setStack(new ItemStack(Items.ROTTEN_FLESH)),
				//かじき座
				new StarRecipe(ItemBlockConstellation.getConstStack(33, 1))
					.setAttribute(EStarAttribute.STAR, 20).setStack(new ItemStack(Items.GOLD_INGOT)),
				//みずへび座
				new StarRecipe(ItemBlockConstellation.getConstStack(43, 1))
					.setAttribute(EStarAttribute.STAR, 20).setAttribute(EStarAttribute.LIQUID, 10).setStack(new ItemStack(ModItems.TSUCHINOKO_SKIN)),
				//へび座
				new StarRecipe(ItemBlockConstellation.getConstStack(74, 1))
					.setAttribute(EStarAttribute.STAR, 20).setStack(new ItemStack(ModItems.TSUCHINOKO_SKIN)),
				//とびうお座
				new StarRecipe(ItemBlockConstellation.getConstStack(87, 1))
					.setAttribute(EStarAttribute.STAR, 20).setStack(new ItemStack(ModItems.FLYING_FISH)),
				//つる座
				new StarRecipe(ItemBlockConstellation.getConstStack(39, 1))
					.setAttribute(EStarAttribute.STAR, 20).setStack(new ItemStack(ModItems.ORIGAMI_CRANE))
		);
		
	}*/
	
	@Override
	public ItemBlock getItemBlock() {
		
		return new ItemBlockConstellation(this);
		
	}

	@Override
	public int getSubtypeCount() {
		
		return 1;
		
	}
	
	@Override
	public boolean registerToAlmagest() {
		
		return true;
		
	}

	@Override
	public List<ItemStack> getSubItems() {
		
		List<ItemStack> items = new ArrayList<>();
		for(Constellation each: Constellation.values()) {
			items.add(HelperConstellation.getConstStack(each.getId(), 1));
			if(each.isIncomplete() == true) {
//				AlmagestCore.log("const number is %s.", each.getId());
				items.add(HelperConstellation.getIncompleteConstStack(each, 1));
			}
		}
		return items;
	}

}
