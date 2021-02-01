package de.fll.regiom.game;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BlanksRiddle extends Riddle {
	private final Set<Integer> solutionNumbers;

	protected BlanksRiddle(String riddle, Integer... solutionNumbers) {
		super(riddle);
		this.solutionNumbers = Set.of(solutionNumbers);
	}

	@Override
	boolean checkSolution(String s) {
		try {
			Set<Integer> attempt = Arrays.stream(s.split(" ")).map(Integer::parseInt).collect(Collectors.toSet());
			return attempt.equals(solutionNumbers);
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
