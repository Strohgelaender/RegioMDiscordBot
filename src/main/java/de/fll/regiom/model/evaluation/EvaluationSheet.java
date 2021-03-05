package de.fll.regiom.model.evaluation;

import de.fll.regiom.model.Team;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class EvaluationSheet {

	private final Team team;
	private final String title;
	private final List<EvaluationCategory> categories;

	EvaluationSheet(@NotNull Team team, @NotNull String title, @NotNull List<EvaluationCategory> categories) {
		this.team = team;
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

	@NotNull
	public Team getTeam() {
		return team;
	}
}
