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
 * @description 「星のちから」：防御力を上げます。
 * level 1: +1;
 * level 2: +2;
 * level 3: +3;
 * level 4: +4;
 */
public class StarAbilityArmor extends StarAbilityEquip {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public StarAbilityArmor() {
		
		super(4, "541c6467-20fe-4b61-8186-64a4b90f964c", "c6ea829e-4f90-4a94-9d77-c0f022fbc7a6", "d5cb174e-243a-47f4-a092-12eff1aae8ec",
				"6c8873ec-77b6-46b5-8842-b7062d3d5dae", "bed52b76-24c8-4b02-83f6-1659ae9c62e6", "bf881ce9-1557-432d-95ab-4aef4731f856");
		this.setEffectable(Effectable.Armor, Effectable.Amulet);
		
	}
	
	@Override
	protected void computeAttribute(LivingEquipmentChangeEvent event, UUID modifierId) {
		
		IAttributeInstance attributeInstance = event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ARMOR);
		if(attributeInstance.getModifier(modifierId) != null) {
			attributeInstance.removeModifier(modifierId);
		}
		
		int[] levels = this.getAbilityLevels(event.getTo());
		if(levels.length != 0) {
//			AlmagestCore.log2("level is %s.", levels[0]);
			int sum = 0;
			for(int each: levels) {
				sum += MathHelper.clamp(each, 1, this.getMaxLevel());
			}
			attributeInstance.applyModifier(new AttributeModifier(modifierId, "armor boost", 1*sum, 0));
		}
		else {
//			AlmagestCore.log2("no ability!");
		}
		
	}

}
