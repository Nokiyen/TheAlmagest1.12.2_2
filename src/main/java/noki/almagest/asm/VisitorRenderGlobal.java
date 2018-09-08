package noki.almagest.asm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.Opcodes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import noki.almagest.AlmagestCore;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.helper.HelperConstellation.LineData;
import noki.almagest.helper.HelperConstellation.OtherStarData;
import noki.almagest.helper.HelperConstellation.StarData;
import noki.almagest.saveddata.gamedata.GameData;



public class VisitorRenderGlobal implements IVisitorProvider, Opcodes {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String TARGET_CLASS_NAME = "net.minecraft.client.renderer.RenderGlobal";
	
	@SuppressWarnings("unused")
	private static net.minecraft.client.renderer.vertex.VertexBuffer lineVBO;
	@SuppressWarnings("unused")
	private static int lineGLCallList;
	private static VertexFormat vertexBufferFormat;
	
	private static HashMap<Integer, net.minecraft.client.renderer.vertex.VertexBuffer> starVBOMap = new HashMap<Integer, net.minecraft.client.renderer.vertex.VertexBuffer>();
	private static HashMap<Integer, Integer> starCalllistMap = new HashMap<Integer, Integer>();
	private static HashMap<Integer, net.minecraft.client.renderer.vertex.VertexBuffer> lineVBOMap = new HashMap<Integer, net.minecraft.client.renderer.vertex.VertexBuffer>();
	private static HashMap<Integer, Integer> lineCalllistMap = new HashMap<Integer, Integer>();
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public boolean match(String transformedName) {
		
//		ASMLoadingPlugin.LOGGER.info(transformedName);
		return transformedName.equals(TARGET_CLASS_NAME);
		
	}
	
	@Override
	public ClassVisitor provideVisitor(String name, ClassWriter writer) {
		
		return new CustomClassVisitor(name, writer);
		
	}
	
	
	//--------------------
	// Inner Class.
	//--------------------
	class CustomClassVisitor extends ClassVisitor {
		
		//*****define member variables.*//
		private String owner;
		
		private static final String TARGET_METHOD_NAME_OBF = "func_174976_a";
		private static final String TARGET_METHOD_DESC = "(FI)V";
		private static final String TARGET_METHOD_NAME = "renderSky";
		
		private static final String TARGET_METHOD_NAME_OBF2 = "func_174963_q";
		private static final String TARGET_METHOD_DESC2 = "()V";
		private static final String TARGET_METHOD_NAME2 = "generateStars";
		
		private static final String TARGET_METHOD_NAME_OBF3 = "func_180444_a";
		//check it.
		private static final String TARGET_METHOD_DESC3 = "(Lnet/minecraft/client/renderer/BufferBuilder;)V";
		private static final String TARGET_METHOD_NAME3 = "renderStars";
		
		
		//*****define member methods.***//
		public CustomClassVisitor(String owner, ClassVisitor cv) {
			super(Opcodes.ASM4, cv);
			this.owner = owner;
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			
			if(ASMHelper.checkMethodName(this.owner, name, desc, TARGET_METHOD_NAME, TARGET_METHOD_NAME_OBF, TARGET_METHOD_DESC)) {
				ASMLoadingPlugin.LOGGER.info("enter method.");
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				return new CustomMethodVisitor(this.api, this.owner, access, name, desc, mv);
			}
			
			if(ASMHelper.checkMethodName(this.owner, name, desc, TARGET_METHOD_NAME2, TARGET_METHOD_NAME_OBF2, TARGET_METHOD_DESC2)) {
				ASMLoadingPlugin.LOGGER.info("enter method 2.");
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				return new CustomMethodVisitor2(this.api, this.owner, access, name, desc, mv);
			}
			
			if(ASMHelper.checkMethodName(this.owner, name, desc, TARGET_METHOD_NAME3, TARGET_METHOD_NAME_OBF3, TARGET_METHOD_DESC3)) {
				ASMLoadingPlugin.LOGGER.info("enter method 3.");
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				return new CustomMethodVisitor3(this.api, this.owner, access, name, desc, mv);
			}
			
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
	}
	
	//MethodVisitorの代わりにAnalyzerAdapterを使うことで、
	//visitMax()やvisitFrame()の処理をしなくてよくなる。
	//その代わり、各種superメソッドを呼び出す必要がある。
	//COMPUTE_MAXでもそんなに速度落ちないという噂も。
	class CustomMethodVisitor extends AnalyzerAdapter {
		
		private static final String TARGET_FIELD_NAME_OBF = "field_175013_s";
		private static final String TARGET_FIELD_NAME = "starVBO";
		private boolean starVBOFlag = false;
		
		private static final String TARGET_FIELD_NAME_OBF2 = "field_72772_v";
		private static final String TARGET_FIELD_NAME2 = "starGLCallList";
		private boolean callListFlag = false;
		
		protected CustomMethodVisitor(int api, String owner, int access, String name, String desc, MethodVisitor mv) {
			super(api, owner, access, name, desc, mv);
			starVBOFlag = false;
			callListFlag = false;
		}
		
		@Override
		public void visitCode() {
			ASMLoadingPlugin.LOGGER.info("enter visitCode().");
			starVBOFlag = false;
			callListFlag = false;
			super.visitCode();
		}
		
		@Override
		public void visitFieldInsn(int opcode, String owner, String name, String desc) {
			if(ASMHelper.checkFieldName(opcode, owner, name, desc, TARGET_FIELD_NAME, TARGET_FIELD_NAME_OBF) && starVBOFlag == false) {
				ASMLoadingPlugin.LOGGER.info("starVBO.");
				starVBOFlag = true;
				super.visitVarInsn(FLOAD, 1);
				super.visitMethodInsn(INVOKESTATIC, "noki/almagest/asm/VisitorRenderGlobal", "renderLinesForVBO", "(F)V", false);
			}
			else if(ASMHelper.checkFieldName(opcode, owner, name, desc, TARGET_FIELD_NAME2, TARGET_FIELD_NAME_OBF2) && callListFlag == false) {
				ASMLoadingPlugin.LOGGER.info("starGLCallList.");
				callListFlag = true;
				super.visitVarInsn(FLOAD, 1);
				super.visitMethodInsn(INVOKESTATIC, "noki/almagest/asm/VisitorRenderGlobal", "renderLinesForCallList", "(F)V", false);
			}
			super.visitFieldInsn(opcode, owner, name, desc);
		}
		
	}
	
	class CustomMethodVisitor2 extends AnalyzerAdapter {
		
		//*****define member variables.*//
		@SuppressWarnings("unused")
		private String owner2;
		
		protected CustomMethodVisitor2(int api, String owner, int access, String name, String desc, MethodVisitor mv) {
			super(api, owner, access, name, desc, mv);
			this.owner2 = owner;
		}
	
		//*****define member methods.***//
		@Override
		public void visitCode() {
			ASMLoadingPlugin.LOGGER.info("enter visitCode().");
			super.visitCode();
//			super.visitVarInsn(ALOAD, 0);
//			super.visitFieldInsn(GETFIELD, this.owner2, "field_175005_X", "Z");
			super.visitMethodInsn(INVOKESTATIC, "noki/almagest/asm/VisitorRenderGlobal", "generateStarsAlt", "()V", false);
	//		super.visitInsn(RETURN);
	//		super.visitEnd();
		}
		
	}
	
	class CustomMethodVisitor3 extends AnalyzerAdapter {
		
		//*****define member variables.*//
		
		//*****define member methods.***//
		protected CustomMethodVisitor3(int api, String owner, int access, String name, String desc, MethodVisitor mv) {
			super(api, owner, access, name, desc, mv);
		}
	
		@Override
		public void visitCode() {
			ASMLoadingPlugin.LOGGER.info("enter visitCode().");
			super.visitCode();
			super.visitMethodInsn(INVOKESTATIC, "noki/almagest/asm/VisitorRenderGlobal", "renderStarsAlt", "()V", false);
			super.visitInsn(RETURN);
			super.visitEnd();
		}
		
	}
	
	public static void renderLinesForVBO(float partialTicks) {
		
		int yearDay = 8*3*4;
		int passedDay = (int)((Minecraft.getMinecraft().world.getWorldTime() - 6000)/24000);
		passedDay = passedDay%yearDay;
		float currentRotate = 360.0F * ((float)passedDay/(float)yearDay);
//		if(Minecraft.getMinecraft().world.getWorldTime()%60 == 0) {
//			AlmagestCore.log("{}, {}, {}, {}, {}.", yearDay, Minecraft.getMinecraft().world.getWorldTime(),
//					(int)((Minecraft.getMinecraft().world.getWorldTime() - 6000)/24000), passedDay, currentRotate);
//		}
		setRotation(partialTicks, currentRotate);
		
		
		float f1 = 1.0F - Minecraft.getMinecraft().world.getRainStrength(partialTicks);
		float f2 = Minecraft.getMinecraft().world.getStarBrightness(partialTicks) * f1;
		GlStateManager.color(f2, f2, f2, f2);
		
		//stars
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
	
		GlStateManager.color(f2, f2, f2, f2);
		
		if(AlmagestCore.proxy.hasPlanisphere() == false) {
			return;
		}
		
		GlStateManager.color(f2, f2, f2, f2);
		
		//lines
	//	AlmagestCore.log("enter renderLinesForVBO().");
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
		
		
		restoreRotation(partialTicks, currentRotate);
		GlStateManager.color(f2, f2, f2, f2);
		
	}
	
	public static void renderLinesForCallList(float partialTicks) {
		
		int yearDay = 8*3*4;
		int passedDay = (int)((Minecraft.getMinecraft().world.getWorldTime() - 6000)/24000);
		passedDay = passedDay%yearDay;
		float currentRotate = 360.0F * ((float)passedDay/(float)yearDay);
//		if(Minecraft.getMinecraft().world.getWorldTime()%60 == 0) {
//			AlmagestCore.log("{}, {}, {}, {}, {}.", yearDay, Minecraft.getMinecraft().world.getWorldTime(),
//					(int)((Minecraft.getMinecraft().world.getWorldTime() - 6000)/24000), passedDay, currentRotate);
//		}
		setRotation(partialTicks, currentRotate);
		
		
		float f1 = 1.0F - Minecraft.getMinecraft().world.getRainStrength(partialTicks);
		float f2 = Minecraft.getMinecraft().world.getStarBrightness(partialTicks) * f1;
		GlStateManager.color(f2, f2, f2, f2/2);
		
	//	AlmagestCore.log("enter renderLinesForCallList().");
		GameData data;
		
		for(int i=1; i<=88; i++) {
			data = AlmagestCore.savedDataManager.getFlagData().getConstellation(Constellation.getConstFromNumber(i));
			if(AlmagestCore.proxy.getPlayer().isCreative() == false && data.isObtained() == false) {
				continue;
			}
			GlStateManager.callList(starCalllistMap.get(i));
		}
		
		GlStateManager.color(f2, f2, f2, f2);
		
		if(AlmagestCore.proxy.hasPlanisphere() == false) {
			return;
		}
		
		GlStateManager.color(f2, f2, f2, f2/2);
		
	/*		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);	// alpha blend.
		GL11.glEnable(GL11.GL_BLEND);
		GlStateManager.color(1F, 1F, 1F, 150F/255F);*/
		
		for(int i=1; i<=88; i++) {
			data = AlmagestCore.savedDataManager.getFlagData().getConstellation(Constellation.getConstFromNumber(i));
			if(AlmagestCore.proxy.getPlayer().isCreative() == false && data.isObtained() == false) {
				continue;
			}
			GlStateManager.callList(lineCalllistMap.get(i));
		}
		
		restoreRotation(partialTicks, currentRotate);
		GlStateManager.color(f2, f2, f2, f2);
		
	/*		GL11.glDisable(GL11.GL_BLEND);
		GlStateManager.resetColor();
		GlStateManager.color(1F, 1F, 1F, 1F);*/
		
	}
	
	public static void setRotation(float partialTicks, float currentRotate) {
		
		//太陽、月の描画のためのrotateを一度もとに戻す。
		GlStateManager.rotate(-1 * Minecraft.getMinecraft().world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
		//赤緯0度(北極星)を真北に合わせる。
		GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		//観測一の緯度に天球位置を修正する(Tokyo)。
		GlStateManager.rotate(-35.0F, 0.0F, 0.0F, 1.0F);
		//赤経12時が春分の日(4月1日と仮定)に深夜0時に南中する位置に合わせる。
		GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
		//ゲーム開始からの経過時間に応じて回転する。
		GlStateManager.rotate(-currentRotate, 0.0F, 1.0F, 0.0F);
		//時間に合わせて回転する。
		GlStateManager.rotate(-1 * Minecraft.getMinecraft().world.getCelestialAngle(partialTicks) * 360.0F, 0.0F, 1.0F, 0.0F);
		
	}
	
	public static void restoreRotation(float partialTicks, float currentRotate) {
		
		//全ての回転を逆順にもとに戻す。
		GlStateManager.rotate(Minecraft.getMinecraft().world.getCelestialAngle(partialTicks) * 360.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(currentRotate, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(35.0F, 0.0F, 0.0F, 1.0F);//Tokyo.
		GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(Minecraft.getMinecraft().world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
		
	}
	
	public static void renderStarsAlt() {
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();
		
		renderer.begin(7, DefaultVertexFormats.POSITION);
	/*		if(AlmagestCore.savedDataManager.getData() != null) {
			for(int i=1; i<=88; i++) {
				GameData gameData = AlmagestCore.savedDataManager.getData().getConstellation(Constellation.getConstFromNumber(i).getName());
				if(gameData == null || gameData.isObtained()) {
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
				}
			}
		}*/
		
	/*		Random random = new Random(10842L);
		for(int i = 0; i < 1000; ++i) {
			double d0 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d1 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d2 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d3 = (double)(0.15F + random.nextFloat() * 0.1F);
			double d4 = d0 * d0 + d1 * d1 + d2 * d2;
	
			if(d4 < 1.0D && d4 > 0.01D) {
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
	
				for(int j = 0; j < 4; ++j){
	//				double d17 = 0.0D;
					double d18 = (double)((j & 2) - 1) * d3;
					double d19 = (double)((j + 1 & 2) - 1) * d3;
	//				double d20 = 0.0D;
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
	
	public static void renderLines(BufferBuilder renderer, int starId, boolean vbo) {
		
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
	
	public static void generateStarsAlt() {
		
		ASMLoadingPlugin.LOGGER.info("enter generateStarsAlt().");
		
		boolean vboEnabled = OpenGlHelper.useVbo();
		
		//keep renderer.
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexBuffer = tessellator.getBuffer();
		
	/*		if(starVBOMap == null) {
			starVBOMap = new HashMap<Integer, net.minecraft.client.renderer.vertex.VertexBuffer>();
		}
		if(lineVBOMap == null) {
			lineVBOMap = new HashMap<Integer, net.minecraft.client.renderer.vertex.VertexBuffer>();
		}
		if(starCalllistMap == null) {
			starCalllistMap = new HashMap<Integer, Integer>();
		}
		if(lineCalllistMap == null) {
			lineCalllistMap = new HashMap<Integer, Integer>();
		}*/
		
		//stars.
	/*		RenderGlobal renderGlobal = Minecraft.getMinecraft().renderGlobal;
		if(renderGlobal == null) {
			return;
		}*/
		
		/*net.minecraft.client.renderer.vertex.VertexBuffer starVBO =
				ReflectionHelper.getPrivateValue(RenderGlobal.class, renderGlobal, "field_175013_s", "starVBO");
		if(starVBO != null) {
			starVBO.deleteGlBuffers();
		}
		
		int starGLCallList =
				ReflectionHelper.getPrivateValue(RenderGlobal.class, renderGlobal, "field_72772_v", "starGLCallList");
		if(starGLCallList >= 0) {
			GLAllocation.deleteDisplayLists(starGLCallList);
			ReflectionHelper.setPrivateValue(RenderGlobal.class, renderGlobal, -1, "field_72772_v", "starGLCallList");
		}*/
		
	/*		boolean vboEnabled =
				ReflectionHelper.getPrivateValue(RenderGlobal.class, renderGlobal, "field_175005_X", "vboEnabled");
	/*		if(vboEnabled) {
			VertexFormat vertexBufferFormat =
					ReflectionHelper.getPrivateValue(RenderGlobal.class, renderGlobal, "field_175014_r", "vertexBufferFormat");
			starVBO = new net.minecraft.client.renderer.vertex.VertexBuffer(vertexBufferFormat);
			renderStarsAlt(vertexBuffer);
			vertexBuffer.finishDrawing();
			vertexBuffer.reset();
			starVBO.bufferData(vertexBuffer.getByteBuffer());
			ReflectionHelper.setPrivateValue(RenderGlobal.class, renderGlobal, starVBO, "field_175013_s", "starVBO");
		}
		else {
			starGLCallList = GLAllocation.generateDisplayLists(1);
			ReflectionHelper.setPrivateValue(RenderGlobal.class, renderGlobal, starGLCallList, "field_72772_v", "starGLCallList");
			GlStateManager.pushMatrix();
			GlStateManager.glNewList(starGLCallList, 4864);
			renderStarsAlt(vertexBuffer);
			tessellator.draw();
			GlStateManager.glEndList();
			GlStateManager.popMatrix();
		}*/
		//stars.
		for(int i=1; i<=88; i++) {
			net.minecraft.client.renderer.vertex.VertexBuffer vbo = starVBOMap.get(i);
			if(vbo != null) {
				vbo.deleteGlBuffers();
			}
			
			// why null po???
			int calllist;
			if(starCalllistMap.containsKey(i)) {
				calllist = starCalllistMap.get(i);
				if(calllist != NULL &&  calllist >= 0) {
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
			
			int calllist;
			if(lineCalllistMap.containsKey(i)) {
				calllist = lineCalllistMap.get(i);
				if(calllist != NULL &&  calllist >= 0) {
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
				renderLines(vertexBuffer, i, vboEnabled);
				vertexBuffer.finishDrawing();
				vertexBuffer.reset();
				vbo.bufferData(vertexBuffer.getByteBuffer());
				lineVBOMap.put(i, vbo);
			}
			else {
				calllist = GLAllocation.generateDisplayLists(1);
				GlStateManager.pushMatrix();
				GlStateManager.glNewList(calllist, 4864);
				renderLines(vertexBuffer, i, vboEnabled);
				tessellator.draw();
				GlStateManager.glEndList();
				GlStateManager.popMatrix();
				lineCalllistMap.put(i, calllist);
			}
		}
	/*		if(lineVBO != null) {
			lineVBO.deleteGlBuffers();
		}
		
		if(lineGLCallList >= 0) {
			GLAllocation.deleteDisplayLists(lineGLCallList);
			lineGLCallList = -1;
		}
		
		if(vboEnabled) {
			if(vertexBufferFormat == null) {
				vertexBufferFormat = new VertexFormat();
				vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
			}
			lineVBO = new net.minecraft.client.renderer.vertex.VertexBuffer(vertexBufferFormat);
			renderLines(vertexBuffer);
			vertexBuffer.finishDrawing();
			vertexBuffer.reset();
			lineVBO.bufferData(vertexBuffer.getByteBuffer());
		}
		else {
			lineGLCallList = GLAllocation.generateDisplayLists(1);
			GlStateManager.pushMatrix();
			GlStateManager.glNewList(lineGLCallList, 4864);
			renderLines(vertexBuffer);
			tessellator.draw();
			GlStateManager.glEndList();
			GlStateManager.popMatrix();
		}*/
		
	}
	
}