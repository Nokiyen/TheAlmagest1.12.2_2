package noki.almagest.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noki.almagest.AlmagestCore;
import noki.almagest.attribute.AttributeHelper;
import noki.almagest.attribute.EStarAttribute;


/**********
 * @class StarRecipe
 *
 * @description このmodのブロック・アイテムに汎用的に使うレシピです。
 */
public class StarRecipe implements IRecipe {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private ItemStack resultStack;
	private ResourceLocation registryName;
	private Map<EStarAttribute, Integer> necessaryAttributes = new HashMap<EStarAttribute, Integer>();
	private List<ItemStack> necessaryItem = new ArrayList<ItemStack>();
	private boolean special;
	private int maxStackNum;
	private ArrayList<ItemStack> recipeHints = new ArrayList<ItemStack>();
	
	
	//******************************//
	// define member methods.
	//******************************//
	public StarRecipe(ItemStack resultStack) {
		
		this.resultStack = resultStack;
		this.maxStackNum = 0;
		
	}
	
	public StarRecipe setAttribute(EStarAttribute attribute, int level) {
		
		this.necessaryAttributes.put(attribute, level);
		this.maxStackNum = this.maxStackNum+2;
		return this;
		
	}
	
	public Map<EStarAttribute, Integer> getAttribute() {
		
		return this.necessaryAttributes;
		
	}
	
	public StarRecipe setStack(ItemStack stack) {
		
		this.necessaryItem.add(stack);
		this.maxStackNum = this.maxStackNum+1;
		return this;
		
	}
	
	public List<ItemStack> getStack() {
		
		return this.necessaryItem;
		
	}
	
	@Override
	public IRecipe setRegistryName(ResourceLocation name) {
		
		this.registryName = name;
		return this;
		
	}
	
	@Override
	public ResourceLocation getRegistryName() {
		
		return this.registryName;
		
	}
	
	@Override
	public Class<IRecipe> getRegistryType() {
		
		return IRecipe.class;
		
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		
		int currentStackNum = 0;
		
		Map<ItemStack, Boolean> stackFlag = new HashMap<ItemStack, Boolean>();
		for(ItemStack each: this.necessaryItem) {
			stackFlag.put(each, false);
		}
		
		@SuppressWarnings("unused")
		int minAttribute = 0;
		Map<EStarAttribute, Integer> attributeSum = new HashMap<EStarAttribute, Integer>();
		for(Map.Entry<EStarAttribute, Integer> entry: this.necessaryAttributes.entrySet()) {
			attributeSum.put(entry.getKey(), 0);
			minAttribute += entry.getValue();
		}
		
		@SuppressWarnings("unused")
		int totalAttribute = 0;
		for(int i=0; i<inv.getSizeInventory(); i++) {
			ItemStack selectedStack = inv.getStackInSlot(i);
			if(selectedStack.isEmpty()) {
				continue;
			}
			
			//limit crafting with the number of stacks.
			currentStackNum++;
			if(currentStackNum > this.maxStackNum) {
				return false;
			}
			
			boolean itemFlag = false;
			for(ItemStack keyStack: stackFlag.keySet()) {
				if(selectedStack.isItemEqual(keyStack)) {
					stackFlag.put(keyStack, true);
					itemFlag = true;
					break;
				}
			}
			
			int attributeFlag = 0;
			for(Map.Entry<EStarAttribute, Integer> eachAttribute: attributeSum.entrySet()) {
				int eachLevel = AttributeHelper.getAttrributeLevel(selectedStack, eachAttribute.getKey());
				AlmagestCore.log(Integer.toString(eachLevel));
				attributeSum.put(eachAttribute.getKey(), eachAttribute.getValue() + eachLevel);
				attributeFlag += eachLevel;
			}
			
			//containing irrelevant item. return false.
			if(itemFlag == false && attributeFlag == 0) {
				return false;
			}
			
			totalAttribute += attributeFlag;
		}
		
		boolean isSuccess = true;
		for(Boolean eachFlag: stackFlag.values()) {
			isSuccess = isSuccess && eachFlag;
		}
		for(Map.Entry<EStarAttribute, Integer> eachAttribute: attributeSum.entrySet()) {
			isSuccess = isSuccess && eachAttribute.getValue() >= this.necessaryAttributes.get(eachAttribute.getKey());
		}
		
		//discard this logic.
/*		if(totalAttribute > minAttribute * 2) {
			isSuccess = false;
		}*/
		
//		AlmagestCore.log("mathes is %s.", Boolean.toString(isSuccess));
		return isSuccess;
		
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		
		return this.resultStack.copy();
		
	}
	
	@Override
	public boolean canFit(int width, int height) {
		
		return width*height >= 1;
		
	}
	
	@Override
	public ItemStack getRecipeOutput() {

		return this.resultStack;
		
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		
		NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		
		for(int i=0; i < nonnulllist.size(); ++i) {
			ItemStack itemstack = inv.getStackInSlot(i);
			nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
		}
		
		return nonnulllist;
		
	}
	
	public boolean isSpecial() {
		
		return this.special;
		
	}
	
	public StarRecipe setSpecial(boolean flag) {
		
		this.special = flag;
		return this;
		
	}
	
	public int getMaxStack() {
		
		return this.maxStackNum;
		
	}
	
	public StarRecipe setMaxStack(int max) {
		
		this.maxStackNum = max;
		return this;
		
	}
	
	@Override
	public boolean isDynamic() {
		
		return true;
		
	}
	
	public boolean hasHint() {
		
		return true;
		
	}
	
	public ArrayList<ItemStack> getHintItems() {
		
		return this.recipeHints;
		
	}
	
	public StarRecipe setHint(ItemStack stack) {
		
		if(!this.recipeHints.contains(stack)) {
			this.recipeHints.add(stack);
		}
		return this;
		
	}
	
}
