package noki.almagest.gui.sequence;

import net.minecraft.util.text.TextComponentTranslation;


/**********
 * @class SequenceChoice
 *
 * @description
 */
public class SequenceChoice implements ISequence {
	
	
	//******************************//
	// define member variables.
	//******************************//
	private String name;
	private int cNumber;
	private boolean end = false;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public SequenceChoice(String name, int cNumber) {
		
		this.name = name;
		this.cNumber = cNumber;
		
	}

	@Override
	public ESequenceType getType() {
		
		return ESequenceType.Choice;
		
	}
	
	public int getCNumber() {
		
		return this.cNumber;
		
	}
	
	public String[] getChoiceMessage() {
		
		String[] choices = new String[this.cNumber];
		for(int i=1; i<=this.cNumber; i++) {
			choices[i-1] = new TextComponentTranslation(this.name+".choice."+i).getFormattedText();
		}
		return choices;
		
	}
	
	@Override
	public String getMessage() {
		
		return new TextComponentTranslation(this.name+".message").getFormattedText();
		
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
