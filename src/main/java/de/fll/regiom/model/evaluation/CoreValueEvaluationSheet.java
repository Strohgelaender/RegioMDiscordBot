package de.fll.regiom.model.evaluation;

import java.util.List;

public class CoreValueEvaluationSheet extends EvaluationSheet {
	private static final EvaluationEntry ENTRY = new EvaluationEntryBuilder().title("... Beispiele, die im ganzen Team beobachtet wurden").options("Wenige", "Einige", "Mehrere", null).build();

	public CoreValueEvaluationSheet() {
		super("BEWERTUNGSBOGEN GRUNDWERTE", List.of(
				new EvaluationCategoryBuilder().title("Entdeckung").description("Das Team hat neue Talente und Ideen entdeckt.").entry(ENTRY).build(),
				new EvaluationCategoryBuilder().title("Innovation").description("Das Team war kreativ und ausdauernd bei der Lösung von Problemen.").entry(ENTRY).build(),
				new EvaluationCategoryBuilder().title("Wirkung").description("Das Team hat das Gelernte angewendet, um seine Welt zu verbessern.").entry(ENTRY).build(),
				new EvaluationCategoryBuilder().title("Inklusion").description("Das Team verhält sich respektvoll und akzeptiert seine Unterschiede.").entry(ENTRY).build(),
				new EvaluationCategoryBuilder().title("Teamwork").description("Das Team zeigte klar, dass sie gut zusammenarbeiten.").entry(ENTRY).build(),
				new EvaluationCategoryBuilder().title("Spaß").description("Das Team hatte eindeutig Spaß miteinander und feierte seine Erfolge.").entry(ENTRY).build()
		));
	}
}
