package noki.almagest.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import noki.almagest.ability.StarPropertyCreator.ItemStarLine;


/**********
 * @class ItemFoodWithAttribute
 *
 * @description 多重継承できないのでしょうがない。ItemFoodにはこれを継承します。
 */
public class ItemFoodWithAttribute extends ItemFood implements IWithAttribute {
	
	
	//******************************//
	// define member variables.
	//******************************//
	protected Map<EStarAttribute, Integer> attributes = new HashMap<EStarAttribute, Integer>();
	protected int memory;
	protected ArrayList<ItemStarLine> lines = new ArrayList<ItemStarLine>();
	protected Map<Integer, ArrayList<Integer>> abilities = new HashMap<Integer, ArrayList<Integer>>();
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemFoodWithAttribute(int amount, float saturation, boolean isWolfFood) {
		
		super(amount, saturation, isWolfFood);
		
	}
	
	@Override
	public void setAttributeLevel(EStarAttribute attribute, int level) {
		
		this.attributes.put(attribute, level);
		
	}
	
	@Override
	public int getAttributeLevel(EStarAttribute attribute, ItemStack stack) {
		
		return this.modifyAttribute(attribute, stack);
		
	}
	
	@Override
	public Map<EStarAttribute, Integer> getAttributes(ItemStack stack) {
		
		return this.attributes;
		
	}
	
	@Override
	public int modifyAttribute(EStarAttribute attribute, ItemStack stack) {
		
		Integer level = this.attributes.get(attribute);
		if(level != null) {
			return level;
		}
		return 0;
		
	}
	
	@Override
	public int getMemory(ItemStack stack) {
		
		return this.memory;
		
	}
	
	public void setMemory(int memory) {
		
		this.memory = memory;
		
	}

	@Override
	public ArrayList<ItemStarLine> getLine(ItemStack stack) {
		
		return this.lines;
		
	}
	
	public void addLine(ItemStarLine line) {
		
		if(!this.lines.contains(line)) {
			this.lines.add(line);
		}
		
	}

	@Override
	public Map<Integer, ArrayList<Integer>> getAbilities(ItemStack stack) {
		
		return this.abilities;
		
	}
	
	public void addAbility(int abilityId, int abilityLevel) {
		
		if(this.abilities.containsKey(abilityId)) {
			ArrayList<Integer> targetAbility = this.abilities.get(abilityId);
			if(!targetAbility.contains(abilityLevel)) {
				targetAbility.add(abilityLevel);
			}
		}
		else {
			ArrayList<Integer> newAbility = new ArrayList<Integer>();
			newAbility.add(abilityLevel);
			this.abilities.put(abilityId, newAbility);
		}
		
	}

}
