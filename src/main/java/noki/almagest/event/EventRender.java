package noki.almagest.event;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noki.almagest.AlmagestCore;
import noki.almagest.gui.sequence.GuiContainerSequence;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.packet.PacketHandler;
import noki.almagest.packet.PacketSavedConstBlock;
import noki.almagest.registry.ModBlocks;


/**********
 * @class EventRender
 *
 * @description 
 * 
 */
public class EventRender {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private static BlockPos targetPos;
	@SuppressWarnings("unused")//now.
	private static Constellation targetConst;
	
	private static final double arrowWidth = 0.1D;
	private static final double arrowHeight = 0.05D;
	private static final double arrowDepth = 0.3D;
	
	private boolean state;
	private boolean lastState;
	private int tick;
	
	
	//******************************//
	// define member methods.
	//******************************//
	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent event) {
		
		if(event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
			return;
		}
		
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if(screen == null || !(screen instanceof GuiContainerSequence)) {
			return;
		}
		
		event.setCanceled(true);
		
	}
	
	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		
		this.tick++;//星座がないとき用。
		
		//サーバーに一番近い星座を問い合わせるためのギミック。
		this.lastState = this.state;
		
		EntityPlayer player = AlmagestCore.proxy.getPlayer();
		if(player != null && player.getHeldItemMainhand().getItem() != Item.getItemFromBlock(ModBlocks.STAR_COMPASS)) {
			this.state = false;
			if(this.lastState) {
				targetPos = null;
			}
			return;
		}
		
		this.state = true;
		if(this.state && !this.lastState && player.world.provider.getDimension() != -1) {
			PacketHandler.instance.sendToServer(new PacketSavedConstBlock(player.world.provider.getDimension(), player.getPosition()));
			AlmagestCore.log("send packet to server.");
		}
		
		
		//描画スタート。
		
		//インスタンスをキープ。
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();
		
		//いつものおまじない。
		GL11.glPushMatrix();
		
		//描画座標を計算、移動。プレイヤーの少し前。
		Vec3d currentPos = new Vec3d(
				player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks(),
				player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks(),
				player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks());
		Vec3d forward = new Vec3d(player.getForward().x, 0.0D, player.getForward().z);
		forward = forward.scale(1.5D/forward.lengthVector());
		Vec3d arrowVec = forward.addVector(0.0D, 1.3D, 0.0D);

		GL11.glTranslated(arrowVec.x, arrowVec.y, arrowVec.z);
		
		
		//コンパスを描画。
		GL11.glTranslated(0.0D, -0.2D, 0.0D);
//		Minecraft.getMinecraft().getItemRenderer().renderItem(player, compass, TransformType.FIXED);
		Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModBlocks.STAR_COMPASS), TransformType.FIXED);
//		renderItem.renderItem(new ItemStack(Blocks.ANVIL), TransformType.HEAD);
		GL11.glTranslated(0.0D, 0.2D, 0.0D);
		
		
		//矢印を描画。
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
//		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		
		//一番近い星座に合わせて回転。
		if(targetPos != null) {
			Vec3d constVec = new Vec3d(targetPos.getX()+0.5D, targetPos.getY()+0.5D, targetPos.getZ()+0.5D).subtract(currentPos.add(arrowVec));
			constVec = constVec.scale(1.0D/constVec.lengthVector());
			Vec3d startVec = new Vec3d(1.0D, 0.0D, 0.0D);
			
			double angle = Math.toDegrees(Math.acos(startVec.dotProduct(constVec)));//内積でなす角度。
			Vec3d rotationAxis = startVec.crossProduct(constVec);//外積で垂直なベクトル。
			rotationAxis = rotationAxis.scale(1.0D/rotationAxis.lengthVector());//長さを1に。
			
			GL11.glRotated(angle, rotationAxis.x, rotationAxis.y, rotationAxis.z);
		}
		//そうでなければくるくる回転。
		else {
			GL11.glRotated(360.0D*((double)(this.tick%40)/40.0D), 0.0D, 1.0D, 0.0D);
		}
		
		//矢印自体。
		renderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
		GlStateManager.color(230F/255F, 0F, 51F/255F, 100F/255F);//赤

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
		GlStateManager.color(230F/255F, 180F/255F, 34F/255F, 100F/255F);//金
		
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
//		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDepthMask(true);
		
	}
	
	public static void setTarget(BlockPos pos, int constId) {
		
		targetPos = pos;
		targetConst = Constellation.getConstFromNumber(constId);
		AlmagestCore.log("targetPos: {}/{}/{}.", targetPos.getX(), targetPos.getY(), targetPos.getZ());
		
	}

}
