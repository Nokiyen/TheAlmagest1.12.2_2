package noki.almagest.gui;

import java.io.IOException;

import noki.almagest.gui.sequence.ContainerSequence;
import noki.almagest.gui.sequence.GuiContainerSequence;

public class GuiMiraContainer extends GuiContainerSequence {

	public GuiMiraContainer(ContainerSequence container) {
		super(container);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
		if(keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
			if(!this.container.currentSequence().closeable()) {
				return;
			}
		}
		super.keyTyped(typedChar, keyCode);

	}

}
