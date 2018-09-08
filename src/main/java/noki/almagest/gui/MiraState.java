package noki.almagest.gui;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import noki.almagest.registry.ModBlocks;
import noki.almagest.saveddata.gamedata.ETalkMira;


public enum MiraState implements IMiraState {
	
	/*
	 * state for select display. (constellation, book, talk)
	 */
	SELECT(1){
		
		@Override
		public MiraState initialize(ContainerMira container) {
			
			this.step = 1;
			this.container = container;
			this.removeSlots();
			return this;
			
		}
		
		@Override
		public String getMessage() {
			
			return new TextComponentTranslation("almagest.gui.mira.select.message").getFormattedText();
			
		}
		
	},
	
	
	/*
	 * state for giving constellations.
	 */
	CONSTELLATION(2){
		
		@Override
		public MiraState initialize(ContainerMira container) {
			
			this.step = 1;
			this.container = container;
			this.addSlots(new Slot(this.container.inventory, 0, 120, 17+5){
				@Override
				public boolean isItemValid(ItemStack stack) {
					if(stack.getItem() == Item.getItemFromBlock(ModBlocks.CONSTELLATION_BLOCK)){ 
						return true;
					}
					return false;
				}
			});
			return this;
			
		}
		
		@Override
		public void onContainerClosed(EntityPlayer player) {
			
			if(this.container.world.isRemote == false) {
				ItemStack itemstack = this.container.inventory.removeStackFromSlot(0);
				if(itemstack != null && !itemstack.isEmpty()) {
					this.container.player.dropItem(itemstack, false);
				}
			}
			
		}
		
		@Override
		public String getMessage() {
			
			return new TextComponentTranslation("almagest.gui.mira.constellation.message").getFormattedText();
			
		}
		
	},
	
	
	/*
	 * state for taking the book(Almagest).
	 */
	BOOK(1){
		
		@Override
		public MiraState initialize(ContainerMira container) {
			
			this.step = 1;
			this.container = container;
			this.addSlots(new Slot(this.container.inventory, 0, 120, 17+5){
				@Override
				public boolean isItemValid(ItemStack stack) {
					return false;
				}
			});
			this.onCraftMatrixChanged(this.container.inventory);
			return this;
			
		}
		
		@Override
		public void onCraftMatrixChanged(IInventory inventory) {
			
			if(inventory.getName() == "inventory_mira") {
				if(inventory.getStackInSlot(0).isEmpty()) {
//					inventory.setInventorySlotContents(0, this.container.bookStack);
					inventory.setInventorySlotContents(0, this.container.bookStack);
				}
			}
			
		}
		
		@Override
		public String getMessage() {
			
			return new TextComponentTranslation("almagest.gui.mira.book.message").getFormattedText();
			
		}
		
	},
	
	
	/*
	 * state for random talking.
	 */
	TALK(100){
		
		private ETalkMira talk;
		
		@Override
		public MiraState initialize(ContainerMira container) {
			
			this.step = 1;
			this.talk = ETalkMira.getRandomTalk();
//			this.talk = ETalkMira.TALK001;
			this.lastStep = this.talk.getTalkEnd();
			this.container = container;
			this.removeSlots();
			return this;
			
		}
		
		@Override
		public String getMessage() {
			
			return this.talk.getLocalTalk(this.step);
			
		}
		
		
		
	};
	
	
	/*
	 * default fields and methods. appropriately override them in need.
	 */
	//the first step is 1.
	protected int step;
	protected int lastStep;
	protected ContainerMira container;
	
	private MiraState(int lastStep) {
		
		this.lastStep = lastStep;
		
	}
	
	@Override
	public void goToNextStep() {
		if(this.step != this.lastStep) {
			this.step++;
		}
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
	}
	
	public void removeSlots() {
		
		this.container.inventorySlots = Lists.<Slot>newArrayList();
		
	}
	
	public void addSlots(Slot slot) {
		
		this.removeSlots();
		
		//slot for book or constellation.
		this.addSlotToContainer(slot);
		
		// player's inventory.
		for(int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(this.container.player.inventory, i, 49 + i * 18, 109+5));
		}
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(this.container.player.inventory, j + i * 9 + 9, 49 + j * 18, 51 + i * 18 + 5));
			}
		}
		
	}
	
	public Slot addSlotToContainer(Slot slot) {
		
		slot.slotNumber = this.container.inventorySlots.size();
		this.container.inventorySlots.add(slot);
		this.container.inventoryItemStacks.add(ItemStack.EMPTY);
		return slot;
		
	}
	
}

//Is this not necessary? just for beautifulness.(beautiful???)
interface IMiraState {
	
	abstract IMiraState initialize(ContainerMira container);
	abstract void goToNextStep();
	abstract void onCraftMatrixChanged(IInventory inventory);
	abstract void onContainerClosed(EntityPlayer player);
	abstract String getMessage();
	
}
