package noki.almagest.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import noki.almagest.AlmagestCore;
import noki.almagest.ModInfo;
import noki.almagest.ability.StarAbility;
import noki.almagest.ability.StarAbility.Effectable;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.attribute.AttributeHelper;
import noki.almagest.attribute.EStarAttribute;
import noki.almagest.attribute.AttributeHelper.InfoSet;
import noki.almagest.helper.HelperConstellation;
import noki.almagest.helper.HelperConstellation.LineData;
import noki.almagest.helper.HelperConstellation.OtherStarData;
import noki.almagest.helper.HelperConstellation.StarData;
import noki.almagest.recipe.StarRecipe;
import noki.almagest.registry.IWithRecipe;
import noki.almagest.saveddata.DataCategory;
import noki.almagest.saveddata.gamedata.GameData;
import noki.almagest.saveddata.gamedata.GameDataAbility2;
import noki.almagest.saveddata.gamedata.GameDataBlock;
import noki.almagest.saveddata.gamedata.GameDataConstellation;
import noki.almagest.saveddata.gamedata.GameDataHelp;
import noki.almagest.saveddata.gamedata.GameDataItem;
import noki.almagest.saveddata.gamedata.GameDataList;
import noki.almagest.saveddata.gamedata.GameDataMob;
import noki.almagest.saveddata.gamedata.GameDataRecipe;


/**********
 * @class GuiAlmagest
 *
 * @description ゲーム内図鑑、アルマゲストのGUIです。
 */
public class GuiAlmagest extends GuiScreen {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/gui/almagest.png");
	private static final ResourceLocation textureLeft = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/gui/almagest_left.png");
	private static final ResourceLocation textureRight = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/gui/almagest_right.png");
	
	private static int pageWidth = 200;
	private static int pageHeight = 236;
//	private static int textColor = defaultColor;
	private static int defaultColor = 0XFFB59556;
	
	private int left;
	private int top;
	private int centerX;
	private int centerY;
	
	@SuppressWarnings("unused")
	private ItemStack stack;
	private HashMap<Integer, PageContent> pages = new HashMap<Integer, PageContent>();
	
	private PageLink currentLink;
	private PageLink prevLink;
	private PageLink topLink;
	private PageLink secondLink;
	private PageLink thirdLink;
	
	private int secondMaxPage;
	private GameData secondTargetData;
	
	private int mouseClicked;
	
	private String[] expEffect;
	private String[] expMagnitude;
	private String[] expMira;
	private String[] expConstellationLeft;
	private String[] expConstellationRight;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public GuiAlmagest(ItemStack stack) {
		
		this.stack = stack;
		for(int i = 1; i <= 88; i++) {
			this.pages.put(i, new PageContent(i, 0));
		}
		
	}
	
	@Override
	public void initGui() {
		
		//initialize position.
		this.left = (int)Math.floor((double)(this.width-pageWidth*2)/2.0D);
		this.top = (int)Math.floor((double)(this.height-pageHeight)/2.0D);
		this.centerX = (int)Math.floor((double)this.width/2.0D);
		this.centerY = (int)Math.floor((double)this.height/2.0D);		
		
		//initialize link.
		this.currentLink = new PageLink();
		this.currentLink.setToFirst();
		this.prevLink = new PageLink();
		this.prevLink.setToFirst();
		
		this.topLink = new PageLink();
		this.topLink.setToFirst();
		this.secondLink = new PageLink();
		this.secondLink.setToSecond(null);
		this.thirdLink = new PageLink();
		this.thirdLink.setToThird(null);
		
		//initialize buttons.
		this.buttonList.clear();

		int adjust = 0;
		if(this.width%2 == 1) {
			adjust = 1;
		}
		
		//buttons in the first page (category).
		int start = 1;
//		int startHeight = this.height/2 - DataCategory.values().length*18/2;
		//0-8
		for(DataCategory each: DataCategory.values()) {
			this.buttonList.add(new CategoryButton(start, (int)Math.floor(this.width/2)+3+adjust, this.height/2-18*5+18*(start-1)+10, each));
			start++;
		}
		
		//buttons in the second page (list).
		//9-18
		for(int i=1; i<=10; i++) {
			this.buttonList.add(new EachButton(100+i, (int)Math.floor((this.width-pageWidth*2)/2)+12+adjust, this.height/2-18*5+18*(i-1)+10));
		}
		//19-28
		for(int i=1; i<=10; i++) {
			this.buttonList.add(new EachButton(200+i, (int)Math.floor(this.width/2)+3+adjust, this.height/2-18*5+18*(i-1)+10));
		}
		
		//paging buttons (list).
		//29,30
		this.buttonList.add(new PagingButton(301, this.width/2+pageWidth-12-30, (this.height-pageHeight)/2+14, false));//right
		this.buttonList.add(new PagingButton(302, this.width/2+pageWidth-12-45, (this.height-pageHeight)/2+14, true));//left
		
		
		//map buttons.
		//31,32,33
/*		this.buttonList.add(new StringButton(401, this.width/2+pageWidth-32, (this.height-pageHeight)/2+15, "almagest.gui.almagest.button.top"));
		this.buttonList.add(new StringButton(402, this.width/2+pageWidth-32, (this.height-pageHeight)/2+15, "almagest.gui.almagest.button.top"));
		this.buttonList.add(new StringButton(403, this.width/2+pageWidth-32, (this.height-pageHeight)/2+15, "almagest.gui.almagest.button.top"));*/
		this.buttonList.add(new StringButton(401, (this.width-pageWidth*2)/2+17, (this.height-pageHeight)/2+15, "almagest.gui.almagest.button.top"));
		this.buttonList.add(new StringButton(402, (this.width-pageWidth*2)/2+17, (this.height-pageHeight)/2+15, "almagest.gui.almagest.button.top"));
		this.buttonList.add(new StringButton(403, (this.width-pageWidth*2)/2+17, (this.height-pageHeight)/2+15, "almagest.gui.almagest.button.top"));
		
		//page up.
		//34
		this.buttonList.add(new UpButton(303, this.width/2+pageWidth-12-15, (this.height-pageHeight)/2+13));
		
		this.updateButtons();
		
	}
	
	public void updateButtons() {
		
		switch(this.currentLink.getFloor()) {
			case 1:
//				AlmagestCore.log2("enter case 1.");
				for(int i=0; i<=DataCategory.values().length-1; i++) {
					((AlmagestButton)this.buttonList.get(i)).enable().visible();
				}
				for(int i=9; i<=18; i++) {
					((AlmagestButton)this.buttonList.get(i)).disable().invisible();
				}
				for(int i=19; i<=28; i++) {
					((AlmagestButton)this.buttonList.get(i)).disable().invisible();
				}
				((AlmagestButton)this.buttonList.get(29)).disable().invisible();
				((AlmagestButton)this.buttonList.get(30)).disable().invisible();
				((AlmagestButton)this.buttonList.get(34)).enable().visible();
				
				this.updateMap();
				break;
			case 2:
				for(int i=0; i<=DataCategory.values().length-1; i++) {
					((AlmagestButton)this.buttonList.get(i)).disable().invisible();
				}
				
				ArrayList<GameData> dataSet;
				switch(this.currentLink.getCategory()) {
					case BLOCK:
					case ITEM:
					case MOB:
					case CONSTELLATION:
						dataSet =
							AlmagestCore.savedDataManager.getFlagData().getDataSet(this.currentLink.getCategory(), (this.currentLink.getPageNum()-1)*20+1, 10);
						for(int i=1; i<=10; i++) {
							if(i <= dataSet.size()) {
								((EachButton)this.buttonList.get(i+8)).setData(dataSet.get(i-1)).enable().visible();
							}
							else {
								((AlmagestButton)this.buttonList.get(i+8)).disable().invisible();
							}
						}
						dataSet =
								AlmagestCore.savedDataManager.getFlagData().getDataSet(this.currentLink.getCategory(), (this.currentLink.getPageNum()-1)*20+11, 10);
						for(int i=1; i<=10; i++) {
							if(i <= dataSet.size()) {
								((EachButton)this.buttonList.get(i+18)).setData(dataSet.get(i-1)).enable().visible();
							}
							else {
								((AlmagestButton)this.buttonList.get(i+18)).disable().invisible();
							}
						}
						
						if(AlmagestCore.savedDataManager.getFlagData().getMaxPage(this.currentLink.getCategory(), 20) != this.currentLink.getPageNum()) {
							((AlmagestButton)this.buttonList.get(29)).enable().visible();
						}
						else {
							((AlmagestButton)this.buttonList.get(29)).disable().invisible();
						}
						break;
					case LIST:
					case RECIPE:
					case ABILITY:
					case MEMO:
					case HELP:
						for(int i=1; i<=10; i++) {
							((AlmagestButton)this.buttonList.get(i+8)).disable().invisible();
						}
						dataSet =
								AlmagestCore.savedDataManager.getFlagData().getDataSet(this.currentLink.getCategory(), (this.currentLink.getPageNum()-1)*10+1, 10);
						for(int i=1; i<=10; i++) {
							if(i <= dataSet.size()) {
								((EachButton)this.buttonList.get(i+18)).setData(dataSet.get(i-1)).enable().visible();
/*								if(dataSet.get(i-1).isObtained() || AlmagestCore.proxy.getPlayer().isCreative()) {
									((EachButton)this.buttonList.get(i+18)).setData(dataSet.get(i-1)).enable().visible();
								}
								else {
									((EachButton)this.buttonList.get(i+18)).setData(dataSet.get(i-1)).disable().invisible();
								}*/
							}
							else {
								((AlmagestButton)this.buttonList.get(i+18)).disable().invisible();
							}
						}
						
						if(AlmagestCore.savedDataManager.getFlagData().getMaxPage(this.currentLink.getCategory(), 10) != this.currentLink.getPageNum()) {
							((AlmagestButton)this.buttonList.get(29)).enable().visible();
						}
						else {
							((AlmagestButton)this.buttonList.get(29)).disable().invisible();
						}
						break;
				}
				
				if(this.currentLink.getPageNum() != 1) {
					((AlmagestButton)this.buttonList.get(30)).enable().visible();
				}
				else {
					((AlmagestButton)this.buttonList.get(30)).disable().invisible();
				}
				
				((AlmagestButton)this.buttonList.get(34)).enable().visible();
				
				this.updateMap();
				break;
			case 3:
				switch(this.currentLink.getCategory()) {
					case BLOCK:
					case ITEM:
					case MOB:
					case CONSTELLATION:
						for(int i=0; i<=DataCategory.values().length-1; i++) {
							((AlmagestButton)this.buttonList.get(i)).disable().invisible();
						}
						for(int i=9; i<=18; i++) {
							((AlmagestButton)this.buttonList.get(i)).disable().invisible();
						}
						for(int i=19; i<=28; i++) {
							((AlmagestButton)this.buttonList.get(i)).disable().invisible();
						}
//						((AlmagestButton)this.buttonList.get(29)).disable().invisible();
//						((AlmagestButton)this.buttonList.get(30)).disable().invisible();
						
						GameData prevData = AlmagestCore.savedDataManager.getFlagData().getNextData(this.currentLink.getData(),
								this.currentLink.getCategory(), AlmagestCore.proxy.getPlayer().isCreative());
						if(prevData != null) {
							((AlmagestButton)this.buttonList.get(29)).enable().visible();
						}
						
						GameData nextData = AlmagestCore.savedDataManager.getFlagData().getNextData(this.currentLink.getData(),
								this.currentLink.getCategory(), AlmagestCore.proxy.getPlayer().isCreative());
						if(nextData != null) {
							((AlmagestButton)this.buttonList.get(30)).enable().visible();
						}
						
						((AlmagestButton)this.buttonList.get(34)).enable().visible();
						break;
					case LIST:
					case RECIPE:
					case ABILITY:
					case MEMO:
					case HELP:
/*						for(int i=1; i<=10; i++) {
							((AlmagestButton)this.buttonList.get(i+8)).disable().invisible();
						}
						dataSet =
								AlmagestCore.savedDataManager.getFlagData().getDataSet(this.currentLink.getCategory(), (this.currentLink.getPageNum()-1)*10+1, 10);
						for(int i=1; i<=10; i++) {
							if(i <= dataSet.size()) {
								if(dataSet.get(i-1).isObtained() || AlmagestCore.proxy.getPlayer().isCreative()) {
									((EachButton)this.buttonList.get(i+18)).setData(dataSet.get(i-1)).enable().visible();
								}
								else {
									((EachButton)this.buttonList.get(i+18)).setData(dataSet.get(i-1)).disable().invisible();
								}
							}
							else {
								((AlmagestButton)this.buttonList.get(i+18)).disable().invisible();
							}
						}
						((AlmagestButton)this.buttonList.get(29)).enable().visible();
						if(this.currentLink.getPageNum() != 1) {
							((AlmagestButton)this.buttonList.get(30)).enable().visible();
						}
						else {
							((AlmagestButton)this.buttonList.get(30)).disable().invisible();
						}
						((AlmagestButton)this.buttonList.get(34)).enable().visible();*/
						break;
				}
				this.updateMap();
				break;
		}
		
	}
	
	public void updateMap() {
		
		StringButton first = (StringButton)this.buttonList.get(31);
		StringButton second = (StringButton)this.buttonList.get(32);
		StringButton third = (StringButton)this.buttonList.get(33);
		switch(this.currentLink.getFloor()) {
			case 1:
				third.disable().invisible();
				second.disable().invisible();
//				((StringButton)first.disable().visible()).setx(this.width/2+pageWidth-20);
//				((StringButton)first.disable().visible()).setx((this.width-pageWidth*2)/2+40);
				((StringButton)first.disable().visible())
					.setx((this.width-pageWidth*2)/2+17);
				break;
			case 2:
				third.disable().invisible();
/*				((StringButton)second.disable().visible()).setDisplay(this.secondLink.getCategory().getDisplay())
					.setx(this.width/2+pageWidth-20);
				((StringButton)first.enable().visible()).setx(this.width/2+pageWidth-20-second.width-10);*/
				((StringButton)first.enable().visible())
					.setx((this.width-pageWidth*2)/2+17);
				((StringButton)second.disable().visible()).setDisplay(this.secondLink.getCategory().getDisplay())
					.setx((this.width-pageWidth*2)/2+17+first.width+10);
				break;
			case 3:
/*				((StringButton)third.disable().visible()).setDisplay(this.thirdLink.getData().getDisplay())
					.setx(this.width/2+pageWidth-20);
				((StringButton)second.enable().visible()).setDisplay(this.secondLink.getCategory().getDisplay())
					.setx(this.width/2+pageWidth-20-third.width-10);
				((StringButton)first.enable().visible()).setx(this.width/2+pageWidth-20-third.width-10-second.width-10);*/
				((StringButton)first.enable().visible())
					.setx((this.width-pageWidth*2)/2+17);
				((StringButton)second.enable().visible()).setDisplay(this.secondLink.getCategory().getDisplay())
					.setx((this.width-pageWidth*2)/2+17+first.width+10);
				((StringButton)third.disable().visible()).setDisplay(this.thirdLink.getData().getDisplay())
					.setx((this.width-pageWidth*2)/2+17+first.width+10+second.width+10);
				break;
		}
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		
		this.mouseClicked++;
		if(this.mouseClicked > 1) {
			return;
		}
		PageLink prev = this.prevLink;
		this.prevLink = this.currentLink.clone();
		
		switch(button.id) {
//			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				this.currentLink.setToSecond(((CategoryButton)button).getCategory());
				this.secondLink.setToSecond(((CategoryButton)button).getCategory());
				switch(this.currentLink.getCategory()) {
					case BLOCK:
					case ITEM:
					case MOB:
					case CONSTELLATION:
						this.secondMaxPage = AlmagestCore.savedDataManager.getFlagData().getMaxPage(this.currentLink.getCategory(), 20);
						break;
					case LIST:
					case RECIPE:
					case ABILITY:
					case MEMO:
					case HELP:
						this.secondMaxPage = AlmagestCore.savedDataManager.getFlagData().getMaxPage(this.currentLink.getCategory(), 10);
						break;
				}
				this.secondTargetData = null;
				break;
			case 101:
			case 102:
			case 103:
			case 104:
			case 105:
			case 106:
			case 107:
			case 108:
			case 109:
			case 110:
			case 201:
			case 202:
			case 203:
			case 204:
			case 205:
			case 206:
			case 207:
			case 208:
			case 209:
			case 210:
				switch(this.currentLink.getCategory()) {
					case BLOCK:
					case ITEM:
					case MOB:
					case CONSTELLATION:
						this.currentLink.setToThird(((EachButton)button).getData());
						this.thirdLink.setToThird(((EachButton)button).getData());
						this.setThirdExplanation();
						break;
					case LIST:
					case RECIPE:
					case ABILITY:
					case MEMO:
					case HELP:
						this.secondTargetData = ((EachButton)button).getData();
						break;
				}
			break;
			case 301:
				if(this.currentLink.getFloor() == 3) {
					GameData nextData = AlmagestCore.savedDataManager.getFlagData().getNextData(this.currentLink.getData(),
							this.currentLink.getCategory(), AlmagestCore.proxy.getPlayer().isCreative());
					if(nextData != null) {
						this.currentLink.setToThird(nextData);
						this.thirdLink.setToThird(nextData);
						this.setThirdExplanation();
					}
				}
				else {
					this.currentLink.nextPage();
				}
				break;
			case 302:
				if(this.currentLink.getFloor() == 3) {
					GameData prevData = AlmagestCore.savedDataManager.getFlagData().getPrevData(this.currentLink.getData(),
							this.currentLink.getCategory(), AlmagestCore.proxy.getPlayer().isCreative());
					if(prevData != null) {
						this.currentLink.setToThird(prevData);
						this.thirdLink.setToThird(prevData);
						this.setThirdExplanation();
					}
				}
				else {
					this.currentLink.prevPage();
				}
				break;
			case 303:
				this.currentLink = prev;
				break;
			case 401:
				this.currentLink.setToFirst();
				break;
			case 402:
				this.currentLink.setToSecond(this.secondLink.getCategory());
				this.currentLink.setPageNum(this.secondLink.getPageNum());
				this.secondTargetData = null;
				break;
			case 403:
				break;
		}
		
		this.updateButtons();
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		this.mouseClicked = 0;
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
		if(keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
			this.mc.displayGuiScreen((GuiScreen)null);

			if(this.mc.currentScreen == null) {
				this.mc.setIngameFocus();
			}
		}
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		//reset color.
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		//draw background.
		this.mc.getTextureManager().bindTexture(textureLeft);
		this.drawTexturedModalRect(this.centerX-200, this.centerY-118, 0, 0, 200, 236);
		this.mc.getTextureManager().bindTexture(textureRight);
		this.drawTexturedModalRect(this.centerX, this.centerY-118, 0, 0, 200, 236);
		
		
		this.mc.getTextureManager().bindTexture(texture);
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		//draw with floor and category.
		switch(this.currentLink.getFloor()) {
			case 2:
				//draw "current page / max page".
				int currentPageNumWidth = this.fontRenderer.getStringWidth(String.valueOf(this.currentLink.getPageNum()));
				int currentPageNumWidthOffset = 0;
				if(this.currentLink.getPageNum() % 10 == 1) {
					currentPageNumWidthOffset = 1;
				}
				this.fontRenderer.drawString(
						String.valueOf(this.currentLink.getPageNum()),
						this.width/2+128-currentPageNumWidth-currentPageNumWidthOffset-2, (this.height-pageHeight)/2+14, defaultColor);
				this.fontRenderer.drawString(
						"/", this.width/2+128, (this.height-pageHeight)/2+14, defaultColor);
				this.fontRenderer.drawString(
						String.valueOf(this.secondMaxPage), this.width/2+133, (this.height-pageHeight)/2+14, defaultColor);
				
				//each drawing.
				switch(this.currentLink.getCategory()) {
					case ABILITY:
						if(this.secondTargetData != null) this.drawForAbility();
						break;
					case LIST:
						if(this.secondTargetData != null) this.drawForList();
						break;
					case RECIPE:
						if(this.secondTargetData != null) this.drawForRecipe();
						break;
					case MEMO:
						break;
					case HELP:
//						this.drawForHelp();
						if(this.secondTargetData != null) this.drawForHelp();
						break;
					case BLOCK:
					case ITEM:
					case CONSTELLATION:
					case MOB:
						//maybe no rendering on second floor.
						break;
				}
				
				break;
			case 3:
				//each drawing.
				switch(this.currentLink.getCategory()) {
					case BLOCK:
					case ITEM:
						this.drawForBlock();
						break;
					case CONSTELLATION:
						this.drawForConstellation();
						break;
					case MOB:
						this.drawForMob(partialTicks);
						break;
					case ABILITY:
					case LIST:
					case RECIPE:
					case MEMO:
					case HELP:
						//maybe no rendering on third floor.
						break;
				}
				break;
			default:
				break;
		}
		
	}
	
	private void drawForBlock() {
		
		//left page.
		
		//display name.
		this.drawScaledTranslated(this.currentLink.getData().getDisplay(), this.left+20, this.top+103, 1.5D, defaultColor, true);
		
		//display bars and recipe, component.
		drawRect(this.left+16, this.top+118, this.centerX-5, this.top+120, 0xffd3c3a2);
		drawRect(this.left+104, this.top+120, this.left+106, this.top+223, 0xffd3c3a2);
		this.fontRenderer.drawString(this.getTranslated("almagest.gui.almagest.component", true), this.left+19, this.top+125, defaultColor);
//		this.fontRenderer.drawString(this.getTranslated("almagest.gui.almagest.recipe", true), this.left+110, this.top+125, defaultColor);
		
		//display component.
		int component = 0;
		for(EStarAttribute each: EStarAttribute.values()) {
			int level = AttributeHelper.getAttrributeLevel(((GameDataBlock)this.currentLink.getData()).getStack(), each);
			if(level != 0) {
				this.fontRenderer.drawString(this.getTranslated(each.getName())+" : "+level, this.left+21, this.top+143+component*16, defaultColor);
				component++;
			}
		}
		for(int i=0; i<=4; i++) {
			drawRect(this.left+18, this.top+138+i*16, this.left+100, this.top+139+i*16, 0xffd3c3a2);
		}
		
		//display recipe;
//		drawRect((this.width-pageWidth*2)/2+18, (this.height-pageHeight)/2+218, (this.width-pageWidth*2)/2+100, (this.height-pageHeight)/2+219, 0xffd3c3a2);
		boolean hasRecipe;
		if(this.currentLink.getCategory() == DataCategory.BLOCK) {
			hasRecipe = ((GameDataBlock)this.currentLink.getData()).getBlock() instanceof IWithRecipe;
		}
		else {
			hasRecipe = ((GameDataItem)this.currentLink.getData()).getItem() instanceof IWithRecipe;
		}
		if(hasRecipe) {
			IWithRecipe withRecipe;
			if(this.currentLink.getCategory() == DataCategory.BLOCK) {
				withRecipe = (IWithRecipe)((GameDataBlock)this.currentLink.getData()).getBlock();
			}
			else {
				withRecipe = (IWithRecipe)((GameDataItem)this.currentLink.getData()).getItem();
			}
			IRecipe recipe = withRecipe.getRecipe(((GameDataBlock)this.currentLink.getData()).getStack());
			if(recipe instanceof StarRecipe && ((StarRecipe)recipe).isSpecial() == false) {
				StarRecipe starRecipe = (StarRecipe)recipe;
				
				this.fontRenderer.drawString(
						this.getTranslated("almagest.gui.almagest.recipe", true)+" ("+starRecipe.getMaxStack()+")",
						this.left+110, this.top+125, defaultColor);

				int recipeCount = 0;
				for(Map.Entry<EStarAttribute, Integer> each: starRecipe.getAttribute().entrySet()) {
					this.fontRenderer.drawString(this.getTranslated(each.getKey().getName())+" : "+each.getValue(),
							this.left+21+93, this.top+143+recipeCount*16, defaultColor);
					recipeCount++;
				}
				for(ItemStack each: starRecipe.getStack()) {
					this.fontRenderer.drawString(this.getTranslated(each.getDisplayName()),
							this.left+21+93, this.top+143+recipeCount*16, defaultColor);
					recipeCount++;
				}
			}
			else {
				if(recipe instanceof StarRecipe) {
					this.fontRenderer.drawString(
							this.getTranslated("almagest.gui.almagest.recipe", true)+" ("+((StarRecipe)recipe).getMaxStack()+")",
							this.left+110, this.top+125, defaultColor);
				}
				else {
					this.fontRenderer.drawString(
							this.getTranslated("almagest.gui.almagest.recipe", true)+" ("+recipe.getIngredients().size()+")",
							this.left+110, this.top+125, defaultColor);
				}
				
				for(int i=0; i<=4; i++) {
					this.fontRenderer.drawString(
						this.getTranslated("almagest.gui.almagest."+this.currentLink.getData().getName().toString()+".recipe."+i),
						this.left+21+93, this.top+143+i*16, defaultColor);
				}
			}
		}
		else {
			this.fontRenderer.drawString(this.getTranslated("almagest.gui.almagest.recipe", true), this.left+110, this.top+125, defaultColor);
			this.fontRenderer.drawString(
					this.getTranslated("almagest.gui.almagest.recipe.nocraft"), this.left+21+93, this.top+143, 0XFF38b48b);
		}
		
		for(int i=0; i<=4; i++) {
			drawRect(this.left+18+93, this.top+138+i*16, this.left+100+93, this.top+139+i*16, 0xffd3c3a2);
		}
//		drawRect((this.width-pageWidth*2)/2+18+93, (this.height-pageHeight)/2+218, (this.width-pageWidth*2)/2+100+93, (this.height-pageHeight)/2+219, 0xffd3c3a2);
		
		//right page.
		int thirdStart = (this.height-pageHeight)/2+17;
		this.fontRenderer.drawString(new TextComponentTranslation("almagest.gui.almagest.effect").setStyle(new Style().setBold(true)).getFormattedText(),
				this.width/2+7, thirdStart, defaultColor);
		drawRect(this.width/2+6, thirdStart+9, this.width/2+pageWidth-13, thirdStart+11, 0xffd3c3a2);
/*			for(int i=0; i<=6; i++) {
			this.fontRenderer.drawString("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", this.width/2+7, thirdStart+14+i*10, defaultColor);
		}*/
		for(int i=0; i<this.expEffect.length; i++) {
			this.fontRenderer.drawString(this.expEffect[i], this.width/2+7, thirdStart+14+i*10, defaultColor);
		}
		thirdStart = (this.height-pageHeight)/2+104;
		this.fontRenderer.drawString(new TextComponentTranslation("almagest.gui.almagest.magnitude").setStyle(new Style().setBold(true)).getFormattedText(),
				this.width/2+7, thirdStart, defaultColor);
		drawRect(this.width/2+6, thirdStart+9, this.width/2+pageWidth-13, thirdStart+11, 0xffd3c3a2);
		for(int i=0; i<this.expMagnitude.length; i++) {
			this.fontRenderer.drawString(this.expMagnitude[i], this.width/2+7, thirdStart+14+i*10, defaultColor);
		}
		thirdStart = (this.height-pageHeight)/2+190;
		this.fontRenderer.drawString(new TextComponentTranslation("almagest.gui.almagest.mira").setStyle(new Style().setBold(true)).getFormattedText(),
				this.width/2+7, thirdStart, defaultColor);
		drawRect(this.width/2+6, thirdStart+9, this.width/2+pageWidth-13, thirdStart+11, 0xffd3c3a2);
		for(int i=0; i<this.expMira.length; i++) {
			this.fontRenderer.drawString(this.expMira[i], this.width/2+7, thirdStart+14+i*10, defaultColor);
		}
		
		//item rendering.
		this.zLevel = 100.0F;
		this.itemRender.zLevel = 100.0F;
		RenderHelper.disableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
//		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		GlStateManager.pushMatrix();
		GlStateManager.scale(3.0D, 3.0D, 3.0D);
		this.itemRender.renderItemAndEffectIntoGUI(((GameDataBlock)this.currentLink.getData()).getStack(), (int)(((this.width-pageWidth*2)/2+83)/3.0D), (int)(((this.height-pageHeight)/2+42)/3.0D));
		GlStateManager.popMatrix();
//		GlStateManager.enableDepth();
		GlStateManager.enableLighting();
		RenderHelper.enableStandardItemLighting();
		this.itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
		
	}
	
	private void drawForConstellation() {
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(2.0D, 2.0D, 2.0D);
		this.fontRenderer.drawString(new TextComponentTranslation(this.currentLink.getData().getDisplay()).setStyle(new Style().setBold(false)).getFormattedText(),
				(int)(((this.width-pageWidth*2)/2+17)/1.8D), (int)(((this.height-pageHeight)/2+152)/1.8D), defaultColor);
		GlStateManager.popMatrix();
		drawRect((this.width-pageWidth*2)/2+17, (this.height-pageHeight)/2+176+9, this.width/2-5, (this.height-pageHeight)/2+176+11, 0xffd3c3a2);
/*					for(int i=0; i<this.expConstellationLeft.length; i++) {
			this.fontRenderer.drawString(this.expConstellationLeft[i], (this.width-pageWidth*2)/2+17, (this.height-pageHeight)/2+191+14+i*10, defaultColor);
		}*/
		this.mc.getTextureManager().bindTexture(texture);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
//			this.drawTexturedModalRect((this.width-pageWidth*2)/2+55, (this.height-pageHeight)/2+35, 140, 0, 95, 70);
		this.drawTexturedModalRect((this.width-pageWidth*2)/2+40+0, (this.height-pageHeight)/2+31, 82, 0, 50, 130);
		this.drawTexturedModalRect((this.width-pageWidth*2)/2+40+50, (this.height-pageHeight)/2+31, 82, 0, 50, 130);
		this.drawTexturedModalRect((this.width-pageWidth*2)/2+40+100, (this.height-pageHeight)/2+31, 82, 0, 30, 130);
		
		GlStateManager.disableBlend();
		this.pages.get(((GameDataConstellation)this.currentLink.getData()).getConstellation().getId()).renderConstellation(
				(this.width-pageWidth*2)/2+105, (this.height-pageHeight)/2+96);
		
		for(int i=0; i<this.expConstellationLeft.length; i++) {
			this.fontRenderer.drawString(this.expConstellationLeft[i], (this.width-pageWidth*2)/2+17, (this.height-pageHeight)/2+190+i*10, defaultColor);
		}
		int rightStart = (this.height-pageHeight)/2+27;
		for(int i=0; i<this.expConstellationRight.length; i++) {
			this.fontRenderer.drawString(this.expConstellationRight[i], this.width/2+7, rightStart+i*10, defaultColor);
		}
		
	}
	
	private void drawForAbility() {
		
		GameDataAbility2 abilityData = (GameDataAbility2)this.secondTargetData;
		
		//ability name.
		this.drawScaledTranslated(abilityData.getAbility().getName(abilityData.getLevel()), this.left+20, this.top+45, 1.5D, defaultColor, true);
		drawRect(this.left+16, this.top+60, this.centerX-5, this.top+62, 0xFFd3c3a2);
		
		//combination.
		if(abilityData.getAbility().canCombine() && abilityData.getLevel() > 2) {
			String combineLine = "";
			combineLine += new TextComponentTranslation("almagest.ability."+String.format("%02d", abilityData.getAbility().getId())+"."+(abilityData.getLevel()-2)+".name").getFormattedText();
			combineLine += new TextComponentTranslation(" × ").getFormattedText();
			combineLine += new TextComponentTranslation("almagest.ability."+String.format("%02d", abilityData.getAbility().getId())+"."+(abilityData.getLevel()-1)+".name").getFormattedText();
			this.fontRenderer.drawString(combineLine, this.left+17, this.top+69, 0XFF38b48b);
		}
		
		//explanation.
		String[] exp = this.explode(
				this.getTranslated("almagest.ability."+String.format("%02d", abilityData.getAbility().getId())+"."+(abilityData.getLevel())+".exp"), 180, 7);
		for(int i=0; i<exp.length; i++) {
			this.fontRenderer.drawString(exp[i], this.left+17, this.top+85+i*10, defaultColor);
		}
		
		//effectable.
		this.fontRenderer.drawString(
				new TextComponentTranslation("almagest.gui.ability.effectable").getFormattedText(),
				this.left+17, this.top+123, defaultColor);
		drawRect(this.left+16, this.top+133, this.centerX-5, this.top+135, 0xFFd3c3a2);
		
		Effectable[] effectable = StarAbility.Effectable.values();
		for(int i=0; i<effectable.length; i++) {
			this.mc.getTextureManager().bindTexture(texture);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			
			int row = (i>3) ? 1 : 0;
			int col = i%4;
			int offsetX = (i>5) ? 174 : 0;
			int offsetY = (i>5) ? -72 : 0;
			int gray = abilityData.getAbility().hasEffectable(effectable[i]) ? 0 : 41;
			this.drawTexturedModalRect(this.left+17+col*44, this.top+138+row*44, 0+offsetX+gray, 0+i*41+offsetY, 41, 41);
			
			GlStateManager.disableBlend();
			
			int textColor = abilityData.getAbility().hasEffectable(effectable[i]) ? defaultColor : 0XFFbfbfbf;
			String effectableText = new TextComponentTranslation(effectable[i].getName()).setStyle(new Style().setBold(true)).getFormattedText();
			float textX = (float)(this.left+17+col*44+22 - this.fontRenderer.getStringWidth(effectableText)/2);
			this.fontRenderer.drawString(effectableText, textX, this.top+138+row*44+30, textColor, false);
		}
		
	}
	
	private void drawForList() {
		
		GameDataList gameData = (GameDataList)this.secondTargetData;
		
		//display name.
		this.drawScaledTranslatedWithMax(gameData.getDisplay(), this.left+20, this.top+103, 1.5D, defaultColor, true, 180);
		
		//display bars and recipe, component.
		drawRect(this.left+16, this.top+118, this.centerX-5, this.top+120, 0xffd3c3a2);
		drawRect(this.left+104, this.top+120, this.left+106, this.top+223, 0xffd3c3a2);
		this.fontRenderer.drawString(this.getTranslated("almagest.gui.almagest.component", true), this.left+19, this.top+125, defaultColor);
		this.fontRenderer.drawString(this.getTranslated("almagest.gui.almagest.list.ability_label", true), this.left+110, this.top+125, defaultColor);
		
		//keep target.
		InfoSet set  = AttributeHelper.getVanillaInfoSet(gameData.getStack());
		if(set == null) {
			return;
		}
		
		//display component.
		int component = 0;
		for(EStarAttribute each: EStarAttribute.values()) {
			int level = set.getAttribute(each);
			if(level != 0) {
				this.fontRenderer.drawString(this.getTranslated(each.getName())+" : "+level, this.left+21, this.top+143+component*16, defaultColor);
				component++;
			}
		}
		for(int i=0; i<=4; i++) {
			drawRect(this.left+18, this.top+138+i*16, this.left+100, this.top+139+i*16, 0xffd3c3a2);
		}
		
		//display star ability;
		Map<Integer, ArrayList<Integer>> abilities = set.getAbilities();
		if(abilities.size() != 0) {
			int abilityCount = 0;
			for(Integer eachId: abilities.keySet()) {
				for(Integer eachLevel: abilities.get(eachId)) {
					this.fontRenderer.drawString(this.getTranslated(StarAbilityCreator.getAbility(eachId).getName(eachLevel)),
							this.left+21+93, this.top+143+abilityCount*16, defaultColor);
					abilityCount++;
				}
			}
		}
		else {
			this.fontRenderer.drawString(
					this.getTranslated("almagest.gui.almagest.list.no_ability"), this.left+21+93, this.top+143, 0XFF38b48b);
		}
		
		for(int i=0; i<=4; i++) {
			drawRect(this.left+18+93, this.top+138+i*16, this.left+100+93, this.top+139+i*16, 0xffd3c3a2);
		}
		
		//item rendering.
		this.zLevel = 100.0F;
		this.itemRender.zLevel = 100.0F;
		RenderHelper.disableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		GlStateManager.pushMatrix();
		GlStateManager.scale(3.0D, 3.0D, 3.0D);
		this.itemRender.renderItemAndEffectIntoGUI(gameData.getStack(), (int)(((this.width-pageWidth*2)/2+83)/3.0D), (int)(((this.height-pageHeight)/2+42)/3.0D));
		GlStateManager.popMatrix();
		GlStateManager.enableDepth();
		GlStateManager.enableLighting();
		RenderHelper.enableStandardItemLighting();
		this.itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
		
	}
	
	private void drawForMob(float partialTicks) {
		
		GameDataMob mobData = (GameDataMob)this.currentLink.getData();
		
		//left page.
		
		//display name.
		this.drawScaledTranslated(this.currentLink.getData().getDisplay(), this.left+20, this.top+133, 1.5D, defaultColor, true);
		
		//display status and drop.
		drawRect(this.left+16, this.top+148, this.centerX-5, this.top+150, 0xffd3c3a2);
		drawRect(this.left+104, this.top+150, this.left+106, this.top+223, 0xffd3c3a2);
		this.fontRenderer.drawString(this.getTranslated("almagest.gui.almagest.mob.status.label", true), this.left+19, this.top+155, defaultColor);
		this.fontRenderer.drawString(this.getTranslated("almagest.gui.almagest.mob.drop.label", true), this.left+110, this.top+155, defaultColor);
		
		//display status.
		for(int i=0; i<=3; i++) {
			drawRect(this.left+18, this.top+168+i*16, this.left+100, this.top+169+i*16, 0xffd3c3a2);
		}
		this.fontRenderer.drawString(
				this.getTranslated("almagest.gui.almagest.mob.status.heart")+" : "+mobData.getEntity().getHealth(),
				this.left+21, this.top+173+0*16, defaultColor);
		
		IAttributeInstance attackAttribute = mobData.getEntity().getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		this.fontRenderer.drawString(
				this.getTranslated("almagest.gui.almagest.mob.status.attack")+" : "+((attackAttribute==null) ? 0 : attackAttribute.getBaseValue()),
				this.left+21, this.top+173+1*16, defaultColor);
		this.fontRenderer.drawString(
				this.getTranslated("almagest.gui.almagest.mob.status.defence")+" : "+mobData.getEntity().getTotalArmorValue(),
				this.left+21, this.top+173+2*16, defaultColor);
		
		//display drop;
//		drawRect((this.width-pageWidth*2)/2+18, (this.height-pageHeight)/2+218, (this.width-pageWidth*2)/2+100, (this.height-pageHeight)/2+219, 0xffd3c3a2);
		for(int i=0; i<=3; i++) {
			drawRect(this.left+18+93, this.top+168+i*16, this.left+100+93, this.top+169+i*16, 0xffd3c3a2);
		}
		for(int i=0; i<=2; i++) {
			this.fontRenderer.drawString(
					this.getTranslated("almagest.gui.almagest.mob."+mobData.getName().toString()+".drop."+i),
					this.left+21+93, this.top+173+i*16, defaultColor);
		}
		
		
		//right page.
		int thirdStart = (this.height-pageHeight)/2+17;
		this.fontRenderer.drawString(
				new TextComponentTranslation("almagest.gui.almagest.mob.place.label").setStyle(new Style().setBold(true)).getFormattedText(),
				this.width/2+7, thirdStart, defaultColor);
		drawRect(this.width/2+6, thirdStart+9, this.width/2+pageWidth-13, thirdStart+11, 0xffd3c3a2);
		for(int i=0; i<this.expEffect.length; i++) {
			this.fontRenderer.drawString(this.expEffect[i], this.width/2+7, thirdStart+14+i*10, defaultColor);
		}
		
		thirdStart = (this.height-pageHeight)/2+74;
		this.fontRenderer.drawString(
				new TextComponentTranslation("almagest.gui.almagest.mob.exp.label").setStyle(new Style().setBold(true)).getFormattedText(),
				this.width/2+7, thirdStart, defaultColor);
		drawRect(this.width/2+6, thirdStart+9, this.width/2+pageWidth-13, thirdStart+11, 0xffd3c3a2);
		for(int i=0; i<this.expMagnitude.length; i++) {
			this.fontRenderer.drawString(this.expMagnitude[i], this.width/2+7, thirdStart+14+i*10, defaultColor);
		}
		
		thirdStart = (this.height-pageHeight)/2+190;
		this.fontRenderer.drawString(
				new TextComponentTranslation("almagest.gui.almagest.mira").setStyle(new Style().setBold(true)).getFormattedText(),
				this.width/2+7, thirdStart, defaultColor);
		drawRect(this.width/2+6, thirdStart+9, this.width/2+pageWidth-13, thirdStart+11, 0xffd3c3a2);
		for(int i=0; i<this.expMira.length; i++) {
			this.fontRenderer.drawString(this.expMira[i], this.width/2+7, thirdStart+14+i*10, defaultColor);
		}
		
		
		//entity rendering.
		GlStateManager.enableColorMaterial();
		GL11.glPushMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		mobData.getEntity().setWorld(AlmagestCore.proxy.getPlayer().world);
		
		GlStateManager.translate((float)(this.left+100), (float)(this.top+120), 50.0F);
		GlStateManager.scale((float)-60, (float)60, (float)60);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
//		double rotationRedidue = (int)Minecraft.getSystemTime()%(1000*8);
//		GL11.glRotated((rotationRedidue/(1000D*8D)) * 360D, 0, 1, 0);
		GL11.glRotated(-30D, 0, 1, 0);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.renderEntity(mobData.getEntity(), 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true);
		
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

		//GuiInventory.drawEntityOnScreen(this.left + 150, this.top + 120, 60, 0, 0, mobData.getEntity());
		
/*		GL11.glPushMatrix();
		RenderHelper.disableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableLighting();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		GL11.glTranslatef(this.left + 100, this.top + 120, 100);
		GL11.glScalef(-50f, 50f, 50f);
		GL11.glRotatef(180, 0, 0, 1);
		GL11.glRotatef(-30, 0, 1, 0);
		
		mobData.getEntity().setWorld(AlmagestCore.proxy.getPlayer().world);
		this.mc.getRenderManager().getEntityRenderObject(mobData.getEntity()).doRender(mobData.getEntity(), 0, 0, 0, 0, partialTicks);
//		RenderLivingBase<?> renderLiving = (RenderLivingBase<?>)this.mc.getRenderManager().getEntityRenderObject(mobData.getEntity());
//		if(mobData.getEntity() instanceof EntityMira) {
//			renderLiving.bindTexture(RenderEntityMira.texture);
//			renderLiving.getMainModel().render(mobData.getEntity(), 0, 0, 0, 0, 0, partialTicks);
//		}
//		((RenderLivingBase<?>)this.mc.getRenderManager().getEntityRenderObject(mobData.getEntity())).getMainModel()
//			.render(mobData.getEntity(), 0, 0, 0, 0, 0, partialTicks);
		GL11.glPopMatrix();
		
		GlStateManager.enableLighting();
		RenderHelper.enableStandardItemLighting();*/
		
/*		this.zLevel = 100.0F;
//		this.itemRender.zLevel = 100.0F;
		RenderHelper.disableStandardItemLighting();
//		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0, 0);
		GlStateManager.scale(3.0D, 3.0D, 3.0D);
		mobData.getEntity().setWorld(AlmagestCore.proxy.getPlayer().world);
		this.mc.getRenderManager().getEntityRenderObject(mobData.getEntity()).doRender(mobData.getEntity(), 1, 1, 1, 1, 1);
		GlStateManager.popMatrix();
		GlStateManager.enableDepth();
		GlStateManager.enableLighting();
		RenderHelper.enableStandardItemLighting();
//		this.itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;*/
		
	}
	
	private void drawForHelp() {
		
		GameDataHelp gameData = (GameDataHelp)this.secondTargetData;
		if(gameData.hasImg()) {
			GlStateManager.color(1F, 1F, 1F);
			this.mc.getTextureManager().bindTexture(gameData.getImg());
			drawScaledCustomSizeModalRect(this.left+15, this.top+28, 0, 0, 854, 480, 178, 100, 854, 480);
			
			//title.
			this.drawScaledTranslatedWithMax(gameData.getDisplay(), this.left+20, this.top+132, 1.2D, defaultColor, true, 175);
			drawRect(this.left+16, this.top+142, this.centerX-5, this.top+144, 0xFFd3c3a2);
			
			//explanation.
			String[] exp = this.explode(this.getTranslated(gameData.getDisplay()+".exp"), 180, 8);
			for(int i=0; i<exp.length; i++) {
				this.fontRenderer.drawString(exp[i], this.left+17, this.top+146+i*10, defaultColor);
			}
		}
		else {
			
		}
		
	}
	
	private void drawForRecipe() {
		
		ItemStack stack = ((GameDataRecipe)this.secondTargetData).getRecipe().getRecipeOutput();
		//left page.
		
		//display name.
		this.drawScaledTranslated(stack.getDisplayName(), this.left+20, this.top+103, 1.5D, defaultColor, true);
		
		//display bars.
		drawRect(this.left+16, this.top+118, this.centerX-5, this.top+120, 0xffd3c3a2);
		for(int i=0; i<=4; i++) {
			drawRect(this.left+18+43, this.top+138+i*16, this.left+100+43, this.top+139+i*16, 0xffd3c3a2);
		}
		
		//display recipe;
		IRecipe recipe = (((GameDataRecipe)this.secondTargetData).getRecipe());
		if(recipe instanceof StarRecipe && ((StarRecipe)recipe).isSpecial() == false) {
			this.fontRenderer.drawString(
					this.getTranslated("almagest.gui.almagest.recipe", true)+" ("+((StarRecipe)recipe).getMaxStack()+")",
					this.left+60, this.top+125, defaultColor);
			
			StarRecipe starRecipe = (StarRecipe)recipe;
			int recipeCount = 0;
			for(Map.Entry<EStarAttribute, Integer> each: starRecipe.getAttribute().entrySet()) {
				this.fontRenderer.drawString(this.getTranslated(each.getKey().getName())+" : "+each.getValue(),
						this.left+21+43, this.top+143+recipeCount*16, defaultColor);
				recipeCount++;
			}
			for(ItemStack each: starRecipe.getStack()) {
				this.fontRenderer.drawString(this.getTranslated(each.getDisplayName()),
						this.left+21+43, this.top+143+recipeCount*16, defaultColor);
				recipeCount++;
			}
		}
		else {
			this.fontRenderer.drawString(
					this.getTranslated("almagest.gui.almagest.recipe", true)+" ("+recipe.getIngredients().size()+")",
					this.left+60, this.top+125, defaultColor);
			for(int i=0; i<=4; i++) {
				this.fontRenderer.drawString(
					this.getTranslated("almagest.gui.almagest."+this.currentLink.getData().getName().toString()+".recipe."+i),
					this.left+21+43, this.top+143+i*16, defaultColor);
			}
		}
		
		//item rendering.
		this.zLevel = 100.0F;
		this.itemRender.zLevel = 100.0F;
		RenderHelper.disableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
//		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		GlStateManager.pushMatrix();
		GlStateManager.scale(3.0D, 3.0D, 3.0D);
		this.itemRender.renderItemAndEffectIntoGUI(stack, (int)(((this.width-pageWidth*2)/2+83)/3.0D), (int)(((this.height-pageHeight)/2+42)/3.0D));
		GlStateManager.popMatrix();
//		GlStateManager.enableDepth();
		GlStateManager.enableLighting();
		RenderHelper.enableStandardItemLighting();
		this.itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
		
	}
	
	public String getTranslated(String key) {
		
		return this.getTranslated(key, false);
		
	}
	
	public String getTranslated(String key, boolean bold) {
		
		return new TextComponentTranslation(key).setStyle(new Style().setBold(bold)).getFormattedText();
		
	}
	
	public void drawScaledTranslated(String key, int x, int y, double scale, int color, boolean bold) {
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale, scale, scale);
		this.fontRenderer.drawString(this.getTranslated(key, bold), (int)(x/scale), (int)(y/scale), color);
		GlStateManager.popMatrix();
		
	}
	
	public void drawScaledTranslatedWithMax(String key, int x, int y, double scale, int color, boolean bold, int max) {
		
		String text = this.getTranslated(key, bold);
		
		GlStateManager.pushMatrix();
		if(max <= 0) {
			GlStateManager.scale(scale, scale, scale);
			this.fontRenderer.drawString(text, (int)(x/scale), (int)(y/scale), color);
		}
		else {
			boolean drawn = false;
			double newScale = scale;
			for(int i=0; i<10; i++) {
				newScale = scale * (1.0D - i*0.05D);
				int strWidth = MathHelper.ceil((double)this.fontRenderer.getStringWidth(text) * newScale);
				if(strWidth <= max) {
					GlStateManager.scale(newScale, newScale, newScale);
					this.fontRenderer.drawString(text, (int)(x/newScale), (int)(y/newScale), color);
					drawn = true;
					break;
				}
			}
			if(!drawn) {
				newScale = scale*0.5D;
				GlStateManager.scale(newScale, newScale, newScale);
				this.fontRenderer.drawString(text, (int)(x/newScale), (int)(y/newScale), color);
			}
		}
		GlStateManager.popMatrix();
		
	}
	
	private void setThirdExplanation() {
		
		switch(this.currentLink.getCategory()) {
			case BLOCK:
			case ITEM:
				this.expEffect = this.explode(
						this.getTranslated("almagest.gui.almagest."+this.currentLink.getData().getName().toString()+".effect"), 180, 7);
				this.expMagnitude = this.explode(
						this.getTranslated("almagest.gui.almagest."+this.currentLink.getData().getName().toString()+".magnitude"), 180, 7);
				this.expMira = this.explode(
						this.getTranslated("almagest.gui.almagest."+this.currentLink.getData().getName().toString()+".mira"), 180, 2);
				break;
			case CONSTELLATION:
				this.expConstellationLeft
					= this.explode(new TextComponentTranslation("almagest.gui.constellation."+String.format("%02d",((GameDataConstellation)this.currentLink.getData()).getConstellation().getId())+".explanation.left").getFormattedText(), 180, 2);
				this.expConstellationRight
				= this.explode(new TextComponentTranslation("almagest.gui.constellation."+String.format("%02d",((GameDataConstellation)this.currentLink.getData()).getConstellation().getId())+".explanation.right").getFormattedText(), 180, 19);
				break;
			case MOB:
				this.expEffect = this.explode(
						this.getTranslated("almagest.gui.almagest.mob."+this.currentLink.getData().getName().toString()+".place"), 180, 3);
				this.expMagnitude = this.explode(
						this.getTranslated("almagest.gui.almagest.mob."+this.currentLink.getData().getName().toString()+".exp"), 180, 10);
				this.expMira = this.explode(
						this.getTranslated("almagest.gui.almagest.mob."+this.currentLink.getData().getName().toString()+".mira"), 180, 2);
				break;
			case LIST:
			case RECIPE:
			case ABILITY:
			case MEMO:
			case HELP:
				break;
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
	
	private class PageContent {
		
		public int constNumber;
		@SuppressWarnings("unused")
		public int flag;
		public ArrayList<StarData> stars;
		public ArrayList<OtherStarData> otherStars;
		public List<LineData> lines;
		
		private static final double size = 90;
		private static final double maxWidth = 100;
//		private static final double maxStarWidth = 150;
//		private static final float scale = 0.00390625F;
		private double weight;
/*		private double adjustLong;
		private double adjustLat;
		private double relativeAddLong;
		private double relativeAddLat;*/
		private double centerLong;
		private double centerLat;
		private Map<Integer, Vector3d> calcStar = new HashMap<Integer, Vector3d>();
		
		public PageContent(int constNumber, int flag) {
			
			this.constNumber = constNumber;
			this.flag = flag;
			this.stars = HelperConstellation.getStars(this.constNumber);
			this.lines = HelperConstellation.getLines(this.constNumber);
			this.otherStars = HelperConstellation.getOtherStars(this.constNumber);
			
			//calculate the center of the constellation.
			double minLong = Integer.MAX_VALUE;	//赤経
			double minLat = Integer.MAX_VALUE;	//赤緯
			double maxLong = Integer.MIN_VALUE;
			double maxLat = Integer.MIN_VALUE;
			for(StarData each: stars) {
				minLong = Math.min(minLong, each.getCalculatedLong());
				minLat = Math.min(minLat, each.getCalculatedLat());
				maxLong = Math.max(maxLong, each.getCalculatedLong());
				maxLat = Math.max(maxLat, each.getCalculatedLat());
			}
			this.centerLong = (minLong+maxLong) / 2.0D;
			if(Math.abs(minLong-maxLong) > 180) {
				double otherDist = (360.0D - Math.abs(minLong-maxLong)) / 2.0D;
				if((minLong-otherDist) > 0) {
					this.centerLong = minLong - otherDist;
				}
				else {
					this.centerLong = maxLong + otherDist;
				}
			}
			this.centerLat = (minLat+maxLat) / 2.0D;
			
//			this.centerLong = Constellation.getConstFromNumber(this.constNumber).getRa();
//			this.centerLat = Constellation.getConstFromNumber(this.constNumber).getDec();
			
			double minY = Integer.MAX_VALUE;
			double minX = Integer.MAX_VALUE;
			double maxY = Integer.MIN_VALUE;
			double maxX = Integer.MIN_VALUE;
			//transform sphere coordination into plane coordination.
			for(StarData each: this.stars) {
				Vector3d relative = this.calcRotate(this.createVector(size, 0, 0), each.getCalculatedLong(), -1.0D*each.getCalculatedLat());
				relative = this.moveToCenter(relative, -1.0D*this.centerLong, this.centerLat);
//				relative = this.calcRotate(relative, -1.0D*this.centerLong, this.centerLat);
				this.calcStar.put(each.hip, relative);
				minY = Math.min(minY, relative.z);
				minX = Math.min(minX, relative.y);
				maxY = Math.max(maxY, relative.z);
				maxX = Math.max(maxX, relative.y);
			}
			for(OtherStarData each: this.otherStars) {
				Vector3d relative = this.calcRotate(this.createVector(size, 0, 0), each.ra, -1.0D*each.dec);
				relative = this.moveToCenter(relative, -1.0D*this.centerLong, this.centerLat);
				this.calcStar.put(each.hip, relative);
			}
			this.weight = maxWidth / Math.max(Math.abs(minX-maxX), Math.abs(minY-maxY));
			
/*			double defLong = Math.abs(maxLong - minLong);
			double defLat = Math.abs(maxLat - minLat);
			this.weight = size / Math.max(defLong, defLat);
			this.adjustLong = minLong * -1;
			this.adjustLat = minLat * -1;
			this.relativeAddLong = (size-defLong*this.weight)/2;
			this.relativeAddLat = (size-defLat*this.weight)/2;*/
			
			
		}
		
		public void renderConstellation(double startX, double startY) {
			
/*			for(OtherStarData each: this.otherStars) {
				Vector3d relative = this.calcStar.get(each.hip);
				double scale = 0.4 - Math.min(each.magnitude*0.05, 0.25);
				double adjust = 16*scale/2;
				if(Math.abs(relative.z*this.weight) > maxStarWidth/2 || Math.abs(relative.y*this.weight) > maxStarWidth/2) {
					continue;
				}
				this.drawRect(startX-relative.y*this.weight-adjust, startY-relative.z*this.weight-adjust,
						140, 81+(each.spectrum.getMetadata()-1)*16, 16, 16, scale);
			}*/
			
			for(LineData each: this.lines) {
/*				double relativeX1 = startX - ((each.star1.getCalculatedLong() + this.adjustLong) * this.weight + this.relativeAddLong);
				double relativeY1 = startY - ((each.star1.getCalculatedLat() + this.adjustLat) * this.weight + this.relativeAddLat);
				double relativeX2 = startX - ((each.star2.getCalculatedLong() + this.adjustLong) * this.weight + this.relativeAddLong);
				double relativeY2 = startY - ((each.star2.getCalculatedLat() + this.adjustLat) * this.weight + this.relativeAddLat);*/
				Vector3d relative1 = this.calcStar.get(each.star1.hip);
				Vector3d relative2 = this.calcStar.get(each.star2.hip);
				
				this.drawLine(startX-relative1.y*this.weight, startY-relative1.z*this.weight,
						startX-relative2.y*this.weight, startY-relative2.z*this.weight);
			}
			
			for(StarData each: this.stars) {
/*				double relativeX = startX - ((each.getCalculatedLong() + this.adjustLong) * this.weight + this.relativeAddLong);
				double relativeY = startY - ((each.getCalculatedLat() + this.adjustLat) * this.weight + this.relativeAddLat);*/
				Vector3d relative = this.calcStar.get(each.hip);

				double scale = 0.4 - Math.min(each.magnitude*0.05, 0.25);
				double adjust = 16*scale/2;
				this.drawRect(startX-relative.y*this.weight-adjust, startY-relative.z*this.weight-adjust,
						140, 81+(each.spectrum.getMetadata()-1)*16, 16, 16, scale);
			}
			
		}
		
		private void drawRect(double x, double y, int textureX, int textureY, int width, int height, double scale) {
			
			float f = 0.00390625F;
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder renderer = tessellator.getBuffer();
			
			renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			renderer.pos((double)(x+0*scale), (double)(y+height*scale), (double)(GuiAlmagest.this.zLevel))
				.tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f)).endVertex();
			renderer.pos((double)(x+width*scale), (double)(y+height*scale), (double)(GuiAlmagest.this.zLevel))
				.tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f)).endVertex();
			renderer.pos((double)(x+width*scale), (double)(y+0*scale), (double)(GuiAlmagest.this.zLevel))
				.tex((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f)).endVertex();
			renderer.pos((double)(x+0*scale), (double)(y+0*scale), (double)(GuiAlmagest.this.zLevel))
				.tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f)).endVertex();
			tessellator.draw();
			
		}
		
		private void drawLine(double relativeX1, double relativeY1, double relativeX2, double relativeY2) {
		
			BufferBuilder renderer = Tessellator.getInstance().getBuffer();
			GlStateManager.enableBlend();
			GlStateManager.disableTexture2D();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.color(1F, 1F, 1F, 1F);
			
			renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
			GL11.glLineWidth(2);
			renderer.pos(relativeX1, relativeY1, GuiAlmagest.this.zLevel).endVertex();
			renderer.pos(relativeX2, relativeY2, GuiAlmagest.this.zLevel).endVertex();
			Tessellator.getInstance().draw();
			
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
			
		}
		
		private Vector3d calcRotate(Vector3d vector, double g, double b) {
			
			double sinB = Math.sin(Math.toRadians(b));
			double sinG = Math.sin(Math.toRadians(g));
			double cosB = Math.cos(Math.toRadians(b));
			double cosG = Math.cos(Math.toRadians(g));

			double newX = vector.x*cosB*cosG + vector.z*sinB*cosG - vector.y*sinG;
			double newY = vector.x*sinG*cosB + vector.z*sinG*sinB + vector.y*cosG;
			double newZ = -1*vector.x*sinB + vector.z*cosB;
			
			return this.createVector(newX, newY, newZ);
			
		}
		
		private Vector3d createVector(double x, double y, double z) {
			
			Vector3d newVector = new Vector3d();
			newVector.x = x;
			newVector.y = y;
			newVector.z = z;
			
			return newVector;
			
		}
		
		private Vector3d moveToCenter(Vector3d vector, double g, double b) {
			
			double sinB = Math.sin(Math.toRadians(b));
			double sinG = Math.sin(Math.toRadians(g));
			double cosB = Math.cos(Math.toRadians(b));
			double cosG = Math.cos(Math.toRadians(g));

			double newX = vector.x*cosB*cosG - vector.y*cosB*sinG + vector.z*sinB;
			double newY = vector.x*sinG + vector.y*cosG;
			double newZ = -1*vector.x*sinB*cosG + vector.y*sinB*sinG + vector.z*cosB;
			
			return this.createVector(newX, newY, newZ);
			
		}
		
	}
	
	private class AlmagestButton extends GuiButton {

		public AlmagestButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
			super(buttonId, x, y, widthIn, heightIn, buttonText);
		}
		
		public AlmagestButton enable() {
			this.enabled = true;
			return this;
		}
		
		public AlmagestButton disable() {
			this.enabled = false;
			return this;
		}
		
		public AlmagestButton visible() {
			this.visible = true;
			return this;
		}
		
		public AlmagestButton invisible() {
			this.visible = false;
			return this;
		}
		
	}
	
	private class PagingButton extends AlmagestButton {
		
		private final boolean isLeft;
		
		public PagingButton(int buttonID, int x, int y, boolean isLeft) {
			super(buttonID, x, y, 12, 9, "");
			this.isLeft = isLeft;
		}
		
		//drawButton.
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTick) {
			if(this.visible) {
				boolean mouseOver = false;
				if(mouseX >= this.x && mouseY >= this.y
						&& mouseX < this.x + this.width && mouseY < this.y + this.height) {
					mouseOver = true;
				}
				
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(GuiAlmagest.textureLeft);
				int x = 214;
				int y = 237;
				
				if(mouseOver) {
					y += 10;
				}
				if(!isLeft) {
					x += 16;
				}
				
				this.drawTexturedModalRect(this.x, this.y, x, y, this.width, this.height);
				GlStateManager.disableBlend();
			}
		}
		
	}
	
	private class UpButton extends AlmagestButton {
		
		public UpButton(int buttonID, int x, int y) {
			super(buttonID, x, y, 9, 10, "");
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTick) {
			if(this.visible) {
				boolean mouseOver = false;
				if(mouseX >= this.x && mouseY >= this.y
						&& mouseX < this.x + this.width && mouseY < this.y + this.height) {
					mouseOver = true;
				}
				
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(GuiAlmagest.textureLeft);
				int x = 247;
				int y = 236;
				if(mouseOver) {
					y += 10;
				}
				this.drawTexturedModalRect(this.x, this.y, x, y, this.width, this.height);
				GlStateManager.disableBlend();
			}
		}
		
	}
	
	private class CategoryButton extends AlmagestButton {
		
		private DataCategory category;
		
		public CategoryButton(int buttonId, int x, int y, DataCategory category) {
			super(buttonId, x, y, 185, 18, "");
			this.category = category;
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTick) {
			if(this.visible) {
				boolean mouseOver = false;
				if(mouseX >= this.x && mouseY >= this.y
						&& mouseX < this.x + this.width && mouseY < this.y + this.height) {
					mouseOver = true;
				}
				
				if(mouseOver) {
					GlStateManager.enableBlend();
					GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
					GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					drawRect(this.x, this.y, this.x+this.width, this.y+this.height, 0x30ffa500);
					GlStateManager.disableBlend();
				}
				mc.fontRenderer.drawString(I18n.format(this.category.getDisplay()), this.x+10, this.y+6, defaultColor, false);
			}
		}
		
		public DataCategory getCategory() {
			return this.category;
		}
		
	}
	
	private class EachButton extends AlmagestButton {
		
		private GameData data;
		private boolean isUsable;
		
		public EachButton(int buttonId, int x, int y) {
			super(buttonId, x, y, 185, 18, "");
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTick) {
			if(this.visible) {
				boolean mouseOver = false;
				if(mouseX >= this.x && mouseY >= this.y
						&& mouseX < this.x + this.width && mouseY < this.y + this.height) {
					mouseOver = true;
				}
				
				if(mouseOver) {
					GlStateManager.enableBlend();
					GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
					GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					drawRect(this.x, this.y, this.x+this.width, this.y+this.height, 0x30ffa500);
					GlStateManager.disableBlend();
				}
				if(!this.isUsable) {
					mc.fontRenderer.drawString("?????", this.x+10, this.y+6, defaultColor);
				}
				else {
					String buttonText = "";
					if(this.data instanceof GameDataConstellation &&
							AlmagestCore.savedDataManager.getFlagData().isCostellationPresented(
									((GameDataConstellation)this.data).getConstellation())) {
						buttonText = I18n.format(this.data.getDisplay())
								+ new TextComponentTranslation("almagest.gui.tooltip.presented").getFormattedText();
					}
					else {
						buttonText = I18n.format(this.data.getDisplay());
					}
					mc.fontRenderer.drawString(buttonText, this.x+10, this.y+6, defaultColor);
				}
			}
		}
		
		public AlmagestButton setData(GameData data) {
			this.data = data;
			if((this.data == null || this.data.isObtained() == false) && AlmagestCore.proxy.getPlayer().isCreative() == false) {
				this.isUsable = false;
			}
			else {
				this.isUsable = true;
			}
			return this;
		}
		
		public GameData getData() {
			return this.data;
		}
		
		@Override
		public AlmagestButton enable() {
			this.enabled = this.isUsable;
			return this;
		}
		
	}
	
	private class StringButton extends AlmagestButton {
		
		public StringButton(int buttonId, int x, int y, String display) {
			super(buttonId, x, y, 180, 20, display);
			this.setDisplay(display);
			this.setx(x);
		}
		
		public StringButton setDisplay(String display) {
			this.displayString = display;
			int stringWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(I18n.format(this.displayString));
			this.width = stringWidth;
			return this;
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTick) {
			if(this.visible) {
				boolean mouseOver = false;
				if(mouseX >= this.x && mouseY >= this.y
						&& mouseX < this.x + this.width && mouseY < this.y + this.height) {
					mouseOver = true;
				}
/*				if(mouseOver) {
					drawRect(this.x, this.y, this.x+this.width, this.y+this.height, 0xffa50050);
				}*/
				mc.fontRenderer.drawString(
						new TextComponentTranslation(this.displayString).setStyle(new Style().setUnderlined(this.enabled && !mouseOver)).getFormattedText(),
						this.x, this.y, defaultColor);
				if(this.enabled) {
					mc.fontRenderer.drawString(">", this.x+this.width+3, this.y, defaultColor);
					
				}
			}
		}
		
		public StringButton setx(int x) {
//			this.x = x-this.width;
			this.x = x;
			return this;
		}
		
	}
	
	private class PageLink {
		
		private int floor;
		private DataCategory category;
		private GameData data;
		private int pageNum;
		
		public void setToFirst() {
			this.floor = 1;
			this.category = null;
			this.data = null;
			this.pageNum = 1;
		}
		
		public void setToSecond(DataCategory category) {
			this.floor = 2;
			this.category = category;
			this.data = null;
			this.pageNum = 1;
		}
		
		public void setToThird(GameData data) {
			this.floor = 3;
			this.data = data;
//			this.pageNum = 1;
		}
		
		public int getFloor() {
			return this.floor;
		}
		
		public int getPageNum() {
			return this.pageNum;
		}
		
		public void nextPage() {
			this.pageNum++;
		}
		
		public void prevPage() {
			if(this.pageNum != 1) {
				this.pageNum--;
			}
		}
		
		public void setPageNum(int pageNum) {
			this.pageNum = pageNum;
		}
		
		public DataCategory getCategory() {
			return this.category;
		}
		
		public GameData getData() {
			return this.data;
		}
		
		public PageLink clone() {
			
			PageLink newLink = new PageLink();
			switch(this.floor) {
			case 1:
				newLink.setToFirst();
				break;
			case 2:
				newLink.setToSecond(this.getCategory());
				newLink.setPageNum(this.pageNum);
				break;
			case 3:
				newLink.setToSecond(this.getCategory());
				newLink.setPageNum(this.pageNum);
				newLink.setToThird(this.getData());
				break;
			}
			
			return newLink;
			
		}
		
	}
	
}
