package noki.almagest.saveddata;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import noki.almagest.AlmagestCore;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.ability.StarAbilityCreator.StarAbility;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.saveddata.gamedata.EHelpData;
import noki.almagest.saveddata.gamedata.EMemoData;

public class TempDataManager {
	
	public static void tempRegister() {
		
		//for block.
/*		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.ANVIL, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.BEDROCK, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.BREWING_STAND, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.BRICK_BLOCK, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.CAKE, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.CARPET, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.DETECTOR_RAIL, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.DIAMOND_BLOCK, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.EMERALD_BLOCK, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.ENCHANTING_TABLE, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.FLOWER_POT, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.FURNACE, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.GLOWSTONE, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.GOLD_BLOCK, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.HOPPER, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.HAY_BLOCK, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.ICE, false);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.JUKEBOX, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.LADDER, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.LAPIS_BLOCK, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.MELON_BLOCK, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.MOSSY_COBBLESTONE, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.NOTEBLOCK, true);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.OBSIDIAN, false);
		AlmagestCore.savedDataManager.getFlagData().registerBlock(Blocks.PUMPKIN, false);*/
		
		//for item.
/*		AlmagestCore.savedDataManager.getFlagData().registerItem(noki.almagest.registry.ModItems.PLANISPHERE, true);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.ACACIA_BOAT, true);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.APPLE, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.ARROW, true);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.BEEF, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.BLAZE_ROD, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.CARROT, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.CHICKEN, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.DIAMOND, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.DIAMOND_HOE, true);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.EMERALD, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.ENDER_EYE, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.FEATHER, true);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.FIRE_CHARGE, true);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.FISH, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.GOLD_INGOT, true);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.GUNPOWDER, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.ITEM_FRAME, true);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.LEATHER, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.MILK_BUCKET, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.NETHER_STAR, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.PAINTING, true);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.QUARTZ, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.RABBIT_FOOT, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.RECORD_11, false);
		AlmagestCore.savedDataManager.getFlagData().registerItem(Items.SADDLE, false);*/
		
		//for mob.
/*		AlmagestCore.savedDataManager.getFlagData().registerMob(new EntityZombie(null));
		AlmagestCore.savedDataManager.getFlagData().registerMob(new EntitySkeleton(null));
		AlmagestCore.savedDataManager.getFlagData().registerMob(new EntityCow(null));
		AlmagestCore.savedDataManager.getFlagData().registerMob(new EntitySheep(null));
		AlmagestCore.savedDataManager.getFlagData().registerMob(new EntityPig(null));*/
/*		AlmagestCore.savedDataManager.getData().registerMob(new EntityBlaze(null));
		AlmagestCore.savedDataManager.getData().registerMob(new EntityChicken(null));
		AlmagestCore.savedDataManager.getData().registerMob(new EntityCreeper(null));
		AlmagestCore.savedDataManager.getData().registerMob(new EntityHorse(null));
		AlmagestCore.savedDataManager.getData().registerMob(new EntityRabbit(null));
		AlmagestCore.savedDataManager.getData().registerMob(new EntityEnderman(null));*/
		
		//for ability.
/*		for(StarAbility each: StarAbility.values()) {
			AlmagestCore.savedDataManager.getData().registerAbility(each);
		}*/
		StarAbilityCreator.registerAbilities();
		
		for(Constellation each: Constellation.values()) {
			AlmagestCore.savedDataManager.getFlagData().registerConstellation(each);
		}
		
		for(EMemoData each: EMemoData.values()) {
			AlmagestCore.savedDataManager.getFlagData().registerMemo(each);
		}
		
		for(EHelpData each: EHelpData.values()) {
			AlmagestCore.savedDataManager.getFlagData().registerHelp(each);
		}
		
	}

}
