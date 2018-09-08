package noki.almagest.saveddata.gamedata;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public enum ETalkMira {
	
	TALK001(3),
	TALK002(3),
	TALK003(3),
	TALK004(3),
	TALK005(3),
	TALK006(3),
	TALK007(3),
	TALK008(3),
	TALK009(3),
	TALK010(3);
	
	
	private int talkEnd;
	private ResourceLocation resource;
	private ETalkMira(int talkEnd) {
		this.talkEnd = talkEnd;
		this.resource = new ResourceLocation("almagest.talk.mira.random."+this.toString().toLowerCase());
	}
	public ResourceLocation getResource() {
		return this.resource;
	}
	public String getLocalTalk(int talkId) {
		return new TextComponentTranslation(this.resource.getResourcePath()+"."+talkId).getFormattedText();
	}
	public int getTalkEnd(){
		return this.talkEnd;
	}
	
	public static ETalkMira getRandomTalk() {
		int target = new Random().nextInt(ETalkMira.values().length);
		return ETalkMira.values()[target];
	}
	
}
