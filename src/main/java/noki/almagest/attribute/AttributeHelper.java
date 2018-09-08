package noki.almagest.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import noki.almagest.AlmagestCore;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.ability.StarPropertyCreator.ItemStarLine;
import noki.almagest.event.post.AttributeLevelEvent;
import noki.almagest.helper.HelperNBTStack;


/**********
 * @class AttributeHelper
 *
 * @description star attributeを取り出すためのクラスです。
 * バニラ等のブロック・アイテムのattributeも管理します。
 */
public class AttributeHelper {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private static Map<ItemSet, InfoSet> vanillaAttributes = new HashMap<ItemSet, InfoSet>();
	private static final String NBT_attributeName = "attribute_";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public static void register() {
		
		register(Blocks.STONE, 3, make().set(EStarAttribute.MINERAL, 40).set(50).set(ItemStarLine.TOP).set(2,1));
		register(Blocks.STONE, 1, make().set(EStarAttribute.MINERAL, 40).set(70).set(ItemStarLine.TOP).set(3,1));
		register(Blocks.STONE, 0, make().set(EStarAttribute.MINERAL, 30).set(50).set(ItemStarLine.TOP).set(4,1));
		register(Blocks.STONE, 5, make().set(EStarAttribute.MINERAL, 40).set(60).set(ItemStarLine.BOTTOM).set(18,1));
		register(Blocks.STONE, 2, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.STONE, 4, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.STONE, 6, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.GRASS, make().set(EStarAttribute.MINERAL, 20).set(EStarAttribute.PLANT, 20).set(50).set(ItemStarLine.LEFT).set(8,1));
		register(Blocks.DIRT, make().set(EStarAttribute.MINERAL, 10).set(40).set(ItemStarLine.LEFT).set(12,1));
		register(Blocks.COBBLESTONE, make().set(EStarAttribute.MINERAL, 10).set(40).set(ItemStarLine.RIGHT).set(5,1));
		register(Blocks.PLANKS, make().set(EStarAttribute.WOOD, 20));
		register(Blocks.SAPLING, make().set(EStarAttribute.PLANT, 10).set(75).set(ItemStarLine.RIGHT).set(ItemStarLine.LEFT).set(19,3));
		register(Blocks.BEDROCK, make().set(EStarAttribute.MINERAL, 100));
		register(Blocks.FLOWING_WATER, make().set(EStarAttribute.LIQUID, 20));
		register(Blocks.WATER, make().set(EStarAttribute.LIQUID, 20));
		register(Blocks.FLOWING_LAVA, make().set(EStarAttribute.LIQUID, 30));
		register(Blocks.LAVA, make().set(EStarAttribute.LIQUID, 30));
		register(Blocks.SAND, make().set(EStarAttribute.MINERAL, 30).set(45).set(ItemStarLine.RIGHT).set(3,1));
		register(Blocks.GRAVEL, make().set(EStarAttribute.MINERAL, 30).set(60).set(ItemStarLine.TOP).set(2,2));
		register(Blocks.GOLD_ORE, make().set(EStarAttribute.MINERAL, 40).set(70).set(ItemStarLine.TOP).set(ItemStarLine.BOTTOM).set(7,3));
		register(Blocks.IRON_ORE, make().set(EStarAttribute.MINERAL, 40).set(75).set(ItemStarLine.TOP).set(ItemStarLine.BOTTOM).set(5,3));
		register(Blocks.COAL_ORE, make().set(EStarAttribute.MINERAL, 30).set(65).set(ItemStarLine.RIGHT).set(ItemStarLine.BOTTOM).set(12,3));
		register(Blocks.LOG, make().set(EStarAttribute.WOOD, 30).set(50).set(ItemStarLine.LEFT).set(13,1));
		register(Blocks.LOG2, make().set(EStarAttribute.WOOD, 40).set(50).set(ItemStarLine.LEFT).set(13,1));
		register(Blocks.LEAVES, make().set(EStarAttribute.PLANT, 20).set(50).set(ItemStarLine.BOTTOM).set(15,1));
		register(Blocks.LEAVES2, make().set(EStarAttribute.PLANT, 20).set(50).set(ItemStarLine.BOTTOM).set(15,1));
		register(Blocks.SPONGE, make().set(EStarAttribute.PLANT, 20).set(EStarAttribute.ANIMAL, 20).set(75).set(ItemStarLine.TOP).set(ItemStarLine.RIGHT).set(ItemStarLine.LEFT));
		register(Blocks.GLASS, make().set(EStarAttribute.MINERAL, 40));
		register(Blocks.LAPIS_ORE, make().set(EStarAttribute.MINERAL, 40));
		register(Blocks.LAPIS_BLOCK, make().set(EStarAttribute.MINERAL, 60).set(70).set(ItemStarLine.RIGHT).set(ItemStarLine.LEFT).set(9,2));
		register(Blocks.DISPENSER, make().set(EStarAttribute.MACHINE, 40));
		register(Blocks.SANDSTONE, make().set(EStarAttribute.MINERAL, 40).set(55).set(ItemStarLine.TOP).set(19,1));
		register(Blocks.NOTEBLOCK, make().set(EStarAttribute.MACHINE, 40));
		register(Blocks.BED, make().set(EStarAttribute.TOOL, 20));
		register(Blocks.GOLDEN_RAIL, make().set(EStarAttribute.MACHINE, 40));
		register(Blocks.DETECTOR_RAIL, make().set(EStarAttribute.MACHINE, 40));
		register(Blocks.STICKY_PISTON, make().set(EStarAttribute.MACHINE, 40));
		register(Blocks.WEB, make().set(EStarAttribute.MONSTER, 30).set(75).set(ItemStarLine.TOP).set(ItemStarLine.RIGHT).set(19,3));
		register(Blocks.TALLGRASS, make().set(EStarAttribute.PLANT, 10).set(60).set(ItemStarLine.BOTTOM).set(6,2));
		register(Blocks.DEADBUSH, make().set(EStarAttribute.PLANT, 10).set(60).set(ItemStarLine.BOTTOM).set(7,2));
		register(Blocks.PISTON, make().set(EStarAttribute.MACHINE, 40));
		register(Blocks.PISTON_HEAD, make().set(EStarAttribute.MACHINE, 40));
		register(Blocks.WOOL, make().set(EStarAttribute.ANIMAL, 20).set(60).set(ItemStarLine.TOP).set(1,1));
		register(Blocks.PISTON_EXTENSION, make().set(EStarAttribute.MACHINE, 40));
		register(Blocks.YELLOW_FLOWER, make().set(EStarAttribute.PLANT, 10).set(60).set(ItemStarLine.LEFT).set(16,1));
		register(Blocks.RED_FLOWER, make().set(EStarAttribute.PLANT, 10).set(65).set(ItemStarLine.TOP).set(14,1));
		register(Blocks.BROWN_MUSHROOM, make().set(EStarAttribute.PLANT, 10).set(EStarAttribute.ANIMAL, 10).set(60).set(ItemStarLine.TOP).set(13,2));
		register(Blocks.RED_MUSHROOM, make().set(EStarAttribute.PLANT, 10).set(EStarAttribute.ANIMAL, 10).set(70).set(ItemStarLine.BOTTOM).set(18,1));
		register(Blocks.GOLD_BLOCK, make().set(EStarAttribute.MINERAL, 60));
		register(Blocks.IRON_BLOCK, make().set(EStarAttribute.MINERAL, 60));
		register(Blocks.DOUBLE_STONE_SLAB, make().set(EStarAttribute.DECORATIVE, 20).set(EStarAttribute.MINERAL, 20));
		register(Blocks.STONE_SLAB, make().set(EStarAttribute.DECORATIVE, 20).set(EStarAttribute.MINERAL, 20));
		register(Blocks.BRICK_BLOCK, make().set(EStarAttribute.DECORATIVE, 20).set(EStarAttribute.MINERAL, 20));
		register(Blocks.TNT, make().set(EStarAttribute.MINERAL, 20).set(EStarAttribute.EXPLOSIVE, 50));
		register(Blocks.BOOKSHELF, make().set(EStarAttribute.TOOL, 20).set(EStarAttribute.WOOD, 10));
		register(Blocks.MOSSY_COBBLESTONE, make().set(EStarAttribute.MINERAL, 20).set(EStarAttribute.PLANT, 20).set(70).set(ItemStarLine.TOP).set(ItemStarLine.LEFT).set(11,1));
		register(Blocks.OBSIDIAN, make().set(EStarAttribute.MINERAL, 70).set(70).set(ItemStarLine.TOP).set(ItemStarLine.RIGHT).set(15,3));
		register(Blocks.TORCH, make().set(EStarAttribute.TOOL, 10));
		//skip FIRE
		register(Blocks.MOB_SPAWNER, make().set(EStarAttribute.MONSTER, 50));
		register(Blocks.OAK_STAIRS, make().set(EStarAttribute.WOOD, 20));
		register(Blocks.CHEST, make().set(EStarAttribute.WOOD, 20));
		register(Blocks.REDSTONE_WIRE, make().set(EStarAttribute.MACHINE, 10));
		register(Blocks.DIAMOND_ORE, make().set(EStarAttribute.JEWEL, 50).set(80).set(ItemStarLine.TOP).set(ItemStarLine.RIGHT).set(ItemStarLine.BOTTOM));
		register(Blocks.DIAMOND_BLOCK, make().set(EStarAttribute.JEWEL, 80));
		register(Blocks.CRAFTING_TABLE, make().set(EStarAttribute.TOOL, 10).set(EStarAttribute.WOOD, 10));
		register(Blocks.WHEAT, make().set(EStarAttribute.PLANT, 10).set(60).set(ItemStarLine.BOTTOM).set(16,2));
		register(Blocks.FARMLAND, make().set(EStarAttribute.MINERAL, 30));
		register(Blocks.FURNACE, make().set(EStarAttribute.MINERAL, 30).set(EStarAttribute.TOOL, 20));
		//skip LIT_FURNACE
		register(Blocks.STANDING_SIGN, make().set(EStarAttribute.DECORATIVE, 20).set(EStarAttribute.WOOD, 10));
		register(Blocks.OAK_DOOR, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 10));
		register(Blocks.SPRUCE_DOOR, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 10));
		register(Blocks.BIRCH_DOOR, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 10));
		register(Blocks.JUNGLE_DOOR, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 10));
		register(Blocks.ACACIA_DOOR, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 10));
		register(Blocks.DARK_OAK_DOOR, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 10));
		register(Blocks.LADDER, make().set(EStarAttribute.DECORATIVE, 20).set(EStarAttribute.WOOD, 10));
		register(Blocks.RAIL, make().set(EStarAttribute.MACHINE, 30));
		register(Blocks.STONE_STAIRS, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.MINERAL, 10));
		//skip WALL_SIGN
		register(Blocks.LEVER, make().set(EStarAttribute.MACHINE, 20));
		register(Blocks.STONE_PRESSURE_PLATE, make().set(EStarAttribute.MACHINE, 20).set(EStarAttribute.MINERAL, 10));
		register(Blocks.IRON_DOOR, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.METAL, 20));
		register(Blocks.WOODEN_PRESSURE_PLATE, make().set(EStarAttribute.MACHINE, 20).set(EStarAttribute.WOOD, 10));
		register(Blocks.REDSTONE_ORE, make().set(EStarAttribute.MINERAL, 40));
		//skip LIT_REDSTONE_ORE
		//skip UNLIT_REDSTONE_TORCH
		register(Blocks.REDSTONE_TORCH, make().set(EStarAttribute.MACHINE, 20));
		register(Blocks.STONE_BUTTON, make().set(EStarAttribute.MACHINE, 10).set(EStarAttribute.MINERAL, 20));
		//skip SNOW_LAYER
		register(Blocks.ICE, make().set(EStarAttribute.LIQUID, 30).set(65).set(ItemStarLine.BOTTOM).set(5,2));
		register(Blocks.SNOW, make().set(EStarAttribute.LIQUID, 30));
		register(Blocks.CACTUS, make().set(EStarAttribute.PLANT, 10).set(60).set(ItemStarLine.LEFT).set(9,4));
		register(Blocks.CLAY, make().set(EStarAttribute.MINERAL, 30).set(65).set(ItemStarLine.LEFT).set(13,2));
		register(Blocks.REEDS, make().set(EStarAttribute.PLANT, 10).set(50).set(ItemStarLine.BOTTOM).set(15,1));
		register(Blocks.JUKEBOX, make().set(EStarAttribute.MACHINE, 40));
		register(Blocks.OAK_FENCE, make().set(EStarAttribute.WOOD, 10));
		register(Blocks.SPRUCE_FENCE, make().set(EStarAttribute.WOOD, 10));
		register(Blocks.BIRCH_FENCE, make().set(EStarAttribute.WOOD, 10));
		register(Blocks.JUNGLE_FENCE, make().set(EStarAttribute.WOOD, 10));
		register(Blocks.DARK_OAK_FENCE, make().set(EStarAttribute.WOOD, 10));
		register(Blocks.ACACIA_FENCE, make().set(EStarAttribute.WOOD, 10));
		register(Blocks.PUMPKIN, make().set(EStarAttribute.PLANT, 10).set(70).set(ItemStarLine.BOTTOM).set(4,2));
		register(Blocks.NETHERRACK, make().set(EStarAttribute.MINERAL, 10).set(45).set(ItemStarLine.LEFT).set(6,1));
		register(Blocks.SOUL_SAND, make().set(EStarAttribute.MINERAL, 30).set(55).set(ItemStarLine.BOTTOM).set(5,1));
		register(Blocks.GLOWSTONE, make().set(EStarAttribute.MINERAL, 50).set(65).set(ItemStarLine.TOP).set(ItemStarLine.BOTTOM).set(14,3));
		//skip PORTAL
		register(Blocks.LIT_PUMPKIN, make().set(EStarAttribute.PLANT, 10));
		//skip CAKE
		register(Blocks.UNPOWERED_REPEATER, make().set(EStarAttribute.MACHINE, 40));
		//skip POWERED_REPEATER
		register(Blocks.TRAPDOOR, make().set(EStarAttribute.MACHINE, 30).set(EStarAttribute.WOOD, 20));
		register(Blocks.MONSTER_EGG, make().set(EStarAttribute.MINERAL, 30).set(EStarAttribute.MONSTER, 30));
		register(Blocks.STONEBRICK, make().set(EStarAttribute.MINERAL, 40).set(75).set(ItemStarLine.RIGHT).set(ItemStarLine.LEFT).set(20,2));
		register(Blocks.BROWN_MUSHROOM_BLOCK, make().set(EStarAttribute.PLANT, 10).set(EStarAttribute.ANIMAL, 10).set(60).set(ItemStarLine.TOP).set(ItemStarLine.RIGHT).set(8,3));
		register(Blocks.RED_MUSHROOM_BLOCK, make().set(EStarAttribute.PLANT, 10).set(EStarAttribute.ANIMAL, 10).set(80).set(ItemStarLine.RIGHT).set(ItemStarLine.BOTTOM).set(2,3));
		register(Blocks.IRON_BARS, make().set(EStarAttribute.DECORATIVE, 20).set(EStarAttribute.METAL, 50));
		register(Blocks.GLASS_PANE, make().set(EStarAttribute.DECORATIVE, 20).set(EStarAttribute.MINERAL, 40));
		register(Blocks.MELON_BLOCK, make().set(EStarAttribute.PLANT, 20).set(60).set(ItemStarLine.TOP).set(17,1));
		//skip PUMPKIN_STEM
		//skip MELON_STEM
		register(Blocks.VINE, make().set(EStarAttribute.PLANT, 10).set(65).set(ItemStarLine.LEFT).set(4,2));
		register(Blocks.OAK_FENCE_GATE, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 10));
		register(Blocks.SPRUCE_FENCE_GATE, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 10));
		register(Blocks.BIRCH_FENCE_GATE, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 10));
		register(Blocks.JUNGLE_FENCE_GATE, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 10));
		register(Blocks.DARK_OAK_FENCE_GATE, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 10));
		register(Blocks.ACACIA_FENCE_GATE, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 10));
		register(Blocks.BRICK_STAIRS, make().set(EStarAttribute.DECORATIVE, 20).set(EStarAttribute.MINERAL, 40));
		register(Blocks.STONE_BRICK_STAIRS, make().set(EStarAttribute.DECORATIVE, 20).set(EStarAttribute.MINERAL, 30));
		register(Blocks.MYCELIUM, make().set(EStarAttribute.MINERAL, 20).set(EStarAttribute.PLANT, 20).set(EStarAttribute.ANIMAL, 20).set(60).set(ItemStarLine.LEFT).set(19,2));
		register(Blocks.WATERLILY, make().set(EStarAttribute.PLANT, 20).set(60).set(ItemStarLine.LEFT).set(16,1));
		register(Blocks.NETHER_BRICK, make().set(EStarAttribute.MINERAL, 40));
		register(Blocks.NETHER_BRICK_FENCE, make().set(EStarAttribute.DECORATIVE, 20).set(EStarAttribute.MINERAL, 40));
		register(Blocks.NETHER_BRICK_STAIRS, make().set(EStarAttribute.DECORATIVE, 20).set(EStarAttribute.MINERAL, 40));
		register(Blocks.NETHER_WART, make().set(EStarAttribute.PLANT, 20).set(55).set(ItemStarLine.RIGHT).set(15,2));
		register(Blocks.ENCHANTING_TABLE, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.BREWING_STAND, make().set(EStarAttribute.TOOL, 20));
		register(Blocks.CAULDRON, make().set(EStarAttribute.METAL, 50).set(EStarAttribute.TOOL, 50));
		//skip END_PORTAL
		//skip END_PORTAL_FRAME
		register(Blocks.END_STONE, make().set(EStarAttribute.MINERAL, 40).set(50).set(ItemStarLine.BOTTOM).set(4,1));
		register(Blocks.DRAGON_EGG, make().set(EStarAttribute.MONSTER, 100).set(90).set(ItemStarLine.TOP).set(ItemStarLine.RIGHT).set(ItemStarLine.BOTTOM).set(9,7));
		register(Blocks.REDSTONE_LAMP, make().set(EStarAttribute.MACHINE, 30));
		//skip LIT_REDSTONE_LAMP
		register(Blocks.DOUBLE_WOODEN_SLAB, make().set(EStarAttribute.WOOD, 10));
		register(Blocks.WOODEN_SLAB, make().set(EStarAttribute.WOOD, 10));
		register(Blocks.COCOA, make().set(EStarAttribute.PLANT, 20).set(65).set(ItemStarLine.BOTTOM).set(ItemStarLine.LEFT).set(5,3));
		register(Blocks.SANDSTONE_STAIRS, make().set(EStarAttribute.DECORATIVE, 20).set(EStarAttribute.MINERAL, 30));
		register(Blocks.EMERALD_ORE, make().set(EStarAttribute.MINERAL, 50).set(80).set(ItemStarLine.TOP).set(ItemStarLine.RIGHT).set(ItemStarLine.BOTTOM));
		register(Blocks.ENDER_CHEST, make().set(EStarAttribute.TOOL, 10));
		register(Blocks.TRIPWIRE_HOOK, make().set(EStarAttribute.MACHINE, 30));
		//skip TRIPWIRE
		register(Blocks.EMERALD_BLOCK, make().set(EStarAttribute.MINERAL, 70));
		register(Blocks.SPRUCE_STAIRS, make().set(EStarAttribute.WOOD, 10));
		register(Blocks.BIRCH_STAIRS, make().set(EStarAttribute.WOOD, 10));
		register(Blocks.JUNGLE_STAIRS, make().set(EStarAttribute.WOOD, 10));
		//skip COMMAND_BLOCK
		register(Blocks.BEACON, make().set(EStarAttribute.TOOL, 10));
		register(Blocks.COBBLESTONE_WALL, make().set(EStarAttribute.DECORATIVE, 20).set(EStarAttribute.MINERAL, 30));
		//skip FLOWER_POT
		//skip CARROTS
		//skip POTATOES
		register(Blocks.WOODEN_BUTTON, make().set(EStarAttribute.MACHINE, 10).set(EStarAttribute.WOOD, 20));
		register(Blocks.SKULL, make().set(EStarAttribute.ANIMAL, 100).set(80).set(ItemStarLine.RIGHT).set(ItemStarLine.BOTTOM).set(ItemStarLine.LEFT));
		register(Blocks.ANVIL, make().set(EStarAttribute.METAL, 50).set(EStarAttribute.TOOL, 40));
		register(Blocks.TRAPPED_CHEST, make().set(EStarAttribute.TOOL, 10));
		register(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, make().set(EStarAttribute.MACHINE, 10).set(EStarAttribute.WOOD, 20));
		register(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, make().set(EStarAttribute.MACHINE, 10).set(EStarAttribute.MINERAL, 20));
		register(Blocks.UNPOWERED_COMPARATOR, make().set(EStarAttribute.MACHINE, 40));
		//skip POWERED_COMPARATOR
		register(Blocks.DAYLIGHT_DETECTOR, make().set(EStarAttribute.MACHINE, 40));
		//skip DAYLIGHT_DETECTOR_INVERTED
		register(Blocks.REDSTONE_BLOCK, make().set(EStarAttribute.MINERAL, 30));
		register(Blocks.QUARTZ_ORE, make().set(EStarAttribute.JEWEL, 40).set(70).set(ItemStarLine.RIGHT).set(ItemStarLine.LEFT).set(14,3));
		register(Blocks.HOPPER, make().set(EStarAttribute.MACHINE, 40));
		register(Blocks.QUARTZ_BLOCK, make().set(EStarAttribute.JEWEL, 50));
		register(Blocks.QUARTZ_STAIRS, make().set(EStarAttribute.DECORATIVE, 20).set(EStarAttribute.JEWEL, 50));
		register(Blocks.ACTIVATOR_RAIL, make().set(EStarAttribute.MACHINE, 40));
		register(Blocks.DROPPER, make().set(EStarAttribute.MACHINE, 40));
		register(Blocks.STAINED_HARDENED_CLAY, make().set(EStarAttribute.MINERAL, 40));
		register(Blocks.BARRIER, make().set(EStarAttribute.MACHINE, 100));
		register(Blocks.IRON_TRAPDOOR, make().set(EStarAttribute.MACHINE, 30).set(EStarAttribute.METAL, 30));
		register(Blocks.HAY_BLOCK, make().set(EStarAttribute.PLANT, 10));
		register(Blocks.CARPET, make().set(EStarAttribute.DECORATIVE, 30));
		register(Blocks.HARDENED_CLAY, make().set(EStarAttribute.MINERAL, 40));
		register(Blocks.COAL_BLOCK, make().set(EStarAttribute.MINERAL, 30).set(EStarAttribute.FUEL, 30));
		register(Blocks.PACKED_ICE, make().set(EStarAttribute.LIQUID, 50).set(75).set(ItemStarLine.TOP).set(ItemStarLine.BOTTOM).set(ItemStarLine.LEFT));
		register(Blocks.ACACIA_STAIRS, make().set(EStarAttribute.WOOD, 20));
		register(Blocks.DARK_OAK_STAIRS, make().set(EStarAttribute.WOOD, 20));
		register(Blocks.SLIME_BLOCK, make().set(EStarAttribute.MONSTER, 30));
		register(Blocks.DOUBLE_PLANT, make().set(EStarAttribute.PLANT, 30).set(60).set(ItemStarLine.BOTTOM).set(7,2));
		register(Blocks.STAINED_GLASS, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.STAINED_GLASS_PANE, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 30));
		register(Blocks.PRISMARINE, make().set(EStarAttribute.MINERAL, 20).set(EStarAttribute.ANIMAL, 20).set(EStarAttribute.LIQUID, 20).set(75).set(ItemStarLine.TOP).set(ItemStarLine.BOTTOM).set(18,3));
		register(Blocks.SEA_LANTERN, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.LIQUID, 40).set(65).set(ItemStarLine.TOP).set(ItemStarLine.LEFT).set(7,3));
		register(Blocks.STANDING_BANNER, make().set(EStarAttribute.DECORATIVE, 30));
		//skip WALL_BANNER
		register(Blocks.RED_SANDSTONE, make().set(EStarAttribute.MINERAL, 30).set(55).set(ItemStarLine.LEFT).set(16,2));
		register(Blocks.RED_SANDSTONE_STAIRS, make().set(EStarAttribute.MINERAL, 30).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.DOUBLE_STONE_SLAB2, make().set(EStarAttribute.MINERAL, 20).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.STONE_SLAB2, make().set(EStarAttribute.MINERAL, 20).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.END_ROD, make().set(EStarAttribute.MINERAL, 50).set(70).set(ItemStarLine.RIGHT).set(ItemStarLine.BOTTOM).set(15,3));
		register(Blocks.CHORUS_PLANT, make().set(EStarAttribute.PLANT, 30).set(65).set(ItemStarLine.TOP).set(ItemStarLine.BOTTOM).set(1,3));
		register(Blocks.CHORUS_FLOWER, make().set(EStarAttribute.PLANT, 30).set(70).set(ItemStarLine.BOTTOM).set(ItemStarLine.LEFT).set(20,2));
		register(Blocks.PURPUR_BLOCK, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.PLANT, 40));
		register(Blocks.PURPUR_PILLAR, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.PLANT, 40));
		register(Blocks.PURPUR_STAIRS, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.PLANT, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.PURPUR_DOUBLE_SLAB, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.PLANT, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.PURPUR_SLAB, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.PLANT, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.END_BRICKS, make().set(EStarAttribute.MINERAL, 50));
		register(Blocks.BEETROOTS, make().set(EStarAttribute.PLANT, 30).set(55).set(ItemStarLine.TOP).set(5,2));
		register(Blocks.GRASS_PATH, make().set(EStarAttribute.MINERAL, 40));
		//skip END_GATEWAY
		//skip REPEATING_COMMAND_BLOCK
		//skip CHAIN_COMMAND_BLOCK
		register(Blocks.FROSTED_ICE, make().set(EStarAttribute.LIQUID, 30));
		register(Blocks.MAGMA, make().set(EStarAttribute.MINERAL, 30).set(EStarAttribute.LIQUID, 30));
		register(Blocks.NETHER_WART_BLOCK, make().set(EStarAttribute.PLANT, 30));
		register(Blocks.RED_NETHER_BRICK, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.BONE_BLOCK, make().set(EStarAttribute.MONSTER, 30));
		register(Blocks.STRUCTURE_VOID, make().set(EStarAttribute.MACHINE, 100));
		register(Blocks.OBSERVER, make().set(EStarAttribute.MACHINE, 40));
		register(Blocks.WHITE_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.ORANGE_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.MAGENTA_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.LIGHT_BLUE_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.YELLOW_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.LIME_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.PINK_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.GRAY_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.SILVER_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.CYAN_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.PURPLE_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.BLUE_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.BROWN_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.GREEN_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.RED_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.BLACK_SHULKER_BOX, make().set(EStarAttribute.TOOL, 30));
		register(Blocks.WHITE_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.ORANGE_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.MAGENTA_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.YELLOW_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.LIME_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.PINK_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.GRAY_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.SILVER_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.CYAN_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.PURPLE_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.BLUE_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.BROWN_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.GREEN_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.RED_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.BLACK_GLAZED_TERRACOTTA, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.DECORATIVE, 20));
		register(Blocks.CONCRETE, make().set(EStarAttribute.MINERAL, 50).set(EStarAttribute.LIQUID, 30));
		register(Blocks.CONCRETE_POWDER, make().set(EStarAttribute.MINERAL, 40));
		//skip STRUCTURE_BLOCK
		//skip AIR
		register(Items.IRON_SHOVEL, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.IRON_PICKAXE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.IRON_AXE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.FLINT_AND_STEEL, make().set(EStarAttribute.TOOL, 30));
		register(Items.APPLE, make().set(EStarAttribute.PLANT, 30).set(EStarAttribute.FOOD, 10).set(70).set(ItemStarLine.BOTTOM).set(1,1));
		register(Items.BOW, make().set(EStarAttribute.TOOL, 30));
		register(Items.ARROW, make().set(EStarAttribute.TOOL, 30));
		register(Items.SPECTRAL_ARROW, make().set(EStarAttribute.TOOL, 30));
		register(Items.TIPPED_ARROW, make().set(EStarAttribute.TOOL, 30));
		register(Items.COAL, make().set(EStarAttribute.MINERAL, 20).set(EStarAttribute.FUEL, 40).set(60).set(ItemStarLine.TOP).set(19,2));
		register(Items.DIAMOND, make().set(EStarAttribute.JEWEL, 60).set(80).set(ItemStarLine.TOP).set(ItemStarLine.RIGHT).set(ItemStarLine.LEFT));
		register(Items.IRON_INGOT, make().set(EStarAttribute.METAL, 40));
		register(Items.GOLD_INGOT, make().set(EStarAttribute.METAL, 40));
		register(Items.IRON_SWORD, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.WOODEN_SWORD, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.WOOD, 40));
		register(Items.WOODEN_SHOVEL, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.WOOD, 40));
		register(Items.WOODEN_PICKAXE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.WOOD, 40));
		register(Items.WOODEN_AXE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.WOOD, 40));
		register(Items.STONE_SWORD, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.MINERAL, 40));
		register(Items.STONE_SHOVEL, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.MINERAL, 40));
		register(Items.STONE_PICKAXE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.MINERAL, 40));
		register(Items.STONE_AXE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.MINERAL, 40));
		register(Items.DIAMOND_SWORD, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.JEWEL, 40));
		register(Items.DIAMOND_SHOVEL, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.JEWEL, 40));
		register(Items.DIAMOND_PICKAXE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.JEWEL, 40));
		register(Items.DIAMOND_AXE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.JEWEL, 40));
		register(Items.STICK, make().set(EStarAttribute.WOOD, 20));
		register(Items.BOWL, make().set(EStarAttribute.WOOD, 20));
		register(Items.MUSHROOM_STEW, make().set(EStarAttribute.FOOD, 40).set(EStarAttribute.LIQUID, 20));
		register(Items.GOLDEN_SWORD, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.GOLDEN_SHOVEL, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.GOLDEN_PICKAXE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.GOLDEN_AXE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.STRING, make().set(EStarAttribute.ANIMAL, 20).set(70).set(ItemStarLine.RIGHT).set(13,1));
		register(Items.FEATHER, make().set(EStarAttribute.ANIMAL, 20).set(55).set(ItemStarLine.LEFT).set(3,2));
		register(Items.GUNPOWDER, make().set(EStarAttribute.MONSTER, 20).set(EStarAttribute.EXPLOSIVE, 20).set(55).set(ItemStarLine.LEFT).set(7,1));
		register(Items.WOODEN_HOE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.WOOD, 40));
		register(Items.STONE_HOE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.MINERAL, 40));
		register(Items.IRON_HOE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.DIAMOND_HOE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.JEWEL, 40));
		register(Items.GOLDEN_HOE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.WHEAT_SEEDS, make().set(EStarAttribute.PLANT, 30).set(55).set(ItemStarLine.RIGHT).set(18,2));
		register(Items.WHEAT, make().set(EStarAttribute.PLANT, 30).set(60).set(ItemStarLine.RIGHT).set(14,1));
		register(Items.BREAD, make().set(EStarAttribute.FOOD, 30));
		register(Items.LEATHER_HELMET, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.ANIMAL, 40));
		register(Items.LEATHER_CHESTPLATE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.ANIMAL, 40));
		register(Items.LEATHER_LEGGINGS, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.ANIMAL, 40));
		register(Items.LEATHER_BOOTS, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.ANIMAL, 40));
		register(Items.CHAINMAIL_HELMET, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.CHAINMAIL_CHESTPLATE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.CHAINMAIL_LEGGINGS, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.CHAINMAIL_BOOTS, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.IRON_HELMET, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.IRON_CHESTPLATE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.IRON_LEGGINGS, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.IRON_BOOTS, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.DIAMOND_HELMET, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.JEWEL, 40));
		register(Items.DIAMOND_CHESTPLATE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.JEWEL, 40));
		register(Items.DIAMOND_LEGGINGS, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.JEWEL, 40));
		register(Items.DIAMOND_BOOTS, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.JEWEL, 40));
		register(Items.GOLDEN_HELMET, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.GOLDEN_CHESTPLATE, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.GOLDEN_LEGGINGS, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.GOLDEN_BOOTS, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.FLINT, make().set(EStarAttribute.MINERAL, 20).set(EStarAttribute.FUEL, 20).set(55).set(ItemStarLine.RIGHT).set(3,2));
		register(Items.PORKCHOP, make().set(EStarAttribute.ANIMAL, 20).set(65).set(ItemStarLine.LEFT).set(12,2));
		register(Items.COOKED_PORKCHOP, make().set(EStarAttribute.ANIMAL, 20).set(EStarAttribute.FOOD, 20));
		register(Items.PAINTING, make().set(EStarAttribute.DECORATIVE, 30));
		register(Items.GOLDEN_APPLE, make().set(EStarAttribute.FOOD, 70));
		register(Items.SIGN, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 30));
		register(Items.OAK_DOOR, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 30));
		register(Items.SPRUCE_DOOR, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 30));
		register(Items.BIRCH_DOOR, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 30));
		register(Items.JUNGLE_DOOR, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 30));
		register(Items.ACACIA_DOOR, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 30));
		register(Items.DARK_OAK_DOOR, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.WOOD, 30));
		register(Items.BUCKET, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.WATER_BUCKET, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40).set(EStarAttribute.LIQUID, 20));
		register(Items.LAVA_BUCKET, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40).set(EStarAttribute.LIQUID, 20));
		register(Items.MINECART, make().set(EStarAttribute.MACHINE, 40).set(EStarAttribute.METAL, 20));
		register(Items.SADDLE, make().set(EStarAttribute.TOOL, 30));
		register(Items.IRON_DOOR, make().set(EStarAttribute.DECORATIVE, 30).set(EStarAttribute.METAL, 20));
		register(Items.REDSTONE, make().set(EStarAttribute.JEWEL, 30).set(60).set(ItemStarLine.TOP).set(15,2));
		register(Items.SNOWBALL, make().set(EStarAttribute.LIQUID, 10).set(60).set(ItemStarLine.TOP).set(8,2));
		register(Items.BOAT, make().set(EStarAttribute.TOOL, 20).set(EStarAttribute.WOOD, 20));
		register(Items.SPRUCE_BOAT, make().set(EStarAttribute.TOOL, 20).set(EStarAttribute.WOOD, 20));
		register(Items.BIRCH_BOAT, make().set(EStarAttribute.TOOL, 20).set(EStarAttribute.WOOD, 20));
		register(Items.JUNGLE_BOAT, make().set(EStarAttribute.TOOL, 20).set(EStarAttribute.WOOD, 20));
		register(Items.ACACIA_BOAT, make().set(EStarAttribute.TOOL, 20).set(EStarAttribute.WOOD, 20));
		register(Items.DARK_OAK_BOAT, make().set(EStarAttribute.TOOL, 20).set(EStarAttribute.WOOD, 20));
		register(Items.LEATHER, make().set(EStarAttribute.ANIMAL, 40).set(70).set(ItemStarLine.RIGHT).set(ItemStarLine.LEFT).set(13,3));
		register(Items.MILK_BUCKET, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40).set(EStarAttribute.LIQUID, 10));
		register(Items.BRICK, make().set(EStarAttribute.MINERAL, 40));
		register(Items.CLAY_BALL, make().set(EStarAttribute.MINERAL, 30).set(60).set(ItemStarLine.TOP).set(17,1));
		register(Items.REEDS, make().set(EStarAttribute.PLANT, 30).set(65).set(ItemStarLine.RIGHT).set(18,2));
		register(Items.PAPER, make().set(EStarAttribute.PAPER, 20));
		register(Items.BOOK, make().set(EStarAttribute.PAPER, 40).set(EStarAttribute.TOOL, 20));
		register(Items.SLIME_BALL, make().set(EStarAttribute.MONSTER, 20).set(65).set(ItemStarLine.RIGHT).set(12,2));
		register(Items.CHEST_MINECART, make().set(EStarAttribute.MACHINE, 40).set(EStarAttribute.METAL, 20).set(EStarAttribute.WOOD, 20));
		register(Items.FURNACE_MINECART, make().set(EStarAttribute.MACHINE, 40).set(EStarAttribute.METAL, 20).set(EStarAttribute.MINERAL, 20));
		register(Items.EGG, make().set(EStarAttribute.ANIMAL, 20).set(65).set(ItemStarLine.TOP).set(2,1));
		register(Items.COMPASS, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.FISHING_ROD, make().set(EStarAttribute.TOOL, 30));
		register(Items.CLOCK, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.GLOWSTONE_DUST, make().set(EStarAttribute.MINERAL, 40).set(65).set(ItemStarLine.TOP).set(ItemStarLine.BOTTOM).set(3,3));
		register(Items.FISH, 0, make().set(EStarAttribute.ANIMAL, 20).set(55).set(ItemStarLine.RIGHT).set(19,1));
		register(Items.FISH, 1, make().set(EStarAttribute.ANIMAL, 30).set(55).set(ItemStarLine.RIGHT).set(20,1));
		register(Items.FISH, 2, make().set(EStarAttribute.ANIMAL, 40).set(70).set(ItemStarLine.RIGHT).set(ItemStarLine.LEFT).set(18,3));
		register(Items.FISH, 3, make().set(EStarAttribute.ANIMAL, 50).set(70).set(ItemStarLine.TOP).set(ItemStarLine.BOTTOM).set(ItemStarLine.LEFT));
		register(Items.COOKED_FISH, 0, make().set(EStarAttribute.ANIMAL, 20).set(EStarAttribute.FOOD, 10));
		register(Items.COOKED_FISH, 1, make().set(EStarAttribute.ANIMAL, 30).set(EStarAttribute.FOOD, 10));
		register(Items.DYE, make().set(EStarAttribute.DECORATIVE, 10).set(50).set(ItemStarLine.BOTTOM).set(8,1));
		register(Items.BONE, make().set(EStarAttribute.MONSTER, 20).set(60).set(ItemStarLine.LEFT).set(8,2));
		register(Items.SUGAR, make().set(EStarAttribute.FOOD, 10).set(50).set(ItemStarLine.RIGHT).set(6,1));
		register(Items.CAKE, make().set(EStarAttribute.FOOD, 50));
		register(Items.BED, make().set(EStarAttribute.DECORATIVE, 30));
		register(Items.REPEATER, make().set(EStarAttribute.MACHINE, 40));
		register(Items.COOKIE, make().set(EStarAttribute.FOOD, 20));
		register(Items.FILLED_MAP, make().set(EStarAttribute.PAPER, 30).set(EStarAttribute.TOOL, 20));
		register(Items.SHEARS, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.MELON, make().set(EStarAttribute.PLANT, 30).set(60).set(ItemStarLine.BOTTOM).set(ItemStarLine.LEFT).set(12,3));
		register(Items.PUMPKIN_SEEDS, make().set(EStarAttribute.PLANT, 30).set(70).set(ItemStarLine.TOP).set(ItemStarLine.LEFT).set(13,3));
		register(Items.MELON_SEEDS, make().set(EStarAttribute.PLANT, 30).set(70).set(ItemStarLine.TOP).set(ItemStarLine.RIGHT).set(4,3));
		register(Items.BEEF, make().set(EStarAttribute.ANIMAL, 20).set(50).set(ItemStarLine.LEFT).set(20,1));
		register(Items.COOKED_BEEF, make().set(EStarAttribute.ANIMAL, 20).set(EStarAttribute.FOOD, 10));
		register(Items.CHICKEN, make().set(EStarAttribute.ANIMAL, 20).set(60).set(ItemStarLine.RIGHT).set(7,1));
		register(Items.COOKED_CHICKEN, make().set(EStarAttribute.ANIMAL, 20));
		register(Items.MUTTON, make().set(EStarAttribute.ANIMAL, 20).set(65).set(ItemStarLine.BOTTOM).set(12,1));
		register(Items.COOKED_MUTTON, make().set(EStarAttribute.ANIMAL, 20).set(EStarAttribute.FOOD, 10));
		register(Items.RABBIT, make().set(EStarAttribute.ANIMAL, 20).set(60).set(ItemStarLine.LEFT).set(14,2));
		register(Items.COOKED_RABBIT, make().set(EStarAttribute.ANIMAL, 20).set(EStarAttribute.FOOD, 10));
		register(Items.RABBIT_STEW, make().set(EStarAttribute.ANIMAL, 20).set(EStarAttribute.FOOD, 40));
		register(Items.RABBIT_FOOT, make().set(EStarAttribute.ANIMAL, 40));
		register(Items.RABBIT_HIDE, make().set(EStarAttribute.ANIMAL, 20));
		register(Items.ROTTEN_FLESH, make().set(EStarAttribute.MONSTER, 20).set(65).set(ItemStarLine.RIGHT).set(9,1));
		register(Items.ENDER_PEARL, make().set(EStarAttribute.MONSTER, 40).set(70).set(ItemStarLine.TOP).set(ItemStarLine.RIGHT).set(8,3));
		register(Items.BLAZE_ROD, make().set(EStarAttribute.MONSTER, 40).set(70).set(ItemStarLine.RIGHT).set(ItemStarLine.BOTTOM).set(6,3));
		register(Items.GHAST_TEAR, make().set(EStarAttribute.MONSTER, 60).set(80).set(ItemStarLine.RIGHT).set(ItemStarLine.BOTTOM).set(1,3));
		register(Items.GOLD_NUGGET, make().set(EStarAttribute.METAL, 10).set(65).set(ItemStarLine.TOP).set(9,3));
		register(Items.NETHER_WART, make().set(EStarAttribute.PLANT, 30).set(65).set(ItemStarLine.BOTTOM).set(6,2));
		register(Items.POTIONITEM, make().set(EStarAttribute.LIQUID, 40));
		register(Items.SPLASH_POTION, make().set(EStarAttribute.LIQUID, 50));
		register(Items.LINGERING_POTION, make().set(EStarAttribute.LIQUID, 50));
		register(Items.GLASS_BOTTLE, make().set(EStarAttribute.MINERAL, 30).set(EStarAttribute.DECORATIVE, 20));
		register(Items.DRAGON_BREATH, make().set(EStarAttribute.MONSTER, 100));
		register(Items.SPIDER_EYE, make().set(EStarAttribute.MONSTER, 40).set(65).set(ItemStarLine.BOTTOM).set(ItemStarLine.LEFT).set(2,3));
		register(Items.FERMENTED_SPIDER_EYE, make().set(EStarAttribute.MONSTER, 50));
		register(Items.BLAZE_POWDER, make().set(EStarAttribute.MONSTER, 40));
		register(Items.MAGMA_CREAM, make().set(EStarAttribute.MONSTER, 30).set(75).set(ItemStarLine.TOP).set(ItemStarLine.LEFT).set(9,2));
		register(Items.BREWING_STAND, make().set(EStarAttribute.TOOL, 30));
		register(Items.CAULDRON, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.ENDER_EYE, make().set(EStarAttribute.MONSTER, 60));
		register(Items.SPECKLED_MELON, make().set(EStarAttribute.PLANT, 30).set(EStarAttribute.FOOD, 10));
		register(Items.SPAWN_EGG, make().set(EStarAttribute.MONSTER, 100).set(EStarAttribute.ANIMAL, 100));
		register(Items.EXPERIENCE_BOTTLE, make().set(EStarAttribute.TOOL, 30));
		register(Items.FIRE_CHARGE, make().set(EStarAttribute.TOOL, 30));
		register(Items.WRITABLE_BOOK, make().set(EStarAttribute.PAPER, 40).set(EStarAttribute.TOOL, 20));
		register(Items.WRITTEN_BOOK, make().set(EStarAttribute.PAPER, 40).set(EStarAttribute.TOOL, 20));
		register(Items.EMERALD, make().set(EStarAttribute.JEWEL, 50).set(80).set(ItemStarLine.RIGHT).set(ItemStarLine.BOTTOM).set(16,3));
		register(Items.ITEM_FRAME, make().set(EStarAttribute.DECORATIVE, 30));
		register(Items.FLOWER_POT, make().set(EStarAttribute.DECORATIVE, 30));
		register(Items.CARROT, make().set(EStarAttribute.PLANT, 30).set(EStarAttribute.FOOD, 10).set(60).set(ItemStarLine.TOP).set(1,2));
		register(Items.POTATO, make().set(EStarAttribute.PLANT, 30).set(EStarAttribute.FOOD, 10).set(60).set(ItemStarLine.TOP).set(2,2));
		register(Items.BAKED_POTATO, make().set(EStarAttribute.PLANT, 30).set(EStarAttribute.FOOD, 10));
		register(Items.POISONOUS_POTATO, make().set(EStarAttribute.PLANT, 30).set(EStarAttribute.FOOD, 10).set(75).set(ItemStarLine.TOP).set(ItemStarLine.RIGHT).set(9,1));
		register(Items.MAP, make().set(EStarAttribute.PAPER, 30).set(EStarAttribute.TOOL, 20));
		register(Items.GOLDEN_CARROT, make().set(EStarAttribute.PLANT, 30).set(EStarAttribute.FOOD, 10));
		register(Items.SKULL, make().set(EStarAttribute.DECORATIVE, 100));
		register(Items.CARROT_ON_A_STICK, make().set(EStarAttribute.WOOD, 20).set(EStarAttribute.FOOD, 20));
		register(Items.NETHER_STAR, make().set(EStarAttribute.MONSTER, 100).set(EStarAttribute.STAR, 50).set(85).set(ItemStarLine.TOP).set(ItemStarLine.RIGHT).set(ItemStarLine.LEFT).set(9,6));
		register(Items.PUMPKIN_PIE, make().set(EStarAttribute.FOOD, 30));
		register(Items.FIREWORKS, make().set(EStarAttribute.DECORATIVE, 50).set(EStarAttribute.EXPLOSIVE, 50));
		register(Items.FIREWORK_CHARGE, make().set(EStarAttribute.DECORATIVE, 50).set(EStarAttribute.EXPLOSIVE, 50));
		register(Items.ENCHANTED_BOOK, make().set(EStarAttribute.PAPER, 40).set(EStarAttribute.TOOL, 20));
		register(Items.COMPARATOR, make().set(EStarAttribute.MACHINE, 40));
		register(Items.NETHERBRICK, make().set(EStarAttribute.MINERAL, 30).set(EStarAttribute.DECORATIVE, 20));
		register(Items.QUARTZ, make().set(EStarAttribute.JEWEL, 50).set(80).set(ItemStarLine.BOTTOM).set(ItemStarLine.LEFT).set(16,3));
		register(Items.TNT_MINECART, make().set(EStarAttribute.MACHINE, 40).set(EStarAttribute.METAL, 20).set(EStarAttribute.EXPLOSIVE, 40));
		register(Items.HOPPER_MINECART, make().set(EStarAttribute.MACHINE, 40).set(EStarAttribute.METAL, 40));
		register(Items.ARMOR_STAND, make().set(EStarAttribute.DECORATIVE, 30));
		register(Items.IRON_HORSE_ARMOR, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.GOLDEN_HORSE_ARMOR, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.METAL, 40));
		register(Items.DIAMOND_HORSE_ARMOR, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.JEWEL, 40));
		register(Items.LEAD, make().set(EStarAttribute.TOOL, 30));
		register(Items.NAME_TAG, make().set(EStarAttribute.TOOL, 30));
		register(Items.COMMAND_BLOCK_MINECART, make().set(EStarAttribute.MACHINE, 200));
		register(Items.RECORD_13, make().set(EStarAttribute.TOOL, 30));
		register(Items.RECORD_CAT, make().set(EStarAttribute.TOOL, 30));
		register(Items.RECORD_BLOCKS, make().set(EStarAttribute.TOOL, 30));
		register(Items.RECORD_CHIRP, make().set(EStarAttribute.TOOL, 30));
		register(Items.RECORD_FAR, make().set(EStarAttribute.TOOL, 30));
		register(Items.RECORD_MALL, make().set(EStarAttribute.TOOL, 30));
		register(Items.RECORD_MELLOHI, make().set(EStarAttribute.TOOL, 30));
		register(Items.RECORD_STAL, make().set(EStarAttribute.TOOL, 30));
		register(Items.RECORD_STRAD, make().set(EStarAttribute.TOOL, 30));
		register(Items.RECORD_WARD, make().set(EStarAttribute.TOOL, 30));
		register(Items.RECORD_11, make().set(EStarAttribute.TOOL, 30));
		register(Items.RECORD_WAIT, make().set(EStarAttribute.TOOL, 30));
		register(Items.PRISMARINE_SHARD, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.LIQUID, 40).set(70).set(ItemStarLine.TOP).set(ItemStarLine.LEFT).set(3,3));
		register(Items.PRISMARINE_CRYSTALS, make().set(EStarAttribute.MINERAL, 40).set(EStarAttribute.LIQUID, 40).set(70).set(ItemStarLine.TOP).set(ItemStarLine.LEFT).set(9,5));
		register(Items.BANNER, make().set(EStarAttribute.DECORATIVE, 30));
		register(Items.END_CRYSTAL, make().set(EStarAttribute.JEWEL, 80).set(EStarAttribute.EXPLOSIVE, 50));
		register(Items.SHIELD, make().set(EStarAttribute.TOOL, 30));
		register(Items.ELYTRA, make().set(EStarAttribute.TOOL, 30));
		register(Items.CHORUS_FRUIT, make().set(EStarAttribute.PLANT, 30).set(60).set(ItemStarLine.TOP).set(ItemStarLine.RIGHT).set(6,3));
		register(Items.CHORUS_FRUIT_POPPED, make().set(EStarAttribute.PLANT, 30).set(60).set(ItemStarLine.BOTTOM).set(ItemStarLine.LEFT).set(4,3));
		register(Items.BEETROOT_SEEDS, make().set(EStarAttribute.PLANT, 30).set(70).set(ItemStarLine.RIGHT).set(1,2));
		register(Items.BEETROOT, make().set(EStarAttribute.PLANT, 30).set(55).set(ItemStarLine.RIGHT).set(14,2));
		register(Items.BEETROOT_SOUP, make().set(EStarAttribute.FOOD, 30).set(EStarAttribute.LIQUID, 20));
		register(Items.TOTEM_OF_UNDYING, make().set(EStarAttribute.TOOL, 30));
		register(Items.SHULKER_SHELL, make().set(EStarAttribute.MONSTER, 50).set(85).set(ItemStarLine.RIGHT).set(ItemStarLine.BOTTOM).set(ItemStarLine.LEFT));
		register(Items.IRON_NUGGET, make().set(EStarAttribute.METAL, 10));
		register(Items.KNOWLEDGE_BOOK, make().set(EStarAttribute.TOOL, 30).set(EStarAttribute.PAPER, 40));
		
	}
	
	public static boolean addProperty(ItemStack stack) {
		
		ItemSet target = new ItemSet(stack.getItem(), stack.getMetadata());
		if(!vanillaAttributes.containsKey(target)) {
			target = new ItemSet(stack.getItem(), 0);
			if(!vanillaAttributes.containsKey(target)) {
				return false;
			}
		}
		
/*		AlmagestCore.log("truely addProperty, in AttributeHelper.");
		InfoSet targetSet = vanillaAttributes.get(target);
		targetSet.addProperty(stack);*/
		
		return true;
		
	}
	
	public static int getAttrributeLevel(ItemStack stack, EStarAttribute attribute) {
		
		int level = 0;
		Item item = stack.getItem();
		if(item == null) {
			return 0;
		}

		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		if(nbtStack.hasKey(NBT_attributeName+attribute.getName())) {
			level = nbtStack.getInteger(NBT_attributeName+attribute.getName());
		}
		else {
			if(item instanceof IWithAttribute) {
				level = ((IWithAttribute)item).getAttributeLevel(attribute, stack);
			}
			else if(item instanceof ItemBlock && Block.getBlockFromItem(item) instanceof IWithAttribute) {
				level = ((IWithAttribute)Block.getBlockFromItem(item)).getAttributeLevel(attribute, stack);
			}
			else {
				InfoSet set = vanillaAttributes.get(new ItemSet(item, stack.getMetadata()));
				if(set != null) {
					level = set.getAttribute(attribute);
				}
				else {
					set = vanillaAttributes.get(new ItemSet(item, 0));
					if(set != null) {
						level = set.getAttribute(attribute);
					}
				}
				
			}
		}
		
		return AttributeLevelEvent.postEvent(stack, attribute, level);
		
	}
	
	public static Map<EStarAttribute, Integer> getAttributes(ItemStack stack) {
		
		Map<EStarAttribute, Integer> attributes = new HashMap<EStarAttribute, Integer>();
		for(EStarAttribute each: EStarAttribute.values()) {
			int level = getAttrributeLevel(stack, each);
			if(level != 0) {
				attributes.put(each, level);
			}
		}
		return attributes;
		
	}
	
	public static void setAttributeLevel(ItemStack stack, EStarAttribute attribute, int level) {
		
		new HelperNBTStack(stack).setInteger(NBT_attributeName+attribute.getName(), level);
		
	}
	
	public static int getVanillaMemory(ItemStack stack) {
		
		InfoSet set = vanillaAttributes.get(new ItemSet(stack.getItem(), stack.getMetadata()));
		if(set == null) {
			set = vanillaAttributes.get(new ItemSet(stack.getItem(), 0));
		}
		if(set != null) {
			return set.getMemory();
		}
		return 0;
		
	}
	
	public static ArrayList<ItemStarLine> getVanillaLine(ItemStack stack) {
		
		InfoSet set = vanillaAttributes.get(new ItemSet(stack.getItem(), stack.getMetadata()));
		if(set == null) {
			set = vanillaAttributes.get(new ItemSet(stack.getItem(), 0));
		}
		
		ArrayList<ItemStarLine> lines = new ArrayList<ItemStarLine>();
		if(set != null) {
			for(int i=0; i<4; i++) {
				if(set.getLines()[i]) {
					switch(i) {
						case 0:
							lines.add(ItemStarLine.TOP);
							break;
						case 1:
							lines.add(ItemStarLine.BOTTOM);
							break;
						case 2:
							lines.add(ItemStarLine.LEFT);
							break;
						case 3:
							lines.add(ItemStarLine.RIGHT);
							break;
					}
				}
			}
		}
		return lines;
		
	}
	
	public static Map<Integer, ArrayList<Integer>> getVanillaAbilities(ItemStack stack) {
		
		InfoSet set = vanillaAttributes.get(new ItemSet(stack.getItem(), stack.getMetadata()));
		if(set == null) {
			set = vanillaAttributes.get(new ItemSet(stack.getItem(), 0));
		}
		if(set != null) {
			return set.getAbilities();
		}
		return new HashMap<Integer, ArrayList<Integer>>();
		
	}
	
	public static InfoSet getVanillaInfoSet(ItemStack stack) {
		
		InfoSet set = vanillaAttributes.get(new ItemSet(stack.getItem(), stack.getMetadata()));
		if(set == null) {
			set = vanillaAttributes.get(new ItemSet(stack.getItem(), 0));
		}
		
		return set;
		
	}
	
	public static void register(Item item, InfoSet attributes) {
		
		register(item, 0, attributes);
		
	}
	
	public static void register(Block block, InfoSet attributes) {
		
		register(block, 0, attributes);
		
	}
	
	public static void register(Block block, int metadata, InfoSet attributes) {
		
		register(Item.getItemFromBlock(block), metadata, attributes);
		
	}
	
	public static void register(Item item, int metadata, InfoSet attributes) {
		
		vanillaAttributes.put(new ItemSet(item, metadata), attributes);
		ItemStack stack = new ItemStack(item, 1, metadata);
		if(!stack.isEmpty()) {
			AlmagestCore.savedDataManager.getFlagData().registerList(stack);
//			AlmagestCore.log("register to almagest: {}.", stack.getUnlocalizedName());
		}
		
	}
	
	public static InfoSet make() {
		
		return new InfoSet();
		
	}
	
	public static class InfoSet {
		
		private Map<EStarAttribute, Integer> attributes = new HashMap<>();
		private Map<Integer, ArrayList<Integer>> abilities = new HashMap<Integer, ArrayList<Integer>>();
		private int memory;
		private boolean[] lines = new boolean[4];
		
		
		public InfoSet set(EStarAttribute attribute, int level) {
			this.attributes.put(attribute, level);
			return this;
		}
		
		public InfoSet set(int abilityId, int level) {
			if(this.abilities.containsKey(abilityId) && !this.abilities.get(abilityId).contains(level)) {
				this.abilities.get(abilityId).add(level);
			}
			else {
				ArrayList<Integer> levelList = new ArrayList<Integer>();
				levelList.add(level);
				this.abilities.put(abilityId, levelList);
			}
			return this;
		}
		
		public InfoSet set(int memory) {
			this.memory = memory;
			return this;
		}
		
		public InfoSet set(ItemStarLine line) {
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
			return this;
		}
		
		public int getAttribute(EStarAttribute attribute) {
			
			Integer level = this.attributes.get(attribute);
			if(level == null) {
				return 0;
			}
			return level;
			
		}
		
		public Map<Integer, ArrayList<Integer>> getAbilities() {
			
			return this.abilities;
			
		}
		
		public int getMemory() {
			
			return this.memory;
			
		}
		
		public boolean[] getLines() {
			
			return this.lines;
			
		}
		
		public void addProperty(ItemStack stack) {
			
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
			
		}
		
	}
	
	public static class ItemSet {
		
		private Item item;
		private int metadata;
		
		public ItemSet(Item item, int metadata) {
			this.item = item;
			this.metadata = metadata;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + this.item.hashCode();
			result = prime * result + this.metadata;
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj == null) return false;
			if(this.getClass() != obj.getClass()) return false;
			
			ItemSet other = (ItemSet)obj;
			if(this.item != other.item) return false;
			if(this.metadata != other.metadata) return false;
			
			return true;
		}
		
	}

}
