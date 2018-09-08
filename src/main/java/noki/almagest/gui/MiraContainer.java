package noki.almagest.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noki.almagest.AlmagestCore;
import noki.almagest.entity.EntityMira;
import noki.almagest.gui.sequence.ContainerSequence;
import noki.almagest.gui.sequence.SequenceChoice;
import noki.almagest.gui.sequence.SequenceInventory;
import noki.almagest.gui.sequence.SequenceTalk;
import noki.almagest.helper.HelperReward;
import noki.almagest.helper.HelperConstellation.Constellation;
import noki.almagest.item.ItemBlockConstellation;
import noki.almagest.registry.ModBlocks;
import noki.almagest.registry.ModItems;
import noki.almagest.saveddata.AlmagestDataFlag;
import noki.almagest.saveddata.DataCategory;
import noki.almagest.saveddata.gamedata.EHelpData;


/**********
 * @class MiraContainer
 *
 * @description
 * 
 */
public class MiraContainer extends ContainerSequence {
	
	//******************************//
	// define member variables.
	//******************************//
	private EntityMira talkingMira;
	private Constellation presentedConst;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public MiraContainer(EntityPlayer player, BlockPos pos, World world) {
		
		super(player, pos, world);
		
	}
	
	@Override
	public void defineSequences() {
		
		int mainStoryFlag = AlmagestCore.savedDataManager.getStoryData().getCurrentStory();
		
		switch(mainStoryFlag) {
			case 0:
				this.setSequence(1, new SequenceTalk("almagest.gui.mira.talk001.1", 1).setEnd());
				this.markFlag(1);
				this.markFlag(1000);
				
				//temporally reveal
				AlmagestDataFlag flag = AlmagestCore.savedDataManager.getFlagData();
				flag.setObtained(DataCategory.BLOCK, new ItemStack(ModBlocks.BOOKREST).getUnlocalizedName());
				flag.setObtained(DataCategory.BLOCK, new ItemStack(ModBlocks.STAR_COMPASS).getUnlocalizedName());
				flag.setObtained(DataCategory.ITEM, new ItemStack(ModItems.ALMAGEST).getUnlocalizedName());
				flag.setObtained(DataCategory.ITEM, new ItemStack(ModItems.COCKTAIL,1,0).getUnlocalizedName());
				flag.setObtained(DataCategory.ITEM, new ItemStack(ModItems.COCKTAIL,1,1).getUnlocalizedName());
				flag.setObtained(DataCategory.ITEM, new ItemStack(ModItems.COCKTAIL,1,2).getUnlocalizedName());
				flag.setObtained(DataCategory.ITEM, new ItemStack(ModItems.COCKTAIL_RAINBOW).getUnlocalizedName());
				flag.setObtained(DataCategory.ITEM, new ItemStack(ModItems.OIL).getUnlocalizedName());
				flag.setObtained(DataCategory.ITEM, new ItemStack(ModItems.PLANISPHERE).getUnlocalizedName());
				flag.setObtained(DataCategory.ITEM, new ItemStack(ModItems.POLISH).getUnlocalizedName());
				flag.setObtained(DataCategory.ITEM, new ItemStack(ModItems.PURE_WATER).getUnlocalizedName());
				flag.setObtained(DataCategory.ITEM, new ItemStack(ModItems.STARDUST).getUnlocalizedName());
				flag.setObtained(DataCategory.RECIPE, "almagest:recipe.additional.000");
				flag.setObtained(DataCategory.RECIPE, "almagest:recipe.additional.001");
				flag.setObtained(DataCategory.RECIPE, "almagest:recipe.additional.002");
				for(EHelpData each: EHelpData.values()) {
					flag.setObtained(DataCategory.HELP, each.getDisplay());
				}
				break;
			case 1:
				this.setSequence(1, new SequenceTalk("almagest.gui.mira.talk002.1", 1).setEnd());
				this.markFlag(2);
				break;
			case 2:
				this.setSequence(1, new SequenceTalk("almagest.gui.mira.talk003.1", 3));
				this.setSequence(2, new SequenceChoice("almagest.gui.mira.talk003.2", 2));
				this.setSequence(3, new SequenceChoice("almagest.gui.mira.talk003.3", 2));
				this.setSequence(4, new SequenceTalk("almagest.gui.mira.talk003.4", 4));
				this.setSequence(5, new SequenceChoice("almagest.gui.mira.talk003.5", 2));
				this.setSequence(6, new SequenceChoice("almagest.gui.mira.talk003.6", 2));
				this.setSequence(7, new SequenceTalk("almagest.gui.mira.talk003.7", 2).setEnd());
				
				this.connect(1, 2);
				this.connect(2, 1, 4);
				this.connect(2, 2, 3);
				this.connect(3, 1, 4);
				this.connect(3, 2, 3);
				this.connect(4, 5);
				this.connect(5, 1, 7);
				this.connect(5, 2, 6);
				this.connect(6, 1, 7);
				this.connect(6, 2, 6);
				
				this.markFlag(3);
				break;
			default:
				//choice from three.
				this.setSequence(1, new SequenceChoice("almagest.gui.mira.talk004.1", 3));
				
				//give almagest.
				this.setSequence(2, new SequenceInventory("almagest.gui.mira.talk004.2", false) {
					
					@Override
					public void onSlotCreated(ContainerSequence container) {
						container.inventory.setInventorySlotContents(0, new ItemStack(ModItems.ALMAGEST));
					}
					
					@Override
					public void onCraftmatrixChange(ContainerSequence container) {
						if(container.inventory.getStackInSlot(0).isEmpty()) {
							container.inventory.setInventorySlotContents(0, new ItemStack(ModItems.ALMAGEST));
						}
					}
					
				}.setEnd());
				
				//receive constellations.
				this.setSequence(3, new SequenceInventory("almagest.gui.mira.talk004.3", true) {
					
					private Constellation presentedConst;
					
					@Override
					public int checkInventory(Slot slot) {
						ItemStack stack = slot.getStack();
						if(!stack.isEmpty() && stack.getItem().equals(Item.getItemFromBlock(ModBlocks.CONSTELLATION_BLOCK))) {
							AlmagestDataFlag dataFlag = AlmagestCore.savedDataManager.getFlagData();
							int constNumber = ItemBlockConstellation.getConstNumber(stack);
							if(constNumber != 0) {
								Constellation constellation = Constellation.getConstFromNumber(constNumber);
								if(!dataFlag.isCostellationPresented(constellation)) {
									if(!MiraContainer.this.world.isRemote) {
										dataFlag.setConstellationPresented(constellation);
									}
									this.presentedConst = constellation;
									return 1;
								}
								else {
									return 2;
								}
							}
						}
						return 0;
					}
					
					@Override
					public void onNext(ContainerSequence container) {
						if(this.presentedConst != null) {
							((MiraContainer)container).setPresentedConst(this.presentedConst);
						}
					}
					
					@Override
					public boolean isReturnStack() {
						return true;
					}
					
				});
				
				//not constellations.
				this.setSequence(4, new SequenceTalk("almagest.gui.mira.talk004.4", 1).setEnd());
				//already presented constellations.
				this.setSequence(8, new SequenceTalk("almagest.gui.mira.talk004.8", 1).setEnd());
				
				//give reword.
				this.setSequence(5, new SequenceTalk("almagest.gui.mira.talk004.5", 1));
				this.setSequence(6, new SequenceInventory("almagest.gui.mira.talk004.6", false){
					
					@Override
					public void onSlotCreated(ContainerSequence container) {
						AlmagestCore.log("on reward set.");
						container.inventory.setInventorySlotContents(0, HelperReward.getCurrentReward());
					}
					
					@Override
					public void onEnd(ContainerSequence container) {
						container.clearContainer();
					}
					
					@Override
					public boolean isReturnStack() {
						return true;
					}
					
				}.setEnd());
				
				int randomTalk = this.world.rand.nextInt(4) + 1;
				//random talk.
				this.setSequence(7, new SequenceTalk("almagest.gui.mira.random"+randomTalk+".1", 1).setEnd());
				
				this.connect(1, 2, 2);
				this.connect(1, 3, 7);
				this.connect(1, 1, 3);
				this.connect(3, 0, 4);
				this.connect(3, 1, 5);
				this.connect(3, 2, 8);
				this.connect(5, 6);
				
				break;
		}
		
	}
	
	public void setPresentedConst(Constellation presentedConst) {
		
		this.presentedConst = presentedConst;
		
	}
	
	public Constellation getPresentedConst() {
		
		return presentedConst;
		
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		
		super.onContainerClosed(player);
		
		if(this.talkingMira != null) {
			this.talkingMira.setTalking(false);
			this.talkingMira.setTalker(null);
		}
		
	}
	
	public void setMira(EntityMira mira) {
		
		this.talkingMira = mira;
		
	}

}
