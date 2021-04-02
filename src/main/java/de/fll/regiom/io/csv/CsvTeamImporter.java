package de.fll.regiom.io.csv;

import de.fll.regiom.io.json.JsonExporter;
import de.fll.regiom.model.members.Team;

import java.nio.file.Path;
import java.util.List;

/**
 * Imports Teamdata from FLL-CSV.
 * <b>Use this for one-time imports.</b>
 *
 * @see JsonExporter for regualar Imports (on Restarts)
 */
public class CsvTeamImporter extends AbstractCsvImporter<Team> {

	public CsvTeamImporter(String path) {
		super(Path.of(path), ";");
	}

	public List<Team> importTeams() {
		return fromLines();
	}

	@Override
	protected final boolean filter(String[] line) {
		return line.length != 0 && line[0].strip().equals("Team");
	}

	@Override
	protected final Team fromStrings(String... fields) {
		String name = "unknown";
		int hotID;
		try {
			name = fields[1].substring(6).strip();
			hotID = Integer.parseInt(fields[1].substring(0, 4));
			return new Team(name, hotID);
		} catch (Exception e) {
			System.err.println("Error creating Team " + name + " with number " + fields[1].substring(0, 4));
		}
		return null;
	}

}
