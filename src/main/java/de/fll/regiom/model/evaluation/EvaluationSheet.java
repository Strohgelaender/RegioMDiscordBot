package de.fll.regiom.model.evaluation;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public abstract class EvaluationSheet {

	private final String title;
	private final List<EvaluationCategory> categories;

	EvaluationSheet(@NotNull String title, @NotNull List<EvaluationCategory> categories) {
		Objects.requireNonNull(title);
		Objects.requireNonNull(categories);
		this.title = title;
		this.categories = categories;
	}

	@NotNull
	public List<EvaluationCategory> getCategories() {
		return categories;
	}

	@NotNull
	public String getTitle() {
		return title;
	}
}
