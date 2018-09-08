package noki.almagest.ability;

import java.util.UUID;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;


/**********
 * @class StarAbilityLuck
 *
 * @description 「星のちから」：幸運にします。
 * level 1: +0.1;
 * level 2: +0.3;
 * level 3: +0.5;
 * level 4: +1;
 */
public class StarAbilityLuck extends StarAbilityEquip {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private static float[] scale = {0.1F, 0.3F, 0.5F, 1F};
	
	//******************************//
	// define member methods.
	//******************************//
	public StarAbilityLuck() {
		
		super(4, "efe0ba11-c377-4b7a-97d5-cff3dc721df9", "69d9be22-2d8c-4b4a-b66d-61ba7d29e200", "7de8889a-c595-4634-a9db-4f9541e3878f",
				"2a1d9770-547a-475e-bfd0-f2aefd42232e", "3e71fb0f-d196-4ad1-9fa9-fe94b0e3ec28", "cb14baec-48a5-4824-801e-aaf26b11bf74");
		this.setArmorOnly();
		this.setEffectable(Effectable.Armor, Effectable.Amulet);
		
	}
	
	@Override
	protected void computeAttribute(LivingEquipmentChangeEvent event, UUID modifierId) {
		
		IAttributeInstance attributeInstance = event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.LUCK);
		if(attributeInstance.getModifier(modifierId) != null) {
			attributeInstance.removeModifier(modifierId);
		}
		
		int[] levels = this.getAbilityLevels(event.getTo());
		if(levels.length != 0) {
			float totalScale = 0;
			for(int each: levels) {
				totalScale += scale[MathHelper.clamp(each, 1, this.getMaxLevel())-1];
			}
			attributeInstance.applyModifier(new AttributeModifier(modifierId, "luck", totalScale, 0));
		}
		
	}

}
