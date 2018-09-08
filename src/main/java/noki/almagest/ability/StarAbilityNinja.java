package noki.almagest.ability;

import java.util.UUID;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.asm.event.ArmorVisibilityEvent;


/**********
 * @class StarAbilityNinja
 *
 * @description 「星のちから」：透明化時の防具による発見されやすさを下げます。また速度が少し上昇します。
 * level 1: 速度上昇+0.0175*2, 採掘速度上昇＋+0.1*2;
 */
public class StarAbilityNinja extends StarAbilityEquip {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public StarAbilityNinja() {
		
		super(1, "c94dd0f3-2960-4526-a7f3-5794756d760e", "7ef7ec5a-dc9b-46c4-8cdd-4a300864e186", "c7973a87-dd2a-4806-ac80-0ad868f87cce",
				"00ce230b-6ade-460c-9517-30decbcae9b9", "590190d9-3132-4aaf-91a9-738e1084110a", "c0cd0eb2-cfeb-441e-80fd-254c07ad4b7c");
		this.setArmorOnly();
		this.setEffectable(Effectable.Armor);
		
	}
	
	@SubscribeEvent
	public void onArmorVisibility(ArmorVisibilityEvent event) {
		
		int[] levels = this.getAbilityLevels(event.armor);
		if(levels.length != 0) {
			event.setVibilityLevel(0);
		}
		
	}
	
	@Override
	protected void computeAttribute(LivingEquipmentChangeEvent event, UUID modifierId) {
		
		IAttributeInstance attributeAttack = event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED);
		if(attributeAttack.getModifier(modifierId) != null) {
			attributeAttack.removeModifier(modifierId);
		}
		IAttributeInstance attributeArmor = event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		if(attributeArmor.getModifier(modifierId) != null) {
			attributeArmor.removeModifier(modifierId);
		}
		
		int[] levels = this.getAbilityLevels(event.getTo());
		if(levels.length != 0) {
			attributeAttack.applyModifier(new AttributeModifier(modifierId, "ninja attack", 0.1D*2D, 0));
			attributeArmor.applyModifier(new AttributeModifier(modifierId, "ninja movement", 0.0175D*2D, 0));
		}
		
	}

}
