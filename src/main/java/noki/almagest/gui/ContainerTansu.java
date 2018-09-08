package noki.almagest.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import noki.almagest.AlmagestCore;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.ability.StarAbilityCreator.StarAbility;
import noki.almagest.ability.StarPropertyCreator.ItemStarLine;


/**********
 * @class ContainerBookrest
 *
 * @description
 */
public class ContainerTansu  extends ContainerWorkbench {
	
	//******************************//
	// define member variables.
	//******************************//
	//each line state. 0=no line, 1=has line but not connected, 2=has connected line
	private int[] lineState;
	//sum of each stack's memory.
	private int totalMemory;
	//all list of each stack's abilities.
	private ArrayList<AbilityState> abilities;
	
	private World world;
	private BlockPos pos;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ContainerTansu(InventoryPlayer player, World world, BlockPos pos) {
		
		super(player, world, pos);
		this.world = world;
		this.pos = pos;
		
		this.lineState = new int[12];
		this.abilities = new ArrayList<AbilityState>();
		
		//change display position of each slot.
		int leftTopX = 10;
		int leftTopY = 26;
		//result slot.
		this.changeSlotPosition(0, 200, 25);
		//input slot.
		for(int slotHeight=0; slotHeight<3; slotHeight++) {
			for(int slotWidth=0; slotWidth<3; slotWidth++) {
				this.changeSlotPosition(slotHeight*3+slotWidth+1, leftTopX+slotWidth*28, leftTopY+slotHeight*28);
			}
		}
		//player's inventory.
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.changeSlotPosition(i*9+j+10, 48+j*18, 121+i*18);
			}
		}
		
		// player's inventory.
		for(int i = 0; i < 9; ++i) {
			this.changeSlotPosition(i+37, 48+i*18, 179);
		}
		
		this.onCraftMatrixChanged(this.craftMatrix);
		
	}
	
	private void changeSlotPosition(int slotId, int x, int y) {
		
		Slot slot = this.getSlot(slotId);
		slot.xPos = x;
		slot.yPos = y;
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		
		return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ()+ 0.5D) <= 64D;
		
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		
		//reset values.
		this.lineState = new int[12];
		Arrays.fill(this.lineState, 0);
		this.totalMemory = 0;
		this.abilities = new ArrayList<AbilityState>();
		this.abilities.clear();
		
		//deal with input inventory (id=1~9).
		ItemStack stack;
		for(int i=1; i<=9; i++) {
			stack = this.getSlot(i).getStack();
			if(stack != null) {
				this.totalMemory += StarPropertyCreator.getMemory(stack);
				this.tryToAddAllAbilities(StarAbilityCreator.getAbility2(stack));
				this.checkLines(i, stack);
			}
		}
		
		this.updateOutput();
		
	}
	
	private void checkLines(int targetSlot, ItemStack targetStack) {
		//very very very dirty code. HAHAHA!
		switch(targetSlot) {
		case 1:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					break;
				case BOTTOM:
					this.checkEachLine(2, 4);
					break;
				case LEFT:
					break;
				case RIGHT:
					this.checkEachLine(0, 2);
					break;
				}
			}
			break;
		case 2:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					break;
				case BOTTOM:
					this.checkEachLine(3, 5);
					break;
				case LEFT:
					this.checkEachLine(0, 1);
					break;
				case RIGHT:
					this.checkEachLine(1, 3);
					break;
				}
			}
			break;
		case 3:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					break;
				case BOTTOM:
					this.checkEachLine(4, 6);
					break;
				case LEFT:
					this.checkEachLine(1, 2);
					break;
				case RIGHT:
					break;
				}
			}
			break;
		case 4:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					this.checkEachLine(2, 1);
					break;
				case BOTTOM:
					this.checkEachLine(7, 7);
					break;
				case LEFT:
					break;
				case RIGHT:
					this.checkEachLine(5, 5);
					break;
				}
			}
			break;
		case 5:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					this.checkEachLine(3, 2);
					break;
				case BOTTOM:
					this.checkEachLine(8, 8);
					break;
				case LEFT:
					this.checkEachLine(5, 4);
					break;
				case RIGHT:
					this.checkEachLine(6, 6);
					break;
				}
			}
			break;
		case 6:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					this.checkEachLine(4, 3);
					break;
				case BOTTOM:
					this.checkEachLine(9, 9);
					break;
				case LEFT:
					this.checkEachLine(6, 5);
					break;
				case RIGHT:
					break;
				}
			}
			break;
		case 7:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					this.checkEachLine(7, 4);
					break;
				case BOTTOM:
					break;
				case LEFT:
					break;
				case RIGHT:
					this.checkEachLine(10, 8);
					break;
				}
			}
			break;
		case 8:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					this.checkEachLine(8, 5);
					break;
				case BOTTOM:
					break;
				case LEFT:
					this.checkEachLine(10, 7);
					break;
				case RIGHT:
					this.checkEachLine(11, 9);
					break;
				}
			}
			break;
		case 9:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					this.checkEachLine(9, 6);
					break;
				case BOTTOM:
					break;
				case LEFT:
					this.checkEachLine(11, 8);
					break;
				case RIGHT:
					break;
				}
			}
			break;
		}	
	}
	
/*	private void checkLines(int targetSlot, ItemStack targetStack) {
		
		//very very very dirty code. HAHAHA!
		switch(targetSlot) {
		case 1:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					break;
				case BOTTOM:
					this.checkEachLine(2, 4, ItemStarLine.TOP);
					break;
				case LEFT:
					break;
				case RIGHT:
					this.checkEachLine(0, 2, ItemStarLine.LEFT);
					break;
				}
			}
			break;
		case 2:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					break;
				case BOTTOM:
					this.checkEachLine(3, 5, ItemStarLine.TOP);
					break;
				case LEFT:
					this.checkEachLine(0, 1, ItemStarLine.RIGHT);
					break;
				case RIGHT:
					this.checkEachLine(1, 3, ItemStarLine.LEFT);
					break;
				}
			}
			break;
		case 3:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					break;
				case BOTTOM:
					this.checkEachLine(4, 6, ItemStarLine.TOP);
					break;
				case LEFT:
					this.checkEachLine(1, 2, ItemStarLine.RIGHT);
					break;
				case RIGHT:
					break;
				}
			}
			break;
		case 4:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					this.checkEachLine(2, 1, ItemStarLine.BOTTOM);
					break;
				case BOTTOM:
					this.checkEachLine(7, 7, ItemStarLine.TOP);
					break;
				case LEFT:
					break;
				case RIGHT:
					this.checkEachLine(5, 5, ItemStarLine.LEFT);
					break;
				}
			}
			break;
		case 5:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					this.checkEachLine(3, 2, ItemStarLine.BOTTOM);
					break;
				case BOTTOM:
					this.checkEachLine(8, 8, ItemStarLine.TOP);
					break;
				case LEFT:
					this.checkEachLine(5, 4, ItemStarLine.RIGHT);
					break;
				case RIGHT:
					this.checkEachLine(6, 6, ItemStarLine.LEFT);
					break;
				}
			}
			break;
		case 6:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					this.checkEachLine(4, 3, ItemStarLine.BOTTOM);
					break;
				case BOTTOM:
					this.checkEachLine(9, 9, ItemStarLine.TOP);
					break;
				case LEFT:
					this.checkEachLine(6, 5, ItemStarLine.RIGHT);
					break;
				case RIGHT:
					break;
				}
			}
			break;
		case 7:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					this.checkEachLine(7, 4, ItemStarLine.BOTTOM);
					break;
				case BOTTOM:
					break;
				case LEFT:
					break;
				case RIGHT:
					this.checkEachLine(10, 8, ItemStarLine.LEFT);
					break;
				}
			}
			break;
		case 8:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					this.checkEachLine(8, 5, ItemStarLine.BOTTOM);
					break;
				case BOTTOM:
					break;
				case LEFT:
					this.checkEachLine(10, 7, ItemStarLine.RIGHT);
					break;
				case RIGHT:
					this.checkEachLine(11, 9, ItemStarLine.LEFT);
					break;
				}
			}
			break;
		case 9:
			for(ItemStarLine each: StarPropertyCreator.getLines(targetStack)) {
				switch(each) {
				case TOP:
					this.checkEachLine(9, 6, ItemStarLine.BOTTOM);
					break;
				case BOTTOM:
					break;
				case LEFT:
					this.checkEachLine(11, 8, ItemStarLine.RIGHT);
					break;
				case RIGHT:
					break;
				}
			}
			break;
		}
	}*/
	
/*	private void checkEachLine(int targetLine, int partnerId, ItemStarLine partnerLine) {
		
		if(StarPropertyCreator.getLines(this.getSlot(partnerId).getStack()).contains(partnerLine)) {
			this.lineState[targetLine] = 2;
		}
		else {
			this.lineState[targetLine] = 1;
		}
		
	}*/
	
	private void checkEachLine(int targetLine, int partnerId) {
		
		if(this.getSlot(partnerId).getStack() != null && this.getSlot(partnerId).getStack().isEmpty() == false) {
			this.lineState[targetLine] = 2;
		}
		else {
			this.lineState[targetLine] = 1;
		}
		
	}
	
	private void tryToAddAllAbilities(Map<Integer, ArrayList<Integer>> abilities) {
		
		for(Map.Entry<Integer, ArrayList<Integer>> entry: abilities.entrySet()) {
			for(Integer level: entry.getValue()) {
				this.tryToAddAbility(entry.getKey(), level);
			}
		}
		
	}
	
	private void tryToAddAbility(int abilityId, int level) {
		
		boolean found = false;
		for(AbilityState each: this.abilities) {
			if(each.isSame(abilityId, level)) {
				each.addCount();
				found = true;
				break;
			}
		}
		if(found == false) {
			this.abilities.add(new AbilityState(abilityId, level));
		}
		
	}
	
	private void updateOutput() {
		
		ItemStack result = CraftingManager.findMatchingResult(this.craftMatrix, this.world);
		IRecipe recipe = CraftingManager.findMatchingRecipe(this.craftMatrix, this.world);
		if(recipe != null) {
			AlmagestCore.log(recipe.getRegistryName().toString());
			AlmagestCore.log(recipe.getCraftingResult(this.craftMatrix).getDisplayName());
		}
		
		if(result != null) {
			AlmagestCore.log(result.getDisplayName());
		}
		if(result != null && result.isEmpty() == false) {
			AlmagestCore.log("enter update output.");
			//add abilities.
			for(AbilityState each: this.abilities) {
				if(each.selected()) {
					result = StarAbilityCreator.addAbility2(result, each.getAbilityId(), each.getLevel());
				}
			}
			
			//add star memory.
			result = StarPropertyCreator.setMemory(result, this.totalMemory);
			
			//add lines.
			//needs some calculations.
			int totalLineCount = 0;
			HashMap<ItemStarLine, Integer> eachLineCount = new HashMap<ItemStarLine, Integer>();
			for(ItemStarLine each: ItemStarLine.values()) {
				eachLineCount.put(each, 0);
			}
			int stackCount = 0;
			
			for(int i=1; i<=9; i++) {
				ItemStack stack = this.getSlot(i).getStack();
				if(stack != null && stack.isEmpty() == false) {
					for(ItemStarLine each: StarPropertyCreator.getLines(stack)) {
						eachLineCount.put(each, eachLineCount.get(each)+1);
						totalLineCount++;
					}
					stackCount++;
				}
			}
			
			//the total number of lines is defined by the average.
			int averageLineCount = MathHelper.floor((double)totalLineCount/(double)stackCount);
			//the type of line itself is defined by the largest number in each line's count.
			//first, sort.
			List<Map.Entry<ItemStarLine, Integer>> entries = new ArrayList<Map.Entry<ItemStarLine, Integer>>(eachLineCount.entrySet());
			Collections.sort(entries, new Comparator<Map.Entry<ItemStarLine, Integer>>() {
				@Override
				public int compare(Map.Entry<ItemStarLine, Integer> entry1, Map.Entry<ItemStarLine,Integer> entry2) {
					return ((Integer)entry2.getValue()).compareTo((Integer)entry1.getValue());
				}
			});
			//second, divide entries.
			ArrayList<ArrayList<ItemStarLine>> listGroup = new ArrayList<ArrayList<ItemStarLine>>();
			ArrayList<ItemStarLine> currentGroup = new ArrayList<ItemStarLine>();
			int largestCount = entries.get(0).getValue();
			for(Map.Entry<ItemStarLine, Integer> each: entries) {
				if(each.getValue() == largestCount) {
					currentGroup.add(each.getKey());
				}
				else {
					listGroup.add(currentGroup);
					currentGroup = new ArrayList<ItemStarLine>();
					currentGroup.add(each.getKey());
					largestCount = each.getValue();
				}
			}
			//third, shuffle and merge.
			ArrayList<ItemStarLine> newSort = new ArrayList<ItemStarLine>();
			for(ArrayList<ItemStarLine> each: listGroup) {
				if(each.size() > 1) {
					Collections.shuffle(each);
					newSort.addAll(each);
				}
			}
			//and finally, add lines.
			for(int i=0; i<averageLineCount; i++) {
				if(i>=newSort.size()) {
					break;
				}
				result = StarPropertyCreator.addLines(result, newSort.get(i));
			}
		}
		
		this.craftResult.setInventorySlotContents(0, result);
	}
	
	public int[] getLineState() {
		
		return this.lineState;
		
	}
	
	public int getMemory() {
		
		return this.totalMemory;
		
	}
	
	public ArrayList<AbilityState> getAbilities() {
		
		return this.abilities;
		
	}
	
	private void setAbilitySelected(int abilityId, int abilityLevel, boolean flag) {
		
		for(AbilityState each: this.abilities) {
			if(each.isSame(abilityId, abilityLevel)) {
				each.setSelected(flag);
			}
			break;
		}
		
	}
	
	public void switchAbilitySelected(int listId) {
		
		if(listId < this.abilities.size()) {
			this.abilities.get(listId).switchSelect();
			this.updateOutput();
		}
		
	}
	
	public class AbilityState {
		
		private int abilityId;
		private int level;
		private int count;
		private boolean selected;
		
		public AbilityState(int abilityId, int level) {
			this.abilityId = abilityId;
			this.level = level;
			this.count = 1;
			this.selected = false;
		}
		
		public boolean isSame(int abilityId, int level) {
			
			if(this.abilityId == abilityId && this.level == level) {
				return true;
			}
			return false;
			
		}
		
		public int getAbilityId() {
			
			return this.abilityId;
			
		}
		
		public int getLevel() {
			
			return this.level;
			
		}
		
/*		public int getCount() {
			return this.count;
		}*/
		
		public void addCount() {
			this.count++;
		}
		
		public boolean selected() {
			return this.selected;
		}
		
		public void switchSelect() {
			if(this.selected) {
				this.selected = false;
			}
			else {
				this.selected = true;
			}
		}
		
		public void setSelected(boolean flag) {
			this.selected = flag;
		}
		
	}
	
}
