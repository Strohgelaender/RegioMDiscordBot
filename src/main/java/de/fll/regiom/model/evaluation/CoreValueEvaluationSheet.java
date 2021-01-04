package de.fll.regiom.model.evaluation;

import de.fll.regiom.model.Team;

import java.util.List;

public class CoreValueEvaluationSheet extends EvaluationSheet {
	private static final String[] ENTRIES = {"... Beispiele, die im ganzen Team beobachtet wurden", "Wenige", "Einige", "Mehrere", null};

	public CoreValueEvaluationSheet(Team team) {
		super(team, "BEWERTUNGSBOGEN GRUNDWERTE", List.of(
				EvaluationCategory.of("Entdeckung", "Das Team hat neue Talente und Ideen entdeckt.", ENTRIES),
				EvaluationCategory.of("Innovation", "Das Team war kreativ und ausdauernd bei der Lösung von Problemen.", ENTRIES),
				EvaluationCategory.of("Wirkung", "Das Team hat das Gelernte angewendet, um seine Welt zu verbessern.", ENTRIES),
				EvaluationCategory.of("Inklusion", "Das Team verhält sich respektvoll und akzeptiert seine Unterschiede.", ENTRIES),
				EvaluationCategory.of("Teamwork", "Das Team zeigte klar, dass sie gut zusammenarbeiten.", ENTRIES),
				EvaluationCategory.of("Spaß", "Das Team hatte eindeutig Spaß miteinander und feierte seine Erfolge.", ENTRIES)
		));
	}
}
