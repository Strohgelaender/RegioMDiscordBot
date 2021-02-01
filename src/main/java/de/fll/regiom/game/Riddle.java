package de.fll.regiom.game;

public abstract class Riddle {
	private final String riddleText;

	protected Riddle(String riddle) {
		this.riddleText = riddle;
	}

	abstract boolean checkSolution(String s);

	public String getRiddleText() {
		return riddleText;
	}
}
