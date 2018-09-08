package noki.almagest.ability;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import noki.almagest.attribute.AttributeHelper;
import noki.almagest.attribute.IWithAttribute;
import noki.almagest.event.post.MemoryEvent;
import noki.almagest.helper.HelperNBTStack;


/**********
 * @class StarPropertyCreator
 *
 * @description star propertyである、memory, lineについて処理するクラスです。
 * @description_en
 */
public class StarPropertyCreator {
	
	//******************************//
	// define member variables.
	//******************************//
	public static final String memoryName = "memory";

	
	//******************************//
	// define member methods.
	//******************************//
	public static ItemStack addLines(ItemStack stack, ItemStarLine... line) {
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		
		for(ItemStarLine each : line) {
			nbtStack.setBoolean(each.getName(), true);
		}
		
		return nbtStack.getStack();
		
	}

	public static ItemStack addLine(ItemStack stack, ItemStarLine line) {
		
		return new HelperNBTStack(stack).setBoolean(line.getName(), true).getStack();
		
	}
	
	public static ArrayList<ItemStarLine> getLines(ItemStack stack) {

		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		ArrayList<ItemStarLine> list = new ArrayList<ItemStarLine>();
		
		boolean hasKey = false;
		for(ItemStarLine each : ItemStarLine.values()) {
			if(nbtStack.hasKey(each.getName())) {
				hasKey = true;
				if(nbtStack.getBoolean(each.getName())) {
					list.add(each);
				}
			}
		}
		
		if(!hasKey) {
			if(stack.getItem() instanceof ItemBlock) {
				Block block = Block.getBlockFromItem(stack.getItem());
				if(block instanceof IWithAttribute) {
					list = ((IWithAttribute)block).getLine(stack);
				}
				else {
					list = AttributeHelper.getVanillaLine(stack);
				}
			}
			else {
				if(stack.getItem() instanceof IWithAttribute) {
					list = ((IWithAttribute)stack.getItem()).getLine(stack);
				}
				else {
					list = AttributeHelper.getVanillaLine(stack);
				}
			}
		}
		
		return list;
		
	}
	
	public static boolean hasLine(ItemStack stack) {
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		for(ItemStarLine each : ItemStarLine.values()) {
			if(nbtStack.getBoolean(each.getName())) {
				return true;
			}
		}
		return false;
		
	}
	
	public static ItemStack setMemory(ItemStack stack, int memory) {
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		return nbtStack.setInteger(memoryName, memory).getStack();
		
	}
	
	public static int getMemory(ItemStack stack) {
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		int memory = 0;
		if(nbtStack.hasKey(memoryName)) {
			memory = nbtStack.getInteger(memoryName);
		}
		else {
			if(stack.getItem() instanceof ItemBlock) {
				Block block = Block.getBlockFromItem(stack.getItem());
				if(block instanceof IWithAttribute) {
					memory = ((IWithAttribute)block).getMemory(stack);
				}
				else {
					memory = AttributeHelper.getVanillaMemory(stack);
				}
			}
			else {
				if(stack.getItem() instanceof IWithAttribute) {
					memory = ((IWithAttribute)stack.getItem()).getMemory(stack);
				}
				else {
					memory = AttributeHelper.getVanillaMemory(stack);
				}
			}
		}
		
		return MemoryEvent.postEvent(stack, memory);
		
	}
	
	public static int getMagnitude(ItemStack stack) {
		
		return getMagnitude(getMemory(stack));
		
	}
	
	public static int getMagnitude(int memory) {
		
		double division = Math.floor(memory / 30.0D);
		int magnitude = 6 - (int)Math.min(5, division);
		
		return magnitude;
		
	}
	
	public static boolean isMagnitude(ItemStack stack, int magnitude) {
		
		return isMagnitude(getMemory(stack), magnitude);
		
	}
	
	public static boolean isMagnitude(int memory, int magnitude) {
		
		if(getMagnitude(memory) <= magnitude) {
			return true;
		}
		return false;
		
	}
	
	public static ItemStack setProperty(ItemStack stack, int memory, ItemStarLine... lines) {
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		
		for(ItemStarLine each : lines) {
			nbtStack.setBoolean(each.getName(), true);
		}
		nbtStack.setInteger(memoryName, memory);
		
		return nbtStack.getStack();
//		return addLines(setMemory(stack, memory), lines);
		
	}
	
	public enum ItemStarLine {
		
		TOP("top"),
		BOTTOM("bottom"),
		LEFT("left"),
		RIGHT("right");
		
		private String name;
		
		private ItemStarLine(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
		
	}

}
