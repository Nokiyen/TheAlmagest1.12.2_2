package noki.almagest.saveddata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.ability.StarPropertyCreator.ItemStarLine;


/**********
 * @class PropertySet
 *
 * @description item stackにつける情報をまとめて格納するクラスです。
 * @description_en
 */
public class PropertySet {
	
	//******************************//
	// define member variables.
	//******************************//
	private int memory;
	private boolean[] lines = new boolean[4];
	private Map<Integer, ArrayList<Integer>> abilities = new HashMap<Integer, ArrayList<Integer>>();
	
	private final static String nbt_propertyMemory = "property_memory";
	private final static String nbt_propertyLine = "property_line";
	private final static String nbt_propertyAbilities = "property_abilities";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public PropertySet() {
		
	}
	
	//create property set from nbt.
	public PropertySet(NBTTagCompound nbt) {
		
		this.setMemory(nbt.getInteger(nbt_propertyMemory));

		for(int eachLine: nbt.getIntArray(nbt_propertyLine)) {
			switch(eachLine) {
				case 1:
					this.setLine(ItemStarLine.TOP);
					break;
				case 2:
					this.setLine(ItemStarLine.BOTTOM);
					break;
				case 3:
					this.setLine(ItemStarLine.LEFT);
					break;
				case 4:
					this.setLine(ItemStarLine.RIGHT);
					break;
			}
		}
		
		NBTTagCompound abilitiesNbt = nbt.getCompoundTag(nbt_propertyAbilities);
		for(String eachAbility: abilitiesNbt.getKeySet()) {
			for(int eachLevel: abilitiesNbt.getIntArray(eachAbility)) {
				this.setAbility(Integer.valueOf(eachAbility), eachLevel);
			}
		}
		
	}
	
	// create property set from item stack;
	public PropertySet(ItemStack stack) {
		
		this.setMemory(StarPropertyCreator.getMemory(stack));
		
		ArrayList<ItemStarLine> lines = StarPropertyCreator.getLines(stack);
		for(ItemStarLine each: lines) {
			this.setLine(each);
		}
		
		Map<Integer, ArrayList<Integer>> abilities = StarAbilityCreator.getAbility2(stack);
		for(Map.Entry<Integer, ArrayList<Integer>> eachAbility: abilities.entrySet()) {
			for(Integer eachLevel: eachAbility.getValue()) {
				this.setAbility(eachAbility.getKey(), eachLevel);
			}
		}
		
	}
	
	public NBTTagCompound createNbt() {
		
		NBTTagCompound newNbt = new NBTTagCompound();
		
		newNbt.setInteger(nbt_propertyMemory, this.memory);
		
		int[] lines = new int[4];
		lines[0] = (this.lines[0]) ? 1 : 0;
		lines[1] = (this.lines[1]) ? 1 : 0;
		lines[2] = (this.lines[2]) ? 1 : 0;
		lines[3] = (this.lines[3]) ? 1 : 0;
		newNbt.setIntArray(nbt_propertyLine, lines);
		
		NBTTagCompound abilitiesNbt = new NBTTagCompound();
		for(Map.Entry<Integer, ArrayList<Integer>> eachAbility : this.abilities.entrySet()) {
			ArrayList<Integer> eachLevels = eachAbility.getValue();
			int[] levels = new int[eachLevels.size()];
			for(int i=0; i<eachLevels.size(); i++) {
				levels[i] = eachLevels.get(i);
			}
			abilitiesNbt.setIntArray(String.valueOf(eachAbility.getKey()), levels);
		}
		newNbt.setTag(nbt_propertyAbilities, abilitiesNbt);
		
		return newNbt;
		
	}
	
	public void setMemory(int memory) {
		
		this.memory = memory;
		
	}
	
	public void setLine(ItemStarLine line) {
		
		switch (line) {
			case TOP:
				this.lines[0] = true;
				break;
			case BOTTOM:
				this.lines[1] = true;
				break;
			case LEFT:
				this.lines[2] = true;
				break;
			case RIGHT:
				this.lines[3] = true;
				break;
		}

	}
	
	public void setAbility(int abilityId, int level) {
		
		ArrayList<Integer> levelSet = this.abilities.get(abilityId);
		if(levelSet == null) {
			levelSet = new ArrayList<Integer>();
			levelSet.add(level);
			this.abilities.put(abilityId, levelSet);
		}
		else {
			levelSet.add(level);
		}
		
	}
	
	public int getMemory() {
		
		return this.memory;
		
	}
	
	public ItemStack addProperty(ItemStack stack) {
		
		ItemStack addedStack = stack;
		
		addedStack = StarPropertyCreator.setMemory(stack, this.memory);
		
		if(this.lines[0] == true) {
			addedStack = StarPropertyCreator.addLines(addedStack, ItemStarLine.TOP);
		}
		if(this.lines[1] == true) {
			addedStack = StarPropertyCreator.addLines(addedStack, ItemStarLine.BOTTOM);
		}
		if(this.lines[2] == true) {
			addedStack = StarPropertyCreator.addLines(addedStack, ItemStarLine.LEFT);
		}
		if(this.lines[3] == true) {
			addedStack = StarPropertyCreator.addLines(addedStack, ItemStarLine.RIGHT);
		}
		
		for(Map.Entry<Integer, ArrayList<Integer>> entry: this.abilities.entrySet()) {
			for(int eachLevel: entry.getValue()) {
				addedStack = StarAbilityCreator.addAbility2(addedStack, entry.getKey(), eachLevel);
			}
		}
		
		return addedStack;
		
	}
	
}
