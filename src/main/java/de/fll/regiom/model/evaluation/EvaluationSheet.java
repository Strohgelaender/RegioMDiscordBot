package de.fll.regiom.model.evaluation;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class EvaluationSheet {

	private final String title;
	private final List<EvaluationCategory> categories;

	EvaluationSheet(@NotNull String title, @NotNull List<EvaluationCategory> categories) {
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
