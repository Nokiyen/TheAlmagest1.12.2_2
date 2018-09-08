package noki.almagest.ability;

import java.util.UUID;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;


/**********
 * @class StarAbilitySpeed
 *
 * @description 「星のちから」：移動速度を上げます。
 * level 1: +0.03125;
 * level 2: +0.03125*2;
 * level 3: +0.03125*3;
 * level 4: +0.03125*4;
 */
public class StarAbilitySpeed extends StarAbilityEquip {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public StarAbilitySpeed() {
		
		super(4, "c5a1fffa-3dda-4ce9-bbde-5c043d078251", "68d21f38-dd16-411d-9558-a8700a481fd2", "d4121396-69be-4915-a478-d40cdd179ff7",
				"f47676fe-1f25-4091-9440-2a74bac39659", "af457c61-fa57-452f-841d-d249e718cfc1", "0cad870d-c6fd-4691-8de1-e07859da8040");
		this.setArmorOnly();
		this.setEffectable(Effectable.Armor, Effectable.Amulet);
		
	}
	
	@Override
	protected void computeAttribute(LivingEquipmentChangeEvent event, UUID modifierId) {
		
		IAttributeInstance attributeInstance = event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		if(attributeInstance.getModifier(modifierId) != null) {
			attributeInstance.removeModifier(modifierId);
		}
		
		int[] levels = this.getAbilityLevels(event.getTo());
		if(levels.length != 0) {
			int sum = 0;
			for(int each: levels) {
				sum += MathHelper.clamp(each, 1, this.getMaxLevel());
			}
			attributeInstance.applyModifier(new AttributeModifier(modifierId, "speed boost", 0.03125D*sum, 0));
		}
		else {
		}
		
	}

}
