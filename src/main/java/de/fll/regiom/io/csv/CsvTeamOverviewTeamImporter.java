package de.fll.regiom.io.csv;

import de.fll.regiom.model.members.Team;

import java.nio.file.Path;

public class CsvTeamOverviewTeamImporter extends AbstractCsvImporter<Team> {

	public CsvTeamOverviewTeamImporter(String filePath) {
		super(Path.of(filePath), ",");
	}

	@Override
	protected boolean filter(String[] line) {
		if (line.length < 2)
			return false;
		return line[0].matches("\\d+"); //valid number (hotID)
	}

	@Override
	protected Team fromStrings(String[] line) {
		try {
			int hotId = Integer.parseInt(line[0]);
			String name = line[1];
			return new Team(name, hotId);
		} catch (NumberFormatException e) {
			//this should never happen because the filter above was already applied.
			//but in theory the string could contain a higher number than Integer.MAX_INT
			//and this would throw an exception.
			return null;
		}
	}
}
