package de.fll.regiom.model.evaluation;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class EvaluationEntry {

	private final String title;
	private final String[] options;

	public EvaluationEntry(@NotNull String title, @NotNull String... options) {
		Objects.requireNonNull(title);
		Objects.requireNonNull(options);
		this.title = title;
		this.options = options;
	}

	public EvaluationEntry(@NotNull String title, @NotNull List<String> options) {
		this(title, options.toArray(new String[0]));
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
