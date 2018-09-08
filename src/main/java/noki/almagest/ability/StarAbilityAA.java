package noki.almagest.ability;

import java.util.UUID;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;


/**********
 * @class StarAbilityAA
 *
 * @description 「星のちから」：攻撃力と防御力を同時に上げます。
 * level 1: +0.75;
 * level 2: +1.5;
 * level 3: +2.25;
 * level 4: +3;
 */
public class StarAbilityAA extends StarAbilityEquip {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public StarAbilityAA() {
		
		super(4, "f886824f-b0cb-4088-9af3-d7286a801bda", "0109fd31-60fe-4733-9bb1-e2a57a898375", "c82be4d7-bb13-4b31-83b6-63bdac6a19d2",
				"55400aff-ecaa-4d1c-94da-11d0d7182c6e", "40398ceb-b45f-4eb9-a5c2-086e2323a5dd", "39f87fd8-3a39-47fd-a924-94f26514cfa7");
		this.setEffectable(Effectable.Armor, Effectable.Amulet);
		
	}
	
	@Override
	protected void computeAttribute(LivingEquipmentChangeEvent event, UUID modifierId) {
		
		IAttributeInstance attributeAttack = event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		if(attributeAttack.getModifier(modifierId) != null) {
			attributeAttack.removeModifier(modifierId);
		}
		IAttributeInstance attributeArmor = event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ARMOR);
		if(attributeArmor.getModifier(modifierId) != null) {
			attributeArmor.removeModifier(modifierId);
		}
		
		int[] levels = this.getAbilityLevels(event.getTo());
		if(levels.length != 0) {
			int sum = 0;
			for(int each: levels) {
				sum += MathHelper.clamp(each, 1, this.getMaxLevel());
			}
			attributeAttack.applyModifier(new AttributeModifier(modifierId, "attack boost", 0.75*sum, 0));
			attributeArmor.applyModifier(new AttributeModifier(modifierId, "armor boost", 0.75*sum, 0));
		}
		
	}

}
