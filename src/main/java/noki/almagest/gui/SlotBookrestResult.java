package noki.almagest.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBookrestResult extends Slot {

	public SlotBookrestResult(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		
		super(inventoryIn, index, xPosition, yPosition);
		
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
	
	@Override
	public ItemStack onTake(EntityPlayer playerIn, ItemStack stack) {
		
		return stack;
		
	}

}
