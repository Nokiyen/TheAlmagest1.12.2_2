package noki.almagest.ability;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import noki.almagest.helper.HelperNBTStack;


/**********
 * @class StarAbility
 *
 * @description 「星のちから」の継承用クラスです。
 */
public class StarAbility {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String NBT_abilities = "abilities";
	protected String name = "";
	protected int id = 0;
	protected int maxLevel = 1;
	protected boolean canCombine = false;
	protected ArrayList<Effectable> effectable = new ArrayList<Effectable>();
	
	
	//******************************//
	// define member methods.
	//******************************//
	public String getName(int level) {
		
		return "almagest.ability."+String.format("%02d", this.id)+"."+MathHelper.clamp(level, 1, this.getMaxLevel())+".name";
		
	}
	
	public StarAbility setId(int id) {
		
		this.id = id;
		return this;
		
	}
	
	public int getId() {
		
		return this.id;
		
	}
	
	public void setMaxLevel(int level) {
		
		this.maxLevel = level;
		if(this.maxLevel > 1) {
			this.canCombine = true;
		}
		
	}
	
	public int getMaxLevel() {
		
		return this.maxLevel;
		
	}
	
	public void setCombine(boolean flag) {
		
		this.canCombine = flag;
		
	}
	
	public boolean canCombine() {
		
		return this.canCombine;
		
	}
	
	public int[] getAbilityLevels(ItemStack stack) {
		
		NBTTagCompound abilities = new HelperNBTStack(stack).getTag(NBT_abilities);
		return abilities.getIntArray(String.valueOf(this.id));
		
	}
	
	protected void setEffectable(Effectable... targets) {
		
		for(Effectable each: targets) {
			this.effectable.add(each);
		}
		
	}
	
	public ArrayList<Effectable> getEffectable() {
		
		return this.effectable;
		
	}
	
	public boolean hasEffectable(Effectable effectable) {
		
		if(this.effectable.contains(effectable)) {
			return true;
		}
		return false;
		
	}
	
	public enum Effectable {
		
		Weapon("weapon"),
		Armor("armor"),
		Tool("tool"),
		Amulet("amulet"),
		Attack("attack"),
		Heal("heal"),
		Support("support"),
		Block("block");
		
		private String name;
		
		private Effectable(String name){
			this.name = name;
		}
		
		public String getName() {
			return "almagest.ability.effectable."+this.name+".name";
		}
		
	}

}
