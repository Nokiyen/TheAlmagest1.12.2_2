package noki.almagest.gui.sequence;

import net.minecraft.util.text.TextComponentTranslation;


/**********
 * @class SequenceTalk
 *
 * @description
 */
public class SequenceTalk implements ISequence {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private String name;
	private int step;
	private int currentStep;
	private boolean end;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public SequenceTalk(String name, int step) {
		
		this.name = name;
		this.step = step;
		this.currentStep = 1;
		
	}
	
	@Override
	public ESequenceType getType() {
		
		return ESequenceType.Talk;
		
	}
	
	@Override
	public String getMessage() {
		
		return new TextComponentTranslation(this.name+".message."+this.currentStep).getFormattedText();
		
	}
	
	public void goToNextMessage() {
		
		if(this.currentStep != this.step) {
			this.currentStep++;
		}
		
	}
	
	public boolean isLastMessage() {
		
		return this.currentStep == this.step;
		
	}
	
	@Override
	public ISequence setEnd() {
		
		this.end = true;
		return this;
		
	}
	
	@Override
	public boolean isEnd() {
		
		return this.end;
		
	}
	
	@Override
	public void onEnd(ContainerSequence container) {
		
	}
	
	@Override
	public void onNext(ContainerSequence container) {
		
	}

}
