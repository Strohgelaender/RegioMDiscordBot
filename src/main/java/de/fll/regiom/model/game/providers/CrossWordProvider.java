package de.fll.regiom.model.game.providers;

import de.fll.regiom.model.game.CrossWordRiddle;
import de.fll.regiom.model.game.Riddle;

import java.util.Iterator;
import java.util.List;

public class CrossWordProvider implements RiddleProvider<CrossWordRiddle> {

	private List<CrossWordRiddle> riddles;
	private Iterator<CrossWordRiddle> provider;

	@Override
	public CrossWordRiddle getNewRiddle(Riddle prev) {
		if (!provider.hasNext())
			provider = riddles.listIterator();
		return provider.next();
	}

	public void setRiddles(List<CrossWordRiddle> riddles) {
		this.riddles = riddles;
		provider = riddles.listIterator();
	}
}
