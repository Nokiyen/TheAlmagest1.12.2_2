package noki.almagest.asm.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;


@Cancelable
public class ItemSmeltingEvent extends Event {
	
	public ItemStack from;
	public ItemStack to;
	
	public ItemSmeltingEvent(ItemStack from, ItemStack to) {
		
		this.from = from;
		this.to = to;
		
	}
	
	public static ItemStack postEvent(ItemStack from, ItemStack to) {
		
		ItemSmeltingEvent event = new ItemSmeltingEvent(from, to);
		MinecraftForge.EVENT_BUS.post(event);
		return event.to;
		
	}

}
