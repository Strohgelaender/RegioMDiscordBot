package de.fll.regiom.game;

public class DecryptRiddle extends Riddle {
	private final String solution;

	protected DecryptRiddle(String riddle, String solution) {
		super(riddle);
		this.solution = solution;
	}

	@Override
	boolean checkSolution(String s) {
		return solution.equals(s);
	}
}
