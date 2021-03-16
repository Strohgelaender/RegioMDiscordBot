package de.fll.regiom.model.game;

import org.jetbrains.annotations.NotNull;

public class CrossWordRiddle extends Riddle {

	private final String solution;

	public CrossWordRiddle(@NotNull String url, int id, @NotNull String solution) {
		super(url, id);
		this.solution = solution;
	}

	@Override
	public boolean checkSolution(String s) {
		return solution.equalsIgnoreCase(s);
	}

}
