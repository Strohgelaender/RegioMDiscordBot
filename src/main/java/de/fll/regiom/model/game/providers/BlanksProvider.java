package de.fll.regiom.model.game.providers;

import de.fll.regiom.model.game.BlanksRiddle;

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
		this.provider = riddles.listIterator();
	}
}
