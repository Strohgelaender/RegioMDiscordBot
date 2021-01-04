package de.fll.regiom.model.evaluation;

import java.util.Arrays;

public final class EvaluationCategory {

	private final String title;
	private final String description;
	private final EvaluationEntry[] entries;

	public EvaluationCategory(String title, String description, EvaluationEntry lineOne, EvaluationEntry lineTwo) {
		this(title, description, new EvaluationEntry[]{lineOne, lineTwo});
	}

	public EvaluationCategory(String title, String description, EvaluationEntry[] entries) {
		this.title = title;
		this.description = description;
		this.entries = entries;
	}

	public EvaluationCategory(String title, String description, EvaluationEntry entry) {
		this(title, description, new EvaluationEntry[]{entry});
	}

	public static EvaluationCategory of(String title, String description, int entriesFirstLine, String... entries) {
		String[] lineOne = new String[4];
		String[] lineTwo = new String[4];
		String[] titles = new String[2];
		for (int i = 0; i < entries.length; i++) {
			if (i == 0)
				titles[0] = entries[0];
			else if (i == entriesFirstLine + 1)
				titles[1] = entries[i];
			else {
				if (i <= entriesFirstLine)
					lineOne[i - 1] = entries[i];
				else
					lineTwo[i - entriesFirstLine - 2] = entries[i];
			}

		}
		return new EvaluationCategory(title, description, new EvaluationEntry(titles[0], lineOne), new EvaluationEntry(titles[1], lineTwo));

	}

	public static EvaluationCategory of(String title, String description, String... entries) {
		return new EvaluationCategory(title, description, new EvaluationEntry(entries[0], Arrays.copyOfRange(entries, 1, entries.length)));
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public EvaluationEntry[] getEntries() {
		return entries;
	}

	public void check(int line, int index) {
		if (line < 0 || line >= 2 || index < 0 || index >= 4)
			return;
		entries[line].setChecked(index);
	}

	public int evaluate() {
		int sum = 0;
		for (EvaluationEntry entry : entries) {
			sum += entry.getChecked();
		}
		return sum;
	}
}