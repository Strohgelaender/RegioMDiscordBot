package de.fll.regiom.model.evaluation;

import java.util.List;

import de.fll.regiom.model.Team;

public class ResearchEvaluationSheet extends EvaluationSheet{
	public ResearchEvaluationSheet(Team team) {
		super(team, "BEWERTUNGSBOGEN FORSCHUNG", List.of(
				EvaluationCategory.of("Benennen", "Das Team hat ein Problem klar definiert und es gut untersucht.",
						3, "Problem wurde ... klar definiert", "nicht", "teilweise", "völlig",
						"Das Team hat ... Forschung betrieben", "wenig", "bei unklarer Qualität etwas", "breite und qualitativ hochwertige"),
				EvaluationCategory.of("Entwickeln", "Das Team hat selbständig innovative Ideen erarbeitet, daraus eine ausgewählt und planvoll weiterentwickelt.",
						3, "...  Ideen des gesamten Teams", "Wenige", "Hinweise auf einige", "Viele",
						"... Planung mit ... Teammitgliedern", "Wenige + wenigen", "etwas + einigen", "Hocheffektive + allen"),
				EvaluationCategory.of("Erstellen", "Das Team hat eine eigene Idee entwickelt oder auf einer bestehenden aufgebaut. Mit einem Prototypmodell oder einer Zeichnung veranschaulicht das Team die Lösung. ",
						3, "... Entwicklung einer innovativen Lösung", "Wenig", "Teilweise", "Umfangreiche",
						"...  Modell/Zeichnung der Lösung", "Kein(e)", "Einfache(s) und hilfreiche(s)", "Detaillierte(s) und hilfreiche(s)"),
				EvaluationCategory.of("Iteration", "Das Team hat seine Ideen vorgestellt, Feedback gesammelt und Verbesserungen in seine Lösung eingearbeitet.",
						3, "Lösung wurde ... gezeigt", "kaum", "ein paar Male", "häufig",
						"... Hinweise auf Verbesserungen der Lösung", "Kaum", "Einige", "Viele"),
				EvaluationCategory.of("Mitteilen", "Das Team stellte in einer kreativen und überzeugenden Präsentation seine aktuelle Lösung und deren Auswirkungen auf die betroffenen Personen vor.",
						3, "Präsentation ... ansprechend", "wenig", "teilweise", "sehr",
						"Lösung und möglicher Effekt auf andere ...", "unklar", "teilweise klar", "völlig klar")
		));
	}
}
