package noki.almagest.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import noki.almagest.AlmagestCore;
import noki.almagest.AlmagestData;
import noki.almagest.ModInfo;
import noki.almagest.gui.MiraContainer;


public class EntityMira extends EntityCreature implements ITalkable, IEntityAlmagest {
	
	private static final DataParameter<Boolean> IS_TALKING = EntityDataManager.<Boolean>createKey(EntityMira.class, DataSerializers.BOOLEAN);
	private EntityPlayer talkingPlayer;
	
	private int openedTime;
	private boolean justOpened;
	
	private static ResourceLocation resourceLocation = new ResourceLocation(ModInfo.ID.toLowerCase(), "almagest.entity.mira");
	
	public EntityMira(World world) {
		
		super(world);
		this.setEntityInvulnerable(true);
        
	}
	
	@Override
	protected void entityInit() {
		
		super.entityInit();
		this.dataManager.register(IS_TALKING, Boolean.valueOf(false));
		
	}
	
	@Override
	protected void initEntityAI() {
		
		super.initEntityAI();
//		this.tasks.addTask(1, new EntityAITalking(this));
		this.tasks.addTask(1, new EntityAILookAtTalkingEntity(this));
		this.tasks.addTask(4, new EntityAIWander(this, 1.0D, 100));
		this.tasks.addTask(5, new EntityAILookIdle(this));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		
	}
	
/*	@Override
	public boolean isAIEnabled() { return true; }*/
	
	@Override
	public SoundEvent getAmbientSound() {
		
		return SoundEvents.ENTITY_CAT_AMBIENT;
		
	}
	
	@Override
	public SoundEvent getHurtSound(DamageSource source) {
		
		return SoundEvents.ENTITY_CAT_HURT;
		
	}
	
	@Override
	public SoundEvent getDeathSound() {
		
		return SoundEvents.ENTITY_CAT_DEATH;
		
	}
	
	/*
	* このMobが動いているときの音のファイルパスを返す.
	* 引数のblockはMobの下にあるBlock.
	*/
/*	@Override
	protected void playStepSound(BlockPos pos, Block block) {
		
		this.playSound("mob.skeleton.step", 0.15F, 1.0F);
		
	}*/
	
	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		
		return EnumCreatureAttribute.UNDEFINED;
		
	}
	
	@Override
	protected void applyEntityAttributes() {
		
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		
	}
	
	@Override
	public Item getDropItem() {
		
		return null;
		
	}
	
	@Override
	public void onEntityUpdate() {
		
		super.onEntityUpdate();
		
		if(this.justOpened) {
			this.openedTime++;
			if(openedTime>10) {
				this.justOpened = false;
			}
		}
		
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		
/*		if(player.openContainer instanceof ContainerMira) {
			AlmagestCore.log("container is opend.");
			((ContainerMira)player.openContainer).setMira(this);
		}
		else {
			AlmagestCore.log("container is not opend.");
		}*/
		this.setTalker(player);
		this.setTalking(true);
		AlmagestCore.log("on interact.");
		
	/*	if(!this.world.isRemote) {
			if(player.openContainer == null) {
				player.openGui(AlmagestCore.instance, AlmagestData.guiID_mira, world, (int)player.posX, (int)player.posY, (int)player.posZ);
			}
		}
		else {
			if(player.openContainer == null && !AlmagestCore.proxy.guiOpening()) {
				player.openGui(AlmagestCore.instance, AlmagestData.guiID_mira, world, (int)player.posX, (int)player.posY, (int)player.posZ);
			}
		}*/
		if(this.justOpened == false && (player.openContainer == null || !(player.openContainer instanceof MiraContainer))) {
			player.openGui(AlmagestCore.instance, AlmagestData.guiID_mira, world, (int)player.posX, (int)player.posY, (int)player.posZ);
			((MiraContainer)player.openContainer).setMira(this);
			
			this.justOpened = true;
		}
/*		if(!this.world.isRemote) {
			switch(AlmagestCore.savedDataManager.getStoryData().getStoryFlag()) {
				case 0:
				case 1:
				case 2:
					AlmagestCore.savedDataManager.getStoryData().goToNextStory();
					break;
			}
			PacketHandler.instance.sendToAll(new PacketSyncStory(AlmagestCore.savedDataManager.getStoryData().getStoryFlag()));
		}*/
		
		return super.processInteract(player, hand);
		
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
	
	@Override
	public boolean canDespawn() {
		
		return false;
		
	}
	
	@Override
	public ResourceLocation getResource() {
		
		return resourceLocation;
		
	}
	
	public static void setResource(ResourceLocation resource) {
		
		resourceLocation = resource;
		
	}
	
}
