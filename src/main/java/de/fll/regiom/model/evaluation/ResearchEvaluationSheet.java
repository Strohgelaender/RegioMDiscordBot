package de.fll.regiom.model.evaluation;

import java.util.List;

public final class ResearchEvaluationSheet extends EvaluationSheet {

	public ResearchEvaluationSheet() {
		super("BEWERTUNGSBOGEN FORSCHUNG", List.of(
				new EvaluationCategoryBuilder().title("Benennen").description("Das Team hat ein Problem klar definiert und es gut untersucht.")
						.entry(new EvaluationEntryBuilder().title("Problem wurde ... klar definiert").option("nicht").option("teilweise").option("völlig").option(null).build())
						.entry(new EvaluationEntryBuilder().title("Das Team hat ... Forschung betrieben").option("wenig").option("bei unklarer Qualität etwas").option("breite und qualitativ hochwertige").option(null).build()
						).build(),
				new EvaluationCategoryBuilder().title("Entwickeln").description("Das Team hat selbständig innovative Ideen erarbeitet, daraus eine ausgewählt und planvoll weiterentwickelt.")
						.entry(new EvaluationEntryBuilder().title("...  Ideen des gesamten Teams").option("Wenige").option("Hinweise auf einige").option("Viele").option(null).build())
						.entry(new EvaluationEntryBuilder().title("... Planung mit ... Teammitgliedern").option("Wenige + wenigen").option("etwas + einigen").option("Hocheffektive + allen").option(null).build())
						.build(),
				new EvaluationCategoryBuilder().title("Erstellen").description("Das Team hat eine eigene Idee entwickelt oder auf einer bestehenden aufgebaut. Mit einem Prototypmodell oder einer Zeichnung veranschaulicht das Team die Lösung.")
						.entry(new EvaluationEntryBuilder().title("... Entwicklung einer innovativen Lösung").option("Wenig").option("Teilweise").option("Umfangreiche").option(null).build())
						.entry(new EvaluationEntryBuilder().title("...  Modell/Zeichnung der Lösung").option("Kein(e)").option("Einfache(s) und hilfreiche(s)").option("Detaillierte(s) und hilfreiche(s)").option(null).build())
						.build(),
				new EvaluationCategoryBuilder().title("Iteration").description("Das Team hat seine Ideen vorgestellt, Feedback gesammelt und Verbesserungen in seine Lösung eingearbeitet.")
						.entry(new EvaluationEntryBuilder().title("Lösung wurde ... gezeigt").option("kaum").option("ein paar Male").option("häufig").option(null).build())
						.entry(new EvaluationEntryBuilder().title("... Hinweise auf Verbesserungen der Lösung").option("Kaum").option("Einige").option("Viele").option(null).build())
						.build(),
				new EvaluationCategoryBuilder().title("Mitteilen").description("Das Team stellte in einer kreativen und überzeugenden Präsentation seine aktuelle Lösung und deren Auswirkungen auf die betroffenen Personen vor.")
						.entry(new EvaluationEntryBuilder().title("Präsentation ... ansprechend").option("wenig").option("teilweise").option("sehr").option(null).build())
						.entry(new EvaluationEntryBuilder().title("Lösung und möglicher Effekt auf andere ...").option("unklar").option("teilweise klar").option("völlig klar").option(null).build())
						.build()
		));
	}
}
