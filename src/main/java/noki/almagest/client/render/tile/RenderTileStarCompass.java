package noki.almagest.client.render.tile;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import noki.almagest.tile.TileStarCompass;


/**********
 * @class RenderTileConstellation
 *
 * @description 星座ブロックを描画するクラスです。
 * @see BlockConstellation, TileConstellation.
 */
public class RenderTileStarCompass extends TileEntitySpecialRenderer<TileStarCompass> {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final double arrowWidth = 0.1D;
	private static final double arrowHeight = 0.05D;
	private static final double arrowDepth = 0.3D;
	
	private int tick;
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public void render(TileStarCompass tile, double x, double y, double z, float partialTickFrame, int destroy, float par7) {
		
		this.tick = (this.tick+1)%40;//星座がないとき用。
		
		
		//描画スタート。
		
		//インスタンスをキープ。
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();
		
		//おまじない。
		GL11.glPushMatrix();
		
		//描画位置を移動。
		Vec3d currentPos = new Vec3d(x+0.5D, y+0.5D, z+0.5D);
		GL11.glTranslated(currentPos.x, currentPos.y, currentPos.z);
		
		//矢印を描画。
//		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		RenderHelper.disableStandardItemLighting();
		
		//一番近い星座に合わせて回転。
		if(tile.getTargetPos() != null) {
			Vec3d constVec =
					new Vec3d(tile.getTargetPos().getX()+0.5D, tile.getTargetPos().getY()+0.5D, tile.getTargetPos().getZ()+0.5D)
						.subtract(tile.getPos().getX()+0.5D, tile.getPos().getY()+0.5D, tile.getPos().getZ()+0.5D);
//			AlmagestCore.log("current: {}/{}/{}", tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
			constVec = constVec.scale(1.0D/constVec.lengthVector());
			Vec3d startVec = new Vec3d(1.0D, 0.0D, 0.0D);
			
			//--------x軸方向に完全に平行な時におかしくなる。
			double angle = Math.toDegrees(Math.acos(startVec.dotProduct(constVec)));//内積でなす角度。
			Vec3d rotationAxis = startVec.crossProduct(constVec);//外積で垂直なベクトル。
			rotationAxis = rotationAxis.scale(1.0D/rotationAxis.lengthVector());//長さを1に。
			
			GL11.glRotated(angle, rotationAxis.x, rotationAxis.y, rotationAxis.z);
		}
		//そうでなければくるくる回転。
		else {
			GL11.glRotated(360.0D*((double)this.tick/40.0D), 0.0D, 1.0D, 0.0D);
		}
		
		//矢印自体。
		renderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
		GlStateManager.color(230F/255F, 0F, 51F/255F, 1F);//赤

		renderer.pos(arrowDepth, 0.0D, 0.0D).endVertex();
		renderer.pos(0.0D, 0.0D, -arrowWidth).endVertex();
		renderer.pos(0.0D, 0.05D, 0.0D).endVertex();

		renderer.pos(arrowDepth, 0.0D, 0.0D).endVertex();
		renderer.pos(0.0D, 0.05D, 0.0D).endVertex();
		renderer.pos(0.0D, 0.0D, arrowWidth).endVertex();

		renderer.pos(arrowDepth, 0.0D, 0.0D).endVertex();
		renderer.pos(0.0D, 0.0D, -arrowWidth).endVertex();
		renderer.pos(0.0D, -arrowHeight, 0.0D).endVertex();

		renderer.pos(arrowDepth, 0.0D, 0.0D).endVertex();
		renderer.pos(0.0D, -arrowHeight, 0.0D).endVertex();
		renderer.pos(0.0D, 0.0D, arrowWidth).endVertex();
		
		tessellator.draw();

		renderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
		GlStateManager.color(230F/255F, 180F/255F, 34F/255F, 1F);//金
		
		renderer.pos(-arrowDepth, 0.0D, 0.0D).endVertex();
		renderer.pos(0.0D, arrowHeight, 0.0D).endVertex();
		renderer.pos(0.0D, 0.0D, -arrowWidth).endVertex();

		renderer.pos(-arrowDepth, 0.0D, 0.0D).endVertex();
		renderer.pos(0.0D, 0.0D, arrowWidth).endVertex();
		renderer.pos(0.0D, arrowHeight, 0.0D).endVertex();

		renderer.pos(-arrowDepth, 0.0D, 0.0D).endVertex();
		renderer.pos(0.0D, -arrowHeight, 0.0D).endVertex();
		renderer.pos(0.0D, 0.0D, -arrowWidth).endVertex();

		renderer.pos(-arrowDepth, 0.0D, 0.0D).endVertex();
		renderer.pos(0.0D, 0.0D, arrowWidth).endVertex();
		renderer.pos(0.0D, -arrowHeight, 0.0D).endVertex();
		
		tessellator.draw();

		
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
//		GL11.glDepthMask(true);
		RenderHelper.enableStandardItemLighting();
		
	}

}
