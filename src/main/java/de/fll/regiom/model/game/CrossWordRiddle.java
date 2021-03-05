package de.fll.regiom.model.game;

import org.jetbrains.annotations.NotNull;

public class CrossWordRiddle extends Riddle {
	private final String solution;
	private final String url = "https://cdn.discordapp.com/attachments/795627126232449034/806979930910228580/unknown.png";

	public CrossWordRiddle(@NotNull String url, @NotNull String solution) {
		super(url);
		this.solution = solution;
	}

	@Override
	public boolean checkSolution(String s) {
		return solution.equalsIgnoreCase(s);
	}

}
