package noki.almagest.gui;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.ability.StarAbilityCreator.StarAbility;
import noki.almagest.ability.StarPropertyCreator.ItemStarLine;


/**********
 * @class ContainerBookrest
 *
 * @description
 */
public class ContainerBookrest_original  extends Container {
	
	//******************************//
	// define member variables.
	//******************************//
	private EntityPlayer player;
	private World world;
	private int posX;
	private int posY;
	private int posZ;
	
	public IInventory outputInventory;
	public IInventory inputInventory;
	
	private boolean[] lineFlags = new boolean[12];
	private int memory;
	private ArrayList<StarAbility> abilities = new ArrayList<StarAbility>();
	private boolean magnitudeFlag = false;
		
	
	//******************************//
	// define member methods.
	//******************************//
	public ContainerBookrest_original(EntityPlayer player, World world, int x, int y, int z) {

		this.player = player;
		this.world = world;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		
		this.inputInventory = new InventoryBasic("Bookrest", true, 3*3) {
			public void markDirty() {
				super.markDirty();
				ContainerBookrest_original.this.onCraftMatrixChanged(this);
			}
		};
		this.outputInventory = new InventoryCraftResult() {
			public void markDirty() {
				super.markDirty();
				ContainerBookrest_original.this.onCraftMatrixChanged(this);
			}
		};
		
		int leftTopX = 10;
		int leftTopY = 26;
		for(int slotHeight=0; slotHeight<3; slotHeight++) {
			for(int slotWidth=0; slotWidth<3; slotWidth++) {
				this.addSlotToContainer(new Slot(this.inputInventory,
						slotHeight*3+slotWidth, leftTopX+slotWidth*28, leftTopY+slotHeight*28));
			}
		}
		
		this.addSlotToContainer(new SlotBookrestResult(this.outputInventory, 0, 200, 25));
		
		// player's inventory.
		for(int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(this.player.inventory, i, 48 + i * 18, 179));
		}
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(this.player.inventory, j + i * 9 + 9, 48 + j * 18, 121 + i * 18));
			}
		}
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		
		return player.getDistanceSq(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D) <= 64D;
		
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		
		super.onCraftMatrixChanged(inventory);
		
		Arrays.fill(this.lineFlags, false);
		this.memory = 0;
		this.magnitudeFlag = false;
		this.abilities.clear();
		
		int maxSlotId = 8;
		ItemStack stack;
		
		for(int i=0; i<=maxSlotId; i++) {
			stack = this.inventorySlots.get(i).getStack();
			
			if(stack != null) {
				if(stack.isEmpty() == false) {
					magnitudeFlag = true;
				}
				
				this.memory = this.memory + StarPropertyCreator.getMemory(stack);
				
				this.abilities.addAll(StarAbilityCreator.getAbilities(stack));
				
				ArrayList<ItemStarLine> lines = StarPropertyCreator.getLines(stack);
				for(ItemStarLine each: lines) {
					switch(each) {
						case TOP:
							if(3<=i && i<=5) {
								lineFlags[i-1] = true;
							}
							else if(6<=i && i<=8) {
								lineFlags[i+1] = true;
							}
							break;
						case BOTTOM:
							if(0<=i && i<=2) {
								lineFlags[i+2] = true;
							}
							else if(3<=i && i<=5) {
								lineFlags[i+4] = true;
							}
							break;
						case LEFT:
							if(i== 1 || i==2) {
								lineFlags[i-1] = true;
							}
							else if(i== 4 || i==5) {
								lineFlags[i+1] = true;
							}
							else if(i== 7 || i==8) {
								lineFlags[i+3] = true;
							}
							break;
						case RIGHT:
							if(i== 0 || i==1) {
								lineFlags[i] = true;
							}
							else if(i== 3 || i==4) {
								lineFlags[i+2] = true;
							}
							else if(i== 6 || i==7) {
								lineFlags[i+4] = true;
							}
							break;
						default:
							break;
					}
				}
			}
		}
		
		if(inventory == this.inputInventory) {
			this.updateOutput();
		}
		
	}
	
	private void updateOutput() {
		
		if(this.magnitudeFlag == true) {
			StarAbility[] addedAbilities = new StarAbility[this.abilities.size()];
			for(int i=0; i<this.abilities.size(); i++) {
				addedAbilities[i] = this.abilities.get(i);
			}
			this.outputInventory.setInventorySlotContents(0,
					StarAbilityCreator.addAbility(StarPropertyCreator.setMemory(new ItemStack(Items.BREAD), this.memory), addedAbilities));
		}
		else {
			this.outputInventory.removeStackFromSlot(0);
		}
		
/*		ItemStack stack1 = this.inputInventory.getStackInSlot(0);
		ItemStack stack2 = this.inputInventory.getStackInSlot(1);
		
		if(stack1 == null || stack2 == null) {
			this.outputInventory.setInventorySlotContents(0, null);
			return;
		}
		if(stack1.getItem() != Item.getItemFromBlock(AlmagestData.blockConstellation)
				|| stack2.getItem() != AlmagestData.itemMissingStar) {
			this.outputInventory.setInventorySlotContents(0, null);
			return;
		}
		
		int constNumber = HelperConstellation.getConstNumber(stack1);
		int[] missingStars = HelperConstellation.getMissingStars(stack1);
		int givenStar = HelperConstellation.getMissingStarNumber(stack2);
		
		boolean flag = false;
		for(int each: missingStars) {
			if(each == givenStar) {
				flag = true;
				break;
			}
		}
		
		if(flag == false) {
			this.outputInventory.setInventorySlotContents(0, null);
			return;
		}
		
		if(missingStars.length == 1) {
			this.outputInventory.setInventorySlotContents(0, HelperConstellation.getConstStack(constNumber, 1));
		}
		else {
			int[] newMissingStars = new int[missingStars.length-1];
			int count = 0;
			for(int each: missingStars) {
				if(each != givenStar) {
					newMissingStars[count] = each;
					count++;
				}
			}
			this.outputInventory.setInventorySlotContents(0, HelperConstellation.getConstStack(constNumber, newMissingStars, 1));
		}
		
		this.detectAndSendChanges();*/
		
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		
		super.onContainerClosed(player);
		
		if(this.world.isRemote == false) {
			for(int i = 0; i < this.inputInventory.getSizeInventory(); ++i) {
/*				ItemStack stack = this.inputInventory.getStackInSlotOnClosing(i);
				if(stack != null) {
					player.dropPlayerItemWithRandomChoice(stack, false);
				}*/
			}
		}
		
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
	
}
