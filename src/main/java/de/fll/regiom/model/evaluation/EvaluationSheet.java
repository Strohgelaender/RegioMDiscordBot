package de.fll.regiom.model.evaluation;

import de.fll.regiom.model.Team;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class EvaluationSheet implements Comparable<EvaluationSheet> {

    private final Team team;
    private final String title;
    private final List<EvaluationCategory> categories;

    EvaluationSheet(Team team, String title, List<EvaluationCategory> categories) {
        this.team = team;
        this.title = title;
        this.categories = categories;
    }

    public List<EvaluationCategory> getCategories() {
        return categories;
    }

    public String getTitle() {
        return title;
    }

    public int evaluate() {
        int sum = 0;
        for (EvaluationCategory category : categories) {
            sum += category.evaluate();
        }
        return sum;
    }

    @Override
    public int compareTo(@NotNull EvaluationSheet o) {
        return Integer.compare(evaluate(), o.evaluate());
    }
}
