package de.fll.regiom.game.providers;

import de.fll.regiom.game.Riddle;

public interface RiddleProvider<T extends Riddle>{
	T getNewRiddle();
}
