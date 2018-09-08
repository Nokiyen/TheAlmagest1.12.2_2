package noki.almagest.ability;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.asm.event.LivingAttackPostEvent;


/**********
 * @class StarAbilityBuff
 *
 * @description 「星のちから」：攻撃に属性を付与します。追加効果があるものもあります。
 * 毒付与
 * 炎ダメージ & 炎上付与
 * 雷ダメージ & 速度低下
 * サボテンダメージ
 * 魔法ダメージ
 * ウィザー & ウィザー付与
 * ドラゴンブレス & ブレスダメージ
 */
public class StarAbilityElemental extends StarAbility {
	
	public StarAbilityElemental() {
		
		this.setMaxLevel(7);
		this.setCombine(false);
		this.setEffectable(Effectable.Weapon);
		
	}
	
	@SubscribeEvent
	public void onEntityAttackPost(LivingAttackPostEvent event) {
		
		Entity entitySrc = event.damageSource.getTrueSource();
		if(entitySrc == null || !(entitySrc instanceof EntityLivingBase)) {
			return;
		}
		
		EntityLivingBase entity = (EntityLivingBase)entitySrc;
		ItemStack stack = entity.getHeldItemMainhand();
		if(stack == null || stack.isEmpty()) {
			return;
		}
		
		AlmagestCore.log("check ability / {}.", stack.getItem().getUnlocalizedName());
		
		int[] levels = this.getAbilityLevels(stack);
		if(levels.length == 0) {
			AlmagestCore.log("no ability.");
		}
		for(int each: levels) {
			AlmagestCore.log("ability level / {}.", each);
			SideEffect effect = SideEffect.getSideEffect(each);
			if(effect != null) {
				AlmagestCore.log("found effect.");
				AlmagestCore.log("chaged damage source / {}.", String.valueOf(effect.hasDamageSource()));
			}
			if(effect != null && effect.hasDamageSource()) {
				AlmagestCore.log("change damage source.");
				event.setCanceled(true);
				event.changeDamageSource(effect.getDamageSource(entity));
			}
		}
		
	}
	
	@SubscribeEvent
	public void onEntityAttack(LivingAttackEvent event) {
		
		Entity entitySrc = event.getSource().getTrueSource();
		if(entitySrc == null || !(entitySrc instanceof EntityLivingBase)) {
			return;
		}
		
		EntityLivingBase entity = (EntityLivingBase)entitySrc;
		ItemStack stack = entity.getHeldItemMainhand();
		if(stack == null || stack.isEmpty()) {
			return;
		}
		
		int[] levels = this.getAbilityLevels(stack);
		for(int each: levels) {
			SideEffect effect = SideEffect.getSideEffect(each);
			if(effect != null) {
				AlmagestCore.log("try to spawn particles..");
				effect.onEntityDamaged(event);
			}
		}
		
	}
	
	private enum SideEffect {
		
		Poison(1, null) {
			@Override
			public void onEntityDamaged(LivingAttackEvent event) {
				if(event.getEntityLiving().getRNG().nextFloat() < 0.3F) {
					event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.POISON, 20, 0));
				}
				this.spawnParticles(event.getEntityLiving(), EnumParticleTypes.ITEM_CRACK, Item.getIdFromItem(Items.SPIDER_EYE));
			}
		},
		Fire(2, DamageSource.IN_FIRE) {
			@Override
			public void onEntityDamaged(LivingAttackEvent event) {
				if(event.getEntityLiving().getRNG().nextFloat() < 0.3F) {
					event.getEntityLiving().setFire(1);
				}
				this.spawnParticles(event.getEntityLiving(), EnumParticleTypes.FLAME);
			}
		},
		Lighting(3, DamageSource.LIGHTNING_BOLT) {
			@Override
			public void onEntityDamaged(LivingAttackEvent event) {
				if(event.getEntityLiving().getRNG().nextFloat() < 0.3F) {
					event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20, 0));
				}
			}
		},
		Cuctus(4, DamageSource.CACTUS) {
			@Override
			public void onEntityDamaged(LivingAttackEvent event) {
				this.spawnParticles(event.getEntityLiving(), EnumParticleTypes.BLOCK_CRACK, Block.getStateId(Blocks.CACTUS.getDefaultState()));
			}
		},
		Magic(5, DamageSource.MAGIC) {
			@Override
			public void onEntityDamaged(LivingAttackEvent event) {
				this.spawnParticles(event.getEntityLiving(), EnumParticleTypes.SPELL);
			}
		},
		Wither(6, DamageSource.WITHER) {
			@Override
			public void onEntityDamaged(LivingAttackEvent event) {
				if(event.getEntityLiving().getRNG().nextFloat() < 0.3F) {
					event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WITHER, 20, 0));
				}
				this.spawnParticles(event.getEntityLiving(), EnumParticleTypes.SMOKE_NORMAL);
			}
		},
		Dragon(7, DamageSource.DRAGON_BREATH) {
			@Override
			public void onEntityDamaged(LivingAttackEvent event) {
				if(event.getEntityLiving().getRNG().nextFloat() < 0.3F) {
					event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 1));
				}
				this.spawnParticles(event.getEntityLiving(), EnumParticleTypes.DRAGON_BREATH);
			}
		};
		
		private int id;
		private DamageSource damageSource;
		
		private SideEffect(int id, DamageSource damageSource) {
			this.id = id;
			this.damageSource = damageSource;
		}
		
		public boolean hasDamageSource() {
			return this.damageSource != null;
		}
		
		public DamageSource getDamageSource(EntityLivingBase entity) {
			return new ElementalDamage(this.damageSource, entity);
		}
		
		public void onEntityDamaged(LivingAttackEvent event) {
			
		}
		
		public void spawnParticles(EntityLivingBase entity, EnumParticleTypes partcle, int... parameter) {
			if(entity.world instanceof WorldServer) {
				double d0 = (double)(-MathHelper.sin(entity.rotationYaw * 0.017453292F));
				double d1 = (double)MathHelper.cos(entity.rotationYaw * 0.017453292F);
				((WorldServer)entity.world).spawnParticle(
						partcle, entity.posX+d0, entity.posY+(double)entity.height*0.5D, entity.posZ+d1, 15, d0, 0.0D, d1, 0.05D, parameter);
			}
		}
		
		private class ElementalDamage extends DamageSource {
			
			private DamageSource damageSource;
			private EntityLivingBase entity;
			
			public ElementalDamage(DamageSource damageSource, EntityLivingBase entity) {
				super(damageSource.getDamageType());
				this.damageSource = damageSource;
				this.entity = entity;
				if(this.damageSource.isFireDamage()) {
					this.setFireDamage();
				}
				if(this.damageSource.isDamageAbsolute()) {
					this.setDamageIsAbsolute();
				}
				if(this.damageSource.isMagicDamage()) {
					this.setMagicDamage();
				}
				if(this.damageSource.isUnblockable()) {
					this.setDamageBypassesArmor();
				}
			}
			
			@Override
			@Nullable
			public Entity getTrueSource() {
				return this.entity;
			}
			
		}
		
		public static SideEffect getSideEffect(int id) {
			for(SideEffect each: SideEffect.values()) {
				if(each.id == id) {
					return each;
				}
			}
			return null;
		}
		
	}
	
}
