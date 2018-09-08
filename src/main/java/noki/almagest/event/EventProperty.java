package noki.almagest.event;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.ability.StarPropertyCreator.ItemStarLine;
import noki.almagest.asm.event.ItemSmeltingEvent;
import noki.almagest.attribute.AttributeHelper;
import noki.almagest.saveddata.AlmagestDataBlock;
import noki.almagest.saveddata.AlmagestDataChunk;


/**********
 * @class EventProperty
 *
 * @description propertyまわりのイベントです。
 * @description_en
 */
public class EventProperty {
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	//ドロップ時にpropertyをつける。
	@SubscribeEvent
	public void onHarvestDrop(HarvestDropsEvent event) {
		
		AlmagestDataBlock blockData = AlmagestCore.savedDataManager.getBlockData();
		AlmagestDataChunk chunkData = AlmagestCore.savedDataManager.getChunkData();
		
		//a bit loose check.
		for(ItemStack eachStack: event.getDrops()) {
			if(blockData.isBlockPlacedAt(event.getWorld(), event.getPos())) {
				blockData.addProperty(event.getWorld(), event.getPos(), eachStack);
			}
			else {
				if(!AttributeHelper.addProperty(eachStack)) {
					chunkData.addProperty(event.getWorld(), event.getPos().getX()>>4, event.getPos().getZ()>>4, eachStack);
				}
			}
		}
		
	}
	
	//精錬されたアイテムにpropertyを引き継ぐ。
	//ItemSmeltingEventはasmにより追加したイベント。
	@SubscribeEvent
	public void onItemSmelted(ItemSmeltingEvent event) {
		
		if(event.from.isEmpty() || event.to.isEmpty()) {
			return;
		}
		
		int memory = StarPropertyCreator.getMemory(event.from);
		ArrayList<ItemStarLine> lines = StarPropertyCreator.getLines(event.from);
		Map<Integer, ArrayList<Integer>> abilities = StarAbilityCreator.getAbility2(event.from);
		
		ItemStack newStack = event.to.copy();
		
		StarPropertyCreator.setMemory(newStack, memory);
		StarPropertyCreator.addLines(newStack, lines.toArray(new ItemStarLine[lines.size()]));
		for(Integer abilityId: abilities.keySet()) {
			for(Integer abilityLevel: abilities.get(abilityId)) {
				StarAbilityCreator.addAbility2(newStack, abilityId, abilityLevel);
			}
		}
		event.to = newStack;
		
	}
	
	//釣ったアイテムにpropertyをつける。
	@SubscribeEvent
	public void onItemFished(ItemFishedEvent event) {
		
		AlmagestDataChunk chunkData = AlmagestCore.savedDataManager.getChunkData();
		for(ItemStack eachStack: event.getDrops()) {
			if(!AttributeHelper.addProperty(eachStack)) {
				chunkData.addProperty(
						event.getHookEntity().world, event.getHookEntity().chunkCoordX, event.getHookEntity().chunkCoordZ, eachStack);
			}
		}
		
	}
	
	//プレイヤーが設置したブロックの位置を記憶する。
	@SubscribeEvent
	public void onBlockPlaced(PlaceEvent event) {
		
		if(!event.getPlayer().getHeldItem(event.getHand()).hasTagCompound()) {
			return;
		}
		AlmagestCore.savedDataManager.getBlockData().blockPlaced(event.getWorld(), event.getPos(), event.getPlayer().getHeldItem(event.getHand()));
//		AlmagestCore.log2("%s/%s/%s.", event.getPos().getX(), event.getPos().getZ(), event.getPos().getZ());
		
	}

}
