package noki.almagest.client.render.model;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.block.model.IBakedModel;
import noki.almagest.client.render.RenderResource;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.helper.HelperConstellation.LineData;
import noki.almagest.helper.HelperConstellation.MissingStar;
import noki.almagest.helper.HelperConstellation.Spectrum;
import noki.almagest.helper.HelperConstellation.StarData;


public class BakedModelConstellation  implements IBakedModel {
	
	public Constellation constellation;
	public boolean isMissing;
//	public List<BakedQuad> faceQuads = new ArrayList<BakedQuad>();
	public List<BakedQuad> generalQuads = new ArrayList<BakedQuad>();
	public ItemCameraTransforms transforms;
	
	
	@SuppressWarnings("deprecation")
	public BakedModelConstellation(Constellation constellation, boolean isMissing) {
		
		this.constellation = constellation;
		this.isMissing = isMissing;
		this.addQuads();
		
		//transforms.
		ItemTransformVec3f vecGui = new ItemTransformVec3f(new Vector3f(30,225,0), new Vector3f(0,0,0), new Vector3f(0.625F,0.625F,0.625F));
		ItemTransformVec3f vecGround = new ItemTransformVec3f(new Vector3f(0,0,0), new Vector3f(0,0.1F,0), new Vector3f(0.25F,0.25F,0.25F));
		ItemTransformVec3f vecFixed = new ItemTransformVec3f(new Vector3f(0,0,0), new Vector3f(0,0.1F,0), new Vector3f(0.5F,0.5F,0.5F));
		ItemTransformVec3f vecThirdLeft = new ItemTransformVec3f(new Vector3f(75,225,0), new Vector3f(0,0.1F,0), new Vector3f(0.375F,0.375F,0.375F));
		ItemTransformVec3f vecThirdRight = new ItemTransformVec3f(new Vector3f(75,45,0), new Vector3f(0,0.1F,0), new Vector3f(0.375F,0.375F,0.375F));
		ItemTransformVec3f vecFirstLeft = new ItemTransformVec3f(new Vector3f(0,225,0), new Vector3f(0,0,0), new Vector3f(0.4F,0.4F,0.4F));
		ItemTransformVec3f vecFirstRight = new ItemTransformVec3f(new Vector3f(0,45,0), new Vector3f(0,0,0), new Vector3f(0.4F,0.4F,0.4F));
		this.transforms = new ItemCameraTransforms(vecThirdLeft, vecThirdRight, vecFirstLeft, vecFirstRight,
				ItemCameraTransforms.DEFAULT.head, vecGui, vecGround, vecFixed);
		
	}
	
	@Override
	public boolean isAmbientOcclusion() {
		
		return false;
		
	}
	
	@Override
	public boolean isGui3d() {
		
		return true;
		
	}
	
	@Override
	public boolean isBuiltInRenderer() {
		
		return false;
		
	}
	
	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		
		return this.transforms;
		
	}
	
	private void addQuads() {
		
		int constNumber = this.constellation.getId();
		
		MissingStar[] missingStars = this.constellation.getMissingStars();
		
		ArrayList<StarData> stars = HelperConstellation.getStars(constNumber);
		List<LineData> lines = HelperConstellation.getLines(constNumber);
		
		
		//座標変換後の角度からブロック内の座標に補正。
		double minLong = Integer.MAX_VALUE;
		double minLat = Integer.MAX_VALUE;
		double maxLong = Integer.MIN_VALUE;
		double maxLat = Integer.MIN_VALUE;
		for(StarData each: stars) {
			minLong = Math.min(minLong, each.getCalculatedLong());
			minLat = Math.min(minLat, each.getCalculatedLat());
			maxLong = Math.max(maxLong, each.getCalculatedLong());
			maxLat = Math.max(maxLat, each.getCalculatedLat());
		}
		double defLong = Math.abs(maxLong - minLong);
		double defLat = Math.abs(maxLat - minLat);
		double weight = 0.85 / Math.max(defLong, defLat);
		double adjustLong = minLong * -1;
		double adjustLat = minLat * -1;
		double relativeAddLong = (1 - (defLong*weight)) / 2;
		double relativeAddLat = (1 - (defLat*weight)) / 2;
		
		for(StarData each: stars) {
			double relativeX = (each.getCalculatedLong() + adjustLong) * weight + relativeAddLong;
			double relativeY = (each.getCalculatedLat() + adjustLat) * weight + relativeAddLat;
			
			if(this.isMissing == true) {
				boolean flag = false;
				for(MissingStar star: missingStars) {
					if(each.hip == star.getStarNumber()) {
						flag = true;
						break;
					}
				}
				this.addStarQuads(relativeX, relativeY, 0.5, each.magnitude, (flag == true) ? null : each.spectrum);
			}
			else {
				this.addStarQuads(relativeX, relativeY, 0.5, each.magnitude, each.spectrum);				
			}
		}
		
		if(this.isMissing == false) {
			for(LineData each: lines) {
				double relativeX1 = (each.star1.getCalculatedLong() + adjustLong) * weight + relativeAddLong;
				double relativeY1 = (each.star1.getCalculatedLat() + adjustLat) * weight + relativeAddLat;			
				double relativeX2 = (each.star2.getCalculatedLong() + adjustLong) * weight + relativeAddLong;
				double relativeY2 = (each.star2.getCalculatedLat() + adjustLat) * weight + relativeAddLat;
				this.addLines(relativeX1, relativeY1, relativeX2, relativeY2);
			}
		}
		
		
		
		//frame.
		FaceBakery faceBakery = new FaceBakery();
		BlockFaceUV uv = new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0);
		BlockPartFace partFace;
		BakedQuad quad;
		
		
		ResourceLocation frameTexture = RenderResource.textureFrame;
		if(this.constellation.isEcliptical()) {
			frameTexture = RenderResource.textureFrameE;
		}
		TextureAtlasSprite frameSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(frameTexture.toString());
		Vector3f from = new Vector3f(0, 0, 0);
		Vector3f to = new Vector3f(16, 16, 16);
		
		for(EnumFacing face: EnumFacing.VALUES) {
			partFace = new BlockPartFace(face, face.getIndex(), frameTexture.toString(), uv);
			quad = faceBakery.makeBakedQuad(from, to, partFace, frameSprite, face, ModelRotation.X0_Y0,
				null, true, true);
			this.generalQuads.add(quad);
		}
		
	}
	
	private void addStarQuads(double x, double y, double z, double magnitude, Spectrum spectrum) {
		
		float posX = ((float)x/1.0F) * 16.0F;
		float posY = ((float)y/1.0F) * 16.0F;
		float posZ = ((float)z/1.0F) * 16.0F;
		float radius = ((float)(Math.min(0.1F, (7.0F-(float)magnitude)*0.015F) / 1.0F) * 16.0F) / 2;
		Vector3f from = new Vector3f(posX-radius, posY-radius, posZ-radius);
		Vector3f to = new Vector3f(posX+radius, posY+radius, posZ+radius);
		
		ResourceLocation texture = RenderResource.textureBlank;
		if(spectrum != null) {
			switch(spectrum) {
				case W:
					texture = RenderResource.textureW;
//					sprite = RenderResource.spriteW;
					break;
				case O:
					texture = RenderResource.textureO;
//					sprite = RenderResource.spriteO;
					break;
				case B:
					texture = RenderResource.textureB;
//					sprite = RenderResource.spriteB;
					break;
				case A:
					texture = RenderResource.textureA;
//					sprite = RenderResource.spriteA;
					break;
				case F:
					texture = RenderResource.textureF;
//					sprite = RenderResource.spriteF;
					break;
				case G:
					texture = RenderResource.textureG;
//					sprite = RenderResource.spriteG;
					break;
				case K:
					texture = RenderResource.textureK;
//					sprite = RenderResource.spriteK;
					break;
				case M:
					texture = RenderResource.textureM;
//					sprite = RenderResource.spriteM;
					break;
			}
		}
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());
//		TextureAtlasSprite sprite = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getTextureMap().getAtlasSprite(texture.toString());
		
		FaceBakery faceBakery = new FaceBakery();
		BlockFaceUV uv = new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0);
		BlockPartFace partFace;
		BakedQuad quad;
		
		for(EnumFacing face: EnumFacing.VALUES) {
			partFace = new BlockPartFace(face, face.getIndex(), texture.toString(), uv);
			quad = faceBakery.makeBakedQuad(from, to, partFace, sprite, face, ModelRotation.X0_Y0,
				null, true, true);
			this.generalQuads.add(quad);
		}
		
	}
	
	private void addLines(double relativeX1, double relativeY1, double relativeX2, double relativeY2) {
		
		relativeX1 = ((float)relativeX1/1.0F) * 16.0F;
		relativeY1 = ((float)relativeY1/1.0F) * 16.0F;
		relativeX2 = ((float)relativeX2/1.0F) * 16.0F;
		relativeY2 = ((float)relativeY2/1.0F) * 16.0F;
		double z = 8.0D;
		
/*		GL11.glPushMatrix();
		GL11.glTranslated(-0.5D, -0.5D, -0.5D);*/
		
		BufferBuilder renderer= Tessellator.getInstance().getBuffer();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);	// alpha blend.
		GL11.glEnable(GL11.GL_BLEND);
		GlStateManager.color(1F, 1F, 1F, 200F/255F);
		
		renderer.begin(GL11.GL_LINES, DefaultVertexFormats.ITEM);
		GL11.glLineWidth(2);
		renderer.pos(relativeX1, relativeY1, z).endVertex();
		renderer.pos(relativeX2, relativeY2, z).endVertex();
		Tessellator.getInstance().draw();
		
		GL11.glDisable(GL11.GL_BLEND);
//		GL11.glPopMatrix();
		
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		
/*		if(side != null) {
			return new ArrayList<BakedQuad>();
		}
		
		Tessellator.getInstance().draw();
		
		int constNumber = this.constellation.getId();
		
		ArrayList<StarData> stars = HelperConstellation.getStars(constNumber);
		List<LineData> lines = HelperConstellation.getLines(constNumber);
		
		
		//座標変換後の角度からブロック内の座標に補正。
		double minLong = Integer.MAX_VALUE;
		double minLat = Integer.MAX_VALUE;
		double maxLong = Integer.MIN_VALUE;
		double maxLat = Integer.MIN_VALUE;
		for(StarData each: stars) {
			minLong = Math.min(minLong, each.getCalculatedLong());
			minLat = Math.min(minLat, each.getCalculatedLat());
			maxLong = Math.max(maxLong, each.getCalculatedLong());
			maxLat = Math.max(maxLat, each.getCalculatedLat());
		}
		double defLong = Math.abs(maxLong - minLong);
		double defLat = Math.abs(maxLat - minLat);
		double weight = 0.85 / Math.max(defLong, defLat);
		double adjustLong = minLong * -1;
		double adjustLat = minLat * -1;
		double relativeAddLong = (1 - (defLong*weight)) / 2;
		double relativeAddLat = (1 - (defLat*weight)) / 2;
		
		if(this.isMissing == false) {
			for(LineData each: lines) {
				double relativeX1 = (each.star1.getCalculatedLong() + adjustLong) * weight + relativeAddLong;
				double relativeY1 = (each.star1.getCalculatedLat() + adjustLat) * weight + relativeAddLat;			
				double relativeX2 = (each.star2.getCalculatedLong() + adjustLong) * weight + relativeAddLong;
				double relativeY2 = (each.star2.getCalculatedLat() + adjustLat) * weight + relativeAddLat;
				this.addLines(relativeX1, relativeY1, relativeX2, relativeY2);
			}
		}
		
		Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.ITEM);*/
		
		return this.generalQuads;
		
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture() {
		
		return null;
		
	}

	@Override
	public ItemOverrideList getOverrides() {
		
		return null;
		
	}
	
}
