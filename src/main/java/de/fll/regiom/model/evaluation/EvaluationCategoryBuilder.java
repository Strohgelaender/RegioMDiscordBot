package de.fll.regiom.model.evaluation;

import de.fll.regiom.model.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

class EvaluationCategoryBuilder implements Builder<EvaluationCategory> {

	private String title;
	private String description;
	private EvaluationEntry lineOne;
	private EvaluationEntry lineTwo;

	@NotNull
	EvaluationCategoryBuilder title(@NotNull String title) {
		Objects.requireNonNull(title);
		this.title = title;
		return this;
	}

	@NotNull
	EvaluationCategoryBuilder description(@NotNull String description) {
		Objects.requireNonNull(description);
		this.description = description;
		return this;
	}

	@NotNull
	EvaluationCategoryBuilder entry(@NotNull EvaluationEntry entry) {
		Objects.requireNonNull(entry);
		if (lineOne == null)
			lineOne = entry;
		else if (lineTwo == null)
			lineTwo = entry;
		else
			throw new IllegalStateException();
		return this;
	}

	@NotNull
	@Override
	public EvaluationCategory build() {
		if (title == null || description == null || lineOne == null)
			throw new IllegalArgumentException();
		if (lineTwo == null)
			return new EvaluationCategory(title, description, lineOne);
		return new EvaluationCategory(title, description, lineOne, lineTwo);
	}
}
