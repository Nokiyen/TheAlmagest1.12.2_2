package noki.almagest.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import noki.almagest.ModInfo;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.gui.ContainerBookrest.AbilityState;
import noki.almagest.packet.PacketHandler;
import noki.almagest.packet.PacketUpdateBookrest;


/**********
 * @class GuiContainerBookrest
 *
 * @description
 */
public class GuiContainerBookrest extends GuiContainer {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/gui/bookrest.png");
	
	private int currentTopAbility;
	private int currentScrollTop;
	private boolean isPressing;
	@SuppressWarnings("unused")
	private int pressedY;
	private int pressedYDiff;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public GuiContainerBookrest(EntityPlayer player, World world, int x, int y, int z) {
		
		super(new ContainerBookrest(player.inventory, world, new BlockPos(x, y, z)));
		this.xSize = 256;
		this.ySize = 210;
		
		this.currentTopAbility = 0;
		this.currentScrollTop = 0;
		this.isPressing = false;
		this.pressedY = 0;
		this.pressedYDiff = 0;
		
	}
	
	/*GUIの文字等の描画処理*/
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
		
		ContainerBookrest container = (ContainerBookrest)this.inventorySlots;
		
		this.mc.renderEngine.bindTexture(texture);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		for(int i=0; i<12; i++) {
			if(container.getLineState()[i] == 2) {
				switch(i) {
					case 0:
					case 1:
						this.drawTexturedModalRect(27+i*28, 32, 0, 241, 11, 4);
						break;
					case 5:
					case 6:
						this.drawTexturedModalRect(27+(i-5)*28, 60, 0, 241, 11, 4);
						break;
					case 10:
					case 11:
						this.drawTexturedModalRect(27+(i-10)*28, 89, 0, 241, 11, 4);
						break;
					case 2:
					case 3:
					case 4:
						this.drawTexturedModalRect(16+(i-2)*28, 43, 0, 245, 4, 11);
						break;
					case 7:
					case 8:
					case 9:
						this.drawTexturedModalRect(16+(i-7)*28, 71, 0, 245, 4, 11);
						break;
				}
			}
			if(container.getLineState()[i] == 1) {
				switch(i) {
					case 0:
					case 1:
						this.drawTexturedModalRect(27+i*28, 32, 0, 237, 11, 4);
						break;
					case 5:
					case 6:
						this.drawTexturedModalRect(27+(i-5)*28, 60, 0, 237, 11, 4);
						break;
					case 10:
					case 11:
						this.drawTexturedModalRect(27+(i-10)*28, 89, 0, 237, 11, 4);
						break;
					case 2:
					case 3:
					case 4:
						this.drawTexturedModalRect(16+(i-2)*28, 43, 4, 245, 4, 11);
						break;
					case 7:
					case 8:
					case 9:
						this.drawTexturedModalRect(16+(i-7)*28, 71, 4, 245, 4, 11);
						break;
				}
			}
		}
		
		if(container.craftResult != null && container.craftResult.isEmpty() == false) {
			if(container.getMemory() < 100) {
				this.drawTexturedModalRect(216, 70, 88+45, 236, 9, 20);
			}
			else if(container.getMemory() < 200) {
				this.drawTexturedModalRect(216, 70, 88+36, 236, 9, 20);
			}
			else if(container.getMemory() < 300) {
				this.drawTexturedModalRect(216, 70, 88+27, 236, 9, 20);
			}
			else if(container.getMemory() < 400) {
				this.drawTexturedModalRect(216, 70, 88+18, 236, 9, 20);
			}
			else if(container.getMemory() < 500) {
				this.drawTexturedModalRect(216, 70, 88+9, 236, 9, 20);
			}
			else {
				this.drawTexturedModalRect(216, 70, 88, 236, 9, 20);
			}
		}
		
		for(int i=0; i<6; i++) {
			if(i+this.currentTopAbility>=container.getAbilities().size()) {
				break;
			}
			int abilityId = container.getAbilities().get(i+this.currentTopAbility).getAbilityId();
			int level = container.getAbilities().get(i+this.currentTopAbility).getLevel();
			this.drawString(this.fontRenderer,
					new TextComponentTranslation(StarAbilityCreator.getAbility(abilityId).getName(level)).getFormattedText(),
					95, 23+(i)*14, 0xffffff);
		}
		
		GlStateManager.disableBlend();
		
		//for tooltip.
		for(int i=0; i < 46; i++) {
			Slot slot = this.inventorySlots.getSlot(i);
			if(!slot.getStack().isEmpty() && this.isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseX, mouseZ)) {
				this.renderToolTip(slot.getStack(), mouseX-this.guiLeft, mouseZ-this.guiTop);
			}
		}
		
		//for ability hovering text.
		for(int i=0; i<6; i++) {
			if(i+this.currentTopAbility>=container.getAbilities().size()) {
				break;
			}
			if(this.guiLeft+93<=mouseX && mouseX<=this.guiLeft+93+87 && this.guiTop+21+i*14<=mouseZ && mouseZ<=this.guiTop+21+i*14+12) {
				ArrayList<String> texts = new ArrayList<String>();
				AbilityState targetState = container.getAbilities().get(i+this.currentTopAbility);
				if(targetState.getAbility().canCombine() && targetState.getLevel() > 2) {
					String combineLine = "";
					combineLine += new TextComponentTranslation(StarAbilityCreator.getAbility(targetState.getAbilityId()).getName(targetState.getLevel()-2))
							.setStyle(new Style().setColor(TextFormatting.GREEN)).getFormattedText();
					combineLine += new TextComponentTranslation("×")
							.setStyle(new Style().setColor(TextFormatting.GREEN)).getFormattedText();;
					combineLine += new TextComponentTranslation(StarAbilityCreator.getAbility(targetState.getAbilityId()).getName(targetState.getLevel()-1))
									.setStyle(new Style().setColor(TextFormatting.GREEN)).getFormattedText();
					texts.add(combineLine);
				}
				texts.addAll(this.explode(
						new TextComponentTranslation("almagest.ability."+String.format("%02d",targetState.getAbilityId())+"."+targetState.getLevel()+".exp").getFormattedText(), 70));
				this.drawHoveringText(texts, mouseX-this.guiLeft, mouseZ-this.guiTop);
			}
		}
		
	}
	
	/*GUIの背景の描画処理*/
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
		
		//for background.
		this.mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		//for ability slot background on hovering.
		ContainerBookrest container = (ContainerBookrest)this.inventorySlots;
		for(int i=0; i<6; i++) {
			if(i+this.currentTopAbility>=container.getAbilities().size()) {
				break;
			}
			if(container.getAbilities().get(i+this.currentTopAbility).selected()
					|| (this.guiLeft+93<=mouseX && mouseX<=this.guiLeft+93+87 && this.guiTop+21+i*14<=mouseZ && mouseZ<=this.guiTop+21+i*14+12)) {
				this.drawTexturedModalRect(this.guiLeft+93, this.guiTop+21+i*14, 0, 210, 87, 12);
			}
		}
		
		//for scroll button.
		if(container.abilityCountChanged()) {
			this.currentTopAbility = 0;
			this.currentScrollTop = 0;
			this.isPressing = false;
			this.pressedY = 0;
			this.pressedYDiff = 0;
		}
		if(container.getAbilities().size() > 6) {
			this.currentTopAbility = MathHelper.floor((double)this.currentScrollTop/(84D/(double)(container.getAbilities().size()-5)));
			this.drawTexturedModalRect(this.guiLeft+182, this.guiTop+20+this.currentScrollTop, 11, 242, 7, 14);
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
		
		//for abilities.
		ContainerBookrest container = (ContainerBookrest)this.inventorySlots;
		for(int i=0; i<6; i++) {
			if(i+this.currentTopAbility>=container.getAbilities().size()) {
				break;
			}
			if(this.guiLeft+93<=mouseX && mouseX<=this.guiLeft+93+87 && this.guiTop+21+i*14<=mouseY && mouseY<=this.guiTop+21+i*14+12) {
				int currentSelected = 0;
				for(AbilityState state: container.getAbilities()) {
					if(state.selected()) {
						currentSelected++;
					}
				}
				if((currentSelected < container.getAbilitySelectLimit() && !container.getAbilities().get(i+this.currentTopAbility).selected()) || 
						container.getAbilities().get(i+this.currentTopAbility).selected()) {
					container.switchAbilitySelected(i+this.currentTopAbility);
					PacketHandler.instance.sendToServer(new PacketUpdateBookrest(i+this.currentTopAbility));
				}
			}
		}
		
		//for ability's scroll.
		if(this.guiLeft+182 <= mouseX && mouseX <= this.guiLeft+182+7 &&
				this.guiTop+20+this.currentScrollTop <= mouseY && mouseY <= this.guiTop+20+this.currentScrollTop+14) {
			this.isPressing = true;
			this.pressedY = mouseY;
			this.pressedYDiff = mouseY - (this.guiTop+20+this.currentScrollTop);
		}
		
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		
		super.mouseReleased(mouseX, mouseY, state);
		
		if(this.isPressing) {
			this.isPressing = false;
		}
		
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		
		if(this.isPressing) {
			if(this.guiTop+20 <= (mouseY-this.pressedYDiff) && mouseY+(14-this.pressedYDiff) <= this.guiTop+20+85) {
				this.currentScrollTop = mouseY-(this.guiTop+20)-this.pressedYDiff;
			}
		}
		
	}
	
	@Override
	protected void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font) {
		
		if(textLines == null) {
			return;
		}
		
		int predicted = y + textLines.size()*10+12;
		int diff = 0;
		if(this.height < predicted) {
			diff = predicted - this.height;
		}
		super.drawHoveringText(textLines, x, y-diff, font);
		
	}
	
	private List<String> explode(String string, int width) {
		
		String[] split = string.split("<br>", 0);
		
		ArrayList<String> stringSet = new ArrayList<String>();
		String remain;
		for(String each: split) {
			remain = each;
			while(this.fontRenderer.getStringWidth(remain) > width) {
				for(int i=1; i<=100; i++) {
					if(this.fontRenderer.getStringWidth(remain.substring(0, i)) > width) {
						stringSet.add(remain.substring(0, i-1));
						remain = remain.substring(i-1);
						break;
					}
				}
			}
			stringSet.add(remain);
		}
		
		return stringSet;
		
	}
	
}
