package noki.almagest.ability;

import java.util.EnumMap;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


/**********
 * @class StarAbilityEquip
 *
 * @description 装備につくと効果のある「星のちから」用のクラスです。extendsして使います。
 */
public class StarAbilityEquip extends StarAbility {
	
	
	//******************************//
	// define member variables.
	//******************************//
	protected EnumMap<EntityEquipmentSlot, UUID> ids = new EnumMap<EntityEquipmentSlot, UUID>(EntityEquipmentSlot.class);
	private boolean armorOnly;
	private boolean weaponOnly;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public StarAbilityEquip(int maxLevel, String idMainHand, String idOffHand, String idFeet, String idLegs, String idChest, String idHead) {
		
		this.setMaxLevel(maxLevel);
		this.ids.put(EntityEquipmentSlot.MAINHAND, UUID.fromString(idMainHand));
		this.ids.put(EntityEquipmentSlot.OFFHAND, UUID.fromString(idOffHand));
		this.ids.put(EntityEquipmentSlot.HEAD, UUID.fromString(idFeet));
		this.ids.put(EntityEquipmentSlot.CHEST, UUID.fromString(idLegs));
		this.ids.put(EntityEquipmentSlot.FEET, UUID.fromString(idChest));
		this.ids.put(EntityEquipmentSlot.LEGS, UUID.fromString(idHead));
		
		this.armorOnly = false;
		this.weaponOnly = false;
		
	}
	
	protected void setArmorOnly() {
		
		this.armorOnly = true;
		
	}

	protected void setWeaponOnly() {
		
		this.weaponOnly = true;
		
	}
	
	@SubscribeEvent
	public void onEquipChange(LivingEquipmentChangeEvent event) {
		
		if(!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}
		if(event.getTo() == null) {
			return;
		}
		
		switch(event.getSlot()) {
			case MAINHAND:
			case OFFHAND:
				if(this.armorOnly) return;
				break;
			case HEAD:
			case CHEST:
			case FEET:
			case LEGS:
				if(this.weaponOnly) return;
				break;
		}
		
		this.computeAttribute(event, this.ids.get(event.getSlot()));
		
	}
	
	protected void computeAttribute(LivingEquipmentChangeEvent event, UUID modifierId) {
		
	}

}
