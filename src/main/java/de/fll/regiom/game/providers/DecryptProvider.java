package de.fll.regiom.game.providers;

import de.fll.regiom.game.DecryptRiddle;

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
