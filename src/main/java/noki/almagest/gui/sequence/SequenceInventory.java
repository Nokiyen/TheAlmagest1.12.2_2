package noki.almagest.gui.sequence;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.text.TextComponentTranslation;


/**********
 * @class SequenceInventory
 *
 * @description
 */
public class SequenceInventory implements ISequence {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private String name;
	private boolean give;
	private boolean end;
	private boolean closeable = false;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public SequenceInventory(String name, boolean give) {
		
		this.name = name;
		this.give = give;
		
	}
	
	@Override
	public ESequenceType getType() {
		
		return ESequenceType.Inventory;
		
	}
	
	public boolean isGiving() {
		
		return this.give;
		
	}
	
	@Override
	public String getMessage() {
		
		return new TextComponentTranslation(this.name+".message").getFormattedText();
		
	}
	
	@Override
	public ISequence setEnd() {
		
		this.end = true;
		this.closeable = true;
		return this;
		
	}
	
	@Override
	public boolean isEnd() {
		
		return this.end;
		
	}
	
	//used for checking the contents of inventory.
	//depend on the return value, next sequence will be selected.
	public int checkInventory(Slot slot) {
		
		return 0;
		
	}
	
	public Slot createSlot(IInventory inventory) {
		
		return new Slot(inventory, 0, 120, 17+5);
		
	}
	
	public void onCraftmatrixChange(ContainerSequence container) {
		
	}
	
	public void onSlotCreated(ContainerSequence container) {
		
	}
	
	@Override
	public void onEnd(ContainerSequence container) {
		
	}
	
	@Override
	public void onNext(ContainerSequence container) {
		
	}
	
	@Override
	public boolean closeable() {
		
		return this.closeable;
		
	}
	
	public ISequence setCloseable(boolean flag) {
		
		this.closeable = flag;
		return this;
		
	}
	
	public boolean isReturnStack() {
		
		return false;
		
	}

}
