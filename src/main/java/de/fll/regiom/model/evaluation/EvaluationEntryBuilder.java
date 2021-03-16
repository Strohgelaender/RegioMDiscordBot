package de.fll.regiom.model.evaluation;

import de.fll.regiom.model.Builder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class EvaluationEntryBuilder implements Builder<EvaluationEntry> {

	private String title;
	private List<String> options = new ArrayList<>();

	@NotNull
	EvaluationEntryBuilder title(@NotNull String title) {
		this.title = title;
		return this;
	}

	@NotNull
	EvaluationEntryBuilder option(@Nullable String option) {
		options.add(option);
		return this;
	}

	@NotNull
	EvaluationEntryBuilder options(@Nullable String... options) {
		this.options = new ArrayList<>(Arrays.asList(options));
		return this;
	}

	@Override
	@NotNull
	public EvaluationEntry build() {
		if (title == null || options == null || options.isEmpty())
			throw new IllegalArgumentException();
		return new EvaluationEntry(title, options);
	}
}
