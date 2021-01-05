package de.fll.regiom.io;

import de.fll.regiom.model.Team;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Imports Teamdata from FLL-CSV.
 * <b>Use this for one-time imports.</b>
 *
 * @see JSONExporter for regualar Imports (on Restarts)
 */
public class TeamImporter {
	private final File file;

	public TeamImporter(String path) {
		this.file = new File(path);
	}

	public List<Team> importTeams() {
		List<Team> teams = new ArrayList<>();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] fields = line.split(";");
				if (fields.length == 0 || !fields[0].trim().equals("Team"))
					continue;
				int hotID = Integer.parseInt(fields[1].substring(0, 4));
				String name = fields[1].substring(6).trim();
				teams.add(new Team(name, hotID));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return teams;
	}

}