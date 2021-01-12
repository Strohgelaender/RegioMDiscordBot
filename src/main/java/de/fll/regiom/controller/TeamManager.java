package de.fll.regiom.controller;

import de.fll.regiom.io.JsonImporter;
import de.fll.regiom.io.CsvTeamImporter;
import de.fll.regiom.model.Team;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
		teams = JsonImporter.getInstance().importTeams();
		if (teams.isEmpty()) //Falback: Read Teams from HoT-Export
			teams = new CsvTeamImporter("./myregions.csv").importTeams();
	}

	public List<Team> getTeams() {
		return teams;
	}

	public Optional<Team> getTeamByMember(Member member) {
		Set<Long> roles = member.getRoles().stream().map(ISnowflake::getIdLong).collect(Collectors.toSet());
		for (Team team : teams) {
			if (roles.contains(team.getRoleID())) {
				return Optional.of(team);
			}
		}
		return Optional.empty();
	}
}
