package noki.almagest.item;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.AlmagestCore;
import noki.almagest.AlmagestData;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.block.BlockAriadne;
import noki.almagest.helper.HelperNBTStack;
import noki.almagest.helper.HelperTeleport;
import noki.almagest.registry.IWithEvent;


/**********
 * @class ItemBlockAriadne
 *
 * @description
 */
public class ItemBlockAriadne extends ItemBlock implements IWithEvent {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String NBT_xPos = "xpos";
	private static final String NBT_yPos = "ypos";
	private static final String NBT_zPos = "zpos";
	private static final String NBT_dimensionId = "dimension_id";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemBlockAriadne(Block block) {
		
		super(block);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		
		ItemStack stack = player.getHeldItem(hand);
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		boolean hasDimension = nbtStack.hasKey(NBT_dimensionId);
		
		if(!hasDimension) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
		}
		
		int destinationId = nbtStack.getInteger(NBT_dimensionId);
		double posX = (double)nbtStack.getInteger(NBT_xPos);
		double posY = (double)nbtStack.getInteger(NBT_yPos);
		double posZ = (double)nbtStack.getInteger(NBT_zPos);
		
		if(!AlmagestCore.savedDataManager.getAriadneData().isBlockPlacedAt(destinationId, new BlockPos(posX, posY, posZ))) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
		}
		
		if(player.world.provider.getDimension() == destinationId) {
			teleportEntityToCoordinates(player, posX+0.5D, posY+0.5D, posZ+0.5D);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}
		
		if(!world.isRemote && StarPropertyCreator.isMagnitude(stack, 2) && destinationId == 0) {
			//teleport;
			HelperTeleport.teleportPlayer(0, posX+0.5D, posY+0.5D, posZ+0.5D, (EntityPlayerMP)player);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player,
			World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		return this.placeAriadne(player, world, pos, hand, facing, hitX, hitY, hitZ);
		
	}
	
	@SubscribeEvent
	public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		
		if(!event.getEntityPlayer().isCreative()) {
			return;
		}
		
		ItemStack stack = event.getEntityPlayer().getHeldItem(event.getHand());
		if(stack.isEmpty() || !(stack.getItem() instanceof ItemBlockAriadne)) {
			return;
		}
		
		EnumActionResult result = this.placeAriadne(event.getEntityPlayer(), event.getWorld(), event.getPos(), event.getHand(),
				event.getFace(), (float)event.getHitVec().x, (float)event.getHitVec().y, (float)event.getHitVec().z);
		
		if(result == EnumActionResult.SUCCESS) {
			event.setCanceled(true);
			event.setCancellationResult(EnumActionResult.SUCCESS);
		}
		
	}
	
	private EnumActionResult placeAriadne(EntityPlayer player,
			World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		ItemStack stack = player.getHeldItem(hand);
		
		if(stack.isEmpty()) {
			return EnumActionResult.FAIL;
		}
		
		if(stack.getMetadata() == 1 && block instanceof BlockAriadne) {
			AlmagestCore.log("enter 1 && ariadne");
			HelperNBTStack nbtStack = new HelperNBTStack(stack);
			if(pos.getX() == nbtStack.getInteger(NBT_xPos) && pos.getY() == nbtStack.getInteger(NBT_yPos)
					&& pos.getZ() == nbtStack.getInteger(NBT_zPos)
					&& world.provider.getDimension() == nbtStack.getInteger(NBT_dimensionId)) {
				AlmagestCore.log("enter remove.");
				world.playEvent(2001, pos, Block.getStateId(state));
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
				nbtStack.removeTag(NBT_xPos);
				nbtStack.removeTag(NBT_yPos);
				nbtStack.removeTag(NBT_zPos);
				nbtStack.removeTag(NBT_dimensionId);
				stack.setItemDamage(0);
				AlmagestCore.savedDataManager.getAriadneData().removeBlock(world, pos);
			}
			return EnumActionResult.SUCCESS;
		}
		
		if(!block.isReplaceable(world, pos)) {
			pos = pos.offset(facing);
		}
		
		if(stack.getMetadata() == 0 
				&& player.canPlayerEdit(pos, facing, stack) && world.mayPlace(this.block, pos, false, facing, (Entity)null)) {
			int i = this.getMetadata(stack.getMetadata());
			IBlockState newState = this.block.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, i, player, hand);

			if(this.placeBlockAt(stack, player, world, pos, facing, hitX, hitY, hitZ, newState)) {
				newState = world.getBlockState(pos);
				SoundType soundtype = newState.getBlock().getSoundType(newState, world, pos, player);
				world.playSound(player, pos,
						soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
//				stack.shrink(1);
				stack.setItemDamage(1);
				AlmagestCore.log("set NBT.");
				HelperNBTStack nbtStack = new HelperNBTStack(stack);
				nbtStack.setInteger(NBT_xPos, pos.getX());
				nbtStack.setInteger(NBT_yPos, pos.getY());
				nbtStack.setInteger(NBT_zPos, pos.getZ());
				nbtStack.setInteger(NBT_dimensionId, world.provider.getDimension());
				AlmagestCore.savedDataManager.getAriadneData().addBlock(world, pos);
				
				return EnumActionResult.SUCCESS;
			}
		}
		
		return EnumActionResult.PASS;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		if(nbtStack.hasKey(NBT_dimensionId)) {
			tooltip.add(
					new TextComponentTranslation("almagest.tile.ariadne.tooltip",
							nbtStack.getInteger(NBT_dimensionId),
							nbtStack.getInteger(NBT_xPos), nbtStack.getInteger(NBT_yPos), nbtStack.getInteger(NBT_zPos))
					.setStyle(new Style().setColor(TextFormatting.GRAY)).getFormattedText());
			
		}
		
	}
	
	@Override
	public int getMetadata(int metadata) {
		
		return MathHelper.clamp(metadata, 0, 2);
		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		
		return this.getUnlocalizedName() + "." + stack.getMetadata();
		
	}
	
	private static void teleportEntityToCoordinates(Entity entity, double posX, double posY, double posZ) {
		
		if(entity instanceof EntityPlayerMP) {
			Set<SPacketPlayerPosLook.EnumFlags> set = EnumSet.<SPacketPlayerPosLook.EnumFlags>noneOf(SPacketPlayerPosLook.EnumFlags.class);			entity.dismountRidingEntity();
			((EntityPlayerMP)entity).connection.setPlayerLocation(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch, set);
			entity.setRotationYawHead(entity.rotationYaw);
		}
		else {
			entity.setLocationAndAngles(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch);
			entity.setRotationYawHead(entity.rotationYaw);
		}

		if(!(entity instanceof EntityLivingBase) || !((EntityLivingBase)entity).isElytraFlying()) {
			entity.motionY = 0.0D;
			entity.onGround = true;
		}
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		
		if(tab == AlmagestData.tab) {
			list.add(StarPropertyCreator.setMemory(new ItemStack(this), 0));
			list.add(StarPropertyCreator.setMemory(new ItemStack(this), 121));
		}
		
	}
	
}
