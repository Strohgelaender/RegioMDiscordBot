package de.fll.regiom.model.evaluation;

import org.jetbrains.annotations.NotNull;

public final class EvaluationEntry {

	private final String title;
	private final String[] options;

	public EvaluationEntry(@NotNull String title, @NotNull String... options) {
		this.title = title;
		this.options = options;
	}

	@NotNull
	public String getTitle() {
		return title;
	}

	@NotNull
	public String[] getOptions() {
		return options;
	}
}
