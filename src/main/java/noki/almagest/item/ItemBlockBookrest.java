package noki.almagest.item;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import noki.almagest.ability.StarPropertyCreator;


/**********
 * @class ItemBlockStar
 *
 * @description
 */
public class ItemBlockBookrest extends ItemBlock {
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ItemBlockBookrest(Block block) {
		
		super(block);
		
	}
	
	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		
		if(StarPropertyCreator.isMagnitude(stack, 3)) {
			return true;
		}
		return false;
		
	}
	
	@Override
	@Nullable
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		
		EntityItem item = new EntityItem(world, location.posX, location.posY-0.30000001192092896D+(double)location.getEyeHeight(), location.posZ, itemstack){
			@Override
			public boolean attackEntityFrom(DamageSource source, float amount) {
				if(source.isFireDamage() || source.isExplosion()) {
					return false;
				}
				return super.attackEntityFrom(source, amount);
			}
		};
		item.setPickupDelay(40);
		
		return item;
		
	}
	
}
