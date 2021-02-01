package de.fll.regiom.game.providers;

import de.fll.regiom.game.CrossWordRiddle;

import java.util.Iterator;
import java.util.List;

public class CrossWordProvider implements RiddleProvider<CrossWordRiddle> {
	private static List<CrossWordRiddle> riddles; //TODO: CREATE RIDDLES AND LET THEM BE IMPORTED FROM A FILE
	private Iterator<CrossWordRiddle> provider;

	@Override
	public CrossWordRiddle getNewRiddle() {
		if (!provider.hasNext())
			provider = riddles.listIterator();
		return provider.next();
	}
}
