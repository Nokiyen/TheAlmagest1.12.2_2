package noki.almagest.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import noki.almagest.AlmagestCore;
import noki.almagest.AlmagestData;
import noki.almagest.gui.ContainerMira;
import noki.almagest.gui.GuiAlmagest;
import noki.almagest.gui.GuiTsuchinoko;
import noki.almagest.registry.ModItems;


public class EntityTsuchinoko extends EntityAnimal implements ITalkable {
	
	private static final DataParameter<Boolean> IS_TALKING = EntityDataManager.<Boolean>createKey(EntityTsuchinoko.class, DataSerializers.BOOLEAN);
	private EntityPlayer talkingPlayer;
	
	public EntityTsuchinoko(World world) {
		
		super(world);
		this.setSize(14F/16F, 10F/16F);
		
	}
	
	@Override
	protected void entityInit() {
		
		super.entityInit();
		this.dataManager.register(IS_TALKING, Boolean.valueOf(false));
		
	}
	
	@Override
	protected void initEntityAI() {
		
		super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAILookAtTalkingEntity(this));
        this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1.0D, 60));
        
	}
	
	@Override
	public float getEyeHeight() {
		
		return 5F/16F;
		
	}
	
/*	@Override
	public double getYOffset() {
		
		return 5F/16F;
		
	}*/
	
/*	@Override
	public void onUpdate() {
		
		this.renderYawOffset = this.rotationYaw;
		super.onUpdate();
	
	}*/
	
/*	@Override
	public boolean isAIEnabled() { return true; }*/
	
	@Override
	public SoundEvent getAmbientSound() {
		
		return null;
		
	}
	
	@Override
	public SoundEvent getHurtSound(DamageSource source) {
		
		return null;
		
	}
	
	@Override
	public SoundEvent getDeathSound() {
		
		return null;
		
	}
	
	/*
	* このMobが動いているときの音のファイルパスを返す.
	* 引数のblockはMobの下にあるBlock.
	*/
/*	@Override
	protected void playStepSound(BlockPos pos, Block block) {
		
		this.playSound("mob.skeleton.step", 0.15F, 1.0F);
		
	}*/
	
/*	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		
		return EnumCreatureAttribute.UNDEFINED;
		
	}*/
	
	@Override
	protected void applyEntityAttributes() {
		
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
		
	}
	
	@Override
	public Item getDropItem() {
		
		return ModItems.TSUCHINOKO_SKIN;
		
	}
	
/*	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		
		AlmagestCore.log("on dropLoot().");
		this.dropFewItems(wasRecentlyHit, lootingModifier);
		
	}*/
	
	@Override
	protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
		
		AlmagestCore.log("on dropFewItems().");
		Item item = this.getDropItem();
		if(item != null) {
			AlmagestCore.log("item is not null.");
			int prob = this.rand.nextInt(10);
			AlmagestCore.log("prob is {}.", prob);
			if(prob > 5) {
				AlmagestCore.log("about to dropItem().");
				this.dropItem(item, 1);
			}
        }
		
    }
	
/*	@Override
	public boolean canDropLoot() {
		
		AlmagestCore.log("on canDropLoot().");
		return true;
		
	}*/
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		
		if(player.world.isRemote) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiTsuchinoko());
			if(Minecraft.getMinecraft().currentScreen instanceof GuiTsuchinoko) {
				((GuiTsuchinoko)Minecraft.getMinecraft().currentScreen).setTsuchiniko(this);
			}
		}
		this.setTalker(player);
		this.setTalking(true);
		return super.processInteract(player, hand);
		
	}
	
	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		
		return null;
		
	}
	
	@Override
	public boolean isTalking() {
		
		return ((Boolean)this.dataManager.get(IS_TALKING)).booleanValue();
		
	}
	
	public void setTalking(boolean flag) {
		
		this.dataManager.set(IS_TALKING, Boolean.valueOf(flag));
		
	}
	
	@Override
	public void setTalker(EntityPlayer player) {
		
		this.talkingPlayer = player;
		
	}
	
	@Override
	public EntityPlayer getTalker() {
		
		return this.talkingPlayer;
		
	}
	
}
