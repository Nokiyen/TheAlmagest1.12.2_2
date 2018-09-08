package noki.almagest.helper;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import noki.almagest.AlmagestCore;
import noki.almagest.ability.StarPropertyCreator.ItemStarLine;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.registry.ModItems;


/**********
 * @class HelperReward
 *
 * @description
 * 
 */
public class HelperReward {
	
	//******************************//
	// define member variables.
	//******************************//
	public static Map<Integer, ItemStack> rewards;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public static void registerRewards() {
		
		rewards = new HashMap<Integer, ItemStack>();
		
		addReward(1, new ItemStack(Items.IRON_INGOT, 8));
		addReward(2, new ItemStack(Items.COAL, 32));
		addReward(3, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(13, 2).getStack());
		addReward(4, new ItemStack(Blocks.LOG, 64));
		addReward(5, new ItemStack(Items.DIAMOND, 4));
		addReward(6, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(4, 2).getStack());
		addReward(7, new ItemStack(Items.IRON_INGOT, 8));
		addReward(8, new ItemStack(Blocks.FARMLAND, 64));
		addReward(9, new HelperStarStack(new ItemStack(ModItems.STARDUST, 16)).addMemory(120).addLine(ItemStarLine.TOP).addLine(ItemStarLine.LEFT).addLine(ItemStarLine.BOTTOM).addLine(ItemStarLine.RIGHT).addAttribute(EStarAttribute.MINERAL, 100).getStack());
		addReward(10, new ItemStack(Items.IRON_INGOT, 16));
		addReward(11, new ItemStack(Items.REDSTONE, 32));
		addReward(12, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(12, 2).getStack());
		addReward(13, new ItemStack(Blocks.GLASS, 64));
		addReward(14, new ItemStack(Items.DIAMOND, 8));
		addReward(15, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(16, 2).getStack());
		addReward(16, new ItemStack(Items.IRON_INGOT, 16));
		addReward(17, new ItemStack(Blocks.TALLGRASS, 64));
		addReward(18, new HelperStarStack(new ItemStack(ModItems.STARDUST, 16)).addMemory(120).addLine(ItemStarLine.TOP).addLine(ItemStarLine.LEFT).addLine(ItemStarLine.BOTTOM).addLine(ItemStarLine.RIGHT).addAttribute(EStarAttribute.WOOD, 100).getStack());
		addReward(19, new ItemStack(Items.IRON_INGOT, 32));
		addReward(20, new ItemStack(Items.GOLD_INGOT, 32));
		addReward(21, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(5, 2).getStack());
		addReward(22, new ItemStack(Blocks.BOOKSHELF, 64));
		addReward(23, new ItemStack(Items.DIAMOND, 16));
		addReward(24, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(2, 2).getStack());
		addReward(25, new ItemStack(Items.IRON_INGOT, 32));
		addReward(26, new ItemStack(Blocks.GRASS_PATH, 64));
		addReward(27, new HelperStarStack(new ItemStack(ModItems.STARDUST, 16)).addMemory(120).addLine(ItemStarLine.TOP).addLine(ItemStarLine.LEFT).addLine(ItemStarLine.BOTTOM).addLine(ItemStarLine.RIGHT).addAttribute(EStarAttribute.LIQUID, 100).getStack());
		addReward(28, new ItemStack(Items.IRON_INGOT, 64));
		addReward(29, new ItemStack(Items.EMERALD, 64));
		addReward(30, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(15, 2).getStack());
		addReward(31, new ItemStack(Blocks.GLOWSTONE, 64));
		addReward(32, new ItemStack(Items.DIAMOND, 32));
		addReward(33, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(9, 1).getStack());
		addReward(34, new ItemStack(Items.IRON_INGOT, 64));
		addReward(35, new ItemStack(Blocks.MONSTER_EGG, 64));
		addReward(36, new HelperStarStack(new ItemStack(ModItems.STARDUST, 16)).addMemory(120).addLine(ItemStarLine.TOP).addLine(ItemStarLine.LEFT).addLine(ItemStarLine.BOTTOM).addLine(ItemStarLine.RIGHT).addAttribute(EStarAttribute.PAPER, 100).addAttribute(EStarAttribute.FOOD, 100).getStack());
		addReward(37, new ItemStack(Blocks.IRON_BLOCK, 8));
		addReward(38, new ItemStack(Blocks.LAPIS_BLOCK, 16));
		addReward(39, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(6, 2).addAbility(1, 2).getStack());
		addReward(40, new ItemStack(Blocks.BONE_BLOCK, 64));
		addReward(41, new ItemStack(Items.DIAMOND, 64));
		addReward(42, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(8, 2).addAbility(7, 2).getStack());
		addReward(43, new ItemStack(Blocks.IRON_BLOCK, 8));
		addReward(44, new ItemStack(Items.SKULL, 1, 3));
		addReward(45, new HelperStarStack(new ItemStack(ModItems.STARDUST, 16)).addMemory(120).addLine(ItemStarLine.TOP).addLine(ItemStarLine.LEFT).addLine(ItemStarLine.BOTTOM).addLine(ItemStarLine.RIGHT).addAttribute(EStarAttribute.PLANT, 100).addAttribute(EStarAttribute.FUEL, 100).getStack());
		addReward(46, new ItemStack(Blocks.IRON_BLOCK, 12));
		addReward(47, new ItemStack(Blocks.GOLD_BLOCK, 12));
		addReward(48, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(14, 2).addAbility(9, 2).getStack());
		addReward(49, new ItemStack(Items.PRISMARINE_SHARD, 64));
		addReward(100, new ItemStack(Blocks.DIAMOND_BLOCK, 8));
		addReward(51, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(9, 3).addAbility(3, 2).getStack());
		addReward(52, new ItemStack(Blocks.IRON_BLOCK, 12));
		addReward(53, new ItemStack(Blocks.BEDROCK, 64));
		addReward(54, new HelperStarStack(new ItemStack(ModItems.STARDUST, 16)).addMemory(120).addLine(ItemStarLine.TOP).addLine(ItemStarLine.LEFT).addLine(ItemStarLine.BOTTOM).addLine(ItemStarLine.RIGHT).addAttribute(EStarAttribute.METAL, 100).addAttribute(EStarAttribute.TOOL, 100).getStack());
		addReward(55, new ItemStack(Blocks.IRON_BLOCK, 16));
		addReward(56, new ItemStack(Blocks.EMERALD_BLOCK, 16));
		addReward(57, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(13, 3).addAbility(16, 3).getStack());
		addReward(58, new ItemStack(Blocks.PACKED_ICE, 64));
		addReward(59, new ItemStack(Blocks.DIAMOND_BLOCK, 12));
		addReward(60, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(11, 1).addAbility(12, 3).getStack());
		addReward(61, new ItemStack(Blocks.IRON_BLOCK, 16));
		addReward(62, new ItemStack(Blocks.END_PORTAL_FRAME, 11));
		addReward(63, new HelperStarStack(new ItemStack(ModItems.STARDUST, 16)).addMemory(120).addLine(ItemStarLine.TOP).addLine(ItemStarLine.LEFT).addLine(ItemStarLine.BOTTOM).addLine(ItemStarLine.RIGHT).addAttribute(EStarAttribute.ANIMAL, 100).addAttribute(EStarAttribute.MONSTER, 100).getStack());
		addReward(64, new ItemStack(Blocks.IRON_BLOCK, 20));
		addReward(65, new ItemStack(Blocks.LAPIS_BLOCK, 20));
		addReward(66, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(3, 3).addAbility(4, 3).getStack());
		addReward(67, new ItemStack(Blocks.SEA_LANTERN, 64));
		addReward(68, new ItemStack(Blocks.DIAMOND_BLOCK, 16));
		addReward(69, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(14, 3).addAbility(15, 3).getStack());
		addReward(70, new ItemStack(Blocks.IRON_BLOCK, 20));
		addReward(71, new ItemStack(Blocks.END_GATEWAY, 1));
		addReward(72, new HelperStarStack(new ItemStack(ModItems.STARDUST, 16)).addMemory(120).addLine(ItemStarLine.TOP).addLine(ItemStarLine.LEFT).addLine(ItemStarLine.BOTTOM).addLine(ItemStarLine.RIGHT).addAttribute(EStarAttribute.MACHINE, 100).addAttribute(EStarAttribute.EXPLOSIVE, 100).getStack());
		addReward(73, new ItemStack(Blocks.IRON_BLOCK, 24));
		addReward(74, new ItemStack(Blocks.GOLD_BLOCK, 24));
		addReward(75, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(7, 3).addAbility(8, 3).getStack());
		addReward(76, new ItemStack(Items.NETHER_STAR, 64));
		addReward(77, new ItemStack(Blocks.DIAMOND_BLOCK, 32));
		addReward(78, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(5, 3).addAbility(6, 3).getStack());
		addReward(79, new ItemStack(Blocks.IRON_BLOCK, 32));
		addReward(80, new ItemStack(Blocks.MOB_SPAWNER, 64));
		addReward(81, new HelperStarStack(new ItemStack(ModItems.STARDUST, 16)).addMemory(120).addLine(ItemStarLine.TOP).addLine(ItemStarLine.LEFT).addLine(ItemStarLine.BOTTOM).addLine(ItemStarLine.RIGHT).addAttribute(EStarAttribute.JEWEL, 100).addAttribute(EStarAttribute.DECORATIVE, 100).getStack());
		addReward(82, new ItemStack(Blocks.IRON_BLOCK, 32));
		addReward(83, new ItemStack(Blocks.BARRIER, 64));
		addReward(84, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(100).addAbility(1, 3).addAbility(2, 3).getStack());
		addReward(85, new ItemStack(Items.SKULL, 1, 5));
		addReward(86, new ItemStack(Blocks.DIAMOND_BLOCK, 64));
		addReward(87, new HelperStarStack(new ItemStack(ModItems.STARDUST, 16)).addMemory(999).addLine(ItemStarLine.TOP).addLine(ItemStarLine.LEFT).addLine(ItemStarLine.BOTTOM).addLine(ItemStarLine.RIGHT).addAttribute(EStarAttribute.STAR, 100).getStack());
		addReward(88, new HelperStarStack(new ItemStack(ModItems.STARDUST, 1)).addMemory(999).addAbility(9, 5).addAbility(9, 6).addAbility(9, 7).getStack());
		
	}
	
	private static void addReward(int id, ItemStack stack) {
		
		rewards.put(id, stack);
		
	}
	
	public static ItemStack getReward(int id) {
		
		return rewards.get(id);
		
	}
	
	public static ItemStack getCurrentReward() {
		
		return getReward(AlmagestCore.savedDataManager.getFlagData().countPresentedConstellation());
		
	}
	
}
