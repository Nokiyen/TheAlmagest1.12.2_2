package noki.almagest.asm.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;


@Cancelable
public class PotionApplyEvent extends Event {
	
	public EntityLivingBase entityLivingBase;
	public PotionEffect potionEffect;
	
	public PotionApplyEvent(EntityLivingBase entityLivingBase, PotionEffect potionEffect) {
		
		this.entityLivingBase = entityLivingBase;
		this.potionEffect = potionEffect;
		
	}
	
	public static boolean postEvent(EntityLivingBase entityLivingBase, PotionEffect potionEffect) {
		
		return MinecraftForge.EVENT_BUS.post(new PotionApplyEvent(entityLivingBase, potionEffect));
		
	}

}
