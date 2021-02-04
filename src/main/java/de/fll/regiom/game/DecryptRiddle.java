package de.fll.regiom.game;

public class DecryptRiddle extends Riddle {
	private final String solution;

	public DecryptRiddle(String url, String solution) {
		super(url);
		this.solution = solution;
	}

	@Override
	boolean checkSolution(String s) {
		return solution.equals(s);
	}
}
