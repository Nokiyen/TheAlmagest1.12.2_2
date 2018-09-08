package noki.almagest.event;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.ability.StarAbilityCreator.StarAbility;

public class EventUseItem {
	
	@SubscribeEvent
	public void onUseItemEvent(LivingEntityUseItemEvent.Finish event) {
		
/*		AlmagestCore.log2("enter into onUseItemEvent().");
		
		ArrayList<StarAbility> abilities = StarAbilityCreator.getAbilities(event.getItem());
		AlmagestCore.log2("item name is %s.", event.getItem().getUnlocalizedName());
		AlmagestCore.log2("item count is %s.", event.getItem().getCount());
		for(StarAbility each: abilities) {
			AlmagestCore.log2(each.getLocalizedName());
		}
		if(abilities.contains(StarAbility.AUTO_EXPLOSION)) {
			EntityLivingBase entity = event.getEntityLiving();
			entity.world.createExplosion(entity, entity.posX, entity.posY, entity.posZ, 10F, true);
			AlmagestCore.log2("finish onUseItemEvent().");
		}*/
		
	}


}
