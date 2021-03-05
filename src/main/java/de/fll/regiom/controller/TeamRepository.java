package de.fll.regiom.controller;

import de.fll.regiom.io.csv.CsvTeamImporter;
import de.fll.regiom.io.json.JsonExporter;
import de.fll.regiom.io.json.JsonImporter;
import de.fll.regiom.model.Storable;
import de.fll.regiom.model.Team;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public enum TeamRepository implements Storable {
	INSTANCE;

	TeamRepository() {
		StorageManager.INSTANCE.register(this);
		load();
	}

	private List<Team> teams;

	public List<Team> getTeams() {
		return teams;
	}

	public void createAllTeamRoles(@NotNull JDA jda) {
		DiscordTeamStructureManager.INSTANCE.createAllTeamRoles(jda, teams);
	}

	public void createAllTeamareas(@NotNull JDA jda) {
		DiscordTeamStructureManager.INSTANCE.createAllTeamareas(jda, teams)
				.thenApply(v -> save());
	}

	public void updateAllWelcomeMessages(@NotNull JDA jda) {
		DiscordTeamStructureManager.INSTANCE.updateAllWelcomeMessages(jda, teams);
	}

	public void removeAllTeamareas(@NotNull JDA jda) {
		DiscordTeamStructureManager.INSTANCE.removeAllTeamareas(jda, teams);
		save();
	}

	public void createAllInvites(@NotNull JDA jda) {
		//DiscordTeamStructureManager.INSTANCE.createAllInvites(jda, teams, InviteManager.getInstance());
		//.thenApply(v -> InviteManager.getInstance().save());
	}

	@NotNull
	public Optional<Team> getTeamByMember(@NotNull Member member) {
		Set<Long> roles = member.getRoles().stream().map(ISnowflake::getIdLong).collect(Collectors.toSet());
		return teams.stream()
				.filter(team -> roles.contains(team.getRoleID()))
				.findFirst();
	}

	@NotNull
	public Optional<Team> getTeamByHotId(int hotId) {
		return teams.stream()
				.filter(team -> team.getHotID() == hotId)
				.findFirst();
	}

	@Override
	public boolean save() {
		JsonExporter exporter = JsonExporter.getInstance();
		exporter.exportTeams(teams);
		//exporter.exportInvites();
		return true;
	}

	@Override
	public void load() {
		teams = JsonImporter.getInstance().importTeams();
		if (teams.isEmpty()) //Fallback: Read Teams from HoT-Export
			teams = new CsvTeamImporter("./myregions.csv").importTeams();
	}
}
