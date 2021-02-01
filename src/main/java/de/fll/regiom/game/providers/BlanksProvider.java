package de.fll.regiom.game.providers;

import de.fll.regiom.game.BlanksRiddle;

import java.util.Iterator;
import java.util.List;

public class BlanksProvider implements RiddleProvider<BlanksRiddle> {
	private static List<BlanksRiddle> riddles; //TODO: CREATE RIDDLES AND LET THEM BE IMPORTED FROM A FILE
	private Iterator<BlanksRiddle> provider;

	@Override
	public BlanksRiddle getNewRiddle() {
		if (!provider.hasNext())
			provider = riddles.listIterator();
		return provider.next();
	}
}
