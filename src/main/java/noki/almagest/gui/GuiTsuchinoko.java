package noki.almagest.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import noki.almagest.ModInfo;
import noki.almagest.entity.EntityTsuchinoko;
import noki.almagest.saveddata.gamedata.ERandomTalkTsuchinoko;


/**********
 * @class GuiContainerBookrest
 *
 * @description
 */
public class GuiTsuchinoko extends GuiScreen {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/gui/mira_inventory.png");
	
	private static final int talkXSize = 256;
	private static final int talkYSize = 50;
	private int left;
	private int top;
	
	private TalkEntry talkEntry = new TalkEntry();
	private EntityTsuchinoko tsuchinoko;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public void initGui() {
		
		this.left = (int)Math.floor(((double)this.width-(double)talkXSize)/2.0D);
		this.top = this.height-talkYSize-10;
		
		this.talkEntry.setRadomTalk();
		
	}
	
	/*GUIの文字等の描画処理*/
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		
		this.mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(this.left, this.top, 0, 139, talkXSize, talkYSize);
		
		String[] messages = this.talkEntry.getMessage();
		for(int i=0; i<messages.length; i++) {
			this.drawString(this.fontRenderer, messages[i], this.left+10, this.top+10, 0xffffff);
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
		
		if(this.left+5<=mouseX && mouseX<=this.left+talkXSize-10 && this.top+5<=mouseY && mouseY<=this.top+talkYSize-5) {
			if(this.talkEntry.isLast()) {
				this.mc.displayGuiScreen((GuiScreen)null);
			}
			else {
				this.talkEntry.goNext();
			}
		}
		
	}
	
	@Override
	public void onGuiClosed() {
		
		this.tsuchinoko.setTalking(false);
		this.tsuchinoko.setTalker(null);
		
	}
	
	public void setTsuchiniko(EntityTsuchinoko tsuchinoko) {
		
		this.tsuchinoko = tsuchinoko;
		
	}
	
	public class TalkEntry {
		
		private ERandomTalkTsuchinoko talk;
		private int step;
		
		public void setRadomTalk() {
			Random random = new Random();
			this.talk = ERandomTalkTsuchinoko.values()[random.nextInt(ERandomTalkTsuchinoko.values().length)];
			this.step = 1;
		}
		
		public void goNext() {
			this.step++;
		}
		
		public String[] getMessage() {
			return this.explode(this.talk.getLocalTalk(this.step), GuiTsuchinoko.talkXSize-10, 3);
		}
		
		public boolean isLast() {
			if(this.step == this.talk.getTalkEnd()) {
				return true;
			}
			return false;
		}
		
		private String[] explode(String string, int width, int maxRow) {
			String[] split = string.split("<br>", 0);
			
			ArrayList<String> stringSet = new ArrayList<String>();
			String remain;
			for(String each: split) {
				remain = each;
				while(GuiTsuchinoko.this.fontRenderer.getStringWidth(remain) > width && stringSet.size() != maxRow) {
					for(int i=1; i<=100; i++) {
						if(GuiTsuchinoko.this.fontRenderer.getStringWidth(remain.substring(0, i)) > width) {
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
	
}
