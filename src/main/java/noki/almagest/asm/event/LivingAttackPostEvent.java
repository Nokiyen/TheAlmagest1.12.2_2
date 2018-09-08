package noki.almagest.asm.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;


@Cancelable
public class LivingAttackPostEvent extends Event {
	
	public EntityLivingBase entityLivingBase;
	public DamageSource damageSource;
	public float amount;
	public DamageSource changedSource;
	
	public LivingAttackPostEvent(EntityLivingBase entity, DamageSource src, float amount) {
		
		this.entityLivingBase = entity;
		this.damageSource = src;
		this.amount = amount;
		
	}
	
	public void changeDamageSource(DamageSource src) {
		
		this.changedSource = src;
		
	}
	
	public static boolean postEvent(EntityLivingBase entity, DamageSource src, float amount) {
		
		LivingAttackPostEvent event = new LivingAttackPostEvent(entity, src, amount);
		boolean result = MinecraftForge.EVENT_BUS.post(event);
		if(result) {
			return !MinecraftForge.EVENT_BUS.post(new LivingAttackEvent(entity, event.changedSource, amount));
		}
		else {
			return !MinecraftForge.EVENT_BUS.post(new LivingAttackEvent(entity, src, amount));
		}
		
	}

}
