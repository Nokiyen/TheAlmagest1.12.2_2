package noki.almagest.saveddata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.WorldSavedData;
import noki.almagest.AlmagestCore;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.ability.StarAbilityCreator.StarAbility;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.saveddata.gamedata.EHelpData;
import noki.almagest.saveddata.gamedata.EMemoData;
import noki.almagest.saveddata.gamedata.GameData;
import noki.almagest.saveddata.gamedata.GameDataAbility;
import noki.almagest.saveddata.gamedata.GameDataAbility2;
import noki.almagest.saveddata.gamedata.GameDataBlock;
import noki.almagest.saveddata.gamedata.GameDataConstellation;
import noki.almagest.saveddata.gamedata.GameDataHelp;
import noki.almagest.saveddata.gamedata.GameDataItem;
import noki.almagest.saveddata.gamedata.GameDataList;
import noki.almagest.saveddata.gamedata.GameDataMemo;
import noki.almagest.saveddata.gamedata.GameDataMob;
import noki.almagest.saveddata.gamedata.GameDataRecipe;


/**********
 * @class AlmagestSavedData
 *
 * @description 様々なフラグについて管理するクラスです。ストーリー進行、アイテムや星座の取得状況など。
 * @description_en
 */
public class AlmagestDataFlag implements IAlmagestData {
	
	//******************************//
	// define member variables.
	//******************************//
	private HashMap<DataCategory, HashMap<String, GameData>> gameDataMap = new HashMap<DataCategory, HashMap<String,GameData>>();
	private WorldSavedData almagestData;
		
	
	//******************************//
	// define member methods.
	//******************************//
	public AlmagestDataFlag() {
		
//		super(name);
		
		for(DataCategory each: DataCategory.values()) {
			this.gameDataMap.put(each, new HashMap<String, GameData>());
		}
		
	}
	
	public HashMap<DataCategory, HashMap<String, GameData>> getMap() {
		
		return this.gameDataMap;
		
	}
	
	public void registerData(DataCategory category, String key, GameData data) {
		
		this.gameDataMap.get(category).put(key, data);
		
	}
	
	public void registerBlock(ItemStack stack, boolean hasRecipe) {
		
		this.gameDataMap.get(DataCategory.BLOCK).put(stack.getUnlocalizedName(), new GameDataBlock(stack, hasRecipe));
		
	}
	
	public void registerItem(ItemStack stack, boolean hasRecipe) {
		
		this.gameDataMap.get(DataCategory.ITEM).put(stack.getUnlocalizedName(), new GameDataItem(stack, hasRecipe));
		
	}
	
	public void registerList(ItemStack stack) {
		
		this.gameDataMap.get(DataCategory.LIST).put(stack.getUnlocalizedName(), new GameDataList(stack));
		
	}
	
	public void registerMob(EntityLiving entity, ResourceLocation location) {
		
		this.gameDataMap.get(DataCategory.MOB).put(location.toString(), new GameDataMob(entity, location));
		
	}
	
	public void registerAbility(StarAbility ability) {
		
		this.gameDataMap.get(DataCategory.ABILITY).put(ability.getName(), new GameDataAbility(ability));
		
	}
	
	public void registerAbility2(noki.almagest.ability.StarAbility ability, int level) {
		
		this.gameDataMap.get(DataCategory.ABILITY).put(ability.getName(level), new GameDataAbility2(ability, level));
		
	}
	
	public void registerConstellation(Constellation constellation) {
		
		this.gameDataMap.get(DataCategory.CONSTELLATION).put(constellation.getName(), new GameDataConstellation(constellation));
		
	}
	
	public void registerMemo(EMemoData memo) {
		
		this.gameDataMap.get(DataCategory.MEMO).put(memo.getDisplay(), new GameDataMemo(memo));
	}

	public void registerHelp(EHelpData help) {
		
		this.gameDataMap.get(DataCategory.HELP).put(help.getDisplay(), new GameDataHelp(help));
		
	}
	
	public void registerRecipe(IRecipe recipe) {
		
		this.gameDataMap.get(DataCategory.RECIPE).put(recipe.getRegistryName().toString(), new GameDataRecipe(recipe));
		
	}
	
	public GameDataBlock getBlock(ItemStack stack) {
		
		return (GameDataBlock)this.gameDataMap.get(DataCategory.BLOCK).get(stack.getUnlocalizedName());
		
	}

	public GameDataItem getItem(ItemStack stack) {
		
		return (GameDataItem)this.gameDataMap.get(DataCategory.ITEM).get(stack.getUnlocalizedName());
		
	}
	
	public GameDataList getList(ItemStack stack) {
		
		return (GameDataList)this.gameDataMap.get(DataCategory.LIST).get(stack.getUnlocalizedName());
		
	}
	
	public GameDataMob getMob(ResourceLocation resource) {
		
		return (GameDataMob)this.gameDataMap.get(DataCategory.MOB).get(resource.toString());
		
	}
	
	public GameDataAbility2 getAbility(int abilityId, int abilityLevel) {
		
		return (GameDataAbility2)this.gameDataMap.get(DataCategory.ABILITY).get(StarAbilityCreator.getAbility(abilityId).getName(abilityLevel));
		
	}
	
	public GameDataConstellation getConstellation(Constellation constellation) {
		
		return (GameDataConstellation)this.gameDataMap.get(DataCategory.CONSTELLATION).get(constellation.getName());
		
	}
	
	public GameDataMemo getMemo(EMemoData memoData) {

		return (GameDataMemo)this.gameDataMap.get(DataCategory.MEMO).get(memoData.getDisplay());
		
	}
	
	public GameData getNextData(GameData data, DataCategory category, boolean isCreative) {
		
		HashMap<String, GameData> categorySet = this.gameDataMap.get(category);
//		java.util.List<String> mapKeys = Arrays.asList(categorySet.keySet().toArray(new String[categorySet.size()]));
//		Collections.sort(mapKeys, String.CASE_INSENSITIVE_ORDER);
		ArrayList<String> mapKeys = new ArrayList<String>();
		mapKeys.addAll(categorySet.keySet());
		this.sortAlmagest(mapKeys, category);
		
		boolean found = false;
		for(String eachKey: mapKeys) {
			GameData eachData = categorySet.get(eachKey);
			if(found && (isCreative || eachData.isObtained())) {
				return eachData;
			}
			if(eachData == data) {
				found = true;
			}
		}
		
		return null;
		
	}
	
	public GameData getPrevData(GameData data, DataCategory category, boolean isCreative) {
		
		HashMap<String, GameData> categorySet = this.gameDataMap.get(category);
//		java.util.List<String> mapKeys = Arrays.asList(categorySet.keySet().toArray(new String[categorySet.size()]));
//		Collections.sort(mapKeys, String.CASE_INSENSITIVE_ORDER);
		ArrayList<String> mapKeys = new ArrayList<String>();
		mapKeys.addAll(categorySet.keySet());
		this.sortAlmagest(mapKeys, category);
		
		GameData prevData = null;
		for(String eachKey: mapKeys) {
			GameData eachData = categorySet.get(eachKey);
			if(eachData == data) {
				return prevData;
			}
			if(isCreative || eachData.isObtained()) {
				prevData = eachData;
			}
		}
		
		return null;
		
	}
	
	public int getMaxPage(DataCategory category, int itemNumber) {
		
		HashMap<String, GameData> map = this.gameDataMap.get(category);
		
		return (int)Math.ceil((double)map.size()/(double)itemNumber);
		
	}
	
	public void setConstellationObtained(Constellation constellation) {
		
		this.setObtained(DataCategory.CONSTELLATION, constellation.getName());
		
	}
	
	public void setConstellationPresented(Constellation constellation) {
		
		this.getConstellation(constellation).setPresented(true);
		this.almagestData.markDirty();
		
	}
	
	public boolean isCostellationPresented(Constellation constellation) {
		
		return this.getConstellation(constellation).isPresented();
		
	}
	
	public void setObtained(DataCategory category, String label) {
		
		this.gameDataMap.get(category).get(label).setObtained(true);
		this.almagestData.markDirty();
		
	}
	
	public void setObtained(GameData gameData) {
		
		gameData.setObtained(true);
		this.almagestData.markDirty();
		
	}
	
	public ArrayList<GameData> getDataSet(DataCategory category, int start, int length) {
		
		ArrayList<GameData> dataSet = new ArrayList<GameData>();
		
		HashMap<String, GameData> categorySet = this.gameDataMap.get(category);
		int size = categorySet.size();
//		AlmagestCore.log2("at saved data, category set size is %s.", size);

		ArrayList<String> mapKeys = new ArrayList<String>();
		mapKeys.addAll(categorySet.keySet());
		this.sortAlmagest(mapKeys, category);
//		Collections.sort(mapkey, String.CASE_INSENSITIVE_ORDER);
		
		if(size < start) {
			AlmagestCore.log2("size < start. hoo!");
			return dataSet;
		}
		int count = 1;
		for(String each: mapKeys) {
			if(start+length <= count) {
				break;
			}
			if(start <= count) {
				dataSet.add(categorySet.get(each));
			}
			count++;
		}
		
		return dataSet;
		
	}
	
	private void sortAlmagest(ArrayList<String> mapKeys, DataCategory category) {
		
		if(category == DataCategory.BLOCK || category == DataCategory.ITEM) {
			String targetString = (category == DataCategory.BLOCK) ? "tile.constellation_block" : "item.missing_star";
			ArrayList<String> formerList = new ArrayList<String>();
			ArrayList<String> latterList = new ArrayList<String>();
			for(String each: mapKeys) {
				if(each.contains(targetString)) {
					latterList.add(each);
				}
				else {
					formerList.add(each);
				}
			}
			Collections.sort(formerList, String.CASE_INSENSITIVE_ORDER);
			Collections.sort(latterList, String.CASE_INSENSITIVE_ORDER);
			mapKeys.clear();
			mapKeys.addAll(formerList);
			mapKeys.addAll(latterList);
		}
		else {
			Collections.sort(mapKeys, String.CASE_INSENSITIVE_ORDER);		
		}
		
	}
	
	public int countObtainedConstellation() {
		
		int count = 0;
		HashMap<String, GameData> constelationData = this.gameDataMap.get(DataCategory.CONSTELLATION);
		for(GameData each: constelationData.values()) {
			if(each.isObtained()) {
				count++;
			}
		}
		
		return count;
		
	}
	
	public int countPresentedConstellation() {
		
		int count = 0;
		HashMap<String, GameData> constelationData = this.gameDataMap.get(DataCategory.CONSTELLATION);
		for(GameData each: constelationData.values()) {
			if(((GameDataConstellation)each).isPresented()) {
				count++;
			}
		}
		
		return count;
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		for(DataCategory each: DataCategory.values()) {
			NBTTagCompound categoryNbt = nbt.getCompoundTag(each.toString());
			
			HashMap<String, GameData> eachData = this.gameDataMap.get(each);
			for(GameData eachGameData: eachData.values()) {
				eachGameData.readFromNbt(categoryNbt.getCompoundTag(eachGameData.getName().toString()));
			}
		}

	}
	
	@Override
	public NBTTagCompound createNBT() {
		
		NBTTagCompound gameDataNbt = new NBTTagCompound();
		
		for(DataCategory each: DataCategory.values()) {
			HashMap<String, GameData> eachData = this.gameDataMap.get(each);
			NBTTagCompound categoryNbt = new NBTTagCompound();
			for(GameData eachGameData: eachData.values()) {
				NBTTagCompound eachNbt = new NBTTagCompound();
				eachGameData.writeToNbt(eachNbt);
				categoryNbt.setTag(eachGameData.getName().toString(), eachNbt);
			}
			gameDataNbt.setTag(each.toString(), categoryNbt);
		}
		
		return gameDataNbt;
		
	}
	
	@Override
	public void setSavedData(WorldSavedData data) {
		
		this.almagestData = data;
		
	}
	
}
