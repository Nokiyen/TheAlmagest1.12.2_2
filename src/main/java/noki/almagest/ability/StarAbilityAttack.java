package noki.almagest.ability;

import java.util.UUID;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;


/**********
 * @class StarAbilityAttack
 *
 * @description 「星のちから」：攻撃力を上げます。
 * level 1: +1;
 * level 2: +2;
 * level 3: +3;
 * level 4: +4;
 */
public class StarAbilityAttack extends StarAbilityEquip {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public StarAbilityAttack() {
		
		super(4, "70805542-20d2-4655-babe-b8f13f561b3e", "1cb99e36-8a2b-46b5-b156-13d44ef9b27b", "bfc07a63-390d-4954-baf1-13ee5b40fa84",
				"79ea1505-339e-4ef8-9eff-9dcd0315384b", "4e58c122-8f52-4374-9be6-7a5c123dc600", "2f60c8f7-32f8-444e-a4d0-9ba92207497a");
		this.setEffectable(Effectable.Armor, Effectable.Amulet);
		
	}
	
	@Override
	protected void computeAttribute(LivingEquipmentChangeEvent event, UUID modifierId) {
		
		IAttributeInstance attributeInstance = event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		if(attributeInstance.getModifier(modifierId) != null) {
			attributeInstance.removeModifier(modifierId);
		}
		
		int[] levels = this.getAbilityLevels(event.getTo());
		if(levels.length != 0) {
			int sum = 0;
			for(int each: levels) {
				sum += MathHelper.clamp(each, 1, this.getMaxLevel());
			}
			attributeInstance.applyModifier(new AttributeModifier(modifierId, "attack boost", 1*sum, 0));
		}
		
	}

}
