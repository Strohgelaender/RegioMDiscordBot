package de.fll.regiom.model.game.providers;

import de.fll.regiom.model.game.BlanksRiddle;
import de.fll.regiom.model.game.Riddle;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlanksProvider implements RiddleProvider<BlanksRiddle> {
	private Map<Integer, BlanksRiddle> byID;

	@Override
	public BlanksRiddle getNewRiddle(Riddle prev) {
		return byID.get(prev.getId() / 2);
	}

	public void setRiddles(List<BlanksRiddle> riddles) {
		byID = riddles.stream().collect(Collectors.toMap(BlanksRiddle::getId, r -> r));
	}
}
