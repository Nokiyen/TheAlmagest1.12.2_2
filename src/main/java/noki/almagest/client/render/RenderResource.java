package noki.almagest.client.render;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.ModInfo;

public class RenderResource {
	
	private static final String domain = ModInfo.ID.toLowerCase();
	private static final String path = "blocks/constellation_";
	public static final ResourceLocation textureO = new ResourceLocation(domain, path+"star_o");
	public static final ResourceLocation textureB = new ResourceLocation(domain, path+"star_b");
	public static final ResourceLocation textureA = new ResourceLocation(domain, path+"star_a");
	public static final ResourceLocation textureF = new ResourceLocation(domain, path+"star_f");
	public static final ResourceLocation textureG = new ResourceLocation(domain, path+"star_g");
	public static final ResourceLocation textureK = new ResourceLocation(domain, path+"star_k");
	public static final ResourceLocation textureM = new ResourceLocation(domain, path+"star_m");
	public static final ResourceLocation textureW = new ResourceLocation(domain, path+"star_w");
	public static final ResourceLocation textureBlank = new ResourceLocation(domain, path+"star_blank");
	public static final ResourceLocation textureFrame = new ResourceLocation(domain, path+"frame");
	public static final ResourceLocation textureFrameE = new ResourceLocation(domain, path+"frame_e");
	
	public static TextureAtlasSprite spriteO;
	public static TextureAtlasSprite spriteB;
	public static TextureAtlasSprite spriteA;
	public static TextureAtlasSprite spriteF;
	public static TextureAtlasSprite spriteG;
	public static TextureAtlasSprite spriteK;
	public static TextureAtlasSprite spriteM;
	public static TextureAtlasSprite spriteW;
	public static TextureAtlasSprite spriteBlank;
	public static TextureAtlasSprite spriteFrame;
	public static TextureAtlasSprite spriteFrameE;
	
	public static void registerTexture(TextureStitchEvent event) {
		
		AlmagestCore.log("enter registerTexture().");
		spriteO = event.getMap().registerSprite(RenderResource.textureO);
		spriteB = event.getMap().registerSprite(RenderResource.textureB);
		spriteA = event.getMap().registerSprite(RenderResource.textureA);
		spriteF = event.getMap().registerSprite(RenderResource.textureF);
		spriteG = event.getMap().registerSprite(RenderResource.textureG);
		spriteK = event.getMap().registerSprite(RenderResource.textureK);
		spriteM = event.getMap().registerSprite(RenderResource.textureM);
		spriteW = event.getMap().registerSprite(RenderResource.textureW);
		spriteBlank = event.getMap().registerSprite(RenderResource.textureBlank);
		spriteFrame = event.getMap().registerSprite(RenderResource.textureFrame);
		spriteFrameE = event.getMap().registerSprite(RenderResource.textureFrameE);
		
	}

}
