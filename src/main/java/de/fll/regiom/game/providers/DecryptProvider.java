package de.fll.regiom.game.providers;

import de.fll.regiom.game.DecryptRiddle;

public class DecryptProvider implements RiddleProvider<DecryptRiddle> {
	private static DecryptRiddle riddle; //TODO: CREATE RIDDLE AND LET IT BE IMPORTED FROM A FILE / HARDCODE HERE

	@Override
	public DecryptRiddle getNewRiddle() {
		return riddle;
	}
}
