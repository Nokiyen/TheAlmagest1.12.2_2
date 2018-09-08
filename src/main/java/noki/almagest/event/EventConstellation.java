package noki.almagest.event;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.block.BlockConstellation;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperConstellation.ConstFamily;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.registry.ModBlocks;
import noki.almagest.tile.TileConstellation;


/**********
 * @class EventConstellation
 *
 * @description
 * 
 */
public class EventConstellation {
	
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	//星座の入手を処理する。
	//ブロックとしての星座の入手処理はnoki.almagest.event.EventObtained.onPickupItem()。
	@SubscribeEvent
	public void onPickupItem(EntityItemPickupEvent event) {
		
		if(event.getItem() == null || event.getEntityPlayer() == null) {
			return;
		}
		if(event.getItem().getItem().getItem() != Item.getItemFromBlock(ModBlocks.CONSTELLATION_BLOCK)) {
			return;
		}
		
		Constellation constellation = Constellation.getConstFromNumber(HelperConstellation.getConstNumber(event.getItem().getItem()));
		if(AlmagestCore.savedDataManager.getFlagData().getConstellation(constellation).isObtained() == false) {
			AlmagestCore.savedDataManager.getFlagData().setConstellationObtained(constellation);
		}
		
	}
	
	//モブを倒すことで星座ドロップ。
	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event) {
		
		EntityLivingBase entity = event.getEntityLiving();
		if(entity == null) {
			return;
		}
		
		//drop la calille-family constellation for hostile mobs.
		if(entity instanceof IMob && !(entity instanceof EntityDragon || entity instanceof EntityWither)) {
			if(entity.world.rand.nextDouble() < 0.1D) {
				
				ArrayList<Constellation> lacaille = ConstFamily.La_Caille.getMembers();
				int target = entity.world.rand.nextInt(lacaille.size());
				event.getDrops().add(
					new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ,
							HelperConstellation.getConstStack(lacaille.get(target), 1))
				);
			}
		}
		
		//drop bayer-family constellation for passive mob.
		if(entity instanceof IAnimals) {
			if(entity.world.rand.nextDouble() < 0.1D) {
				ArrayList<Constellation> bayel = ConstFamily.Bayer.getMembers();
				int target = entity.world.rand.nextInt(bayel.size());
				event.getDrops().add(
					new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ,
							HelperConstellation.getConstStack(bayel.get(target), 1))
				);
			}
		}
		
	}
	
	//動物は手なずけることでもドロップ。
	@SubscribeEvent
	public void onAnimalTame(AnimalTameEvent event) {
		
		EntityAnimal entity = event.getAnimal();
		if(entity == null) {
			return;
		}

		if(entity.world.rand.nextDouble() < 0.1D) {
			ArrayList<Constellation> bayel = ConstFamily.Bayer.getMembers();
			int target = entity.world.rand.nextInt(bayel.size());
			entity.world.spawnEntity(
				new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ,
						HelperConstellation.getConstStack(bayel.get(target), 1))
			);
		}
		
	}
	
	//ワールド生成時に星座を配置する。
	@SubscribeEvent
	public void onWorldDecorate(DecorateBiomeEvent.Pre event) {
		
		int dimensionId = event.getWorld().provider.getDimension();
		if(dimensionId != 0 && dimensionId != -1) {
			return;
		}
		
		//原則5で割り切れるチャンクに生成するが、正規分布で1~5に歪ませる。
		int diviser = 5 - (int)Math.round(MathHelper.clamp(this.nextNormalDist(event.getWorld().rand, 0.0D, 1.0D), 0.0D, 4.0D));
		if(event.getChunkPos().x%diviser != 0 || event.getChunkPos().z%diviser != 0) {
			return;
		}
		
		//更に10%の確率。
		if(event.getRand().nextDouble() > 0.1D) {
			return;
		}
		
		//更に、チャンク内のランダムの1点を取得し、airである場合に限り生成する。
		BlockPos pos = event.getChunkPos().getBlock(event.getRand().nextInt(16), event.getRand().nextInt(256), event.getRand().nextInt(16));
		IBlockState state = event.getWorld().getBlockState(pos);
		if(state.getBlock() != Blocks.AIR && !state.getBlock().isReplaceable(event.getWorld(), pos)) {
			return;
		}
		
		//生成開始。
		//一番下の地面に生成する。
		boolean stopFlag = false;
		BlockPos next = pos;
		while(true) {
			if(next.getY() == 1) {
				break;
			}
			
			IBlockState nextState = event.getWorld().getBlockState(next.down());
			if(nextState.getBlock() != Blocks.AIR && !nextState.getBlock().isReplaceable(event.getWorld(), next.down())) {
				stopFlag = true;
				break;
			}
			else {
				next = next.down();
			}
		}
		
		if(!stopFlag) {
			return;
		}
		
		//overworld(=0)なら、ラカイユ、バイエル、オリオン以外から生成する。
		//nether(=-1)ならオリオンから生成する。
		AlmagestCore.log(String.valueOf(stopFlag));
		ArrayList<Constellation> targetGroup = new ArrayList<Constellation>();
		if(dimensionId == -1) {
			targetGroup.addAll(ConstFamily.Orion.getMembers());
		}
		else {
			for(ConstFamily eachFamily: ConstFamily.values()) {
				if(eachFamily != ConstFamily.Orion && eachFamily != ConstFamily.La_Caille && eachFamily != ConstFamily.Bayer) {
					targetGroup.addAll(eachFamily.getMembers());
				}
			}
		}
		
		int target = event.getRand().nextInt(targetGroup.size());
		event.getWorld().setBlockState(next, ((BlockConstellation)ModBlocks.CONSTELLATION_BLOCK).getStateFromMeta(
				(targetGroup.get(target).getFamily() == ConstFamily.Zodiac) ? 1 : 0));
		AlmagestCore.log("generate constellation: {}/{}/{}.", next.getX(), next.getY(), next.getZ());
		if(!event.getWorld().isRemote) {
			TileEntity tile = event.getWorld().getTileEntity(next);
			if(tile != null && tile instanceof TileConstellation) {
				((TileConstellation)tile).setConstNumber(targetGroup.get(target).getId());
				((TileConstellation)tile).setMissingStars(new int[0]);
			}
		}
		//生成した星座の位置を記憶する。
		AlmagestCore.savedDataManager.getConstData().addBlock(event.getWorld(), next, targetGroup.get(target));
		
	}
	
	private double nextNormalDist(Random rand, double mean, double sd) {
		
		return rand.nextGaussian()*sd + mean;
		
	}
	
	@SubscribeEvent
	public void onBreakBlock(BlockEvent.BreakEvent event) {
		
		if(event.getWorld().isRemote) {
			return;
		}
		
		if(event.getState().getBlock() != ModBlocks.CONSTELLATION_BLOCK) {
			return;
		}
		
		AlmagestCore.savedDataManager.getConstData().removeBlock(event.getWorld(), event.getPos());
		
	}
	
/*	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event) {
		
		// プレイヤーダメージによる死亡のみ。
		if(event.source.getDamageType().equals("player") == false) {
			return;
		}
		
		EntityLivingBase entity = event.entityLiving;
		
		// 指定のモブのみ。
		MobType type = MobType.getType(entity);
		if(type == null) {
			return;
		}
		
		Random rand = entity.getRNG();
		int prov = 0;
		
		// モブごとに指定の星座をドロップ。
		Constellation dropConst = null;
		switch(type) {
			case Sheep:
				boolean sheared = ((EntitySheep)entity).getSheared();
				prov = rand.nextInt(20);
				if(sheared == true && prov < 10) {
					dropConst = Constellation.Cap;	// やぎ座
				}
				else if(prov == 0){
					dropConst = Constellation.Cap;
				}
				else if(prov < 5) {
					dropConst = Constellation.Ari;	// おひつじ座
				}
				break;
			case Cow:
				prov = rand.nextInt(20);
				if(prov == 0) {
					dropConst = Constellation.Boo;	// うしかい座
				}
				else if(prov < 5) {
					dropConst = Constellation.Tau;	// おうし座
				}
				break;
			case Mooshroom:
				dropConst = filterConst(Constellation.Boo, rand, 4);	// うしかい座
				break;
			case Pig:
				dropConst = filterConst(Constellation.Cyg, rand, 4);	// はくちょう座
				break;
			case Chicken:
				prov = rand.nextInt(16);
				if(prov == 0) {
					dropConst = Constellation.Aps;	// ふうちょう座
				}
				else if(prov == 1) {
					dropConst = Constellation.Tuc;	// きょしちょう座
				}
				else if(prov == 2) {
					dropConst = Constellation.Pav;	// くじゃく座
				}
				else if(prov == 3) {
					dropConst = Constellation.Gru;	//つる座
				}
				break;
			case Horse:
				int variant = ((EntityHorse)entity).getHorseVariant() & 255;
				prov = rand.nextInt(20);
				if(variant == 0 && prov < 5){
					dropConst = Constellation.Mon;	// いっかくじゅう座
				}
				else if(prov == 0) {
					dropConst = Constellation.Mon;
				}
				else if(prov < 5) {
					dropConst = Constellation.Equ;	// こうま座
				}
				break;
			case Rabbit:
				dropConst = filterConst(Constellation.Lep, rand, 4);
				break;
			case Wolf:
				boolean tamed = ((EntityWolf)entity).isTamed();
				prov = rand.nextInt(8);
				if(tamed == true) {
					if(prov == 0) {
						dropConst = Constellation.CMa;
					}
					else if(prov == 1) {
						dropConst = Constellation.CMi;
					}
				}
				else {
					if(prov == 0) {
						dropConst = Constellation.Lup;
					}
					else if (prov == 1) {
						dropConst = Constellation.CVn;
					}
				}
				break;
			case Ocelot:
				dropConst = filterConst(Constellation.Lyn, rand, 4);
				break;
			case Bat:
				dropConst = filterConst(Constellation.Crv, rand, 4);
				break;
			case Squid:
				dropConst = filterConst(Constellation.Del, rand, 4);
				break;
			case Zombie:
				dropConst = filterConst(Constellation.Ara, rand, 4);
				break;
			case Skeleton:
				if(((EntitySkeleton)entity).getSkeletonType() == 1) {
					dropConst = filterConst(Constellation.Sct, rand, 4);
				}
				else {
					dropConst = filterConst(Constellation.Sgr, rand, 4);
				}
				break;
			case Spider:
				dropConst = filterConst(Constellation.Sco, rand, 4);
				break;
			case Creeper:
				dropConst = filterConst(Constellation.Ori, rand, 4);
				break;
			case Slime:
				dropConst = filterConst(Constellation.Cha, rand, 4);
				break;
			case Silverfish:
				dropConst = filterConst(Constellation.Mus, rand, 4);
				break;
			case CaveSpider:
				dropConst = filterConst(Constellation.Ser, rand, 4);
				break;
			case Enderman:
				dropConst = filterConst(Constellation.UMa, rand, 4);
				break;
			case Endermite:
				dropConst = filterConst(Constellation.UMi, rand, 4);
				break;
			case Witch:
				dropConst = filterConst(Constellation.Oph, rand, 4);
				break;
			case PigMan:
				dropConst = filterConst(Constellation.Cen, rand, 4);
				break;
			case Ghast:
				dropConst = filterConst(Constellation.Col, rand, 4);
				break;
			case MagmaCube:
				dropConst = filterConst(Constellation.Lac, rand, 4);
				break;
			case Blaze:
				dropConst = filterConst(Constellation.Phe, rand, 4);
				break;
			case Guardian:
				if(((EntityGuardian)entity).isElder() == false) {
					dropConst = filterConst(Constellation.Hya, rand, 4);
				}
				else {
					dropConst = Constellation.Hyi;					
				}
				break;
			case EnderDragon:
				dropConst = Constellation.Dra;
				break;
			case Wither:
				dropConst = Constellation.Aql;
				break;
			case IronGolem:
				dropConst = filterConst(Constellation.Her, rand, 4);
				break;
			case SnowGolem:
				dropConst = filterConst(Constellation.Com, rand, 4);
				break;
			default:
				break;
		}
		
		if(dropConst == null) {
			return;
		}
		
		event.drops.add(new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ,
				HelperConstellation.getIncompleteConstStack(dropConst, 1)));
		
	}*/
	
/*	@SubscribeEvent
	public void onEntityInteract(EntityInteractEvent event) {
		
		if(event.entityPlayer.worldObj.isRemote == true) {
			return;
		}
		
		if(!(event.target instanceof EntityAnimal) && !(event.target instanceof EntityIronGolem)) {
			return;
		}
		EntityLivingBase entity = (EntityLivingBase)event.target;
		
		MobType type = MobType.getType(entity);
		if(type == null) {
			return;
		}
		
		ItemStack heldItem = event.entityPlayer.getHeldItem();
		if(entity instanceof EntityAnimal) {
			if(heldItem == null) {
				return;
			}
			EntityAnimal animal = (EntityAnimal)entity;
			if(animal.isBreedingItem(heldItem) == false || animal.isInLove() == true || animal.isChild() == true) {
				return;
			}
		}
		else {
			if(heldItem.getItem() != Item.getItemFromBlock(Blocks.red_flower)
					|| heldItem.getItemDamage() != BlockFlower.EnumFlowerType.POPPY.getMeta()) {
				return;
			}
		}
		
		Random rand = entity.getRNG();
		int prov = 0;
		
		Constellation dropConst = null;
		switch(type) {
			case Sheep:
				boolean sheared = ((EntitySheep)entity).getSheared();
				prov = rand.nextInt(20);
				if(sheared == true && prov < 10) {
					dropConst = Constellation.Cap;	// やぎ座
				}
				else if(prov == 0){
					dropConst = Constellation.Cap;
				}
				else if(prov < 10) {
					dropConst = Constellation.Ari;	// おひつじ座
				}
				break;
			case Cow:
				prov = rand.nextInt(20);
				if(prov == 0) {
					dropConst = Constellation.Boo;	// うしかい座
				}
				else if(prov < 10) {
					dropConst = Constellation.Tau;	// おうし座
				}
				break;
			case Mooshroom:
				dropConst = filterConst(Constellation.Boo, rand, 2);	// うしかい座
				break;
			case Pig:
				dropConst = filterConst(Constellation.Cyg, rand, 2);	// はくちょう座
				break;
			case Chicken:
				prov = rand.nextInt(8);
				if(prov == 0) {
					dropConst = Constellation.Aps;	// ふうちょう座
				}
				else if(prov == 1) {
					dropConst = Constellation.Tuc;	// きょしちょう座
				}
				else if(prov == 2) {
					dropConst = Constellation.Pav;	// くじゃく座
				}
				else if(prov == 3) {
					dropConst = Constellation.Gru;	//つる座
				}
				break;
			case Horse:
				int variant = ((EntityHorse)entity).getHorseVariant() & 255;
				prov = rand.nextInt(20);
				if(variant == 0 && prov < 10){
					dropConst = Constellation.Mon;	// いっかくじゅう座
				}
				else if(prov == 0) {
					dropConst = Constellation.Mon;
				}
				else if(prov < 10) {
					dropConst = Constellation.Equ;	// こうま座
				}
				break;
			case Rabbit:
				dropConst = filterConst(Constellation.Lep, rand, 2);
				break;
			case Wolf:
				boolean tamed = ((EntityWolf)entity).isTamed();
				prov = rand.nextInt(4);
				if(tamed == true) {
					if(prov == 0) {
						dropConst = Constellation.CMa;
					}
					else if(prov == 1) {
						dropConst = Constellation.CMi;
					}
				}
				else {
					if(prov == 0) {
						dropConst = Constellation.Lup;
					}
					else if (prov == 1) {
						dropConst = Constellation.CVn;
					}
				}
				break;
			case Ocelot:
				dropConst = filterConst(Constellation.Lyn, rand, 2);
				break;
			case IronGolem:
				EntityPlayer player = event.entityPlayer;
				if(!player.capabilities.isCreativeMode) {
					--heldItem.stackSize;
					if(heldItem.stackSize <= 0) {
						player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
					}
				}
				if(rand.nextInt(4) == 0) {
					dropConst = Constellation.Her;
				}
				break;
			default:
				break;
		}
		
		if(dropConst == null) {
			return;
		}
		
		EntityItem dropItem = new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ,
				HelperConstellation.getIncompleteConstStack(dropConst, 1));
		entity.worldObj.spawnEntityInWorld(dropItem);
				
	}*/

}
