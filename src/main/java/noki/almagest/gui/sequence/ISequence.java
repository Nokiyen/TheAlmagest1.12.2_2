package noki.almagest.gui.sequence;

public interface ISequence {
	
	abstract ESequenceType getType();
	abstract String getMessage();
	abstract boolean isEnd();
	abstract ISequence setEnd();
	abstract void onEnd(ContainerSequence container);
	abstract void onNext(ContainerSequence container);

}
