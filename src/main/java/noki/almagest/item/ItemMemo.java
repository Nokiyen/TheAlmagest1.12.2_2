package noki.almagest.item;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import noki.almagest.AlmagestData;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.ItemWithAttribute;
import noki.almagest.helper.HelperNBTStack;
import noki.almagest.registry.IWithEvent;
import noki.almagest.registry.ModItems;
import noki.almagest.saveddata.gamedata.EMemoData;


/**********
 * @class ItemMemo
 *
 * @description
 */
public class ItemMemo extends ItemWithAttribute implements IWithEvent {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String NBT_memoNumber = "memoNumber";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemMemo() {
		
		this.setAttributeLevel(EStarAttribute.PAPER, 20);
		this.setAttributeLevel(EStarAttribute.FUEL, 10);
		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		
		if(new HelperNBTStack(stack).hasKey(NBT_memoNumber)) {
			return this.getUnlocalizedName() + "." + String.format("%02d", getMemoNumber(stack));
		}
		return this.getUnlocalizedName();
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		
		if(tab == AlmagestData.tab) {
			List<ItemStack> items = new ArrayList<>();
			for(EMemoData each: EMemoData.values()) {
				items.add(getMemo(each));
			}
			list.addAll(items);
		}
		
	}
	
/*	@Override
	public List<ItemStack> getSubItems() {
		
		List<ItemStack> items = new ArrayList<>();
		for(EMemoData each: EMemoData.values()) {
			items.add(getMemo(each));
		}
		return items;
		
	}
	
	@Override
	public int getSubtypeCount() {
		
		return EMemoData.values().length;
		
	}
	
	@Override
	public boolean registerToAlmagest() {
		
		return true;
		
	}*/
	
	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event) {
		
		if(event.getItemStack().getItem() != this) {
			return;
		}
		
		event.getToolTip().add(
				new TextComponentTranslation(getMemoDataFromStack(event.getItemStack()).getDisplay()).getFormattedText());
		event.getToolTip().add("");
		
	}
		
	//----------
	//Static Methods.
	//----------
	public static ItemStack getMemo(EMemoData memoData) {
		
		// create ItemStack with NBT.
		ItemStack stack = new ItemStack(ModItems.MEMO, 1);
		return new HelperNBTStack(stack).setInteger(NBT_memoNumber, memoData.getId()).getStack();
		
	}
	
	public static EMemoData getMemoDataFromStack(ItemStack stack) {
		
		return EMemoData.getMemoData(getMemoNumber(stack));
		
	}
	
	public static int getMemoNumber(ItemStack stack) {
		
		return new HelperNBTStack(stack).getInteger(NBT_memoNumber);
		
	}

}
