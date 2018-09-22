package noki.almagest.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.helper.HelperNBTStack;
import noki.almagest.tile.TileTent;


/**********
 * @class ItemBlockStar
 *
 * @description
 */
public class ItemBlockTent extends ItemBlock {
	
	//******************************//
	// define member variables.
	//******************************//
	private static String NBT_wallBlock = "wall_block";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemBlockTent(Block block) {
		
		super(block);
		this.setMaxStackSize(1);
		
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState state) {
		
		boolean res = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, state);
		if(res == false) {
			return false;
		}
		
		if(!world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null && tile instanceof TileTent) {
				TileTent tent = (TileTent)tile;
				switch(StarPropertyCreator.getMagnitude(stack)) {
					case 1:
						tent.setInfo(4, new ItemStack(new HelperNBTStack(stack).getTag(NBT_wallBlock)));
						break;
					case 2:
						tent.setInfo(3, new ItemStack(new HelperNBTStack(stack).getTag(NBT_wallBlock)));
						break;
					case 3:
						tent.setInfo(2, new ItemStack(new HelperNBTStack(stack).getTag(NBT_wallBlock)));
						break;
					case 4:
						tent.setInfo(2, null);
						break;
					case 5:
					case 6:
						break;
				}
			}
		}
		
		return true;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		
		ItemStack wallStack = new ItemStack(new HelperNBTStack(stack).getTag(NBT_wallBlock));
		int wallSize = 3;
		boolean wallFlag = false;
		switch(StarPropertyCreator.getMagnitude(stack)) {
			case 1:
				wallSize = 9;
				wallFlag = true;
				break;
			case 2:
				wallSize = 7;
				wallFlag = true;
				break;
			case 3:
				wallSize = 5;
				wallFlag = true;
				break;
			case 4:
				wallSize = 5;
				break;
			case 5:
			case 6:
				break;
		}
		if(!wallStack.isEmpty() && wallFlag) {
			tooltip.add(new TextComponentTranslation("almagest.tile.tent.tooltip.ver2", wallSize, wallSize, wallStack.getDisplayName())
					.setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText());
		}
		else {
			tooltip.add(new TextComponentTranslation("almagest.tile.tent.tooltip.ver1", wallSize, wallSize)
					.setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText());
		}
		
	}
	
	public static ItemStack getStackWithWall(ItemStack stack, ItemStack wallStack) {
		
		return new HelperNBTStack(stack.copy()).setTag(NBT_wallBlock, wallStack.serializeNBT()).getStack();
		
	}
	
	public static ItemStack removeWall(ItemStack stack) {
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack.copy());
		if(nbtStack.hasKey(NBT_wallBlock)) {
			return nbtStack.removeTag(NBT_wallBlock).getStack();
		}
		return null;
		
	}
	
}
