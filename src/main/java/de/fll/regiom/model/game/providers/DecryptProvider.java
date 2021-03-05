package de.fll.regiom.model.game.providers;

import de.fll.regiom.model.game.DecryptRiddle;
import de.fll.regiom.model.game.Riddle;

public class DecryptProvider implements RiddleProvider<DecryptRiddle> {
	private DecryptRiddle riddle;

	@Override
	public DecryptRiddle getNewRiddle(Riddle prev) {
		return riddle;
	}

	@Override
	public DecryptRiddle getByID(int id) {
		return riddle;
	}

	public void setRiddle(DecryptRiddle riddle) {
		this.riddle = riddle;
	}
}
