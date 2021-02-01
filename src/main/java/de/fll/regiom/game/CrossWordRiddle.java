package de.fll.regiom.game;

import org.jetbrains.annotations.NotNull;

public class CrossWordRiddle extends Riddle {
	private final String solution;

	public CrossWordRiddle(@NotNull String riddle, @NotNull String solution) {
		super(riddle);
		this.solution = solution;
	}

	@Override
	boolean checkSolution(String s) {
		return solution.equals(s);
	}
}
