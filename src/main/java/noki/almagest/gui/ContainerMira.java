package noki.almagest.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noki.almagest.entity.EntityMira;
import noki.almagest.registry.ModItems;


/**********
 * @class ContainerMira
 *
 * @description
 */
public class ContainerMira  extends Container {
	
	//******************************//
	// define member variables.
	//******************************//
	public EntityPlayer player;
	public World world;
	private BlockPos pos;
	
	public IInventory inventory;
	public IMiraState state;
//	public HashMap<MiraState, Integer> state = new HashMap<MiraState, Integer>();
	public ItemStack bookStack;
	
	private EntityMira talkingMira;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ContainerMira(EntityPlayer player, World world, BlockPos pos) {

		this.player = player;
		this.world = world;
		this.pos = pos;
		
		this.inventory = new InventoryBasic("inventory_mira", true, 1) {
			public void markDirty() {
				super.markDirty();
				ContainerMira.this.onCraftMatrixChanged(this);
			}
		};
		
		this.state = MiraState.SELECT.initialize(this);
//		this.state.put(MiraState.SELECT, 1);
		this.bookStack = new ItemStack(ModItems.ALMAGEST);
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		
		return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64D;
		
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		
		super.onCraftMatrixChanged(inventory);
		this.state.onCraftMatrixChanged(inventory);
		
	}
	
	public void setState(MiraState state) {
		
		this.state = state.initialize(this);
	}
	
	public void goToNext() {
		this.state.goToNextStep();
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		
		super.onContainerClosed(player);
		if(this.talkingMira != null) {
			this.talkingMira.setTalking(false);
			this.talkingMira.setTalker(null);
		}
		this.state.onContainerClosed(player);
		
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(index == 2) {
				if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
					return null;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index != 0 && index != 1) {
				if(index >= 3 && index < 39 && !this.mergeItemStack(itemstack1, 0, 2, false)) {
					return null;
				}
			}
			else if(!this.mergeItemStack(itemstack1, 3, 39, false)) {
				return null;
			}
			
			if(itemstack1.getCount() == 0) {
				slot.putStack((ItemStack)null);
			}
			else {
				slot.onSlotChanged();
			}
			
			if(itemstack1.getCount() == itemstack.getCount()) {
				return null;
			}
			
			slot.onTake(playerIn, itemstack1);
		}
		
		return itemstack;
		
	}
	
	public void setMira(EntityMira mira) {
		
		this.talkingMira = mira;
		
	}
	
}
