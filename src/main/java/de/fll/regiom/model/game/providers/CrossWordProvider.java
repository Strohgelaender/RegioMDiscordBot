package de.fll.regiom.model.game.providers;

import de.fll.regiom.model.game.CrossWordRiddle;
import de.fll.regiom.model.game.Riddle;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CrossWordProvider implements RiddleProvider<CrossWordRiddle> {
	private Map<Integer, CrossWordRiddle> byID;
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
		byID = riddles.stream().collect(Collectors.toMap(Riddle::getId, r -> r));
		provider = riddles.listIterator();
	}

	@Override
	public CrossWordRiddle getByID(int id) {
		return byID.get(id);
	}
}
