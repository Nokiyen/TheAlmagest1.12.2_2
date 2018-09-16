package noki.almagest;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.ability.StarAbilityCreator.StarAbility;
import noki.almagest.ability.StarPropertyCreator.ItemStarLine;
import noki.almagest.registry.ModBlocks;

public class TabAlmagest extends CreativeTabs {
	
	//******************************//
	// define member variables.
	//******************************//
	public static String label = "Almagest";

	
	//******************************//
	// define member methods.
	//******************************//
	public TabAlmagest() {
		
		super(label);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() {
		
		return new ItemStack(Item.getItemFromBlock(ModBlocks.CONSTELLATION_BLOCK));

	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(NonNullList<ItemStack> items) {
		
		super.displayAllRelevantItems(items);
		
		items.add(StarPropertyCreator.setProperty(new ItemStack(noki.almagest.registry.ModItems.STARDUST), 999, ItemStarLine.TOP));
		items.add(StarPropertyCreator.setProperty(new ItemStack(noki.almagest.registry.ModItems.STARDUST), 999, ItemStarLine.BOTTOM));
		items.add(StarPropertyCreator.setProperty(new ItemStack(noki.almagest.registry.ModItems.STARDUST), 999, ItemStarLine.LEFT));
		items.add(StarPropertyCreator.setProperty(new ItemStack(noki.almagest.registry.ModItems.STARDUST), 999, ItemStarLine.RIGHT));
		items.add(StarPropertyCreator.setProperty(new ItemStack(noki.almagest.registry.ModItems.STARDUST), 100, ItemStarLine.TOP, ItemStarLine.BOTTOM));
		items.add(StarPropertyCreator.setProperty(new ItemStack(noki.almagest.registry.ModItems.STARDUST), 100, ItemStarLine.TOP, ItemStarLine.LEFT));
		items.add(StarPropertyCreator.setProperty(new ItemStack(noki.almagest.registry.ModItems.STARDUST), 100, ItemStarLine.TOP, ItemStarLine.RIGHT));
		items.add(StarPropertyCreator.setProperty(new ItemStack(noki.almagest.registry.ModItems.STARDUST), 100, ItemStarLine.BOTTOM, ItemStarLine.LEFT));
		items.add(StarPropertyCreator.setProperty(new ItemStack(noki.almagest.registry.ModItems.STARDUST), 100, ItemStarLine.BOTTOM, ItemStarLine.RIGHT));
		items.add(StarPropertyCreator.setProperty(new ItemStack(noki.almagest.registry.ModItems.STARDUST), 100, ItemStarLine.LEFT, ItemStarLine.RIGHT));
		items.add(StarPropertyCreator.setProperty(new ItemStack(noki.almagest.registry.ModItems.STARDUST), 100, ItemStarLine.TOP, ItemStarLine.BOTTOM, ItemStarLine.LEFT));
		items.add(StarPropertyCreator.setProperty(new ItemStack(noki.almagest.registry.ModItems.STARDUST), 100, ItemStarLine.TOP, ItemStarLine.BOTTOM, ItemStarLine.RIGHT));
		items.add(StarPropertyCreator.setProperty(new ItemStack(noki.almagest.registry.ModItems.STARDUST), 100, ItemStarLine.TOP, ItemStarLine.LEFT, ItemStarLine.RIGHT));
		items.add(StarPropertyCreator.setProperty(new ItemStack(noki.almagest.registry.ModItems.STARDUST), 100, ItemStarLine.BOTTOM, ItemStarLine.LEFT, ItemStarLine.RIGHT));
		items.add(StarPropertyCreator.setProperty(new ItemStack(noki.almagest.registry.ModItems.STARDUST), 100,
				ItemStarLine.TOP, ItemStarLine.BOTTOM, ItemStarLine.LEFT, ItemStarLine.RIGHT));
		items.add(StarAbilityCreator.addAbility(new ItemStack(noki.almagest.registry.ModItems.STARDUST), StarAbility.SHINING));
		items.add(StarAbilityCreator.addAbility(new ItemStack(noki.almagest.registry.ModItems.STARDUST), StarAbility.FOOD_FULL_1));
		items.add(StarAbilityCreator.addAbility(new ItemStack(noki.almagest.registry.ModItems.STARDUST), StarAbility.AUTO_EXPLOSION));
		ItemStack addItem;
		addItem = new ItemStack(Items.DIAMOND_HELMET);
		addItem = StarAbilityCreator.addAbility2(addItem, 1, 4);
		addItem = StarAbilityCreator.addAbility2(addItem, 2, 4);
		addItem = StarAbilityCreator.addAbility2(addItem, 3, 4);
		items.add(addItem);
		addItem = new ItemStack(Items.DIAMOND_CHESTPLATE);
		addItem = StarAbilityCreator.addAbility2(addItem, 1, 4);
		addItem = StarAbilityCreator.addAbility2(addItem, 2, 4);
		addItem = StarAbilityCreator.addAbility2(addItem, 3, 4);
		items.add(addItem);
		addItem = new ItemStack(Items.DIAMOND_LEGGINGS);
		addItem = StarAbilityCreator.addAbility2(addItem, 1, 4);
		addItem = StarAbilityCreator.addAbility2(addItem, 2, 4);
		addItem = StarAbilityCreator.addAbility2(addItem, 3, 4);
		items.add(addItem);
		addItem = new ItemStack(Items.DIAMOND_BOOTS);
		addItem = StarAbilityCreator.addAbility2(addItem, 1, 4);
		addItem = StarAbilityCreator.addAbility2(addItem, 2, 4);
		addItem = StarAbilityCreator.addAbility2(addItem, 3, 4);
		items.add(addItem);
		addItem = new ItemStack(Items.BREAD);
		addItem = StarAbilityCreator.addAbility2(addItem, 7, 4);
		items.add(addItem);
		addItem = new ItemStack(Items.DIAMOND_BOOTS);
		addItem = StarAbilityCreator.addAbility2(addItem, 8, 4);
		items.add(addItem);
		for(int i=1; i<=7; i++) {
			addItem = new ItemStack(Items.DIAMOND_SWORD);
			addItem = StarAbilityCreator.addAbility2(addItem, 9, i);
			items.add(addItem);
		}
		items.add(StarAbilityCreator.addAbility2( new ItemStack(Items.DIAMOND_BOOTS), 10, 4));
		items.add(StarAbilityCreator.addAbility2( new ItemStack(Items.DIAMOND_HELMET), 11, 1));
		items.add(StarAbilityCreator.addAbility2( new ItemStack(Items.DIAMOND_CHESTPLATE), 11, 1));
		items.add(StarAbilityCreator.addAbility2( new ItemStack(Items.DIAMOND_LEGGINGS), 11, 1));
		items.add(StarAbilityCreator.addAbility2( new ItemStack(Items.DIAMOND_BOOTS), 11, 1));
		items.add(StarAbilityCreator.addAbility2( new ItemStack(Items.BREAD), 16, 4));
		items.add(StarAbilityCreator.addAbility2(StarAbilityCreator.addAbility2(new ItemStack(Items.BREAD), 16, 4), 16, 3));
		items.add(StarAbilityCreator.addAbility2( new ItemStack(Items.BREAD), 17, 1));

		addItem = new ItemStack(Blocks.PLANKS);
		addItem = StarAbilityCreator.addAbility2(addItem, 1, 1);
		addItem = StarAbilityCreator.addAbility2(addItem, 2, 1);
		addItem = StarAbilityCreator.addAbility2(addItem, 3, 1);
		items.add(addItem);
		addItem = new ItemStack(Blocks.PLANKS);
		addItem = StarAbilityCreator.addAbility2(addItem, 1, 2);
		addItem = StarAbilityCreator.addAbility2(addItem, 2, 3);
		addItem = StarAbilityCreator.addAbility2(addItem, 3, 3);
		items.add(addItem);
		addItem = new ItemStack(Blocks.PLANKS);
		addItem = StarAbilityCreator.addAbility2(addItem, 4, 1);
		addItem = StarAbilityCreator.addAbility2(addItem, 5, 1);
		addItem = StarAbilityCreator.addAbility2(addItem, 6, 1);
		items.add(addItem);
		addItem = new ItemStack(Blocks.PLANKS);
		addItem = StarAbilityCreator.addAbility2(addItem, 17, 1);
		addItem = StarAbilityCreator.addAbility2(addItem, 18, 1);
		addItem = StarAbilityCreator.addAbility2(addItem, 19, 1);
		items.add(addItem);
		
		addItem = new ItemStack(Items.SIGN);
		addItem = StarPropertyCreator.setMemory(addItem, 999);
		items.add(addItem);
		addItem = new ItemStack(Items.CAULDRON);
		addItem = StarPropertyCreator.setMemory(addItem, 999);
		items.add(addItem);
		addItem = new ItemStack(Items.IRON_INGOT);
		addItem = StarPropertyCreator.setMemory(addItem, 999);
		items.add(addItem);
		addItem = new ItemStack(Items.GOLD_INGOT);
		addItem = StarPropertyCreator.setMemory(addItem, 999);
		items.add(addItem);
		addItem = new ItemStack(Items.DIAMOND);
		addItem = StarPropertyCreator.setMemory(addItem, 999);
		items.add(addItem);
		addItem = new ItemStack(Blocks.PLANKS, 1, 0);
		addItem = StarPropertyCreator.setMemory(addItem, 999);
		items.add(addItem);
		
	}
	
}