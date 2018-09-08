package noki.almagest.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noki.almagest.gui.sequence.ContainerSequence;
import noki.almagest.gui.sequence.SequenceInventory;
import noki.almagest.gui.sequence.SequenceTalk;

public class ContainerSample extends ContainerSequence {
	
	public ContainerSample(EntityPlayer player, BlockPos pos, World world) {
		
		super(player, pos, world);
		
	}

	@Override
	public void defineSequences() {
		
		this.setSequence(1, new SequenceTalk("almagest.gui.sample2.1", 1));
		this.setSequence(2, new SequenceInventory("almagest.gui.sample2.2", true) {
			@Override
			public int checkInventory(Slot slot) {
				
				if(slot != null && slot.getHasStack() && slot.getStack().getItem() == Items.DIAMOND) {
					return 1;
				}
				return 2;
				
			}
		});
		this.setSequence(3, new SequenceTalk("almagest.gui.sample2.3", 1).setEnd());
		this.setSequence(4, new SequenceTalk("almagest.gui.sample2.4", 1).setEnd());
		
		this.connect(1, 2);
		this.connect(2, 1, 3);
		this.connect(2, 2, 4);
		
	}

}
