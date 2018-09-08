package noki.almagest.gui;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noki.almagest.ModInfo;
import noki.almagest.ability.StarAbilityCreator;
import noki.almagest.ability.StarAbilityCreator.StarAbility;
import noki.almagest.ability.StarPropertyCreator;
import noki.almagest.ability.StarPropertyCreator.ItemStarLine;


/**********
 * @class GuiContainerBookrest
 *
 * @description
 */
public class GuiContainerBookrest_original extends GuiContainer {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final ResourceLocation texture = new ResourceLocation(ModInfo.ID.toLowerCase(), "textures/gui/bookrest.png");
	
	private boolean[] lineFlags = new boolean[12];
	private int memory;
	private ArrayList<StarAbility> abilities = new ArrayList<StarAbility>();
	
	
	//******************************//
	// define member methods.
	//******************************//
	public GuiContainerBookrest_original(EntityPlayer player, World world, int x, int y, int z) {
		
		super(new ContainerBookrest_original(player, world, x, y, z));
		this.xSize = 256;
		this.ySize = 210;
		
	}
	
	
	/*GUIの文字等の描画処理*/
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
		
		Arrays.fill(this.lineFlags, false);
		this.memory = 0;
		boolean magnitudeFlag = false;
		this.abilities.clear();
		
		ItemStack stack;
		int maxSlotId = 8;
		
		//wow! hardcode!
		for(int i=0; i<=maxSlotId; i++) {
			stack = this.inventorySlots.getSlot(i).getStack();
			
			if(stack != null) {
				if(stack.isEmpty() == false) {
					magnitudeFlag = true;
				}
				
				this.memory = this.memory + StarPropertyCreator.getMemory(stack);
				
				this.abilities.addAll(StarAbilityCreator.getAbilities(stack));
				
				ArrayList<ItemStarLine> lines = StarPropertyCreator.getLines(stack);
				for(ItemStarLine each: lines) {
					switch(each) {
						case TOP:
							if(3<=i && i<=5) {
								lineFlags[i-1] = true;
							}
							else if(6<=i && i<=8) {
								lineFlags[i+1] = true;
							}
							break;
						case BOTTOM:
							if(0<=i && i<=2) {
								lineFlags[i+2] = true;
							}
							else if(3<=i && i<=5) {
								lineFlags[i+4] = true;
							}
							break;
						case LEFT:
							if(i== 1 || i==2) {
								lineFlags[i-1] = true;
							}
							else if(i== 4 || i==5) {
								lineFlags[i+1] = true;
							}
							else if(i== 7 || i==8) {
								lineFlags[i+3] = true;
							}
							break;
						case RIGHT:
							if(i== 0 || i==1) {
								lineFlags[i] = true;
							}
							else if(i== 3 || i==4) {
								lineFlags[i+2] = true;
							}
							else if(i== 6 || i==7) {
								lineFlags[i+4] = true;
							}
							break;
						default:
							break;
					}
				}
			}
		}
		
		this.mc.renderEngine.bindTexture(texture);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		for(int i=0; i<12; i++) {
			if(lineFlags[i]) {
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
		}
		
		if(magnitudeFlag == false) {
			//nothing to do.
		}
		else if(this.memory < 100) {
			this.drawTexturedModalRect(205, 70, 88+45, 236, 9, 20);
		}
		else if(this.memory < 200) {
			this.drawTexturedModalRect(205, 70, 88+36, 236, 9, 20);
		}
		else if(this.memory < 300) {
			this.drawTexturedModalRect(205, 70, 88+27, 236, 9, 20);
		}
		else if(this.memory < 400) {
			this.drawTexturedModalRect(205, 70, 88+18, 236, 9, 20);
		}
		else if(this.memory < 500) {
			this.drawTexturedModalRect(205, 70, 88+9, 236, 9, 20);
		}
		else {
			this.drawTexturedModalRect(205, 70, 88, 236, 9, 20);
		}
		
		
		for(int i=0; i<this.abilities.size(); i++) {
			this.drawString(this.fontRenderer, this.abilities.get(i).getLocalizedName(), 95, 23+i*14, 0xffffff);
		}
		
		
		GlStateManager.disableBlend();
		
/*		ItemStack stack1 = this.inventorySlots.getSlot(0).getStack();
		if(stack1 != null) {
			this.drawString(this.fontRenderer, Integer.toString(StarPropertyCreator.getMemory(stack1)), 0, 0, 0xffffff);
		}*/
		
	}
	
	/*GUIの背景の描画処理*/
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
		
		this.mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
	}
	
	/*GUIが開いている時にゲームの処理を止めるかどうか。*/
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
		
	}
	
}
