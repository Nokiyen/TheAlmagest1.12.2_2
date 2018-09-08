package noki.almagest.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotBookrestCrafting extends Slot {
	
	public SlotBookrestCrafting(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	public int lineCount;
	public int prevLineCount;
	
	public void incrLineCount() {
		
		this.prevLineCount = this.lineCount;
		this.lineCount++;
		
	}
	
	public void decrLineCount() {
		
		if(this.lineCount > 0) {
			this.prevLineCount = this.lineCount;
			this.lineCount--;
		}
		
	}
	
	public boolean isNewlyPlaced() {
		
		if(this.prevLineCount == 0 && this.lineCount == 1) {
			return true;
		}
		return false;
		
	}

}
