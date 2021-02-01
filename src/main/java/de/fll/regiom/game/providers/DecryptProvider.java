package de.fll.regiom.game.providers;

import de.fll.regiom.game.DecryptRiddle;

public class DecryptProvider implements RiddleProvider<DecryptRiddle> {
	private static DecryptRiddle riddle; //TODO: CREATE RIDDLE AND HARDCODE IT HERE

	@Override
	public DecryptRiddle getNewRiddle() {
		return riddle;
	}
}
