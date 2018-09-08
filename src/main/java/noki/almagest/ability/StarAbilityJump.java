package noki.almagest.ability;

import java.util.UUID;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;


/**********
 * @class StarAbilityArmor
 *
 * @description 「星のちから」：手に持ったアイテムの使用速度を上げます。
 * level 1: +0.125;
 * level 2: +0.125*2;
 * level 3: +0.125*3;
 * level 4: +0.125*4;
 */
public class StarAbilityJump extends StarAbilityEquip {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public StarAbilityJump() {
		
		super(4, "9aa10e04-b3d0-4298-a78b-71ec5ddd72c2", "cfdd6f2f-253d-4047-bdf7-da0d12967051", "b5ef1d63-426f-44a2-8636-743833e13542",
				"b2571524-5cf3-4bcc-ae9e-78ec56b33e24", "bdc6a9ee-af6f-4efa-862f-e71926bc6b46", "2ef706bc-2ceb-463a-99f2-62904530789c");
		this.setArmorOnly();
		
	}
	
	@Override
	protected void computeAttribute(LivingEquipmentChangeEvent event, UUID modifierId) {
		
		IAttributeInstance attributeInstance = event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED);
		if(attributeInstance.getModifier(modifierId) != null) {
			attributeInstance.removeModifier(modifierId);
		}
		
		int[] levels = this.getAbilityLevels(event.getTo());
		if(levels.length != 0) {
			int sum = 0;
			for(int each: levels) {
				sum += MathHelper.clamp(each, 1, this.getMaxLevel());
			}
			attributeInstance.applyModifier(new AttributeModifier(modifierId, "attack speed boost", 0.125D*sum, 0));
		}
		
	}

}
