package noki.almagest.item;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noki.almagest.AlmagestData;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.ItemWithAttribute;
import noki.almagest.gui.GuiAlmagest;
import noki.almagest.helper.HelperNBTStack;
import noki.almagest.registry.ModItems;


/**********
 * @class ItemAlmagest
 *
 * @description ゲーム内ヘルプ・図鑑を表示するためのアイテムです。
 * なんかnewとか表示するのをこのアイテムのNBTに書き込んでいた形跡があるけど、それはなしの方向じゃないかな……。
 */
public class ItemAlmagest extends ItemWithAttribute {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private static String NBT_constPrefix = "constState:";

	
	//******************************//
	// define member methods.
	//******************************//
	public ItemAlmagest() {
		
		this.setAttributeLevel(EStarAttribute.STAR, 20);
		this.setAttributeLevel(EStarAttribute.PAPER, 20);
		this.setMaxStackSize(1);
		
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		
		if(world.isRemote == true) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiAlmagest(player.getHeldItem(hand)));
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		
		if(tab == AlmagestData.tab) {
			ItemStack stack = new ItemStack(ModItems.ALMAGEST, 1, 0);
			for(int i = 1; i <= 88; i++) {
				setConstFlag(stack, i, 1);
			}
			
			list.add(stack);
		}
		
	}
	
	// flag: 0=not get, 1=get&new, 2=get&known
	public static ItemStack setConstFlag(ItemStack stack, int constNumber, int flag) {
		
		return new HelperNBTStack(stack).setInteger(NBT_constPrefix+constNumber, flag).getStack();
		
	}
	
	public static int getConstFlag(ItemStack stack, int constNumber) {
		
		return new HelperNBTStack(stack).getInteger(NBT_constPrefix+constNumber);
		
	}
	
}
