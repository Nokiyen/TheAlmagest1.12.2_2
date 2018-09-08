package noki.almagest.gui;

import java.io.IOException;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import noki.almagest.ModInfo;
import noki.almagest.packet.PacketHandler;
import noki.almagest.packet.PacketUpdateBookrest;


/**********
 * @class GuiContainerBookrest
 *
 * @description
 */
public class GuiContainerTansu extends GuiContainer {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final ResourceLocation texture_back = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/gui/tansu_back.png");
	private static final ResourceLocation texture_overlay = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/gui/tansu_overlay.png");
	
	
	//******************************//
	// define member methods.
	//******************************//
	public GuiContainerTansu(EntityPlayer player, World world, int x, int y, int z) {
		
		super(new ContainerTansu(player.inventory, world, new BlockPos(x, y, z)));
		this.xSize = 232;
		this.ySize = 185;
		
	}
	
	/*GUIの文字等の描画処理*/
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
		
		ContainerBookrest container = (ContainerBookrest)this.inventorySlots;
		
		this.mc.renderEngine.bindTexture(texture_overlay);
		
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

		
		GlStateManager.disableBlend();
		
		for(int i=0; i < 46; i++) {
			Slot slot = this.inventorySlots.getSlot(i);
			if(!slot.getStack().isEmpty() && this.isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseX, mouseZ)) {
				this.renderToolTip(slot.getStack(), mouseX-this.guiLeft, mouseZ-this.guiTop);
			}
		}
		
	}
	
	/*GUIの背景の描画処理*/
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
		
		this.mc.renderEngine.bindTexture(texture_back);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		ContainerBookrest container = (ContainerBookrest)this.inventorySlots;
		
	}
	
	/*GUIが開いている時にゲームの処理を止めるかどうか。*/
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		ContainerBookrest container = (ContainerBookrest)this.inventorySlots;
		
	}
	
}
