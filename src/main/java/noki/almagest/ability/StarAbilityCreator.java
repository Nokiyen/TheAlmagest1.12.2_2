package noki.almagest.ability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import noki.almagest.AlmagestCore;
import noki.almagest.attribute.AttributeHelper;
import noki.almagest.attribute.IWithAttribute;
import noki.almagest.helper.HelperNBTStack;


public class StarAbilityCreator {
	
	public static final String nameAbility = "ability";
	public static HashMap<Integer, noki.almagest.ability.StarAbility> starAbilities = new HashMap<Integer, noki.almagest.ability.StarAbility>();
	
	public static void registerAbilities() {
		
		registerAbility(1, new StarAbilityAttack());
		registerAbility(2, new StarAbilityArmor());
		registerAbility(3, new StarAbilityHealth());
		registerAbility(4, new StarAbilityAA());
		registerAbility(5, new StarAbilityToolAttack());
		registerAbility(6, new StarAbilityToolArmor());
		registerAbility(7, new StarAbilityHeal());
		registerAbility(8, new StarAbilityBuff());
		registerAbility(9, new StarAbilityElemental());
//		registerAbility(10, new StarAbilityReflect());
		registerAbility(11, new StarAbilityNinja());
		registerAbility(12, new StarAbilitySpeed());
		registerAbility(13, new StarAbilityHaste());
		registerAbility(14, new StarAbilityXP());
		registerAbility(15, new StarAbilityLuck());
		registerAbility(16, new StarAbilityQuickUse());
		registerAbility(17, new StarAbilityExplosion());
		registerAbility(18, new StarAbilityAttribute());
		registerAbility(19, new StarAbilityMemory());
		registerAbility(20, new StarAbilityCritical());
		
	}
	
	public static void registerAbility(int id, noki.almagest.ability.StarAbility ability) {
		
		starAbilities.put(id, ability.setId(id));
		for(int i=1; i<=ability.getMaxLevel(); i++) {
			AlmagestCore.savedDataManager.getFlagData().registerAbility2(ability, i);
		}
		MinecraftForge.EVENT_BUS.register(ability);
		
	}
	
	public static ItemStack addAbility(ItemStack stack, StarAbility... abilities) {
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
//		int[] original = nbtStack.getIntArray(nameAbility);
		ArrayList<Integer> original = new ArrayList<Integer>();
		for(int each: nbtStack.getIntArray(nameAbility)) {
			original.add(each);
		}
		
		for(StarAbility each: abilities) {
			if(original.contains(each.getId()) == false) {
				original.add(each.getId());
			}
		}
		
		Collections.sort(original);
		
		int[] newAbilities = new int[original.size()];
		for(int i=0; i<original.size(); i++) {
			newAbilities[i] = original.get(i);
		}
		nbtStack.setIntArray(nameAbility, newAbilities);
		
		return nbtStack.getStack();
		
	}
	
	public static ItemStack addAbility2(ItemStack stack, int abilityId, int level) {
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		NBTTagCompound abilityTag = nbtStack.getTag("abilities");
		int[] levels = abilityTag.getIntArray(String.valueOf(abilityId));
		int[] newLevels = new int[levels.length+1];
		
		for(int i=0; i < levels.length; i++) {
			newLevels[i] = levels[i];
		}
		newLevels[levels.length] = level;
		abilityTag.setIntArray(String.valueOf(abilityId), newLevels);
		nbtStack.setTag("abilities", abilityTag);
		
		return nbtStack.getStack();
		
	}
	
	public static Map<Integer, ArrayList<Integer>> getAbility2(ItemStack stack) {

		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		Map<Integer, ArrayList<Integer>> abilities = new HashMap<Integer, ArrayList<Integer>>();
		
		if(nbtStack.hasKey("abilities")) {
			NBTTagCompound abilityTag = nbtStack.getTag("abilities");
			
			for(String eachKey: abilityTag.getKeySet()) {
				int[] levels = abilityTag.getIntArray(eachKey);
				ArrayList<Integer> levelsList = new ArrayList<Integer>();
				for(int eachLevel: levels) {
					levelsList.add(eachLevel);
				}
				abilities.put(Integer.valueOf(eachKey), levelsList);
			}
		}
		else {
			if(stack.getItem() instanceof ItemBlock) {
				Block block = Block.getBlockFromItem(stack.getItem());
				if(block instanceof IWithAttribute) {
					abilities =  ((IWithAttribute)block).getAbilities(stack);
				}
				else {
					abilities = AttributeHelper.getVanillaAbilities(stack);
				}
			}
			else {
				if(stack.getItem() instanceof IWithAttribute) {
					abilities =  ((IWithAttribute)stack.getItem()).getAbilities(stack);
				}
				else {
					abilities = AttributeHelper.getVanillaAbilities(stack);
				}
			}
		}
		
		return abilities;
		
	}
	
	public static ArrayList<StarAbility> getAbilities(ItemStack stack) {
		
		HelperNBTStack nbtStack = new HelperNBTStack(stack);
		ArrayList<StarAbility> abilities = new ArrayList<StarAbility>();
		
		int[] original = nbtStack.getIntArray(nameAbility);
		for(int each: original) {
			abilities.add(StarAbility.getAbility(each));
		}
		
		return abilities;
		
	}
	
	public static noki.almagest.ability.StarAbility getAbility(int id) {
		
		return starAbilities.get(id);
		
	}
	
	public static int[] getRandomAbility(int count) {
		
		ArrayList<Integer> ids = new ArrayList<Integer>(starAbilities.keySet());
		Collections.shuffle(ids);
		
		int[] randomIds = new int[count];
		for(int i=0; i<count; i++) {
			randomIds[i] = ids.get(i);
		}
		
		return randomIds;
		
	}
	
	public enum StarAbility {
		SHINING(1, "shining", "almagest.ability.shining"),
		FOOD_FULL_1(2, "food_full_1", "almagest.ability.food_full_1"),
		AUTO_EXPLOSION(3, "auto_explosion", "almagest.ability.auto_explosion");
		
		private int id;
		private String name;
		private String localizedName;
		
		private StarAbility(int id, String name, String localizedName) {
			this.id = id;
			this.name = name;
			this.localizedName = localizedName;
		}
		
		public int getId() {
			return this.id;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getLocalizedName() {
			return I18n.format(this.localizedName);
		}
		
		public static StarAbility getAbility(int id) {
			for(StarAbility each: StarAbility.values()) {
				if(each.getId() == id) {
					return each;
				}
			}
			return null;
		}
		
	}

}
