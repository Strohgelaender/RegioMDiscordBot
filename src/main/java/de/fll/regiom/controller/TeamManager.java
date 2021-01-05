package de.fll.regiom.controller;

import de.fll.regiom.io.JSONImporter;
import de.fll.regiom.io.TeamImporter;
import de.fll.regiom.model.Team;

import java.util.List;

public class TeamManager {

	private static TeamManager instance;

	public static TeamManager getInstance() {
		if (instance == null) {
			instance = new TeamManager();
			instance.setup();
		}
		return instance;
	}

	private List<Team> teams;

	public void setup() {
		teams = JSONImporter.getInstance().importTeams();
		if (teams.isEmpty()) //Falback: Read Teams from HoT-Export
			teams = new TeamImporter("./myregions.csv").importTeams();
	}

	public List<Team> getTeams() {
		return teams;
	}
}
