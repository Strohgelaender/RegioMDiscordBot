package de.fll.regiom.model.evaluation;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class EvaluationCategory {

	private final String title;
	private final String description;
	private final EvaluationEntry[] entries;

	public EvaluationCategory(@NotNull String title, @NotNull String description, @NotNull EvaluationEntry lineOne, @NotNull EvaluationEntry lineTwo) {
		this(title, description, new EvaluationEntry[]{lineOne, lineTwo});
	}

	public EvaluationCategory(@NotNull String title, @NotNull String description, @NotNull EvaluationEntry[] entries) {
		Objects.requireNonNull(title);
		Objects.requireNonNull(description);
		Objects.requireNonNull(entries);
		this.title = title;
		this.description = description;
		this.entries = entries;
	}

	public EvaluationCategory(@NotNull String title, @NotNull String description, @NotNull EvaluationEntry entry) {
		this(title, description, new EvaluationEntry[]{entry});
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