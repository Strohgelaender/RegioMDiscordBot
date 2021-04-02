package de.fll.regiom.controller;

import de.fll.regiom.io.csv.CsvTeamOverviewTeamImporter;
import de.fll.regiom.io.json.JsonExporter;
import de.fll.regiom.io.json.JsonImporter;
import de.fll.regiom.model.Storable;
import de.fll.regiom.model.members.Team;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
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

	@NotNull
	public CompletableFuture<?> createAllTeamRoles(@NotNull JDA jda) {
		return DiscordTeamStructureManager.INSTANCE.createAllTeamRoles(jda, teams)
				.thenAccept(v -> save())
				.thenAccept(v -> System.out.println("[TeamRepository] created Roles"));
	}

	@NotNull
	public CompletableFuture<?> createAllTeamareas(@NotNull JDA jda) {
		return DiscordTeamStructureManager.INSTANCE.createAllTeamareas(jda, teams)
				.thenAccept(v -> save())
				.thenAccept(v -> System.out.println("[TeamRepository] created Teamareas"));

	}

	@NotNull
	public CompletableFuture<?> updateAllWelcomeMessages(@NotNull JDA jda) {
		return DiscordTeamStructureManager.INSTANCE.updateAllWelcomeMessages(jda, teams);
	}

	@NotNull
	public CompletableFuture<?> removeAllTeamareas(@NotNull JDA jda) {
		return DiscordTeamStructureManager.INSTANCE.removeAllTeamareas(jda, teams)
				.thenAccept(v -> save());
	}

	@NotNull
	public CompletableFuture<?> removeAllRoles(@NotNull JDA jda) {
		return DiscordTeamStructureManager.INSTANCE.removeAllTeamRoles(jda, teams)
				.thenAccept(v -> save());
	}

	@NotNull
	public CompletableFuture<?> createAllInvites(@NotNull JDA jda) {
		return InviteManager.INSTANCE.createAllInvites(jda, teams)
				.thenAccept(v -> System.out.println("[TeamRepository] created Invites."));
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
			teams = new CsvTeamOverviewTeamImporter("./Teamliste.csv").fromLines();
	}
}
