package de.fll.regiom.model.evaluation;

import de.fll.regiom.model.Team;

import java.util.List;

public class RobotDesignEvaluationSheet extends EvaluationSheet {

	public RobotDesignEvaluationSheet(Team team) {
		super(team, "BEWERTUNGSBOGEN ROBOTERDESIGN", List.of(
				EvaluationCategory.of("Benennen", "Das Team hat eine klar definierte Strategie und ausgelotet, welche Konstruktions- und Programmierfähigkeiten es benötigt.",
						3, "... klare Strategie", "Keine", "Teilweise", "Völlig",
						"... Teammitglieder haben Konstruktions- und Programmierfähigkeiten erlernt", "Einige", "Viele", "Alle"),
				EvaluationCategory.of("Entwickeln", "Das Team hat innovative Entwürfe und einen klaren Arbeitsplan erstellt und hat sich bei Bedarf Rat geholt.",
						3, "... Hinweise auf einen effektiven Arbeitsplan", "Wenige", "Einige", "Viele",
						"... Erläuterungen der innovativen Merkmale von Roboter und Code", "Wenige ", "Einige", "Viele"),
				EvaluationCategory.of("Erstellen", "Das Team hat eine effektive Roboter- und Programmierlösung entwickelt, die zur Strategie passt.",
						3, "... Funktionalität von Roboteranbauten oder Sensoren", "Eingeschränkte", "Teilweise", "Gute",
						"... Erklärung, was der Code beim Roboter bewirkt", "Unklare", "Teilweise klare", "Völlig klare"),
				EvaluationCategory.of("Iteration", "Das Team hat mehrfach seinen Roboter und den Code getestet, um verbesserungswürdige Bereiche zu erkennen und hat die Ergebnisse in seine aktuelle Lösung integriert.",
						3, "Roboter und Code wurde ... getestet", "kaum", "etwas", "intensiv",
						"Roboter und Code wurden ... verbessert", "kaum", "etwas", "stark"),
				EvaluationCategory.of("Mitteilen", "Die Erklärung des Teams zum Roboterdesign-Prozess ist effektiv und zeigt, wie alle Teammitglieder einbezogen wurden.",
						3, "... Erläuterung des Roboterdesign-Prozesses", "Unklare", "Teilweise klare", "Vollständig klare",
						"... Teammitglieder waren involviert", "Einige", "Viele", "Alle")
		));
	}
}
