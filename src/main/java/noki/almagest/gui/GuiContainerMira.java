package noki.almagest.gui;

import java.io.IOException;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import noki.almagest.AlmagestCore;
import noki.almagest.ModInfo;
import noki.almagest.packet.PacketHandler;
import noki.almagest.packet.PacketUpdateMira;


/**********
 * @class GuiContainerMira
 *
 * @description
 */
public class GuiContainerMira extends GuiContainer {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/gui/mira_inventory.png");
	
	private static final int talkXSize = 256;
	private static final int talkYSize = 50;
	private static final int inventoryXSize = 256;
	private static final int inventoryYSize = 139;
	private static final int selectXSize = 78;
	private static final int selectYSize = 48;
	private static final int totalXSize = 256;
	private static final int totalYSize = 200;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public GuiContainerMira(EntityPlayer player, World world, int x, int y, int z) {
		
		super(new ContainerMira(player, world, new BlockPos(x, y, z)));
		this.xSize = totalXSize;
		this.ySize = totalYSize;
		
	}
	
	/*GUIの文字等の描画処理*/
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
		
		this.mc.renderEngine.bindTexture(texture);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		
		GlStateManager.disableBlend();
		
		switch((MiraState)((ContainerMira)this.inventorySlots).state) {
			case SELECT:
	//			int x = ((256-selectXSize)/2);
				int y = ((totalYSize-talkYSize-selectYSize)/2);
	//			this.drawTexturedModalRect(x, y, 0, 190, selectXSize, selectYSize);
				this.drawCenteredString(this.fontRenderer, new TextComponentTranslation("almagest.gui.mira.constellation").getFormattedText(),
						totalXSize/2, y+5, 0xffffff);
				this.drawCenteredString(this.fontRenderer, new TextComponentTranslation("almagest.gui.mira.book").getFormattedText(),
						totalXSize/2, y+5+14, 0xffffff);
				this.drawCenteredString(this.fontRenderer, new TextComponentTranslation("almagest.gui.mira.talk").getFormattedText(),
						totalXSize/2, y+5+28, 0xffffff);
			case CONSTELLATION:
			case BOOK:
				AlmagestCore.log("slot num / {}.", this.inventorySlots.getInventory().size());
				for(int i=0; i < this.inventorySlots.getInventory().size(); i++) {
					AlmagestCore.log("slot / {}.", i);
					Slot slot = this.inventorySlots.getSlot(i);
					if(!slot.getStack().isEmpty() && this.isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseX, mouseZ)) {
						AlmagestCore.log("in range / {}.", i);
						this.renderToolTip(slot.getStack(), mouseX-this.guiLeft, mouseZ-this.guiTop);
					}
				}
			case TALK:
				this.drawString(this.fontRenderer, ((ContainerMira)this.inventorySlots).state.getMessage(),
						10, totalYSize-talkYSize+15, 0xffffff);
				break;
		}
		
	}
	
	/*GUIの背景の描画処理*/
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
		
		this.mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop+totalYSize-talkYSize, 0, 139, talkXSize, talkYSize);
		
		switch((MiraState)((ContainerMira)this.inventorySlots).state) {
		case SELECT:
			int x = this.guiLeft+((256-selectXSize)/2);
			int y = this.guiTop+((totalYSize-talkYSize-selectYSize)/2);
			this.drawTexturedModalRect(x, y, 0, 189, selectXSize, selectYSize);
			
			for(int i=0; i<3; i++) {
				if(x<=mouseX && mouseX<=x+selectXSize-10 && y<=mouseZ && mouseZ<=y+12+i*14) {
					this.drawTexturedModalRect(x+5, y+5+i*14, 79, 189, 70, 12);
					break;
				}
			}
			break;
		case CONSTELLATION:
		case BOOK:
			this.drawTexturedModalRect(this.guiLeft, this.guiTop+((totalYSize-talkYSize-inventoryYSize)/2), 0, 0, inventoryXSize, inventoryYSize);
			break;
		case TALK:
			break;
		}
		
	}
	
	/*GUIが開いている時にゲームの処理を止めるかどうか。*/
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		if((MiraState)((ContainerMira)this.inventorySlots).state == MiraState.SELECT) {
			int x = this.guiLeft+((256-selectXSize)/2)+5;
			int y = this.guiTop+((totalYSize-talkYSize-selectYSize)/2)+5;
			for(int i=0; i<3; i++) {
				if(x<=mouseX && mouseX<=x+selectXSize-10 && y<=mouseY && mouseY<=y+12+i*14) {
					ContainerMira container = (ContainerMira)this.inventorySlots;
					switch(i) {
					case 0:
						container.setState(MiraState.CONSTELLATION);
						break;
					case 1:
						container.setState(MiraState.BOOK);
						break;
					case 2:
						container.setState(MiraState.TALK);
						break;
					}
					PacketHandler.instance.sendToServer(new PacketUpdateMira(i));
					break;
				}
			}
		}
		if((MiraState)((ContainerMira)this.inventorySlots).state == MiraState.TALK) {
			if(this.guiLeft+5<=mouseX && mouseX<=this.guiLeft+talkXSize-10 && 
					this.guiTop+totalYSize-talkYSize+5<=mouseY && mouseY<=this.guiTop+totalYSize-5) {
				((ContainerMira)this.inventorySlots).state.goToNextStep();
			}
		}
		
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
		if(keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
			this.mc.player.closeScreen();
		}
		super.keyTyped(typedChar, keyCode);

	}
	
}
