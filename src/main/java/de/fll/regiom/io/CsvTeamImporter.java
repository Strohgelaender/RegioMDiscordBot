package de.fll.regiom.io;

import de.fll.regiom.model.Team;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Imports Teamdata from FLL-CSV.
 * <b>Use this for one-time imports.</b>
 *
 * @see JsonExporter for regualar Imports (on Restarts)
 */
public class CsvTeamImporter {
	private final File file;

	public CsvTeamImporter(String path) {
		this.file = new File(path);
	}

	public List<Team> importTeams() {
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			return bufferedReader.lines().map(s -> s.split(";"))
					.filter(a -> a.length != 0 && a[0].trim().equals("Team"))
					.map(this::fromStrings).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	private Team fromStrings(String... fields) {
		int hotID = Integer.parseInt(fields[1].substring(0, 4));
		String name = fields[1].substring(6).trim();
		return new Team(name, hotID);
	}

}
