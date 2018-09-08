package noki.almagest.saveddata;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.WorldSavedData;
import noki.almagest.AlmagestCore;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.ability.StarPropertyCreator.ItemStarLine;


/**********
 * @class ChunkProperty
 *
 * @description 個別のチャンクで、ブロック・アイテム等に付与するstar propertyを管理します。
 * @description_en
 */
public class ChunkProperty {
	
	//******************************//
	// define member variables.
	//******************************//
	private Map<String, PropertySet> propertyMap = new HashMap<String, PropertySet>();
	private WorldSavedData almagestData;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ChunkProperty(WorldSavedData data) {
		
		this.almagestData = data;
		
	}
	
	public ChunkProperty(NBTTagCompound nbt, WorldSavedData data) {
		
		for(String eachKey: nbt.getKeySet()) {
			this.propertyMap.put(eachKey, new PropertySet(nbt.getCompoundTag(eachKey)));
		}
		
		this.almagestData = data;
		
	}
	
	public NBTTagCompound createNbt() {
		
		NBTTagCompound newNbt = new NBTTagCompound();
		
		for(Map.Entry<String, PropertySet> entry : this.propertyMap.entrySet()) {
			newNbt.setTag(entry.getKey(), entry.getValue().createNbt());
		}
		return newNbt;
		
	}
	
	public ItemStack addProperty(ItemStack stack) {
		
		PropertySet targetSet = this.propertyMap.get(stack.getItem().getRegistryName().toString());
		if(targetSet != null) {
			return targetSet.addProperty(stack);
		}
		
		//create new property set for the item or block.
		targetSet = new PropertySet();
		
		int obtainedConst = AlmagestCore.savedDataManager.getFlagData().countObtainedConstellation();
		
		//normal distribution where mean is 20 + the number of the obtained constellation, standard deviation is 20/3.
		//the minimum memory is 0. and cut out the decimal.
		int memory = (int)Math.floor(Math.max(this.nextNormalDist(20+obtainedConst, 20.0D/3.0D), 0));
		targetSet.setMemory(memory);
		
		//the minimum line number is 1.
		int lineNumber = 1 + (int)Math.floor(Math.max(this.nextNormalDist(1.5D*(double)obtainedConst/88.0D, 1), 0));
		
		Integer[] targetInt = {1, 2, 3, 4};
		List<Integer> target = Arrays.asList(targetInt);
		Collections.shuffle(target);
		
		for(int i=0; i<lineNumber; i++) {
			switch(target.get(i)) {
				case 1:
					targetSet.setLine(ItemStarLine.TOP);
					break;
				case 2:
					targetSet.setLine(ItemStarLine.BOTTOM);
					break;
				case 3:
					targetSet.setLine(ItemStarLine.LEFT);
					break;
				case 4:
					targetSet.setLine(ItemStarLine.RIGHT);
					break;
			}
		}
		
		//temporarily, choose an ability at complete random.
		int abilityNumber = (int)Math.floor(MathHelper.clamp(this.nextNormalDist(1+1.5D*(double)obtainedConst/88.0D, 1), 0, 3));
		int[] randomIds = StarAbilityCreator.getRandomAbility(abilityNumber);
		for(int i=0; i<randomIds.length; i++) {
			int level = (int)Math.floor(MathHelper.clamp(this.nextNormalDist(1+1.5D*(double)obtainedConst/88.0D, 1), 1, 3));
			targetSet.setAbility(randomIds[i], level);
		}
		
		//finally, keep the new property.
		this.propertyMap.put(stack.getItem().getRegistryName().toString(), targetSet);
		this.almagestData.markDirty();
		
		return targetSet.addProperty(stack);
		
	}
	
	private double nextNormalDist(double mean, double sd) {
		
		Random rand = new Random();
		return rand.nextGaussian()*sd + mean;
		
	}
	
}
