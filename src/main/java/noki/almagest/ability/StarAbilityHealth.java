package noki.almagest.ability;

import java.util.UUID;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;


/**********
 * @class StarAbilityHealth
 *
 * @description 「星のちから」：体力を上げます。
 */
public class StarAbilityHealth extends StarAbilityEquip {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public StarAbilityHealth() {
		
		super(4, "52e21b8b-ee8a-42b7-8d1e-9664e4d3cb57", "aa951495-6ced-4d29-8fdf-8bb3ff76d628", "fdc310b1-aaac-42be-80c9-237240c66230",
				"8c53e8e3-5003-4cc7-8603-3764ebbc04d2", "442b5a04-7a63-437d-997b-5a5a7c68a401", "8b98885e-762f-43b4-af95-7af7b34aea05");
		this.setEffectable(Effectable.Weapon, Effectable.Armor, Effectable.Amulet);
		
	}
	
	@Override
	protected void computeAttribute(LivingEquipmentChangeEvent event, UUID modifierId) {
		
		IAttributeInstance attributeInstance = event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		if(attributeInstance.getModifier(modifierId) != null) {
			attributeInstance.removeModifier(modifierId);
			if(event.getEntityLiving().getHealth() > event.getEntityLiving().getMaxHealth()) {
				event.getEntityLiving().setHealth(event.getEntityLiving().getMaxHealth());
			}
		}
		
		int[] levels = this.getAbilityLevels(event.getTo());
		if(levels.length != 0) {
			int sum = 0;
			for(int each: levels) {
				sum += MathHelper.clamp(each, 1, this.getMaxLevel());
			}
			attributeInstance.applyModifier(new AttributeModifier(modifierId, "health boost", 1*sum, 0));
		}
		
	}

}
