package noki.almagest.asm.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;


public class ArmorVisibilityEvent extends Event {
	
	public int visibilityLevel;
	public EntityPlayer player;
	public ItemStack armor;
	
	public ArmorVisibilityEvent(EntityPlayer player, ItemStack stack) {
		
		this.player = player;
		this.armor = stack;
		this.visibilityLevel = (stack.isEmpty()) ? 0 : 1;
		
	}
	
	public void setVibilityLevel(int level) {
		
		this.visibilityLevel = level;
		
	}
	
	public static int postEvent(EntityPlayer player, ItemStack stack) {
		
		ArmorVisibilityEvent event = new ArmorVisibilityEvent(player, stack);
		MinecraftForge.EVENT_BUS.post(event);
		return event.visibilityLevel;
		
	}

}
