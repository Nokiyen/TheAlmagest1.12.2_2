package noki.almagest.event.post;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import noki.almagest.attribute.EStarAttribute;


public class AttributeLevelEvent extends Event {
	
	private ItemStack stack;
	private EStarAttribute attribute;
	private int level;
	
	public AttributeLevelEvent(ItemStack stack, EStarAttribute attribute, int level) {
		
		this.stack = stack;
		this.attribute = attribute;
		this.level = level;
		
	}
	
	public ItemStack getStack() {
		
		return this.stack;
		
	}
	
	public EStarAttribute getAttribute() {
		
		return this.attribute;
		
	}
	
	public int getLevel() {
		
		return this.level;
		
	}
	
	public void setLevel(int newLevel) {
		
		this.level = Math.max(newLevel, 0);
		
	}

	public static int postEvent(ItemStack stack, EStarAttribute attribute, int level) {
		
		AttributeLevelEvent event = new AttributeLevelEvent(stack, attribute, level);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getLevel();
		
	}

}
