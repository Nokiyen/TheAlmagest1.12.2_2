package noki.almagest.item;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noki.almagest.AlmagestCore;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.packet.PacketHandler;
import noki.almagest.packet.PacketStarCompassSync;
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
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemBlockStarCompass(Block block) {
		
		super(block);
		
	}
	
	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		
		if(StarPropertyCreator.isMagnitude(stack, 3)) {
			return true;
		}
		return false;
		
	}
	
	@Override
	@Nullable
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		
		EntityItem item = new EntityItem(world, location.posX, location.posY-0.30000001192092896D+(double)location.getEyeHeight(), location.posZ, itemstack){
			@Override
			public boolean attackEntityFrom(DamageSource source, float amount) {
				if(source.isFireDamage() || source.isExplosion()) {
					return false;
				}
				return super.attackEntityFrom(source, amount);
			}
		};
		item.setPickupDelay(40);
		
		return item;
		
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState state) {
		
		boolean res = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, state);
		if(res == false) {
			return false;
		}
		
		if(world.provider.getDimension() != -1 && !world.isRemote) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null && tile instanceof TileStarCompass) {
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
	
}
