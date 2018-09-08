package noki.almagest.gui.sequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noki.almagest.AlmagestCore;


public class ContainerSequence extends Container {
	
	private ISequence currentSequence;
	private int currentId;
	private Map<Integer, ISequence> sequences = new HashMap<Integer, ISequence>();
	private Map<SequenceTag, Integer> connections = new HashMap<SequenceTag, Integer>();
	
	protected EntityPlayer player;
	protected BlockPos pos;
	protected World world;
	
	public IInventory inventory;
	
	private boolean markFlag;
	private ArrayList<Integer> markedIds;
	
	
	public ContainerSequence(EntityPlayer player, BlockPos pos, World world) {
		
		this.player = player;
		this.pos = pos;
		this.world = world;
		
		this.inventory = new InventoryBasic("inventory_sequence", true, 1) {
			@Override
			public void markDirty() {
				super.markDirty();
				ContainerSequence.this.onCraftMatrixChanged(this);
			}
		};
		
		this.markFlag = false;
		if(this.markedIds == null) {
			this.markedIds = new ArrayList<Integer>();
		}
		
		this.defineSequences();
		this.currentSequence = this.sequences.get(1);
		this.currentId = 1;
		
	}
	
	//must be overrided.
	public void defineSequences() {
		
	}
	
	public void setSequence(int id, ISequence sequence) {
		
		this.sequences.put(id, sequence);
		
	}
	
	public void connect(int from, int to) {
		
		this.connect(from, 0, to);
		
	}
	
	public void connect(int from, int choiceNumber, int to) {
		
		this.connections.put(new SequenceTag(from, choiceNumber), to);
		
	}
	
	public ESequenceType currentType() {
		
		return this.currentSequence.getType();
		
	}
	
	public ISequence currentSequence() {
		
		return this.currentSequence;
		
	}
	
	public int currentId() {
		
		return this.currentId;
		
	}
	
/*	public boolean goToNextSequence() {
		
		int choiceNumber = 0;
		switch (this.currentType()) {
			case Talk:
			case Choice://never use this method for Choice. Choice always needs the selected number from GUI.
				choiceNumber = 0;
				break;
			case Inventory:
				choiceNumber = ((SequenceInventory)this.currentSequence()).checkInventory(this.getSlot(0));
				break;
		}
		
		return this.goToNextSequence(choiceNumber);
		
	}*/
	
	public boolean goToNextSequence(int choiceNumber) {
		
		AlmagestCore.log("goToNextSequence:Container");
		AlmagestCore.log("id: {}", this.currentId);
		
		switch(this.currentType()) {
			case Talk:
				choiceNumber = 0;
				break;
			case Choice:
				break;
			case Inventory:
				choiceNumber = ((SequenceInventory)this.currentSequence()).checkInventory(this.getSlot(0));
				if(choiceNumber != 1) {
					this.clearContainer(this.player, this.world, this.inventory);
				}
				break;
		}
		
		this.currentSequence().onNext(this);
		
		if(this.currentSequence.getType() == ESequenceType.Talk) {
			if(((SequenceTalk)this.currentSequence).isLastMessage() == false) {
				((SequenceTalk)this.currentSequence).goToNextMessage();
				return false;
			}
		}
		
		if(this.currentSequence.isEnd()) {
			this.currentSequence().onEnd(this);
			return true;
		}
		
		if(this.currentSequence.getType() == ESequenceType.Inventory) {
			this.removeSlots();
		}
		
		int nextId = this.connections.get(new SequenceTag(this.currentId, choiceNumber));
		this.currentId = nextId;
		this.currentSequence = this.sequences.get(nextId);
		
		if(this.currentSequence().getType() == ESequenceType.Inventory) {
			this.addSlots(((SequenceInventory)this.currentSequence).createSlot(this.inventory));
			((SequenceInventory)this.currentSequence).onSlotCreated(this);
		}
		
		return false;
		
	}
	
	public void removeSlots() {
		
		this.inventorySlots = Lists.<Slot>newArrayList();
		
	}
	
	public void addSlots(Slot slot) {
		
		//slot for target inventory.
		this.addSlotToContainer(slot);
		
		// player's inventory.
		for(int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(this.player.inventory, i, 49 + i * 18, 109+5));
		}
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(this.player.inventory, j + i * 9 + 9, 49 + j * 18, 51 + i * 18 + 5));
			}
		}
		
	}
	
	public void clearContainer() {
		
		this.clearContainer(this.player, this.world, this.inventory);
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		
		return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64D;
		
	}
	
	protected void markFlag(int flagId) {
		
		if(!this.world.isRemote) {
			this.markFlag = true;
			this.markedIds.add(flagId);
		}
		
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		
		super.onCraftMatrixChanged(inventory);
		if(this.currentType() == ESequenceType.Inventory) {
			((SequenceInventory)this.currentSequence()).onCraftmatrixChange(this);
		}
		
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		
		super.onContainerClosed(player);
		
		if(this.markFlag && !this.world.isRemote) {
			for(Integer each: this.markedIds) {
				AlmagestCore.savedDataManager.getStoryData().markStoryFlagOnServer(each);
			}
		}
		
		if(this.currentType() == ESequenceType.Inventory && ((SequenceInventory)this.currentSequence()).isReturnStack()) {
			this.clearContainer();
		}
		
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
	
		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(index == 0) {
				itemstack1.getItem().onCreated(itemstack1, this.world, playerIn);
	
				if(!this.mergeItemStack(itemstack1, 1, 36, true)) {
					return ItemStack.EMPTY;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if(index >= 1 && index <= 9) {
				if(!this.mergeItemStack(itemstack1, 10, 36, false)) {
					return ItemStack.EMPTY;
				}
			}
			else if(index >= 10 && index <= 36) {
				if(!this.mergeItemStack(itemstack1, 1, 9, false)) {
					return ItemStack.EMPTY;
				}
			}
			
			if(itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}
			else {
				slot.onSlotChanged();
			}
	
			if(itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
	
			ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
	
			if(index == 0) {
				playerIn.dropItem(itemstack2, false);
			}
		}
		
		return itemstack;
	
	}
	
	private class SequenceTag {
		public int id;
		public int choiceId;
		
		public SequenceTag(int id, int choiceId) {
			this.id = id;
			this.choiceId = choiceId;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + choiceId;
			result = prime * result + id;
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj == null) return false;
			if(this.getClass() != obj.getClass()) return false;
			
			SequenceTag other = (SequenceTag)obj;
			if(choiceId != other.choiceId) return false;
			if(id != other.id) return false;
			
			return true;
		}
	}
	
}
