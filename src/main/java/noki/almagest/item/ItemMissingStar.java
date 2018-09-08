package noki.almagest.item;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import noki.almagest.AlmagestData;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.ItemWithAttribute;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperConstellation.MissingStar;
import noki.almagest.helper.HelperConstellation.Spectrum;
import noki.almagest.helper.HelperConstellation.StarData;
import noki.almagest.helper.HelperNBTStack;
import noki.almagest.registry.IWithSubTypes;
import noki.almagest.registry.ModItems;


/**********
 * @class ItemMissingStar
 *
 * @description 1等星の星を表すアイテムです。一部の星座を完成させるのに使います。
 * missing starのアイテムとして、スペクトルに応じた8種類の見た目(metadata)がありますが、全部で1等星の星・23種(NBT)があります。
 */
public class ItemMissingStar extends ItemWithAttribute implements IWithSubTypes {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String NBT_starNumber = "starNumber";
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemMissingStar() {
		
		this.setHasSubtypes(true);
		this.setAttributeLevel(EStarAttribute.STAR, 10);
		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		
		return this.getUnlocalizedName() + "." + getMissingStarNumber(stack);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		
		if(tab == AlmagestData.tab) {
			list.addAll(this.getSubItems());
		}
		
	}
	
	@Override
	public int getSubtypeCount() {
		
		return Spectrum.values().length;
		
	}
	
	@Override
	public boolean registerToAlmagest() {
		
		return true;
		
	}
	
	
	//----------
	//Static Methods.
	//----------
	public static ItemStack getMissingStar(int starNumber, int size) {
		
		// check star's existence.
		StarData star = HelperConstellation.getStar(starNumber);
		if(star == null) {
			return null;
		}
		
		// check size.
		size = MathHelper.clamp(size, 1, 64);
		
		// create ItemStack with NBT.
		ItemStack stack = new ItemStack(ModItems.MISSING_STAR, size, star.spectrum.getMetadata());
		return new HelperNBTStack(stack).setInteger(NBT_starNumber, starNumber).getStack();
		
	}
	
	public static int getMissingStarNumber(ItemStack stack) {
		
		return new HelperNBTStack(stack).getInteger(NBT_starNumber);
		
	}

	@Override
	public List<ItemStack> getSubItems() {
		
		List<ItemStack> items = new ArrayList<>();
		for(MissingStar each: MissingStar.values()) {
			items.add(getMissingStar(each.getStarNumber(), 1));
		}
		return items;
		
	}
	
}
