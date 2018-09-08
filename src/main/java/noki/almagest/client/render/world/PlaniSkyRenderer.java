package noki.almagest.client.render.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraftforge.client.IRenderHandler;
import noki.almagest.AlmagestCore;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.helper.HelperConstellation.LineData;
import noki.almagest.helper.HelperConstellation.OtherStarData;
import noki.almagest.helper.HelperConstellation.StarData;
import noki.almagest.saveddata.gamedata.GameData;


/**********
 * @class PlaniSkyRenderer
 *
 * @description 星座盤世界の空を描画するクラスです。
 * 現在入手済みの星座数に対応しています。
 */
public class PlaniSkyRenderer extends IRenderHandler {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private net.minecraft.client.renderer.vertex.VertexBuffer lineVBO;
	private int lineGLCallList;
	private net.minecraft.client.renderer.vertex.VertexBuffer starVBO;
	private int starGLCallList;
	private static VertexFormat vertexBufferFormat;
	
	private static HashMap<Integer, net.minecraft.client.renderer.vertex.VertexBuffer> starVBOMap = new HashMap<Integer, net.minecraft.client.renderer.vertex.VertexBuffer>();
	private static HashMap<Integer, Integer> starCalllistMap = new HashMap<Integer, Integer>();
	private static HashMap<Integer, net.minecraft.client.renderer.vertex.VertexBuffer> lineVBOMap = new HashMap<Integer, net.minecraft.client.renderer.vertex.VertexBuffer>();
	private static HashMap<Integer, Integer> lineCalllistMap = new HashMap<Integer, Integer>();
	
	
	//******************************//
	// define member methods.
	//******************************//
	public PlaniSkyRenderer() {
		
		this.generateSkys();

	}
	
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		
		//set render settings.
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		
		
		//start rendering.
		GlStateManager.pushMatrix();
//		GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
//		GlStateManager.rotate(this.theWorld.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
		
		//render stars.
		if(OpenGlHelper.useVbo()) {
			renderForVBO(partialTicks);
		}
		else {
			renderForCallList(partialTicks);
		}
		
		//render lines.
/*		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
		if(OpenGlHelper.useVbo()) {
			this.lineVBO.bindBuffer();
			GlStateManager.glEnableClientState(32884);
			GlStateManager.glVertexPointer(3, 5126, 12, 0);
			this.lineVBO.drawArrays(GL11.GL_LINES);
			this.lineVBO.unbindBuffer();
			GlStateManager.glDisableClientState(32884);
		}
		else {
			GlStateManager.callList(this.lineGLCallList);
		}*/
		
		GlStateManager.popMatrix();
		
		
		//reset render settings.
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableFog();
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		
	}
	
	public static void renderForVBO(float partialTicks) {
		
		//stars
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		net.minecraft.client.renderer.vertex.VertexBuffer vbo;
		GameData data;
		for(int i=0; i<=88; i++) {
			data = AlmagestCore.savedDataManager.getFlagData().getConstellation(Constellation.getConstFromNumber(i));
			if(AlmagestCore.proxy.getPlayer().isCreative() || data.isObtained()) {
				vbo = starVBOMap.get(i);
				if(vbo == null) {
					continue;
				}
				vbo.bindBuffer();
				GlStateManager.glEnableClientState(32884);
				GlStateManager.glVertexPointer(3, 5126, 12, 0);
				vbo.drawArrays(7);
				vbo.unbindBuffer();
				GlStateManager.glDisableClientState(32884);
			}
		}
		
		//lines
	//	AlmagestCore.log("enter renderLinesForVBO().");
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
		for(int i=1; i<=88; i++) {
			data = AlmagestCore.savedDataManager.getFlagData().getConstellation(Constellation.getConstFromNumber(i));
			if(AlmagestCore.proxy.getPlayer().isCreative() == false && data.isObtained() == false) {
				continue;
			}
			vbo = lineVBOMap.get(i);
			if(vbo == null) {
				continue;
			}
			vbo.bindBuffer();
			GlStateManager.glEnableClientState(32884);
			GlStateManager.glVertexPointer(3, 5126, 12, 0);
			vbo.drawArrays(GL11.GL_LINES);
			vbo.unbindBuffer();
			GlStateManager.glDisableClientState(32884);
		}
		
	}
	
	public static void renderForCallList(float partialTicks) {
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GameData data;
		
		for(int i=1; i<=88; i++) {
			data = AlmagestCore.savedDataManager.getFlagData().getConstellation(Constellation.getConstFromNumber(i));
			if(AlmagestCore.proxy.getPlayer().isCreative() == false && data.isObtained() == false) {
				continue;
			}
			GlStateManager.callList(starCalllistMap.get(i));
		}
				
	/*		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);	// alpha blend.
		GL11.glEnable(GL11.GL_BLEND);
		GlStateManager.color(1F, 1F, 1F, 150F/255F);*/
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
		for(int i=1; i<=88; i++) {
			data = AlmagestCore.savedDataManager.getFlagData().getConstellation(Constellation.getConstFromNumber(i));
			if(AlmagestCore.proxy.getPlayer().isCreative() == false && data.isObtained() == false) {
				continue;
			}
			GlStateManager.callList(lineCalllistMap.get(i));
		}
		
	/*		GL11.glDisable(GL11.GL_BLEND);
		GlStateManager.resetColor();
		GlStateManager.color(1F, 1F, 1F, 1F);*/
		
	}
	
	private void generateSkys() {
		
		//keep rendering object.
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexBuffer = tessellator.getBuffer();
		boolean vboEnabled = OpenGlHelper.useVbo();
		
		
		//stars.
		for(int i=1; i<=88; i++) {
			net.minecraft.client.renderer.vertex.VertexBuffer vbo = starVBOMap.get(i);
			if(vbo != null) {
				vbo.deleteGlBuffers();
			}
			
			Integer calllist;
			if(starCalllistMap.containsKey(i)) {
				calllist = starCalllistMap.get(i);
				if(calllist != null &&  calllist >= 0) {
					GLAllocation.deleteDisplayLists(calllist);
					starCalllistMap.put(i, -1);
				}
			}
			
			if(vboEnabled) {
				if(vertexBufferFormat == null) {
					vertexBufferFormat = new VertexFormat();
					vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
				}
				vbo = new net.minecraft.client.renderer.vertex.VertexBuffer(vertexBufferFormat);
				renderStarsAlt(vertexBuffer, i, vboEnabled);
				vertexBuffer.finishDrawing();
				vertexBuffer.reset();
				vbo.bufferData(vertexBuffer.getByteBuffer());
				starVBOMap.put(i, vbo);
			}
			else {
				calllist = GLAllocation.generateDisplayLists(1);
				GlStateManager.pushMatrix();
				GlStateManager.glNewList(calllist, 4864);
				renderStarsAlt(vertexBuffer, i, vboEnabled);
				tessellator.draw();
				GlStateManager.glEndList();
				GlStateManager.popMatrix();
				starCalllistMap.put(i, calllist);
			}
		}		
		
		//lines.
		for(int i=1; i<=88; i++) {
			net.minecraft.client.renderer.vertex.VertexBuffer vbo = lineVBOMap.get(i);
			if(vbo != null) {
				vbo.deleteGlBuffers();
			}
			
			Integer calllist;
			if(lineCalllistMap.containsKey(i)) {
				calllist = lineCalllistMap.get(i);
				if(calllist != null &&  calllist >= 0) {
					GLAllocation.deleteDisplayLists(calllist);
					lineCalllistMap.put(i, -1);
				}
			}
			
			if(vboEnabled) {
				if(vertexBufferFormat == null) {
					vertexBufferFormat = new VertexFormat();
					vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
				}
				vbo = new net.minecraft.client.renderer.vertex.VertexBuffer(vertexBufferFormat);
				renderLinesAlt(vertexBuffer, i, vboEnabled);
				vertexBuffer.finishDrawing();
				vertexBuffer.reset();
				vbo.bufferData(vertexBuffer.getByteBuffer());
				lineVBOMap.put(i, vbo);
			}
			else {
				calllist = GLAllocation.generateDisplayLists(1);
				GlStateManager.pushMatrix();
				GlStateManager.glNewList(calllist, 4864);
				renderLinesAlt(vertexBuffer, i, vboEnabled);
				tessellator.draw();
				GlStateManager.glEndList();
				GlStateManager.popMatrix();
				lineCalllistMap.put(i, calllist);
			}
		}
		
		
/*		//generate stars.
		if(this.starVBO != null) {
			this.starVBO.deleteGlBuffers();
		}
		
		if(this.starGLCallList >= 0) {
			GLAllocation.deleteDisplayLists(this.starGLCallList);
			this.starGLCallList = -1;
		}
		
		if(OpenGlHelper.useVbo()) {
			if(this.vertexBufferFormat == null) {
				this.vertexBufferFormat = new VertexFormat();
				this.vertexBufferFormat.addElement(
					new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
			}
			this.starVBO = new net.minecraft.client.renderer.vertex.VertexBuffer(this.vertexBufferFormat);
			this.renderStars(vertexBuffer, OpenGlHelper.useVbo());
			vertexBuffer.finishDrawing();
			vertexBuffer.reset();
			this.starVBO.bufferData(vertexBuffer.getByteBuffer());
		}
		else {
			this.starGLCallList = GLAllocation.generateDisplayLists(1);
			GlStateManager.pushMatrix();
			GlStateManager.glNewList(this.starGLCallList, 4864);
			this.renderStars(vertexBuffer, OpenGlHelper.useVbo());
			tessellator.draw();
			GlStateManager.glEndList();
			GlStateManager.popMatrix();
		}
		
		
		//generate lines.
		if(this.lineVBO != null) {
			this.lineVBO.deleteGlBuffers();
		}
		
		if(this.lineGLCallList >= 0) {
			GLAllocation.deleteDisplayLists(this.lineGLCallList);
			this.lineGLCallList = -1;
		}
		
		if(OpenGlHelper.useVbo()) {
			if(this.vertexBufferFormat == null) {
				this.vertexBufferFormat = new VertexFormat();
				this.vertexBufferFormat.addElement(
					new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
			}
			this.lineVBO = new net.minecraft.client.renderer.vertex.VertexBuffer(this.vertexBufferFormat);
			this.renderLines(vertexBuffer);
			vertexBuffer.finishDrawing();
			vertexBuffer.reset();
			this.lineVBO.bufferData(vertexBuffer.getByteBuffer());
		}
		else {
			this.lineGLCallList = GLAllocation.generateDisplayLists(1);
			GlStateManager.pushMatrix();
			GlStateManager.glNewList(this.lineGLCallList, 4864);
			renderLines(vertexBuffer);
			tessellator.draw();
			GlStateManager.glEndList();
			GlStateManager.popMatrix();
		}*/
		
	}
	
	private void renderStars(BufferBuilder renderer, boolean vbo) {
		
		renderer.begin(7, DefaultVertexFormats.POSITION);
		for(int i=1; i<=88; i++) {
			ArrayList<StarData> targetStars = HelperConstellation.getStars(i);
			double radius = 200;
			for(StarData each: targetStars) {
				double size = Math.max(1.0D - 0.1D*each.magnitude*1.5D, 0.1D);
				Vector3d vector1 = calcRotate(createVector(radius, size, size), each.getCalculatedLong(), -1.0D*each.getCalculatedLat());
				Vector3d vector2 = calcRotate(createVector(radius, size, -1D*size), each.getCalculatedLong(), -1.0D*each.getCalculatedLat());
				Vector3d vector3 = calcRotate(createVector(radius, -1D*size, -1D*size), each.getCalculatedLong(), -1.0D*each.getCalculatedLat());
				Vector3d vector4 = calcRotate(createVector(radius, -1D*size, 1D*size), each.getCalculatedLong(), -1.0D*each.getCalculatedLat());
/*				Vector3d vector1 = this.calcRotate(this.createVector(radius, 3D, 3D), each.getCalculatedLong(), 0);
				Vector3d vector2 = this.calcRotate(this.createVector(radius, 3D, -3D), each.getCalculatedLong(), 0);
				Vector3d vector3 = this.calcRotate(this.createVector(radius, -3D, -3D), each.getCalculatedLong(), 0);
				Vector3d vector4 = this.calcRotate(this.createVector(radius, -3D, 3D), each.getCalculatedLong(), 0);*/
				renderer.pos(vector1.y, vector1.z, vector1.x).endVertex();
				renderer.pos(vector2.y, vector2.z, vector2.x).endVertex();
				renderer.pos(vector3.y, vector3.z, vector3.x).endVertex();
				renderer.pos(vector4.y, vector4.z, vector4.x).endVertex();
			}
			
			ArrayList<OtherStarData> targetOtherStars = HelperConstellation.getOtherStars(i);
			for(OtherStarData each: targetOtherStars) {
				double size = Math.max(1.0D - 0.1D*each.magnitude*1.5D, 0.1D);
				Vector3d vector1 = calcRotate(createVector(radius, size, size), each.ra, -1.0D*each.dec);
				Vector3d vector2 = calcRotate(createVector(radius, size, -1D*size), each.ra, -1.0D*each.dec);
				Vector3d vector3 = calcRotate(createVector(radius, -1D*size, -1D*size), each.ra, -1.0D*each.dec);
				Vector3d vector4 = calcRotate(createVector(radius, -1D*size, 1D*size), each.ra, -1.0D*each.dec);
				renderer.pos(vector1.y, vector1.z, vector1.x).endVertex();
				renderer.pos(vector2.y, vector2.z, vector2.x).endVertex();
				renderer.pos(vector3.y, vector3.z, vector3.x).endVertex();
				renderer.pos(vector4.y, vector4.z, vector4.x).endVertex();
			}
			
/*			if(i>4) {
				break;
			}*/
		}
		
//		renderer.begin(7, DefaultVertexFormats.POSITION);
/*		for(int i=1; i<=88; i++) {
			ArrayList<StarData> targetStars = HelperConstellation.getStars(i);
			
			for(StarData each: targetStars) {
				double radius = 300;
				double lat = Math.toRadians(each.getCalculatedLat());
				double lon = Math.toRadians(each.getCalculatedLong());
				
				double c = Math.acos(Math.sin(0)*Math.sin(lat)+Math.cos(0)*Math.cos(lat)*Math.cos(lon-0));
				double k = c/Math.sin(c);
				
				double starX = radius*k*Math.cos(lat)*Math.sin(lon-0);
				double starZ = radius*k*Math.cos(0)*Math.sin(lat)-Math.sin(0)*Math.cos(lat)*Math.cos(lon-0);
				double starY = radius;
				renderer.pos(starX, starY, starZ).endVertex();
				renderer.pos(starX, starY, starZ+10).endVertex();
				renderer.pos(starX+10, starY, starZ+10).endVertex();
				renderer.pos(starX+10, starY, starZ).endVertex();
			}
		}*/
			
/*		renderer.begin(7, DefaultVertexFormats.POSITION);
		for(int i=1; i<=88; i++) {
			ArrayList<StarData> targetStars = HelperConstellation.getStars(i);
			
			for(StarData each: targetStars) {
				double radius = 200;
				double lat = Math.toRadians(each.getCalculatedLat());
				double lon = Math.toRadians(each.getCalculatedLong());
				double starY = radius * Math.sin(lat);
				double starX = radius * Math.cos(lat) * Math.sin(lon);
				double starZ = radius * Math.cos(lat) * Math.cos(lon);				
				renderer.pos(starX, starY, starZ).endVertex();
				renderer.pos(starX, starY, starZ+1).endVertex();
				renderer.pos(starX+1, starY, starZ+1).endVertex();
				renderer.pos(starX+1, starY, starZ).endVertex();
			}
		}*/
		
/*		Random random = new Random(10842L);
		for (int i = 0; i < 1500; ++i)
		{
			double d0 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d1 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d2 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d3 = (double)(0.15F + random.nextFloat() * 0.1F);
			double d4 = d0 * d0 + d1 * d1 + d2 * d2;

			if (d4 < 1.0D && d4 > 0.01D)
			{
				d4 = 1.0D / Math.sqrt(d4);
				d0 = d0 * d4;
				d1 = d1 * d4;
				d2 = d2 * d4;
				double d5 = d0 * 100.0D;
				double d6 = d1 * 100.0D;
				double d7 = d2 * 100.0D;
				double d8 = Math.atan2(d0, d2);
				double d9 = Math.sin(d8);
				double d10 = Math.cos(d8);
				double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
				double d12 = Math.sin(d11);
				double d13 = Math.cos(d11);
				double d14 = random.nextDouble() * Math.PI * 2.0D;
				double d15 = Math.sin(d14);
				double d16 = Math.cos(d14);

				for (int j = 0; j < 4; ++j)
				{
//					double d17 = 0.0D;
					double d18 = (double)((j & 2) - 1) * d3;
					double d19 = (double)((j + 1 & 2) - 1) * d3;
//					double d20 = 0.0D;
					double d21 = d18 * d16 - d19 * d15;
					double d22 = d19 * d16 + d18 * d15;
					double d23 = d21 * d12 + 0.0D * d13;
					double d24 = 0.0D * d12 - d21 * d13;
					double d25 = d24 * d9 - d22 * d10;
					double d26 = d22 * d9 + d24 * d10;
					renderer.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
				}
			}
		}*/
		
	}
	
	public static void renderStarsAlt(BufferBuilder renderer, int starId, boolean vbo) {
		
	//	Tessellator tessellator = Tessellator.getInstance();
	//	VertexBuffer renderer = tessellator.getBuffer();
		
	/*		renderer.begin(7, DefaultVertexFormats.POSITION);
		ArrayList<StarData> targetStars = HelperConstellation.getStars(starId);
		
		for(StarData each: targetStars) {
			double radius = 200;
			double lat = Math.toRadians(each.getCalculatedLat());
			double lon = Math.toRadians(each.getCalculatedLong());
			double starY = radius * Math.sin(lat);
			double starX = radius * Math.cos(lat) * Math.sin(lon);
			double starZ = radius * Math.cos(lat) * Math.cos(lon);				
			renderer.pos(starX, starY, starZ).endVertex();
			renderer.pos(starX, starY, starZ+1).endVertex();
			renderer.pos(starX+1, starY, starZ+1).endVertex();
			renderer.pos(starX+1, starY, starZ).endVertex();
		}*/
		
		renderer.begin(7, DefaultVertexFormats.POSITION);
		ArrayList<StarData> targetStars = HelperConstellation.getStars(starId);
		double radius = 200;
		for(StarData each: targetStars) {
			double size = Math.max(1.0D - 0.1D*each.magnitude*1.5D, 0.1D);
			Vector3d vector1 = calcRotate(createVector(radius, size, size), each.getCalculatedLong(), -1.0D*each.getCalculatedLat());
			Vector3d vector2 = calcRotate(createVector(radius, size, -1D*size), each.getCalculatedLong(), -1.0D*each.getCalculatedLat());
			Vector3d vector3 = calcRotate(createVector(radius, -1D*size, -1D*size), each.getCalculatedLong(), -1.0D*each.getCalculatedLat());
			Vector3d vector4 = calcRotate(createVector(radius, -1D*size, 1D*size), each.getCalculatedLong(), -1.0D*each.getCalculatedLat());
	/*			Vector3d vector1 = this.calcRotate(this.createVector(radius, 3D, 3D), each.getCalculatedLong(), 0);
			Vector3d vector2 = this.calcRotate(this.createVector(radius, 3D, -3D), each.getCalculatedLong(), 0);
			Vector3d vector3 = this.calcRotate(this.createVector(radius, -3D, -3D), each.getCalculatedLong(), 0);
			Vector3d vector4 = this.calcRotate(this.createVector(radius, -3D, 3D), each.getCalculatedLong(), 0);*/
			renderer.pos(vector1.y, vector1.z, vector1.x).endVertex();
			renderer.pos(vector2.y, vector2.z, vector2.x).endVertex();
			renderer.pos(vector3.y, vector3.z, vector3.x).endVertex();
			renderer.pos(vector4.y, vector4.z, vector4.x).endVertex();
		}
		
		ArrayList<OtherStarData> targetOtherStars = HelperConstellation.getOtherStars(starId);
		for(OtherStarData each: targetOtherStars) {
			double size = Math.max(1.0D - 0.1D*each.magnitude*1.5D, 0.1D);
			Vector3d vector1 = calcRotate(createVector(radius, size, size), each.ra, -1.0D*each.dec);
			Vector3d vector2 = calcRotate(createVector(radius, size, -1D*size), each.ra, -1.0D*each.dec);
			Vector3d vector3 = calcRotate(createVector(radius, -1D*size, -1D*size), each.ra, -1.0D*each.dec);
			Vector3d vector4 = calcRotate(createVector(radius, -1D*size, 1D*size), each.ra, -1.0D*each.dec);
			renderer.pos(vector1.y, vector1.z, vector1.x).endVertex();
			renderer.pos(vector2.y, vector2.z, vector2.x).endVertex();
			renderer.pos(vector3.y, vector3.z, vector3.x).endVertex();
			renderer.pos(vector4.y, vector4.z, vector4.x).endVertex();
		}
					
	}
	
	private static Vector3d calcRotate(Vector3d vector, double g, double b) {
		
		double sinB = Math.sin(Math.toRadians(b));
		double sinG = Math.sin(Math.toRadians(g));
		double cosB = Math.cos(Math.toRadians(b));
		double cosG = Math.cos(Math.toRadians(g));

/*		double newX = vector.x*cosB*cosG - vector.y*cosB*sinG + vector.z*sinB;
		double newY = vector.x*sinG + vector.y*cosG;
		double newZ = -1*vector.x*sinB*cosG + vector.y*sinB*sinG + vector.z*cosB;*/
		double newX = vector.x*cosB*cosG + vector.z*sinB*cosG - vector.y*sinG;
		double newY = vector.x*sinG*cosB + vector.z*sinG*sinB + vector.y*cosG;
		double newZ = -1*vector.x*sinB + vector.z*cosB;
		
		return createVector(newX, newY, newZ);
		
	}
	
	private static Vector3d createVector(double x, double y, double z) {
		
		Vector3d newVector = new Vector3d();
		newVector.x = x;
		newVector.y = y;
		newVector.z = z;
		
		return newVector;
		
	}
	
	private void renderLines(BufferBuilder renderer) {
		
/*		renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		GL11.glLineWidth(1);
		double latCenter = Math.toRadians(0);
		double lonCenter = Math.toRadians(0);
		
		for(int i=1; i<=88; i++) {
			List<LineData> targetLines = HelperConstellation.getLines(i);
			for(LineData each: targetLines) {
				double radius = 300;
				double lat = Math.toRadians(each.star1.getCalculatedLat());
				double lon = Math.toRadians(each.star1.getCalculatedLong());
				
				double c = Math.acos(Math.sin(latCenter)*Math.sin(lat)+Math.cos(latCenter)*Math.cos(lat)*Math.cos(lon-lonCenter));
				double k = c/Math.sin(c);
				
				double star1X = radius*k*Math.cos(lat)*Math.sin(lon-lonCenter);
				double star1Z = radius*k*Math.cos(latCenter)*Math.sin(lat)-Math.sin(latCenter)*Math.cos(lat)*Math.cos(lon-lonCenter);
				double star1Y = radius;
				renderer.pos(star1X, star1Y, star1Z).endVertex();
				
				
				lat = Math.toRadians(each.star2.getCalculatedLat());
				lon = Math.toRadians(each.star2.getCalculatedLong());
				
				c = Math.acos(Math.sin(latCenter)*Math.sin(lat)+Math.cos(latCenter)*Math.cos(lat)*Math.cos(lon-lonCenter));
				k = c/Math.sin(c);
				
				double star2X = radius*k*Math.cos(lat)*Math.sin(lon-lonCenter);
				double star2Z = radius*k*Math.cos(latCenter)*Math.sin(lat)-Math.sin(latCenter)*Math.cos(lat)*Math.cos(lon-lonCenter);
				double star2Y = radius;
				renderer.pos(star2X, star2Y, star2Z).endVertex();
			}
		}*/
		
		renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		GL11.glLineWidth(1);
		
		for(int i=1; i<=88; i++) {
			List<LineData> targetLines = HelperConstellation.getLines(i);
			
			for(LineData each: targetLines) {
				double radius = 200;
				double star1Y = radius * Math.sin(Math.toRadians(each.star1.getCalculatedLat()));
				double star1X = radius * Math.cos(Math.toRadians(each.star1.getCalculatedLat())) * Math.sin(Math.toRadians(each.star1.getCalculatedLong()));
				double star1Z = radius * Math.cos(Math.toRadians(each.star1.getCalculatedLat())) * Math.cos(Math.toRadians(each.star1.getCalculatedLong()));
				double star2Y = radius * Math.sin(Math.toRadians(each.star2.getCalculatedLat()));
				double star2X = radius * Math.cos(Math.toRadians(each.star2.getCalculatedLat())) * Math.sin(Math.toRadians(each.star2.getCalculatedLong()));
				double star2Z = radius * Math.cos(Math.toRadians(each.star2.getCalculatedLat())) * Math.cos(Math.toRadians(each.star2.getCalculatedLong()));
				renderer.pos(star1X, star1Y, star1Z).endVertex();
				renderer.pos(star2X, star2Y, star2Z).endVertex();
			}
/*			if(i>4) {
				break;
			}*/
		}
		
	}
	
	public static void renderLinesAlt(BufferBuilder renderer, int starId, boolean vbo) {
		
		renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		GL11.glLineWidth(1);
		
		List<LineData> targetLines = HelperConstellation.getLines(starId);
		
		for(LineData each: targetLines) {
			double radius = 200;
			double star1Y = radius * Math.sin(Math.toRadians(each.star1.getCalculatedLat()));
			double star1X = radius * Math.cos(Math.toRadians(each.star1.getCalculatedLat())) * Math.sin(Math.toRadians(each.star1.getCalculatedLong()));
			double star1Z = radius * Math.cos(Math.toRadians(each.star1.getCalculatedLat())) * Math.cos(Math.toRadians(each.star1.getCalculatedLong()));
			double star2Y = radius * Math.sin(Math.toRadians(each.star2.getCalculatedLat()));
			double star2X = radius * Math.cos(Math.toRadians(each.star2.getCalculatedLat())) * Math.sin(Math.toRadians(each.star2.getCalculatedLong()));
			double star2Z = radius * Math.cos(Math.toRadians(each.star2.getCalculatedLat())) * Math.cos(Math.toRadians(each.star2.getCalculatedLong()));
			renderer.pos(star1X, star1Y, star1Z).endVertex();
			renderer.pos(star2X, star2Y, star2Z).endVertex();
		}
		
	}

}
