package de.fll.regiom.model.evaluation;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class EvaluationCategory {

	private final String title;
	private final String description;
	private final EvaluationEntry[] entries;

	public EvaluationCategory(@NotNull String title, @NotNull String description, @NotNull EvaluationEntry lineOne, @NotNull EvaluationEntry lineTwo) {
		this(title, description, new EvaluationEntry[]{lineOne, lineTwo});
	}

	public EvaluationCategory(@NotNull String title, @NotNull String description, @NotNull EvaluationEntry[] entries) {
		this.title = title;
		this.description = description;
		this.entries = entries;
	}

	public EvaluationCategory(@NotNull String title, @NotNull String description, @NotNull EvaluationEntry entry) {
		this(title, description, new EvaluationEntry[]{entry});
	}

	@NotNull
	public static EvaluationCategory of(@NotNull String title, @NotNull String description, int entriesFirstLine, @NotNull String... entries) {
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

	@NotNull
	public static EvaluationCategory of(@NotNull String title, @NotNull String description, @NotNull String... entries) {
		return new EvaluationCategory(title, description, new EvaluationEntry(entries[0], Arrays.copyOfRange(entries, 1, entries.length)));
	}

	@NotNull
	public String getTitle() {
		return title;
	}

	@NotNull
	public String getDescription() {
		return description;
	}

	@NotNull
	public EvaluationEntry[] getEntries() {
		return entries;
	}
}