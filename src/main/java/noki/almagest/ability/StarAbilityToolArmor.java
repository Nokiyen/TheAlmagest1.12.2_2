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
 * @description 「星のちから」：防具の防御力を上げます。
 * level 1: +1;
 * level 2: +2;
 * level 3: +3;
 * level 4: +4;
 */
public class StarAbilityToolArmor extends StarAbilityEquip {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public StarAbilityToolArmor() {
		
		super(4, "1e115106-1ac9-417e-90b7-74ebb31b5e34", "e7f7bb2e-310c-414f-8a1d-f765b6624c05", "27fa840c-2a53-47b5-bec4-b380c4583fc1",
				"2822d7d6-6585-4121-8165-bca56d12f9d4", "6d9a3617-92ea-4b6c-a238-1f2056c6e2df", "9e57ebc6-a9a0-4938-8fa4-d56b5afd7809");
		this.setEffectable(Effectable.Armor);
		
	}
	
	@Override
	protected void computeAttribute(LivingEquipmentChangeEvent event, UUID modifierId) {
		
		if(event.getSlot() == EntityEquipmentSlot.MAINHAND || event.getSlot() == EntityEquipmentSlot.OFFHAND) {
			return;
		}
		
		IAttributeInstance attributeInstance = event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ARMOR);
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
					sum += 3;
					break;
				case 4:
					sum += 4;
					break;
				}
			}
			attributeInstance.applyModifier(new AttributeModifier(modifierId, "attack boost", sum, 0));
		}
		
	}

}
