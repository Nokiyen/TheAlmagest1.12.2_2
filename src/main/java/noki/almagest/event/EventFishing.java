package noki.almagest.event;

/*import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.registry.ModItems;
import noki.almagest.saveddata.AlmagestDataChunk;*/


/**********
 * @class EventLootTable
 *
 * @description 釣りに関するイベントです。
 * 
 */
public class EventFishing {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	//とびうおを、fishingのloot tableに追加。
	//星座をすべてワールドから取得する仕様にいったん変えたので、コメントアウト。
/*	@SubscribeEvent
	public void onLootTableLoad(LootTableLoadEvent event) {
		
		if(event.getName().compareTo(LootTableList.GAMEPLAY_FISHING_FISH) == 0) {
			AlmagestCore.log("enter fishing_fish.");
			
			//1個目のプールの名前はmain. それ以後はpool0, pool1...
			LootPool pool = event.getTable().getPool("main");
			if(pool != null) {
				pool.addEntry(new LootEntryItem(ModItems.FLYING_FISH, 25, 1, new LootFunction[0], new LootCondition[0], "almagest_flying_fish"));
			}
		}
		
	}*/
	
	//釣ったアイテムにstar propertyを付与。
	//noki.almagest.event.EventPropertyに移動。
/*	@SubscribeEvent
	public void onItemFished(ItemFishedEvent event) {
		
		AlmagestDataChunk chunkData = AlmagestCore.savedDataManager.getChunkData();
		for(ItemStack each: event.getDrops()) {
			chunkData.addProperty(event.getHookEntity().world, event.getHookEntity().chunkCoordX, event.getHookEntity().chunkCoordZ, each);
		}
		
	}*/

}
