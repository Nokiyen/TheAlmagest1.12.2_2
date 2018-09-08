package noki.almagest.client.render.tile;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix3d;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import noki.almagest.AlmagestCore;
import noki.almagest.ModInfo;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperConstellation.LineData;
import noki.almagest.helper.HelperConstellation.Spectrum;
import noki.almagest.helper.HelperConstellation.StarData;
import noki.almagest.tile.TileConstellation;


/**********
 * @class RenderTileConstellation
 *
 * @description 星座ブロックを描画するクラスです。
 * @see BlockConstellation, TileConstellation.
 */
public class RenderTileConstellation extends TileEntitySpecialRenderer<TileConstellation> {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String domain = ModInfo.ID.toLowerCase();
	private static final String path = "textures/blocks/constellation_";
	private static final ResourceLocation textureO = new ResourceLocation(domain, path+"star_o.png");
	private static final ResourceLocation textureB = new ResourceLocation(domain, path+"star_b.png");
	private static final ResourceLocation textureA = new ResourceLocation(domain, path+"star_a.png");
	private static final ResourceLocation textureF = new ResourceLocation(domain, path+"star_f.png");
	private static final ResourceLocation textureG = new ResourceLocation(domain, path+"star_g.png");
	private static final ResourceLocation textureK = new ResourceLocation(domain, path+"star_k.png");
	private static final ResourceLocation textureM = new ResourceLocation(domain, path+"star_m.png");
	private static final ResourceLocation textureW = new ResourceLocation(domain, path+"star_w.png");
	private static final ResourceLocation textureBlank = new ResourceLocation(domain, path+"star_blank.png");
	private static final ResourceLocation frame = new ResourceLocation(domain, path+"frame.png");
	private static final ResourceLocation frameE = new ResourceLocation(domain, path+"frame_e.png");

//	private boolean flag = false;
	private boolean flag2 = false;
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public void render(TileConstellation tile, double x, double y, double z, float partialTickFrame, int destroy, float par7) {
		
		int constNumber = ((TileConstellation)tile).getConstNumber();
		if(constNumber == 0) {
			return;
		}
		
/*		if(constNumber == 1) {
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder vertexBuffer = tessellator.getBuffer();
			
	        GlStateManager.enableBlend();
	        GlStateManager.disableTexture2D();
	        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.color(1F, 1F, 1F, 1F);
			
//			RenderHelper.disableStandardItemLighting();
			
			for(int i=1; i<=88; i++) {
				ArrayList<StarData> targetStars = HelperConstellation.getStars(i);
				
				for(StarData each: targetStars) {
					double radius = 5;
//					double lat = Math.toRadians(each.getCalculatedLat());
//					double lon = Math.toRadians(each.getCalculatedLong());
					GlStateManager.pushMatrix();
					GL11.glTranslated(x, y, z);
					GL11.glTranslated(0.5, 0.5, 0.5);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glRotated(each.getCalculatedLong(), 0.0D, 1.0D, 0.0D);
					GL11.glRotated(-1.0D*each.getCalculatedLat(), 1.0D, 0.0D, 0.0D);
//					double starY = radius * Math.sin(lat);
//					double starX = radius * Math.cos(lat) * Math.sin(lon);
//					double starZ = radius * Math.cos(lat) * Math.cos(lon);
					vertexBuffer.begin(7, DefaultVertexFormats.POSITION);
					vertexBuffer.pos(0.05F, 0.05F, radius).endVertex();
					vertexBuffer.pos(0.05F, -0.05F, radius).endVertex();
					vertexBuffer.pos(-0.05F, -0.05F, radius).endVertex();
					vertexBuffer.pos(-0.05F, 0.05F, radius).endVertex();
					tessellator.draw();
					GlStateManager.popMatrix();
				}
			}
			
			for(int i=1; i<=88; i++) {
				List<LineData> targetLines = HelperConstellation.getLines(i);
				
				for(LineData each: targetLines) {
					double radius = 5;
					GlStateManager.pushMatrix();
					
					GL11.glLineWidth(1);
					GL11.glTranslated(x, y, z);
					GL11.glTranslated(0.5, 0.5, 0.5);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					vertexBuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
					double star1Y = radius * Math.sin(Math.toRadians(each.star1.getCalculatedLat()));
					double star1X = radius * Math.cos(Math.toRadians(each.star1.getCalculatedLat())) * Math.sin(Math.toRadians(each.star1.getCalculatedLong()));
					double star1Z = radius * Math.cos(Math.toRadians(each.star1.getCalculatedLat())) * Math.cos(Math.toRadians(each.star1.getCalculatedLong()));
					double star2Y = radius * Math.sin(Math.toRadians(each.star2.getCalculatedLat()));
					double star2X = radius * Math.cos(Math.toRadians(each.star2.getCalculatedLat())) * Math.sin(Math.toRadians(each.star2.getCalculatedLong()));
					double star2Z = radius * Math.cos(Math.toRadians(each.star2.getCalculatedLat())) * Math.cos(Math.toRadians(each.star2.getCalculatedLong()));
					vertexBuffer.pos(star1X, star1Y, star1Z).endVertex();
					vertexBuffer.pos(star2X, star2Y, star2Z).endVertex();
					tessellator.draw();
					
					GlStateManager.popMatrix();
					
					GlStateManager.pushMatrix();
					
					GL11.glLineWidth(3);
					GL11.glTranslated(x, y, z);
					GL11.glTranslated(0.5, 0.5, 0.5);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					
					GlStateManager.rotate(-1*(float)each.star1.getCalculatedLat(), 1.0F, 0.0F, 0.0F);
					GlStateManager.rotate((float)each.star1.getCalculatedLong()-90F, 0.0F, 1.0F, 0.0F);
					vertexBuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
					
					vertexBuffer.pos(radius, 0.0F, 0.0F).endVertex();
					
					GlStateManager.rotate((float)each.star1.getCalculatedLat(), 1.0F, 0.0F, 0.0F);
					GlStateManager.rotate(-1*((float)each.star1.getCalculatedLong()-90F), 0.0F, 1.0F, 0.0F);
					GlStateManager.rotate(-1*(float)each.star2.getCalculatedLat(), 1.0F, 0.0F, 0.0F);
					GlStateManager.rotate((float)each.star2.getCalculatedLong()-90F, 0.0F, 1.0F, 0.0F);
					
//					GL11.glTranslated(x, y, z);
//					GL11.glTranslated(0.5, 0.5, 0.5);
//					GlStateManager.rotate(-1*(float)each.star2.getCalculatedLat(), 1.0F, 0.0F, 0.0F);
//					GlStateManager.rotate((float)each.star2.getCalculatedLong()-90F, 0.0F, 1.0F, 0.0F);
					vertexBuffer.pos(radius, 0.0F, 0.0F).endVertex();
					
					tessellator.draw();

					GlStateManager.popMatrix();

				}
			}
	        GlStateManager.disableBlend();
	        GlStateManager.enableTexture2D();
//			RenderHelper.disableStandardItemLighting();
//			RenderHelper.enableStandardItemLighting();
			return;
		}*/
		
		int[] missingStars = ((TileConstellation)tile).getMissingStars();
		
		ArrayList<StarData> stars = HelperConstellation.getStars(constNumber);
		List<LineData> lines = HelperConstellation.getLines(constNumber);
		
		GL11.glPushMatrix();
		
		GL11.glTranslated(x, y, z);
		RenderHelper.disableStandardItemLighting();
//		setLight(0.4F, 0.6F, 0.0F);
		
		this.renderInside(tile);

		GL11.glTranslated(0.5, 0.5, 0.5);
		GL11.glRotatef((float)((TileConstellation)tile).getRotation()*(360F/1200F), 0.0F, 1.0F, 0.0F);
		GL11.glTranslated(-0.5, -0.5, -0.5);
		
		//各星の緯度・経度を、ブロックの1*1*1内の座標に補正する。
		
		//元の角度から、中心の角度を決定。
/*		double minLongOrigin = Integer.MAX_VALUE;
		double minLatOrigin = Integer.MAX_VALUE;
		double maxLongOrigin = Integer.MIN_VALUE;
		double maxLatOrigin = Integer.MIN_VALUE;
		for(StarData each: stars) {
			minLongOrigin = Math.min(minLongOrigin, each.getCalculatedLong());
			minLatOrigin = Math.min(minLatOrigin, each.getCalculatedLat());
			maxLongOrigin = Math.max(maxLongOrigin, each.getCalculatedLong());
			maxLatOrigin = Math.max(maxLatOrigin, each.getCalculatedLat());
		}
		double centerLong = (minLongOrigin + maxLongOrigin) / 2;
		double centerLat = (minLatOrigin + maxLatOrigin) / 2;*/
		
		//座標変換後の角度からブロック内の座標に補正。
		double minLong = Integer.MAX_VALUE;
		double minLat = Integer.MAX_VALUE;
		double maxLong = Integer.MIN_VALUE;
		double maxLat = Integer.MIN_VALUE;
/*		for(StarData each: stars) {
			Vec3 vector = new Vec3(each.getCalculatedLong(), each.getCalculatedLat(), 1);
			Vec3 adjusted = this.getAdjustedDegrees(vector, centerLong, centerLat);
			minLong = Math.min(minLong, adjusted.xCoord);
			minLat = Math.min(minLat, adjusted.yCoord);
			maxLong = Math.max(maxLong, adjusted.xCoord);
			maxLat = Math.max(maxLat, adjusted.yCoord);
		}*/
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
		
/*		for(StarData each: stars) {
			Vec3 vector = new Vec3(each.getCalculatedLong(), each.getCalculatedLat(), 1);
			Vec3 adjusted = this.getAdjustedDegrees(vector, centerLong, centerLat);
			double relativeX = (adjusted.xCoord + adjustLong) * weight + relativeAddLong;
			double relativeY = (adjusted.yCoord + adjustLat) * weight + relativeAddLat;
			boolean flag = false;
			for(int star: missingStars) {
				if(each.hip == star) {
					flag = true;
					break;
				}
			}
			if(flag == true) {
				this.renderStar(relativeX, relativeY, 0.5, each.magnitude, null);
			}
			else {
				this.renderStar(relativeX, relativeY, 0.5, each.magnitude, each.spectrum);
			}
		}*/
		for(StarData each: stars) {
			double relativeX = (each.getCalculatedLong() + adjustLong) * weight + relativeAddLong;
			double relativeY = (each.getCalculatedLat() + adjustLat) * weight + relativeAddLat;
			boolean flag = false;
			for(int star: missingStars) {
				if(each.hip == star) {
					flag = true;
					break;
				}
			}
			if(flag == true) {
				this.renderStar(relativeX, relativeY, 0.5, each.magnitude, null);
			}
			else {
				this.renderStar(relativeX, relativeY, 0.5, each.magnitude, each.spectrum);
			}
		}
		
/*		for(LineData each: lines) {
			Vec3 vector1 = new Vec3(each.star1.getCalculatedLong(), each.star1.getCalculatedLat(), 1);
			Vec3 adjusted1 = this.getAdjustedDegrees(vector1, centerLong, centerLat);
			Vec3 vector2 = new Vec3(each.star2.getCalculatedLong(), each.star2.getCalculatedLat(), 1);
			Vec3 adjusted2 = this.getAdjustedDegrees(vector2, centerLong, centerLat);

			double relativeX1 = (adjusted1.xCoord + adjustLong) * weight + relativeAddLong;
			double relativeY1 = (adjusted1.yCoord + adjustLat) * weight + relativeAddLat;			
			double relativeX2 = (adjusted2.xCoord + adjustLong) * weight + relativeAddLong;
			double relativeY2 = (adjusted2.yCoord + adjustLat) * weight + relativeAddLat;
			this.renderLine(relativeX1, relativeY1, relativeX2, relativeY2);
		}*/
		for(LineData each: lines) {
			double relativeX1 = (each.star1.getCalculatedLong() + adjustLong) * weight + relativeAddLong;
			double relativeY1 = (each.star1.getCalculatedLat() + adjustLat) * weight + relativeAddLat;			
			double relativeX2 = (each.star2.getCalculatedLong() + adjustLong) * weight + relativeAddLong;
			double relativeY2 = (each.star2.getCalculatedLat() + adjustLat) * weight + relativeAddLat;
			this.renderLine(relativeX1, relativeY1, relativeX2, relativeY2);
		}
		
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		RenderHelper.enableStandardItemLighting();
		
	}
	
	private void renderStar(double x, double y, double z, double magnitude, Spectrum spectrum) {

		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);	// alpha blend.
		GL11.glEnable(GL11.GL_BLEND);

		if(spectrum == null) {
			this.bindTexture(textureBlank);			
		}
		else {
			switch(spectrum) {
				case W:
					this.bindTexture(textureW); break;
				case O:
					this.bindTexture(textureO); break;
				case B:
					this.bindTexture(textureB); break;
				case A:
					this.bindTexture(textureA); break;
				case F:
					this.bindTexture(textureF); break;
				case G:
					this.bindTexture(textureG); break;
				case K:
					this.bindTexture(textureK); break;
				case M:
					this.bindTexture(textureM); break;
			}
		}
		
		double size = Math.min(0.1, (7 - magnitude) * 0.015);
//		double size = 0.5;
		double minX = -1 * size/2;
		double minY = -1 * size/2;
		double minZ = -1 * size/2;
		double maxX = size/2;
		double maxY = size/2;
		double maxZ = size/2;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();
		renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		
		renderer.pos(minX, minY, minZ).tex(0, 0).endVertex();
		renderer.pos(minX, maxY, minZ).tex(0, 1).endVertex();
		renderer.pos(maxX, maxY, minZ).tex(1, 1).endVertex();
		renderer.pos(maxX, minY, minZ).tex(1, 0).endVertex();
		
		renderer.pos(minX, minY, maxZ).tex(0, 0).endVertex();
		renderer.pos(maxX, minY, maxZ).tex(1, 0).endVertex();
		renderer.pos(maxX, maxY, maxZ).tex(1, 1).endVertex();
		renderer.pos(minX, maxY, maxZ).tex(0, 1).endVertex();
		
		renderer.pos(minX, minY, minZ).tex(0, 0).endVertex();
		renderer.pos(maxX, minY, minZ).tex(0, 1).endVertex();
		renderer.pos(maxX, minY, maxZ).tex(1, 1).endVertex();
		renderer.pos(minX, minY, maxZ).tex(1, 0).endVertex();
		
		renderer.pos(minX, maxY, minZ).tex(0, 0).endVertex();
		renderer.pos(minX, maxY, maxZ).tex(1, 0).endVertex();
		renderer.pos(maxX, maxY, maxZ).tex(1, 1).endVertex();
		renderer.pos(maxX, maxY, minZ).tex(0, 1).endVertex();
		
		renderer.pos(minX, minY, minZ).tex(0, 0).endVertex();
		renderer.pos(minX, minY, maxZ).tex(0, 1).endVertex();
		renderer.pos(minX, maxY, maxZ).tex(1, 1).endVertex();
		renderer.pos(minX, maxY, minZ).tex(1, 0).endVertex();
		
		renderer.pos(maxX, minY, minZ).tex(0, 0).endVertex();
		renderer.pos(maxX, maxY, minZ).tex(1, 0).endVertex();
		renderer.pos(maxX, maxY, maxZ).tex(1, 1).endVertex();
		renderer.pos(maxX, minY, maxZ).tex(0, 1).endVertex();
		
		tessellator.draw();
		GL11.glPopMatrix();
		
		GL11.glDisable(GL11.GL_BLEND);
		
	}
	
	private void renderLine(double relativeX1, double relativeY1, double relativeX2, double relativeY2) {
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);	// alpha blend.
		GL11.glEnable(GL11.GL_BLEND);
		GlStateManager.color(1F, 1F, 1F, 200F/255F);
		
		renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		GL11.glLineWidth(2);
		renderer.pos(relativeX1, relativeY1, 0.5).endVertex();;
		renderer.pos(relativeX2, relativeY2, 0.5).endVertex();;
		tessellator.draw();
		
		GL11.glDisable(GL11.GL_BLEND);
		
	}
	
	private void renderInside(TileEntity tile) {
		
		if(tile.getBlockMetadata() == 1 || tile.getBlockMetadata() == 3) {
			this.bindTexture(frameE);
		}
		else {
			this.bindTexture(frame);
		}
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();
		renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		
		renderer.pos(0.001, 0.001, 0.001).tex(0, 0).endVertex();
		renderer.pos(0.999, 0.001, 0.001).tex(0, 1).endVertex();
		renderer.pos(0.999, 0.999, 0.001).tex(1, 1).endVertex();
		renderer.pos(0.001, 0.999, 0.001).tex(1, 0).endVertex();
		
		renderer.pos(0.001, 0.001, 0.999).tex(0, 0).endVertex();
		renderer.pos(0.001, 0.999, 0.999).tex(1, 0).endVertex();
		renderer.pos(0.999, 0.999, 0.999).tex(1, 1).endVertex();
		renderer.pos(0.999, 0.001, 0.999).tex(0, 1).endVertex();
		
		renderer.pos(0.001, 0.001, 0.001).tex(0, 0).endVertex();
		renderer.pos(0.001, 0.001, 0.999).tex(0, 1).endVertex();
		renderer.pos(0.999, 0.001, 0.999).tex(1, 1).endVertex();
		renderer.pos(0.999, 0.001, 0.001).tex(1, 0).endVertex();
		
		renderer.pos(0.001, 0.999, 0.001).tex(0, 0).endVertex();
		renderer.pos(0.999, 0.999, 0.001).tex(1, 0).endVertex();
		renderer.pos(0.999, 0.999, 0.999).tex(1, 1).endVertex();
		renderer.pos(0.001, 0.999, 0.999).tex(0, 1).endVertex();
		
		renderer.pos(0.001, 0.001, 0.001).tex(0, 0).endVertex();
		renderer.pos(0.001, 0.999, 0.001).tex(0, 1).endVertex();
		renderer.pos(0.001, 0.999, 0.999).tex(1, 1).endVertex();
		renderer.pos(0.001, 0.001, 0.999).tex(1, 0).endVertex();
		
		renderer.pos(0.999, 0.001, 0.001).tex(0, 0).endVertex();
		renderer.pos(0.999, 0.001, 0.999).tex(1, 0).endVertex();
		renderer.pos(0.999, 0.999, 0.999).tex(1, 1).endVertex();
		renderer.pos(0.999, 0.999, 0.001).tex(0, 1).endVertex();
		
		tessellator.draw();
		
	}
	
/*	private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);
	private static final Vec3 field_82884_b = Vec3.createVectorHelper(0.20000000298023224D, 1.0D, -0.699999988079071D).normalize();
	private static final Vec3 field_82885_c = Vec3.createVectorHelper(-0.20000000298023224D, 1.0D, 0.699999988079071D).normalize();
	
	public static void setLight(float diffuse, float ambient, float specular) {
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
		float f = diffuse;
		float f1 = ambient;
		float f2 = specular;
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, setColorBuffer(field_82884_b.xCoord, field_82884_b.yCoord, field_82884_b.zCoord, 0.0D));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, setColorBuffer(f1, f1, f1, 1.0F));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, setColorBuffer(f2, f2, f2, 1.0F));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, setColorBuffer(field_82885_c.xCoord, field_82885_c.yCoord, field_82885_c.zCoord, 0.0D));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, setColorBuffer(f1, f1, f1, 1.0F));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, setColorBuffer(f2, f2, f2, 1.0F));
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, setColorBuffer(f, f, f, 1.0F));
		
	}
	
	private static FloatBuffer setColorBuffer(float p_74521_0_, float p_74521_1_, float p_74521_2_, float p_74521_3_) {
		
		colorBuffer.clear();
		colorBuffer.put(p_74521_0_).put(p_74521_1_).put(p_74521_2_).put(p_74521_3_);
		colorBuffer.flip();
		return colorBuffer;
		
	}

	private static FloatBuffer setColorBuffer(double p_74517_0_, double p_74517_2_, double p_74517_4_, double p_74517_6_) {
		
		return setColorBuffer((float)p_74517_0_, (float)p_74517_2_, (float)p_74517_4_, (float)p_74517_6_);
		
	}*/
	
	public Vec3d getAdjustedDegrees(Vec3d vector, double rotationZ, double rotationY) {
		
		if(flag2 == false) {
			AlmagestCore.log("Original A is %s.", vector.x);
			AlmagestCore.log("Original D is %s.", vector.y);
			AlmagestCore.log("Original Z is %s.", rotationZ);
			AlmagestCore.log("Original Y is %s.", rotationY);
		}

		double radianA = Math.toRadians(vector.x);
		double radianD = Math.toRadians(vector.y);
		double radianZ = Math.toRadians(rotationZ);
		double radianY = Math.toRadians(rotationY);

		if(flag2 == false) {
			AlmagestCore.log("Radian A is %s.", radianA);
			AlmagestCore.log("Radian D is %s.", radianD);
			AlmagestCore.log("Radian Z is %s.", radianZ);
			AlmagestCore.log("Radian Y is %s.", radianY);
		}

		Matrix3d origin = new Matrix3d(
				Math.cos(radianD)*Math.cos(radianA),	0, 0,
				Math.cos(radianD)*Math.sin(radianA),	0, 0,
				Math.sin(radianD),						0, 0);
		Matrix3d matrixZ = new Matrix3d(
				Math.cos(radianZ),	Math.sin(radianZ),	0,
				-Math.sin(radianZ),	Math.cos(radianZ),	0,
				0,					0,					1);
		Matrix3d matrixY = new Matrix3d(
				Math.cos(radianY),	0,	-Math.sin(radianY),
				0,					1,	0,
				Math.sin(radianY),	0,	Math.cos(radianY));
		
		matrixZ.mul(origin);
		if(flag2 == false) {
			AlmagestCore.log("first result 00 is %s.", matrixZ.getM00());
			AlmagestCore.log("first result 10 is %s.", matrixZ.getM10());
			AlmagestCore.log("first result 20 is %s.", matrixZ.getM20());
		}
		matrixY.mul(matrixZ);
		if(flag2 == false) {
			AlmagestCore.log("second result 00 is %s.", matrixY.getM00());
			AlmagestCore.log("second result 10 is %s.", matrixY.getM10());
			AlmagestCore.log("second result 20 is %s.", matrixY.getM20());
		}
		
		double resRadianD = Math.asin(matrixY.getM20());
		if(matrixY.getM20() < 0) {
			resRadianD = -1*resRadianD;
		}
		double resRadianA = Math.acos(matrixY.getM00()/Math.cos(resRadianD));
		if(matrixY.getM10() < 0) {
			resRadianA = -1*resRadianA;
		}
		if(flag2 == false) {
			AlmagestCore.log("res radian A is %s.", resRadianA);
			AlmagestCore.log("res radian D is %s.", resRadianD);
	
			AlmagestCore.log("res degree A is %s.", Math.toDegrees(resRadianA));
			AlmagestCore.log("res degree D is %s.", Math.toDegrees(resRadianD));
		}
		
		flag2 = true;
		return new Vec3d(Math.toDegrees(resRadianA), Math.toDegrees(resRadianD), 1);

	}

}
