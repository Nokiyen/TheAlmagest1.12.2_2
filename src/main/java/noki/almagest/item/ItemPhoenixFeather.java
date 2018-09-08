package noki.almagest.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import noki.almagest.AlmagestCore;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.ItemWithAttribute;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithEvent;
import noki.almagest.registry.IWithRecipe;
import noki.almagest.registry.ModItems;


/**********
 * @class ItemPhoenixFeather
 *
 * @description
 */
public class ItemPhoenixFeather extends ItemWithAttribute implements IWithRecipe, IWithEvent {
	
	
	//******************************//
	// define member variables.
	//******************************//
	//used for event.
	public static Map<UUID, Integer> resurrected = new HashMap<>();
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemPhoenixFeather() {
		
		this.setAttributeLevel(EStarAttribute.STAR, 50);
		this.setAttributeLevel(EStarAttribute.ANIMAL, 50);
		this.setMaxStackSize(1);
		
	}

	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				new StarRecipe(new ItemStack(this))
					.setStack(new ItemStack(ModItems.RAINBOW_FEATHER))
					.setStack(new ItemStack(ModItems.WHITE_FEATHER))
					.setStack(new ItemStack(ModItems.BLACK_FEATHER))
					.setStack(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.REGENERATION))
				);
		
	}
	
	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent event) {
		
		if(!(event.getEntityLiving() instanceof EntityPlayer)) {
			return;
		}
		
		EntityPlayer player = (EntityPlayer)event.getEntityLiving();
		IInventory inventory = player.inventory;
		boolean found = false;
		int foundSlot = 0;
		for(int i=0; i<inventory.getSizeInventory(); i++) {
			if(inventory.getStackInSlot(i).getItem() == this) {
				found = true;
				foundSlot = i;
				break;
			}
		}
		
		if(found) {
			event.setCanceled(true);
			player.setHealth(player.getMaxHealth());
			player.setEntityInvulnerable(true);
			
			resurrected.put(player.getUniqueID(), 20*10);
			if(player.world.isRemote) {
				player.world.playSound(player, player.getPosition(), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
			
			inventory.getStackInSlot(foundSlot).shrink(1);
		}
		
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		
		if(event.phase != Phase.START) {
			return;
		}
		
		if(event.side == Side.CLIENT) {
	//		AlmagestCore.log("enter player tick event. client");
		}
		else {
//			AlmagestCore.log("enter player tick event. server");
		}
		
		EntityPlayer player = event.player;
		
		Integer remainingTime = resurrected.get(player.getUniqueID());
		if(remainingTime == null) {
//			AlmagestCore.log("not resurrecting player. : {}", event.side.toString());
			return;
		}
		
		int next = remainingTime-1;
//		AlmagestCore.log("now {}.", next);
		resurrected.put(player.getUniqueID(), next);
		
		if(event.player.world.isRemote && next%5 == 0) {
			Random rand = player.world.rand;
			for(int k = 0; k < 10; ++k) {
				double d2 = rand.nextGaussian() * 0.02D;
				double d0 = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				player.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL,
						player.posX + (double)(rand.nextFloat() * player.width * 2.0F) - (double)player.width,
						player.posY + (double)(rand.nextFloat() * player.height),
						player.posZ + (double)(rand.nextFloat() * player.width * 2.0F) - (double)player.width, d2, d0, d1);
			}
		}
		
		if(next < 0) {
//			AlmagestCore.log("set entity vulnerable. {}", event.side.toString());
			player.setEntityInvulnerable(false);
			resurrected.remove(player.getUniqueID());
		}
		
	}

}
