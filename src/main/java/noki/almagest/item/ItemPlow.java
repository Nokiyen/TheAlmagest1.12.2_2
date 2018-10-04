package noki.almagest.item;

import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.AlmagestData;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.ItemWithAttribute;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithRecipe;


/**********
 * @class ItemPlow
 *
 * @description 
 */
public class ItemPlow extends ItemWithAttribute implements IWithRecipe {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemPlow() {
		
		this.setAttributeLevel(EStarAttribute.TOOL, 40);
		this.setAttributeLevel(EStarAttribute.METAL, 20);
		this.setMaxStackSize(1);
		this.setMaxDamage(500);
		
	}
	
	public EnumActionResult onItemUse(EntityPlayer player,
			World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		ItemStack stack = player.getHeldItem(hand);

		if(!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
			return EnumActionResult.FAIL;
		}
		
		if(facing == EnumFacing.DOWN || !world.isAirBlock(pos.up())) {
			return EnumActionResult.PASS;
		}
		
		int plowRangeSided = 0;
		switch(StarPropertyCreator.getMagnitude(stack)) {
			case 1:
				plowRangeSided = 3;
				break;
			case 2:
			case 3:
			case 4:
				plowRangeSided = 2;
				break;
			default:
				plowRangeSided = 1;
				break;
		}
		
		EnumFacing playerFacing = player.getHorizontalFacing();
		
		//in case of grass.
		if(this.canChangeIntoFarmland(world, pos)) {
			this.setBlock(player, world, pos, Blocks.FARMLAND.getDefaultState());
			for(int i=1; i<=plowRangeSided; i++) {
				if(this.canChangeIntoFarmland(world, pos.offset(playerFacing.rotateY(), i))) {
					this.setBlock(player, world, pos.offset(playerFacing.rotateY(), i), Blocks.FARMLAND.getDefaultState());
				}
				if(this.canChangeIntoFarmland(world, pos.offset(playerFacing.rotateYCCW(), i))) {
					this.setBlock(player, world, pos.offset(playerFacing.rotateYCCW(), i), Blocks.FARMLAND.getDefaultState());
				}
			}
			stack.damageItem(1, player);
			return EnumActionResult.SUCCESS;
		}
		
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();
		//in case of coarse dirt. dirty code.
		if(block == Blocks.DIRT && (BlockDirt.DirtType)iblockstate.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.COARSE_DIRT) {
			this.setBlock(player, world, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
			for(int i=1; i<=plowRangeSided; i++) {
				BlockPos targetPos = pos.offset(playerFacing.rotateY(), i);
				IBlockState targetState = world.getBlockState(targetPos);
				Block targetBlock = targetState.getBlock();
				if(targetBlock == Blocks.DIRT && (BlockDirt.DirtType)targetState.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT) {
					this.setBlock(player, world, targetPos,
							Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
				}
				targetPos = pos.offset(playerFacing.rotateYCCW(), i);
				targetState = world.getBlockState(targetPos);
				targetBlock = targetState.getBlock();
				if(targetBlock == Blocks.DIRT && (BlockDirt.DirtType)targetState.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT) {
					this.setBlock(player, world, targetPos,
							Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
				}
			}
			stack.damageItem(1, player);
			return EnumActionResult.SUCCESS;
		}
		
		return EnumActionResult.PASS;

	}
	
	private boolean canChangeIntoFarmland(World world, BlockPos pos) {
		
		if(!world.isAirBlock(pos.up())) {
			return false;
		}
		
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		if(block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
			return true;
		}
		
		if(block == Blocks.DIRT && (BlockDirt.DirtType)state.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT) {
			return true;
		}
		
		return false;
		
	}
	
	private void setBlock(EntityPlayer player, World world, BlockPos pos, IBlockState state) {
		
		world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
		if(!world.isRemote) {
			world.setBlockState(pos, state, 11);
		}
		
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		
		if(!(entityLiving instanceof EntityPlayer)) {
			stack.damageItem(1, entityLiving);
			return false;
		}
		
		int plowRangeSided = 0;
		boolean canDestroyBush = false;
		switch(StarPropertyCreator.getMagnitude(stack)) {
			case 1:
				plowRangeSided = 3;
				canDestroyBush = true;
				break;
			case 2:
			case 3:
				plowRangeSided = 2;
				canDestroyBush = true;
				break;
			case 4:
				plowRangeSided = 2;
				break;
			default:
				plowRangeSided = 1;
				break;
		}
		
		EntityPlayer player = (EntityPlayer)entityLiving;
		EnumFacing playerFacing = player.getHorizontalFacing();
		
		//in case of grass.
		if(this.isCrop(state.getBlock()) || (canDestroyBush && this.isBush(state.getBlock()))) {
			for(int i=1; i<=plowRangeSided; i++) {
				BlockPos targetPos = pos.offset(playerFacing.rotateY(), i);
				Block targetBlock = world.getBlockState(targetPos).getBlock();
				if(this.isCrop(targetBlock) || (canDestroyBush && this.isBush(targetBlock))) {
					this.destroyBlock(world, targetPos, pos);
				}
				targetPos = pos.offset(playerFacing.rotateYCCW(), i);
				targetBlock = world.getBlockState(targetPos).getBlock();
				if(this.isCrop(targetBlock) || (canDestroyBush && this.isBush(targetBlock))) {
					this.destroyBlock(world, targetPos, pos);
				}
			}
		}
		
		stack.damageItem(1, entityLiving);
		return false;
		
	}
	
	private boolean isCrop(Block block) {
		
		return block instanceof BlockCrops || block instanceof BlockNetherWart;
		
	}
	
	private boolean isBush(Block block) {
		
		return block instanceof BlockBush;
		
	}
	
	public boolean destroyBlock(World world, BlockPos pos, BlockPos dropPos) {
		
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if(block.isAir(iblockstate, world, pos)) {
			return false;
		}
		else {
			world.playEvent(2001, pos, Block.getStateId(iblockstate));
			block.dropBlockAsItem(world, dropPos, iblockstate, 0);
			return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
		
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		
		stack.damageItem(1, attacker);
		return true;
		
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isFull3D() {
		
		return true;
		
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
		
		if(slot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
					new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 8.0D, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
					new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.0D, 0));
		}
		
		return multimap;
		
	}
	
	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				new StarRecipe(new ItemStack(this))
					.setStack(new ItemStack(Blocks.IRON_BLOCK)).setStack(new ItemStack(Items.STICK)).setAttribute(EStarAttribute.ANIMAL, 20)
					.setHint(new ItemStack(Items.LEATHER))
		);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		
		if(tab == AlmagestData.tab) {
			list.add(StarPropertyCreator.setMemory(new ItemStack(this), 0));
			list.add(StarPropertyCreator.setMemory(new ItemStack(this), 61));
			list.add(StarPropertyCreator.setMemory(new ItemStack(this), 91));
			list.add(StarPropertyCreator.setMemory(new ItemStack(this), 151));
		}
		
	}

}
