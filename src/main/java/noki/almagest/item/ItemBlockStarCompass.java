package noki.almagest.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noki.almagest.AlmagestCore;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.helper.HelperNBTStack;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.packet.PacketHandler;
import noki.almagest.packet.PacketStarCompassSync;
import noki.almagest.packet.PacketSyncCompass;
import noki.almagest.registry.ModBlocks;
import noki.almagest.tile.TileStarCompass;


/**********
 * @class ItemBlockStarCompass
 *
 * @description
 */
public class ItemBlockStarCompass extends ItemBlock {
	
	//******************************//
	// define member variables.
	//******************************//
	private static int timeToUse = 60*3;
	private static final String NBT_tick = "tick";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemBlockStarCompass(Block block) {
		
		super(block);
		this.setMaxDamage(timeToUse);
		this.setMaxStackSize(1);
		
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState state) {
		
		boolean res = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, state);
		if(res == false) {
			return false;
		}
		
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof TileStarCompass) {
			((TileStarCompass)tile).setStackData(stack.getMetadata(), new HelperNBTStack(stack).getInteger(NBT_tick));
			
			boolean dimensinoFlag = world.provider.getDimension() == 0 || StarPropertyCreator.getMagnitude(stack) <= 2;
			if(dimensinoFlag && !world.isRemote) {
				BlockPos targetPos = AlmagestCore.savedDataManager.getConstData().getNearestConstellation(world, pos);
				if(targetPos != null) {
					Constellation constellation = AlmagestCore.savedDataManager.getConstData().getConstellation(world, targetPos);
					((TileStarCompass)tile).setTarget(targetPos, constellation);
					PacketHandler.instance.sendToAll(
							new PacketStarCompassSync(world.provider.getDimension(), pos, targetPos, constellation.getId()));
				}
			}
		}
		
		return true;
		
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		
		if(world.isRemote) {
			return;
		}
		if(!(entity instanceof EntityPlayerMP)) {
			return;
		}
		
		EntityPlayerMP player = (EntityPlayerMP)entity;
		if(!isSelected && player.getHeldItemOffhand() != stack) {
			return;
		}
		int magnitude = StarPropertyCreator.getMagnitude(stack);
		if(magnitude == 1) {
			return;
		}
		
		int consumeTick = 20;
		if(magnitude <= 4) {
			consumeTick = 60;
		}
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		int nextTick = (nbtStack.getInteger(NBT_tick)+1) % consumeTick;
		nbtStack.setInteger(NBT_tick, nextTick);
		
		if(nextTick == 0) {
			stack.damageItem(1, player);
			PacketHandler.instance.sendTo(new PacketSyncCompass(itemSlot, stack.getItemDamage()), player);
		}
		
	}
	
	public static ItemStack getStackWithNbt(int metadata, int tick) {
		
		ItemStack stack = new ItemStack(ModBlocks.STAR_COMPASS);
		stack.setItemDamage(metadata);
		return new HelperNBTStack(stack).setInteger(NBT_tick, tick).getStack();
		
	}
	
}
