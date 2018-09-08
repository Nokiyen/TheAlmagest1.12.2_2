package noki.almagest.item;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.AlmagestData;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.ItemWithAttribute;
import noki.almagest.event.post.AttributeLevelEvent;
import noki.almagest.helper.HelperNBTStack;
import noki.almagest.helper.HelperTeleport;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithEvent;
import noki.almagest.registry.IWithRecipe;


/**********
 * @class ItemPlanisphere
 *
 * @description 右クリックで星座版世界に行ける重要なアイテムです。また、手に持っていると星座線が見えます。
 */
public class ItemPlanisphere extends ItemWithAttribute implements IWithRecipe, IWithEvent {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String NBT_dimensionID = "dimensionID";
	private static final String NBT_posX = "posX";
	private static final String NBT_posY = "posY";
	private static final String NBT_posZ = "posZ";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemPlanisphere() {
		
		this.setAttributeLevel(EStarAttribute.STAR, 20);
		this.setAttributeLevel(EStarAttribute.TOOL, 20);
		
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		
		if(world.isRemote) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
		}
		
		if(AlmagestCore.savedDataManager.getStoryData().getCurrentStory() < 3) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
		}
		
		EntityPlayerMP entityPlayerMP = (EntityPlayerMP)player;
		HelperNBTStack helper = new HelperNBTStack(player.getHeldItem(hand));
		// teleport from Atlas
		if(world.provider.getDimension() == AlmagestData.dimensionID) {
			// in case that the last position is memorized.
			if(helper.hasChild()) {
				int dimensionID = helper.getInteger(NBT_dimensionID);
				int posX = helper.getInteger(NBT_posX);
				int posY = helper.getInteger(NBT_posY);
				int posZ = helper.getInteger(NBT_posZ);
				HelperTeleport.teleportPlayer(dimensionID, (double)posX+0.5D, (double)posY, (double)posZ+0.5D, entityPlayerMP);
			}
			// otherwise.
			else {
				HelperTeleport.teleportPlayer(0, entityPlayerMP);
			}			
		}
		// teleport from others.
		else {
			helper.setInteger(NBT_dimensionID, world.provider.getDimension());
			helper.setInteger(NBT_posX, (int)entityPlayerMP.posX);
			helper.setInteger(NBT_posY, (int)entityPlayerMP.posY);
			helper.setInteger(NBT_posZ, (int)entityPlayerMP.posZ);
			
			HelperTeleport.teleportPlayer(AlmagestData.dimensionID, entityPlayerMP);
//			player.travelToDimension(AlmagestData.dimensionID);
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		
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
	
	@SubscribeEvent
	public void onAttributeLevel(AttributeLevelEvent event) {
		
		if(event.getStack().getItem() == this && event.getAttribute() == EStarAttribute.STAR &&
				StarPropertyCreator.isMagnitude(event.getStack(), 5)) {
			event.setLevel(event.getLevel()+10);
		}
		
	}
	
	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				new StarRecipe(new ItemStack(this))
				.setAttribute(EStarAttribute.STAR, 10).setStack(new ItemStack(Items.PAPER)));

	}
	
}
