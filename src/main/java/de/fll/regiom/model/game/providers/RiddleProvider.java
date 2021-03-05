package de.fll.regiom.model.game.providers;

import de.fll.regiom.model.game.Riddle;

public interface RiddleProvider<T extends Riddle>{
	T getNewRiddle();
}
