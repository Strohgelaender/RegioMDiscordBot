package de.fll.regiom.game.providers;

import de.fll.regiom.game.BlanksRiddle;

import java.util.Iterator;
import java.util.List;

public class BlanksProvider implements RiddleProvider<BlanksRiddle> {
	private List<BlanksRiddle> riddles;
	private Iterator<BlanksRiddle> provider;

	@Override
	public BlanksRiddle getNewRiddle() {
		if (!provider.hasNext())
			provider = riddles.listIterator();
		return provider.next();
	}

	public void setRiddles(List<BlanksRiddle> riddles) {
		this.riddles = riddles;
	}
}
