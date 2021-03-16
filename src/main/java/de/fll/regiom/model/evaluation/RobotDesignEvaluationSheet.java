package de.fll.regiom.model.evaluation;

import java.util.List;

public final class RobotDesignEvaluationSheet extends EvaluationSheet {

	public RobotDesignEvaluationSheet() {
		super("BEWERTUNGSBOGEN ROBOTERDESIGN", List.of(
				new EvaluationCategoryBuilder().title("Benennen").description("Das Team hat eine klar definierte Strategie und ausgelotet, welche Konstruktions- und Programmierfähigkeiten es benötigt.")
						.entry(new EvaluationEntryBuilder().title("... klare Strategie").option("Keine").option("Teilweise").option("Völlig").option(null).build())
						.entry(new EvaluationEntryBuilder().title("... Teammitglieder haben Konstruktions- und Programmierfähigkeiten erlernt").option("Einige").option("Viele").option("Alle").option(null).build())
						.build(),
				new EvaluationCategoryBuilder().title("Entwickeln").description("Das Team hat innovative Entwürfe und einen klaren Arbeitsplan erstellt und hat sich bei Bedarf Rat geholt.")
						.entry(new EvaluationEntryBuilder().title("... Hinweise auf einen effektiven Arbeitsplan").option("Wenige").option("Einige").option("Viele").option(null).build())
						.entry(new EvaluationEntryBuilder().title("... Erläuterungen der innovativen Merkmale von Roboter und Code").option("Wenige ").option("Einige").option("Viele").option(null).build())
						.build(),
				new EvaluationCategoryBuilder().title("Erstellen").description("Das Team hat eine effektive Roboter- und Programmierlösung entwickelt, die zur Strategie passt.")
						.entry(new EvaluationEntryBuilder().title("... Funktionalität von Roboteranbauten oder Sensoren").option("Eingeschränkte").option("Teilweise").option("Gute").option(null).build())
						.entry(new EvaluationEntryBuilder().title("... Erklärung, was der Code beim Roboter bewirkt").option("Unklare").option("Teilweise klare").option("Völlig klare").option(null).build())
						.build(),
				new EvaluationCategoryBuilder().title("Iteration").description("Das Team hat mehrfach seinen Roboter und den Code getestet, um verbesserungswürdige Bereiche zu erkennen und hat die Ergebnisse in seine aktuelle Lösung integriert.")
						.entry(new EvaluationEntryBuilder().title("Roboter und Code wurde ... getestet").option("kaum").option("etwas").option("intensiv").option(null).build())
						.entry(new EvaluationEntryBuilder().title("Roboter und Code wurden ... verbessert").option("kaum").option("etwas").option("stark").option(null).build())
						.build(),
				new EvaluationCategoryBuilder().title("Mitteilen").description("Die Erklärung des Teams zum Roboterdesign-Prozess ist effektiv und zeigt, wie alle Teammitglieder einbezogen wurden.")
						.entry(new EvaluationEntryBuilder().title("... Erläuterung des Roboterdesign-Prozesses").option("Unklare").option("Teilweise klare").option("Vollständig klare").option(null).build())
						.entry(new EvaluationEntryBuilder().title("... Teammitglieder waren involviert").option("Einige").option("Viele").option("Alle").option(null).build())
						.build()
		));
	}
}
