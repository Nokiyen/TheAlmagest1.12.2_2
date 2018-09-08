package noki.almagest.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.attribute.BlockWithAttribute;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.item.ItemBlockStar;
import noki.almagest.registry.IWithItemBlock;
import noki.almagest.registry.IWithSubTypes;


/**********
 * @class BlockStar
 *
 * @description
 * @see ItemBlockStar.
 */
public class BlockStar extends BlockWithAttribute implements IWithSubTypes, IWithItemBlock {
	
	//******************************//
	// define member variables.
	//******************************//
	public static final PropertyInteger METADATA = PropertyInteger.create("metadata", 0, 7);

	
	//******************************//
	// define member methods.
	//******************************//
	public BlockStar() {
		
		super(Material.ROCK);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.STONE);
		this.setLightLevel(1.0F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(METADATA, 0));
		
		this.setAttributeLevel(EStarAttribute.STAR, 10);
		this.setAttributeLevel(EStarAttribute.MINERAL, 10);
		this.setMemory(50);
		
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		
		return ((Integer)state.getValue(METADATA)).intValue();
		
	}
	
	@Override
	public IBlockState getStateFromMeta(int metadata) {
		
		return this.getDefaultState().withProperty(METADATA, Integer.valueOf(metadata));
		
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		
		return new BlockStateContainer(this, METADATA);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		
		list.addAll(this.getSubItems());
		
	}

	@Override
	public ItemBlock getItemBlock() {
		
		return new ItemBlockStar(this);
		
	}
	

	@Override
	public List<ItemStack> getSubItems() {
		
		List<ItemStack> items = new ArrayList<>();
		for(int i=0; i<=7; i++) {
			items.add(new ItemStack(this, 1, i));
		}
		return items;
		
	}
	
	@Override
	public int getSubtypeCount() {
		
		return 8;
		
	}
	
	@Override
	public boolean registerToAlmagest() {
		
		return false;
		
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		
		return this.getMetaFromState(state);
		
	}

}
