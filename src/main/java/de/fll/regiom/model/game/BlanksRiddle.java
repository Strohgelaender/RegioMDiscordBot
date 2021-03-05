package de.fll.regiom.model.game;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BlanksRiddle extends Riddle {
	private final Set<Integer> solutionNumbers;

	public BlanksRiddle(String url, int id, Set<Integer> solutionNumbers) {
		super(url, id);
		this.solutionNumbers = solutionNumbers;
	}

	@Override
	public boolean checkSolution(String s) {
		try {
			Set<Integer> attempt = Arrays.stream(s.split(" ")).map(Integer::parseInt).collect(Collectors.toSet());
			return attempt.equals(solutionNumbers);
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
