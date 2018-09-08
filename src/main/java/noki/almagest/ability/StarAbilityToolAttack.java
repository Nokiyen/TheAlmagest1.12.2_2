package noki.almagest.ability;

import java.util.UUID;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;


/**********
 * @class StarAbilityToolAttack
 *
 * @description 「星のちから」：武器の攻撃力を上げます。
 * level 1: +1;
 * level 2: +2;
 * level 3: +4;
 * level 4: +8;
 */
public class StarAbilityToolAttack extends StarAbilityEquip {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public StarAbilityToolAttack() {
		
		super(4, "ff535a51-3a29-4106-b084-108235c06d89", "88836401-28cc-4a26-b1ef-59b4f382ba21", "8faf7094-9d61-4cbb-812e-8b212ec88ca2",
				"33182dcf-29ff-4286-8e1c-35fb69baa658", "a9bd38ad-b84c-4b39-bc86-4567f80e631f", "2eae0215-434d-4245-befb-373bc7167f7a");
		this.setEffectable(Effectable.Weapon);
		
	}
	
	@Override
	protected void computeAttribute(LivingEquipmentChangeEvent event, UUID modifierId) {
		
		if(event.getSlot() != EntityEquipmentSlot.MAINHAND && event.getSlot() != EntityEquipmentSlot.OFFHAND) {
			return;
		}
		
		IAttributeInstance attributeInstance = event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		if(attributeInstance.getModifier(modifierId) != null) {
			attributeInstance.removeModifier(modifierId);
		}
		
		int[] levels = this.getAbilityLevels(event.getTo());
		if(levels.length != 0) {
			int sum = 0;
			for(int each: levels) {
				switch(each) {
				case 1:
					sum += 1;
					break;
				case 2:
					sum += 2;
					break;
				case 3:
					sum += 4;
					break;
				case 4:
					sum += 8;
					break;
				}
			}
			attributeInstance.applyModifier(new AttributeModifier(modifierId, "attack boost", sum, 0));
		}
		
	}

}
