package noki.almagest.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.AlmagestData;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.ItemWithAttribute;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithEvent;
import noki.almagest.registry.IWithRecipe;


/**********
 * @class ItemBerenice
 *
 * @description
 */
public class ItemBerenice extends ItemWithAttribute implements IWithRecipe, IWithEvent {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemBerenice() {
		
		this.setAttributeLevel(EStarAttribute.TOOL, 40);
		this.setAttributeLevel(EStarAttribute.ANIMAL, 20);
		this.setMaxStackSize(1);
		
	}
	
	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				new StarRecipe(new ItemStack(this))
				.setAttribute(EStarAttribute.DECORATIVE, 30).setAttribute(EStarAttribute.JEWEL, 60).setAttribute(EStarAttribute.ANIMAL, 20)
				.setHint(new ItemStack(Items.EMERALD)).setHint(ItemBlockConstellation.getConstStack(24, 1))
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
		ItemStack foundStack = null;
		for(int i=0; i<inventory.getSizeInventory(); i++) {
			if(inventory.getStackInSlot(i).getItem() == this) {
				found = true;
				foundSlot = i;
				foundStack = inventory.getStackInSlot(i);
				break;
			}
		}
		
		if(!found) {
			return;
		}
		
		event.setCanceled(true);
		
		float healHealth = 3.0F;
		boolean applyBuf = false;
		switch(StarPropertyCreator.getMagnitude(foundStack)) {
			case 1:
				applyBuf = true;
			case 2:
				healHealth = player.getMaxHealth();
				break;
			case 3:
				healHealth = player.getMaxHealth()/2.0F;
				break;
		}
		player.setHealth(healHealth);
//		player.setEntityInvulnerable(true);
		
		if(player.world.isRemote) {
			player.world.playSound(player, player.getPosition(), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
		
		if(!player.world.isRemote) {
			player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 20*5, 4));
			
			if(applyBuf) {
				player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 20*30, 2));
				player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 20*30, 2));
				player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 20*30, 0));
			}
		}
		
		inventory.getStackInSlot(foundSlot).shrink(1);
		
	}
	
/*	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		
		if(event.phase != Phase.START) {
			return;
		}
		
		EntityPlayer player = event.player;
		
		Integer remainingTime = this.resurrected.get(player.getUniqueID());
		if(remainingTime == null) {
			return;
		}
		
		int next = remainingTime-1;
		this.resurrected.put(player.getUniqueID(), next);
		
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
			player.setEntityInvulnerable(false);
			this.resurrected.remove(player.getUniqueID());
		}
		
	}*/
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		
		if(tab == AlmagestData.tab) {
			list.add(StarPropertyCreator.setMemory(new ItemStack(this), 0));
			list.add(StarPropertyCreator.setMemory(new ItemStack(this), 91));
			list.add(StarPropertyCreator.setMemory(new ItemStack(this), 121));
			list.add(StarPropertyCreator.setMemory(new ItemStack(this), 151));
		}
		
	}

}
