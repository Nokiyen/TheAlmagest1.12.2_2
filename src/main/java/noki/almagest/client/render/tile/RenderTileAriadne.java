package noki.almagest.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import noki.almagest.registry.ModBlocks;
import noki.almagest.tile.TileAriadne;


/**********
 * @class ChunkAwakerTile
 *
 * @description
 * @description_en
 */
public class RenderTileAriadne extends TileEntitySpecialRenderer<TileAriadne> {
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//	
	@Override
	public void render(TileAriadne tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		
		GlStateManager.pushMatrix();
		
		//degree, not radians.
		GlStateManager.translate(x, y, z);
		GlStateManager.translate(0.5D, 0.35D, 0.5D);
		float rotationRedidue = (int)Minecraft.getSystemTime()%(1000*8);
		float angle = (rotationRedidue/(1000F*8F)) * 360F;
		GlStateManager.rotate(angle, 0F, 1F, 0F);
		GlStateManager.translate(0D, Math.sin(angle/360F*2*Math.PI)*0.1, 0D);
		
		RenderItem renderer = Minecraft.getMinecraft().getRenderItem();
		renderer.renderItem(new ItemStack(ModBlocks.ARIADNE), TransformType.GROUND);
		
		GlStateManager.popMatrix();
		
	}

}
