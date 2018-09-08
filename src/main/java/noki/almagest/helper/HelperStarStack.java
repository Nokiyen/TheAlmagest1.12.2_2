package noki.almagest.helper;

import net.minecraft.item.ItemStack;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.ability.StarPropertyCreator.ItemStarLine;
import noki.almagest.attribute.AttributeHelper;
import noki.almagest.attribute.EStarAttribute;

public class HelperStarStack {
	
	ItemStack stack;
	
	public HelperStarStack(ItemStack stack) {
		
		this.stack = stack;
		
	}
	
	public HelperStarStack addAbility(int abilityId, int abilityLevel) {
		
		StarAbilityCreator.addAbility2(this.stack, abilityId, abilityLevel);
		return this;
		
	}
	
	public HelperStarStack addMemory(int memory) {
		
		StarPropertyCreator.setMemory(this.stack, memory);
		return this;
		
	}
	
	public HelperStarStack addLine(ItemStarLine line) {
		
		StarPropertyCreator.addLine(this.stack, line);
		return this;
		
	}
	
	public HelperStarStack addAttribute(EStarAttribute attribute, int level) {
		
		AttributeHelper.setAttributeLevel(this.stack, attribute, level);
		return this;
		
	}
	
	public ItemStack getStack() {
		
		return this.stack;
		
	}

}
