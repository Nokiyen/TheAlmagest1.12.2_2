package noki.almagest.event;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.attribute.IWithAttribute;
import noki.almagest.entity.IEntityAlmagest;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.item.ItemMemo;
import noki.almagest.registry.ModItems;
import noki.almagest.saveddata.DataCategory;
import noki.almagest.saveddata.gamedata.GameData;
import noki.almagest.saveddata.gamedata.GameDataAbility2;
import noki.almagest.saveddata.gamedata.GameDataMob;


/**********
 * @class EventObtained
 *
 * @description 各種アイテムを入手した時に、入手したフラグを立てるためのクラスです。
 * 入手したものは、『アルマゲスト』で閲覧可能になります。
 * 
 */
public class EventObtained {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	//worldから拾ったアイテムに対して取得処理をする。
	//abilityについても取得処理をする。
	@SubscribeEvent
	public void onPickupItem(EntityItemPickupEvent event) {
		
/*		if(event.getEntityPlayer() == null) {
			return;
		}
		
		if(event.getItem() == null) {
			return;
		}
		
		//ability.
		checkAbility(event.getItem().getItem());
		
		//other items.
		GameData gameData = getData(event.getItem().getItem());
		if(gameData == null || gameData.isObtained()) {
			return;
		}
		AlmagestCore.savedDataManager.getFlagData().setObtained(gameData);*/
		
	}
	
	//コンテナに入ったアイテムに対して取得処理を行うクラスを差し込む。
	@SubscribeEvent
	public void onOpenContainer(PlayerContainerEvent.Open event) {
		
		event.getContainer().addListener(new ObtainedContainerListener(event.getEntityPlayer()));
		
	}
	
	public class ObtainedContainerListener implements IContainerListener {
		public EntityPlayer player;
		
		public ObtainedContainerListener(EntityPlayer player) {
			this.player = player;
		}
		
		@Override
		public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
			if(stack.isEmpty()) {
				return;
			}
			
			//ability.
			checkAbility(stack);
			
			//other items.
			GameData gameData = EventObtained.getData(stack);
			if(gameData == null || gameData.isObtained()) {
				return;
			}
			
			for(int i=0; i<this.player.inventory.getSizeInventory(); i++) {
				ItemStack targetStack = this.player.inventory.getStackInSlot(i);
				if(targetStack != null && targetStack.getItem() == stack.getItem() && targetStack.getMetadata() == stack.getMetadata()) {
					AlmagestCore.savedDataManager.getFlagData().setObtained(gameData);
					break;
				}
			}
		}
		
		@Override
		public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {
		}
		
		@Override
		public void sendAllWindowProperties(Container containerIn, IInventory inventory) {
		}
		
		@Override
		public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
		}
	}
	
	public static GameData getData(ItemStack stack) {
		
		DataCategory targetCategory = null;
		if(stack.getItem() instanceof ItemBlock) {
			Block block = Block.getBlockFromItem(stack.getItem());
			if(block instanceof IWithAttribute) {
/*				if(block == ModBlocks.CONSTELLATION_BLOCK) {
					targetCategory = DataCategory.CONSTELLATION;
				}
				else {
					targetCategory = DataCategory.BLOCK;
				}*/
				targetCategory = DataCategory.BLOCK;
			}
			else {
				targetCategory = DataCategory.LIST;
			}
		}
		else {
			if(stack.getItem() instanceof IWithAttribute) {
				if(stack.getItem() == ModItems.MEMO) {
					targetCategory = DataCategory.MEMO;
				}
				else {
					targetCategory = DataCategory.ITEM;
				}
			}
			else {
				targetCategory = DataCategory.LIST;
			}
		}
		
		if(targetCategory == null) {
//			AlmagestCore.log("category is null.");
			return null;
		}

//		AlmagestCore.log("category is {}.", targetCategory.getDisplay());

		GameData gameData = null;
		switch(targetCategory) {
			case BLOCK:
				gameData = AlmagestCore.savedDataManager.getFlagData().getBlock(stack);
				if(gameData == null) {
					gameData = AlmagestCore.savedDataManager.getFlagData().getBlock(new ItemStack(Block.getBlockFromItem(stack.getItem()), 1, 0));
				}
				break;
			case ITEM:
				gameData = AlmagestCore.savedDataManager.getFlagData().getItem(stack);
				if(gameData == null) {
					gameData = AlmagestCore.savedDataManager.getFlagData().getItem(new ItemStack(stack.getItem(), 1, 0));
				}
				break;
			case LIST:
				gameData = AlmagestCore.savedDataManager.getFlagData().getList(stack);
				if(gameData == null) {
					gameData = AlmagestCore.savedDataManager.getFlagData().getList(new ItemStack(stack.getItem(), 1, 0));
				}
				break;
			case CONSTELLATION:
				gameData = AlmagestCore.savedDataManager.getFlagData().getConstellation(
						Constellation.getConstFromNumber(HelperConstellation.getConstNumber(stack)));
				break;
			case MEMO:
				gameData = AlmagestCore.savedDataManager.getFlagData().getMemo(ItemMemo.getMemoDataFromStack(stack));
				break;
			default:
				break;
		}
		
		return gameData;
		
	}
	
	public static void checkAbility(ItemStack stack) {
		
		Map<Integer, ArrayList<Integer>> abilities = StarAbilityCreator.getAbility2(stack);
		for(int eachAbility: abilities.keySet()) {
			for(int eachLevel: abilities.get(eachAbility)) {
				GameDataAbility2 abilityData = AlmagestCore.savedDataManager.getFlagData().getAbility(eachAbility, eachLevel);
				if(abilityData == null || abilityData.isObtained()) {
					continue;
				}
				AlmagestCore.savedDataManager.getFlagData().setObtained(abilityData);
			}
		}
		
	}
	
	@SubscribeEvent
	public void onEntityInteract(EntityInteractSpecific event) {
		
		AlmagestCore.log("on interact.");
		if(event.getTarget() == null || !(event.getTarget() instanceof EntityLivingBase)) {
			return;
		}
		checkEntity((EntityLivingBase)event.getTarget());
		
	}
	
	@SubscribeEvent
	public void onEntityDamege(LivingDamageEvent event) {
		
		if(event.getSource() == null || event.getSource().getTrueSource() == null
				|| !(event.getSource().getTrueSource() instanceof EntityPlayer)) {
			return;
		}
		checkEntity(event.getEntityLiving());
		
	}
	
	public static void checkEntity(EntityLivingBase entity) {
		
		if(entity == null || !(entity instanceof IEntityAlmagest)) {
			return;
		}
		
		GameDataMob mobData = AlmagestCore.savedDataManager.getFlagData().getMob(((IEntityAlmagest)entity).getResource());
		if(mobData == null || mobData.isObtained()) {
			return;
		}
		
		AlmagestCore.savedDataManager.getFlagData().setObtained(mobData);
		
	}
	
}
