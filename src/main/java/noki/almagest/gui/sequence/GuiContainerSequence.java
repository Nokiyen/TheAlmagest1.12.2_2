package noki.almagest.gui.sequence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import noki.almagest.AlmagestCore;
import noki.almagest.ModInfo;
import noki.almagest.packet.PacketHandler;
import noki.almagest.packet.PacketSyncContainer;


/**********
 * @class GuiContainerSequence
 *
 * @description
 */
public class GuiContainerSequence extends GuiContainer {
	
	
	//******************************//
	// define member variables.
	//******************************//
	protected ContainerSequence container;
	
	private ArrayList<char[]> lines = new ArrayList<char[]>();
	private int lineLength;
	private int showingTick;
	private double interval = 1.5;
	private int arrowTick;
	
	private int subDisplayX;
	private int subDisplayY;
	private int subInnerWidth;
	private int subDisplayWidth;
	private int subDisplayHeight;
	private int choiceNumber;
	
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/gui/mira_inventory.png");
	private static final int talkXSize = 256;
	private static final int talkYSize = 50;
	private static final int inventoryXSize = 256;
	private static final int inventoryYSize = 139;
	private static final int totalXSize = 256;
	private static final int totalYSize = 200;
	
	private static final int subHeaderLeftWidth = 4;
	private static final int subHeaderLeftHeight = 3;
	private static final int subHeaderLeftX = 0;
	private static final int subHeaderLeftY = 189;

	private static final int subHeaderRightWidth = 4;
	private static final int subHeaderRightHeight = 3;
	private static final int subHeaderRightX = 74;
	private static final int subHeaderRightY = 189;
	
	private static final int subHeaderInnerWidth = 70;
	private static final int subHeaderInnerHeight = 3;
	private static final int subHeaderInnerX = 4;
	private static final int subHeaderInnerY = 189;
	
	private static final int subFooterLeftWidth = 4;
	private static final int subFooterLeftHeight = 3;
	private static final int subFooterLeftX = 0;
	private static final int subFooterLeftY = 234;

	private static final int subFooterRightWidth = 4;
	private static final int subFooterRightHeight = 3;
	private static final int subFooterRightX = 74;
	private static final int subFooterRightY = 234;
	
	private static final int subFooterInnerWidth = 70;
	private static final int subFooterInnerHeight = 3;
	private static final int subFooterInnerX = 4;
	private static final int subFooterInnerY = 234;
	
	private static final int subSelectLeftWidth = 4;
	private static final int subSelectLeftHeight = 14;
	private static final int subSelectLeftX = 0;
	private static final int subSelectLeftY = 192;

	private static final int subSelectRightWidth = 4;
	private static final int subSelectRightHeight = 14;
	private static final int subSelectRightX = 74;
	private static final int subSelectRightY = 192;
	
	private static final int subSelectInnerWidth = 70;
	private static final int subSelectInnerHeight = 14;
	private static final int subSelectInnerX = 4;
	private static final int subSelectInnerY = 192;
	
	private static final int subOverlayWidth = 70;
	private static final int subOverlayHeight = 12;
	private static final int subOverlayX = 78;
	private static final int subOverlayY = 189;
	
	private static final int padding = 10;
	private static final int lineNumber = 3;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public GuiContainerSequence(ContainerSequence container) {
		
		super(container);
		this.container = container;
		this.xSize = totalXSize;
		this.ySize = totalYSize;
		
	}
	
	@Override
	public void initGui() {
		
		super.initGui();
		this.prepareForNextSequence();
		
	}
	
	@Override
	public void updateScreen() {
		
		super.updateScreen();
		
		int totalLength = 0;
		char[] eachLine;
		for(int i=0; i<lineNumber; i++) {
			if(this.lines.size()>i) {
				eachLine = this.lines.get(i);
				if(eachLine != null) {
					totalLength += eachLine.length;
				}
			}
		}
		if(this.showingTick <= (double)totalLength*this.interval) {
			this.showingTick++;
		}
		
		this.arrowTick++;
		this.arrowTick = this.arrowTick%60;
		
	}
	
	/*GUIの文字等の描画処理*/
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
		//transparent drawing.
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		
		GlStateManager.disableBlend();
		
		
		this.drawMessage(padding, totalYSize-talkYSize+15, 0xffffff);
		
		//other drawing.
		switch(this.container.currentType()) {
			case Talk:
				break;
			case Choice:
				String[] choiceMessages = ((SequenceChoice)this.container.currentSequence()).getChoiceMessage();
				for(int i=0; i<choiceMessages.length; i++) {
					this.drawCenteredString(this.fontRenderer, choiceMessages[i],
							this.subDisplayX+this.subDisplayWidth/2-this.guiLeft,
							this.subDisplayY+subHeaderInnerHeight+subSelectInnerHeight*i-this.guiTop+3, 0xffffff);
				}
				break;
			case Inventory:
				for(int i=0; i < this.inventorySlots.getInventory().size(); i++) {
					Slot slot = this.inventorySlots.getSlot(i);
					if(!slot.getStack().isEmpty() && this.isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseX, mouseZ)) {
						this.renderToolTip(slot.getStack(), mouseX-this.guiLeft, mouseZ-this.guiTop);
					}
				}
				break;
		}
		
	}
	
	private void drawMessage(int x, int y, int color) {
		
		char[] eachLine;
		int length = 0;
		int totalLength = 0;
		for(int i=0; i<lineNumber; i++) {
			if(this.lines.size()>i) {
				eachLine = this.lines.get(i);
				if(eachLine != null) {
					length = (int)Math.min(eachLine.length, (double)this.showingTick/this.interval-totalLength);
					if(length != 0) {
						this.drawString(this.fontRenderer, String.valueOf(Arrays.copyOfRange(eachLine, 0, length)), x, y+10*i, color);
					}
				}
				totalLength += length;
			}
		}
		
	}
	
	/*GUIの背景の描画処理*/
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
		//mainly for background image.
		
		
		//bind texture.
		this.mc.renderEngine.bindTexture(texture);
		
		//all types have a text box.
		this.drawTexturedModalRect(this.guiLeft, this.guiTop+totalYSize-talkYSize, 0, 139, talkXSize, talkYSize);
		
		//other images.
		switch(this.container.currentType()) {
			case Talk:
				//nothing to draw.
				break;
			case Choice:
				//draw header part.
				this.drawTexturedModalRect(this.subDisplayX, this.subDisplayY,
						subHeaderLeftX, subHeaderLeftY, subHeaderLeftWidth, subHeaderLeftHeight);
				this.drawVariableWidth(this.subDisplayX+subHeaderLeftWidth, this.subDisplayY,
						subHeaderInnerX, subHeaderInnerY, subHeaderInnerWidth, subHeaderInnerHeight, this.subInnerWidth);
				this.drawTexturedModalRect(this.subDisplayX+subHeaderLeftWidth+this.subInnerWidth, this.subDisplayY,
						subHeaderRightX, subHeaderRightY, subHeaderRightWidth, subHeaderRightHeight);
				//draw the choice backgrounds.
				for(int i=0; i<this.choiceNumber; i++) {
					this.drawTexturedModalRect(this.subDisplayX, this.subDisplayY+subHeaderInnerHeight+subSelectInnerHeight*i,
							subSelectLeftX, subSelectLeftY, subSelectLeftWidth, subSelectLeftHeight);
					this.drawVariableWidth(this.subDisplayX+subSelectLeftWidth, this.subDisplayY+subHeaderInnerHeight+subSelectInnerHeight*i,
							subSelectInnerX, subSelectInnerY, subSelectInnerWidth, subSelectInnerHeight, this.subInnerWidth);
					this.drawTexturedModalRect(this.subDisplayX+subHeaderLeftWidth+this.subInnerWidth, this.subDisplayY+subHeaderInnerHeight+subSelectInnerHeight*i,
							subSelectRightX, subSelectRightY, subSelectRightWidth, subSelectRightHeight);
				}
				//draw footer part.
				this.drawTexturedModalRect(this.subDisplayX, this.subDisplayY+subHeaderInnerHeight+subSelectInnerHeight*this.choiceNumber,
						subFooterLeftX, subFooterLeftY, subFooterLeftWidth, subFooterLeftHeight);
				this.drawVariableWidth(this.subDisplayX+subFooterLeftWidth, this.subDisplayY+subHeaderInnerHeight+subSelectInnerHeight*this.choiceNumber,
						subFooterInnerX, subFooterInnerY, subFooterInnerWidth, subFooterInnerHeight, this.subInnerWidth);
				this.drawTexturedModalRect(this.subDisplayX+subFooterLeftWidth+this.subInnerWidth, this.subDisplayY+subHeaderInnerHeight+subSelectInnerHeight*this.choiceNumber,
						subFooterRightX, subFooterRightY, subFooterRightWidth, subFooterRightHeight);
				
				//overlay.
				for(int i=0; i<this.choiceNumber; i++) {
					if(this.isCursorInRegion(mouseX, mouseZ,
							this.subDisplayX+subSelectLeftWidth, this.subDisplayY+subHeaderInnerHeight+subSelectInnerHeight*i,
							this.subInnerWidth, subSelectInnerHeight)) {
						this.drawVariableWidth(this.subDisplayX+subSelectLeftWidth, this.subDisplayY+subHeaderInnerHeight+subSelectInnerHeight*i+1,
								subOverlayX, subOverlayY, subOverlayWidth, subOverlayHeight, this.subInnerWidth);
						break;
					}
				}
				break;
			case Inventory:
				this.drawTexturedModalRect(this.guiLeft, this.guiTop+((totalYSize-talkYSize-inventoryYSize)/2), 0, 0, inventoryXSize, inventoryYSize);
				break;
		}
		
	}
	
	private void drawVariableWidth(int drawX, int drawY, int imgX, int imgY, int imgWidth, int imgHeight, int targetWidth) {
		
		int loop = 0;
		int current = 0;
		for(current=targetWidth; current>imgWidth; current=current-imgWidth) {
			this.drawTexturedModalRect(drawX+imgWidth*loop, drawY, imgX, imgY, imgWidth, imgHeight);
			loop++;
		}
		this.drawTexturedModalRect(drawX+imgWidth*loop, drawY, imgX, imgY, current, imgHeight);
		
	}
	
	private boolean isCursorInRegion(int cursorX, int cursorY, int regionX, int regionY, int regionWidth, int regionHeight) {
		
		return regionX<=cursorX && cursorX<=regionX+regionWidth && regionY<=cursorY && cursorY<=regionY+regionHeight;
		
	}
	
	/*GUIが開いている時にゲームの処理を止めるかどうか。*/
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		boolean clickTalkWindow = this.isCursorInRegion(mouseX, mouseY, this.guiLeft, this.guiTop+totalYSize-talkYSize, talkXSize, talkYSize);
		boolean clickSelectWindow = false;
		boolean clickWhenShowing = false;
		boolean isEnd = false;
		ESequenceType prevtype = this.container.currentType();
		
		//click the talk window when still showing?
		if(clickTalkWindow) {
			if((double)this.showingTick/this.interval-this.lineLength < 0) {
				this.showingTick = (int)Math.ceil((double)lineLength*this.interval);
				clickWhenShowing = true;
			}
		}
		
		switch(this.container.currentType()) {
			case Talk:
				if(clickTalkWindow && !clickWhenShowing) {
					isEnd = this.goToNextSequence();
				}
				break;
			case Choice:
				for(int i=0; i<this.choiceNumber; i++) {
					if(this.isCursorInRegion(mouseX, mouseY,
							this.subDisplayX+subSelectLeftWidth, this.subDisplayY+subHeaderInnerHeight+subSelectInnerHeight*i,
							this.subInnerWidth, subSelectInnerHeight)) {
						clickSelectWindow = true;
						isEnd = this.goToNextSequence(i+1);
						break;
					}
				}
				break;
			case Inventory:
				break;
		}
		
		if(clickSelectWindow) {
			this.prepareForNextSequence();
		}
		else if(prevtype == ESequenceType.Talk && clickTalkWindow && !clickWhenShowing) {
			if(!isEnd) {
				this.prepareForNextSequence();
			}
			else {
				this.mc.player.closeScreen();
			}
			
		}
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		
		if(this.container.currentType() == ESequenceType.Inventory && ((SequenceInventory)this.container.currentSequence()).isGiving()) {
			switch (button.id) {
				case 1:
					this.goToNextSequence();
					this.prepareForNextSequence();
					break;
			}
		}
		
	}
	
	private boolean goToNextSequence() {
		
		return this.goToNextSequence(0);
		
	}
	
	private boolean goToNextSequence(int nextFlag) {
		
		AlmagestCore.log("go to next sequence.");
		PacketHandler.instance.sendToServer(new PacketSyncContainer(nextFlag));
		return this.container.goToNextSequence(nextFlag);
		
	}
	
	private void prepareForNextSequence() {
		
		//prepare for sub windows.
		this.buttonList.clear();
		
		switch(this.container.currentType()) {
			case Talk:
				break;
			case Choice:
				//define sub display width, based on the choice strings.
				String[] choiceMessages = ((SequenceChoice)this.container.currentSequence()).getChoiceMessage();
				int maxWidth = 0;
				for(int i=0; i<choiceMessages.length; i++) {
					maxWidth = Math.max(maxWidth, this.fontRenderer.getStringWidth(choiceMessages[i]));
				}
				maxWidth = maxWidth + padding;
				this.subInnerWidth = maxWidth;
				this.subDisplayWidth = this.subInnerWidth+subSelectLeftWidth+subSelectRightWidth;
				this.subDisplayHeight = subHeaderInnerHeight + subFooterInnerHeight + subSelectInnerHeight * choiceMessages.length;
				this.subDisplayX = this.guiLeft+((totalXSize-this.subDisplayWidth)/2);
				this.subDisplayY = this.guiTop+((totalYSize-talkYSize-this.subDisplayHeight)/2);
				
				//keep the number of choices.
				this.choiceNumber = choiceMessages.length;
				break;
			case Inventory:
				if(((SequenceInventory)this.container.currentSequence()).isGiving()) {
					this.buttonList.add(new GuiButton(1, this.guiLeft+150, this.guiTop+26, 50, 13, "give"));
				}
				break;
		}
		
		//prepare for talk window.
		String message = this.container.currentSequence().getMessage();
		//§r, why???
		if(message.substring(message.length()-2, message.length()).equals("§r")) {
			message = message.substring(0, message.length()-2);
		}
		String[] tempLines = this.explode(message, talkXSize-padding-6, lineNumber);
		
		this.lines.clear();
		this.lineLength = 0;
		this.showingTick = 0;
		char[] eachLine;
		for(String each: tempLines) {
			eachLine = each.toCharArray();
			this.lines.add(eachLine);
			this.lineLength += eachLine.length;
		}
		
	}
	
	private String[] explode(String string, int width, int maxRow) {
		
		String[] split = string.split("<br>", 0);
		
		ArrayList<String> stringSet = new ArrayList<String>();
		String remain;
		for(String each: split) {
			remain = each;
			while(this.fontRenderer.getStringWidth(remain) > width && stringSet.size() != maxRow) {
				for(int i=1; i<=100; i++) {
					if(this.fontRenderer.getStringWidth(remain.substring(0, i)) > width) {
						stringSet.add(remain.substring(0, i-1));
						remain = remain.substring(i-1);
						break;
					}
				}
			}
			if(stringSet.size() < maxRow) {
				stringSet.add(remain);
			}
		}
		
		return stringSet.toArray(new String[stringSet.size()]);
		
	}

}
