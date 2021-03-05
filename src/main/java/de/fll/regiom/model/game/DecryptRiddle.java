package de.fll.regiom.model.game;

public class DecryptRiddle extends Riddle {
	private final String solution;

	public DecryptRiddle(String url, String solution) {
		super(url);
		this.solution = solution;
	}

	@Override
	public boolean checkSolution(String s) {
		return solution.equalsIgnoreCase(s);
	}
}
