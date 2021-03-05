package de.fll.regiom.model.game.providers;

import de.fll.regiom.model.game.DecryptRiddle;

public class DecryptProvider implements RiddleProvider<DecryptRiddle> {
	private DecryptRiddle riddle;

	@Override
	public DecryptRiddle getNewRiddle() {
		return riddle;
	}

	public void setRiddle(DecryptRiddle riddle) {
		this.riddle = riddle;
	}
}
