package noki.almagest.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.ability.StarPropertyCreator.ItemStarLine;
import noki.almagest.attribute.AttributeHelper;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.item.ItemBlockConstellation;
import noki.almagest.registry.ModBlocks;


/**********
 * @class EventToolTip
 *
 * @description item stackのpropertyを表示するためのイベントです。
 * @description_en
 */
public class EventToolTip {
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event) {
		
		if(event.getItemStack() == null) {
			return;
		}
		
		List<String> tooltip = event.getToolTip();
		ItemStack stack = event.getItemStack();
		
		//if it's presented constellation, add symbol.
		if(stack.getItem() instanceof ItemBlock &&
				net.minecraft.block.Block.getBlockFromItem(stack.getItem()) == ModBlocks.CONSTELLATION_BLOCK) {
			if(tooltip.size() != 0 &&
					AlmagestCore.savedDataManager.getFlagData().isCostellationPresented(
							Constellation.getConstFromNumber(ItemBlockConstellation.getConstNumber(stack)))) {
				tooltip.set(0, tooltip.get(0)+new TextComponentTranslation("almagest.gui.tooltip.presented").getFormattedText());
			}
		}
		
		//keep attributes.
		Map<EStarAttribute, Integer> attributes = AttributeHelper.getAttributes(stack);
		//keep memory.
		int memory = StarPropertyCreator.getMemory(stack);
		int magnitude = StarPropertyCreator.getMagnitude(memory);
		//keep lines.
		ArrayList<ItemStarLine> lines = StarPropertyCreator.getLines(stack);
		//keep abilities.
		Map<Integer, ArrayList<Integer>> abilities = StarAbilityCreator.getAbility2(stack);
		
		//if key's not pressed but the stack has some data, show hint.
		if(!GuiScreen.isCtrlKeyDown()) {
			tooltip.add(new TextComponentTranslation("almagest.gui.tootip.keyoff").setStyle(
					new Style().setColor(TextFormatting.AQUA).setItalic(true)).getFormattedText());
			return;
		}
		
		
		//try to show almagest data.
		tooltip.add(getLine("almagest.gui.tooltip.spacer"));
		
		for(Map.Entry<EStarAttribute, Integer> each: attributes.entrySet()) {
			tooltip.add(getLine("almagest.gui.tooltip.attribute",
					new TextComponentTranslation(each.getKey().getName()).getFormattedText(), each.getValue()));
		}
		tooltip.add(getLine("almagest.gui.tooltip.memory", memory, magnitude));
		
		String lineString = "";
		for(ItemStarLine each: lines) {
			switch(each) {
				case TOP:
					lineString += "T ";
					break;
				case RIGHT:
					lineString += "R ";
					break;
				case BOTTOM:
					lineString += "B ";
					break;
				case LEFT:
					lineString += "L ";
					break;
			}
		}
		tooltip.add(getLine("almagest.gui.tooltip.line", lineString));
		
		if(tooltip.size() != 0 && abilities.size() != 0) {
			tooltip.add(getLine("almagest.gui.tooltip.spacer"));
		}
		
		for(Map.Entry<Integer, ArrayList<Integer>> eachAbilitiy: abilities.entrySet()) {
			for(Integer level: eachAbilitiy.getValue()) {
				event.getToolTip().add(getLine("almagest.ability."+String.format("%02d",eachAbilitiy.getKey())+"."+level+".name"));
			}
		}
		
		tooltip.add(getLine("almagest.gui.tooltip.spacer"));
		
	}
	
	private static String getLine(String key, Object... values) {
		
		return new TextComponentTranslation(key, values).getFormattedText();
		
	}

}
