package noki.almagest.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.AlmagestData;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.ItemWithAttribute;
import noki.almagest.event.post.AttributeLevelEvent;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithEvent;
import noki.almagest.registry.IWithRecipe;
import noki.almagest.registry.IWithSubTypes;


/**********
 * @class ItemCocktail
 *
 * @description 
 */
public class ItemCocktail extends ItemWithAttribute implements IWithRecipe, IWithSubTypes, IWithEvent {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private static final int type = 3;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemCocktail() {
		
		this.setHasSubtypes(true);
		this.setAttributeLevel(EStarAttribute.TOOL, 20);
		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		
		return this.getUnlocalizedName() + "." + MathHelper.clamp(stack.getMetadata(), 0, type-1);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		
		if(tab == AlmagestData.tab) {
			list.addAll(this.getSubItems());
		}
		
	}
	
	@Override
	public int getSubtypeCount() {
		
		return type;
		
	}
	
	@Override
	public boolean registerToAlmagest() {
		
		return true;
		
	}
	
	@Override
	public List<ItemStack> getSubItems() {
		
		List<ItemStack> items = new ArrayList<>();
		for(int i=0; i<type; i++) {
			items.add(new ItemStack(this, 1, i));
		}
		return items;
		
	}
	
	@Override
	public IRecipe getRecipe(ItemStack stack) {
		
		return this.getRecipe().get(MathHelper.clamp(stack.getMetadata(), 0, type));
		
	}
	
	@Override
	public List<IRecipe> getRecipe() {
		
		return this.makeRecipeList(
				new StarRecipe(new ItemStack(this,4,0)).setAttribute(EStarAttribute.MINERAL, 20).setAttribute(EStarAttribute.LIQUID, 20)
				.setHint(new ItemStack(Items.WATER_BUCKET)),
				new StarRecipe(new ItemStack(this,4,1)).setAttribute(EStarAttribute.PLANT, 20).setAttribute(EStarAttribute.LIQUID, 20)
				.setHint(new ItemStack(Items.WATER_BUCKET)),
				new StarRecipe(new ItemStack(this,4,2)).setAttribute(EStarAttribute.LIQUID, 40).setMaxStack(3)
				.setHint(new ItemStack(Items.WATER_BUCKET))
		);
		
	}
	
	@SubscribeEvent
	public void onAttributeLevel(AttributeLevelEvent event) {
		
		if(event.getStack() == null || event.getStack().getItem() != this) {
			return;
		}
		
		switch(StarPropertyCreator.getMagnitude(event.getStack())) {
			case 1:
			case 2:
			case 3:
			case 4:
				switch(MathHelper.clamp(event.getStack().getMetadata(), 0, type-1)) {
					case 0:
						if(event.getAttribute() == EStarAttribute.FUEL) event.setLevel(event.getLevel()+20);
						break;
					case 1:
						if(event.getAttribute() == EStarAttribute.WOOD) event.setLevel(event.getLevel()+20);
						break;
					case 2:
						if(event.getAttribute() == EStarAttribute.DECORATIVE) event.setLevel(event.getLevel()+20);
						break;
				}
			case 5:
				if(event.getAttribute() == EStarAttribute.STAR) event.setLevel(event.getLevel()+10);
			case 6:
				break;
		}
		
	}

}
