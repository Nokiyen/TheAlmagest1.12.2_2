package noki.almagest.saveddata.gamedata;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public enum ERandomTalkTsuchinoko {
	
	TALK001(1),
	TALK002(1);
	
	private int talkEnd;
	private ResourceLocation resource;
	private ERandomTalkTsuchinoko(int talkEnd) {
		this.talkEnd = talkEnd;
		this.resource = new ResourceLocation("almagest.talk.tsuchinoko.random."+this.toString().toLowerCase());
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
	
	public static ERandomTalkTsuchinoko getRandomTalk() {
		int target = new Random().nextInt(ERandomTalkTsuchinoko.values().length);
		return ERandomTalkTsuchinoko.values()[target];
	}
	
}
