package noki.almagest.helper;

public enum MobType {
	
	Sheep("EntitySheep"),
	Cow("EntityCow"),
	Pig("EntityPig"),
	Chicken("EntityChicken"),
	Horse("EntityHorse"),
	Rabbit("EntityRabbit"),
	Mooshroom("EntityMooshroom"),
	
	Wolf("EntityWolf"),
	Ocelot("EntityOcelot"),
	
	Squid("EntitySquid"),
	Bat("EntityBat"),
	
	Zombie("EntityZombie"),
	Skeleton("EntitySkeleton"),	// & WitherSkeleton.
	Spider("EntitySpider"),
	Creeper("EntityCreeper"),
	Slime("EntitySlime"),
	Silverfish("EntitySilverfish"),
	CaveSpider("EntityCaveSpider"),
	Witch("EntityWitch"),
	Enderman("EntityEnderman"),
	Endermite("EntityEndermite"),
	
	PigMan("EntityPigZombie"),
	Ghast("EntityGhast"),
	MagmaCube("EntityMagmaCube"),
	Blaze("EntityBlaze"),
	
	Guardian("EntityGuardian"),	// & Elder Guardian.
	
	EnderDragon("EntityDragon"),
	Wither("EntityWither"),
	
	IronGolem("EntityIronGolem"),
	SnowGolem("EntitySnowman");
	
	
	private String className;
	
	private MobType(String className) {
		
		this.className = className;
		
	}
	
	public String getClassName() {
		
		return this.className;
		
	}
	
	public static MobType getType(Object entity) {
		
		for(MobType each: MobType.values()) {
			if(each.getClassName().equals(entity.getClass().getSimpleName())) {
				return each;
			}
		}
		return null;
		
	}
	
}
