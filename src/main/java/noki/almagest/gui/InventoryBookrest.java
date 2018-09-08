package noki.almagest.gui;

import javax.annotation.Nullable;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryBookrest extends InventoryCrafting {

	public InventoryBookrest(Container eventHandlerIn, int width, int height) {
		
		super(eventHandlerIn, width, height);
		
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count) {
		
		ItemStack stack = super.decrStackSize(index, count);
		if(stack != null) {
			this.updateLineCount();
		}
		return stack;
		
	}
	
	@Override
	public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		
		super.setInventorySlotContents(index, stack);
		this.updateLineCount();
		
	}
	
	public void updateLineCount() {
		
	}

}
