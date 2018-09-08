package noki.almagest.event.post;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;


public class MemoryEvent extends Event {
	
	private ItemStack stack;
	private int memory;
	
	public MemoryEvent(ItemStack stack, int memory) {
		
		this.stack = stack;
		this.memory = memory;
		
	}
	
	public ItemStack getStack() {
		
		return this.stack;
		
	}
	
	public int getMemory() {
		
		return this.memory;
		
	}
	
	
	public void setMemory(int newMemory) {
		
		this.memory = Math.max(newMemory, 0);
		
	}

	public static int postEvent(ItemStack stack, int memory) {
		
		MemoryEvent event = new MemoryEvent(stack, memory);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getMemory();
		
	}

}
