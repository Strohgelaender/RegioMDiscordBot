package de.fll.regiom.controller;

import de.fll.regiom.io.CsvTeamImporter;
import de.fll.regiom.io.JsonExporter;
import de.fll.regiom.io.JsonImporter;
import de.fll.regiom.model.Constants;
import de.fll.regiom.model.Storable;
import de.fll.regiom.model.Team;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public enum TeamManager implements Storable {
	INSTANCE;

	TeamManager() {
		StorageManager.INSTANCE.register(this);
		load();
	}

	private static final EnumSet<Permission> PERMISSIONS_VIEW_TEXT_AND_VOICE_CHANNEL = EnumSet.of(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT);

	private static final String TEAM_CHANNEL_TOPIC_TEXT = "Dieser Channel ist nur für euch. Hier könnt ihr euch unterhalten, eine kleine Linkliste führen oder, oder, oder ... Einfach worauf auch immer ihr gerade Lust habt.";
	private static final String TEAM_WELCOME_TEXT = """
			Liebes Team **%%s**

			dieser Channel ist privat und nur für euch!
			Keine Juroren können lesen, was ihr hier schreibt oder hören, was ihr in eurem Sprachkanal besprecht.
			Nutzt diesen Channel für alles, was gerade anfällt. Ihr könnt Notizen schreiben, Linklisten führen oder einfach Emotes spammen.

			Im anderen Sprachkanal "Bewertung" findet eure Jury-Session statt. Dort wird die Jury euch besuchen kommen.
			Sobald ihr bereit seid, tretet einfach diesem Sprachkanal bei oder nutzt den Bot-Command '!ready', um euch verschieben zu lassen, falls ihr im Team-Sprachkanal seid.

			Bei Fragen oder Problemen könnt ihr den <#%d>-Channel nutzen. Dort findet ihr auch ein FAQ.

			Viel Spaß und Erfolg wünscht euch das Team der FLL München!""".formatted(Constants.SUPPORT_CHANNEL);

	private List<Team> teams;

	public void createAllTeamRoles(JDA jda) {
		Guild guid = jda.getGuildById(Constants.GUILD_ID);
		Role teamBaseRole = guid.getRoleById(Constants.TEAM_ROLE_ID);
		Objects.requireNonNull(guid);
		Objects.requireNonNull(teamBaseRole);
		for (Team team : teams) {
			guid.createCopyOfRole(teamBaseRole)
					.setName(team.getName())
					.setColor((Integer) null)
					.queue(role -> team.setRoleID(role.getIdLong()));
		}
	}

	//TODO save team data after creation is done
	public void createAllTeamareas(JDA jda) {
		Guild guild = jda.getGuildById(Constants.GUILD_ID);
		Category teamarea = jda.getCategoryById(Constants.TEAMAREA_CATEGORY_ID);
		Objects.requireNonNull(guild);
		Objects.requireNonNull(teamarea);

		int teamsSize = teams.size();

		var allFutures = new CompletableFuture[teamsSize];

		teams.sort(Comparator.comparingInt(Team::getHotID));
		for (int i = 0; i < teamsSize; i++) {
			Team team = teams.get(i);
			allFutures[i] = creatTeamCategory(guild, teamarea, team, i).thenComposeAsync(category -> {
				team.setCategoryID(category.getIdLong());

				return CompletableFuture.allOf(createTeamTextChannel(category, team),
						createTeamPrivateVoiceChannel(category, team),
						createTeamEvaluationVoiceChannel(category, team));

			});
		}

		CompletableFuture.allOf(allFutures)
				.thenAcceptAsync(v -> save())
				.thenComposeAsync(v -> guild.getTextChannelById(Constants.LOG_CHANNEL_ID)
						.sendMessage("All Teamareas created successfully.").submit());
	}

	private CompletableFuture<Category> creatTeamCategory(Guild guild, Category teamarea, Team team, int offset) {
		return setEveryonePermission(guild.createCategory(team.toString()))
				.addRolePermissionOverride(team.getRoleID(), PERMISSIONS_VIEW_TEXT_AND_VOICE_CHANNEL, Collections.emptySet())
				.setPosition(teamarea.getPosition() + offset + 1) //+1 for Game-Category (may change later)
				.submit();
	}

	private CompletableFuture<?> createTeamTextChannel(Category teamCategory, Team team) {
		return setPermissions(teamCategory.createTextChannel("\uD83D\uDCAC┇team-textchat"), team, false)
				.setTopic(TEAM_CHANNEL_TOPIC_TEXT)
				.submit().thenComposeAsync(channel -> {
					team.setTextChannelID(channel.getIdLong());
					return channel.sendMessage(createWelcomeTeamText(team)).submit();
				});
	}

	private CompletableFuture<?> createTeamPrivateVoiceChannel(Category teamCategory, Team team) {
		return setPermissions(teamCategory.createVoiceChannel("\uD83D\uDD0A┇Team Voicechat"), team, false)
				.submit().thenAccept(channel -> team.setVoiceChannelID(channel.getIdLong()));
	}

	private CompletableFuture<?> createTeamEvaluationVoiceChannel(Category teamCategory, Team team) {
		return setPermissions(teamCategory.createVoiceChannel("\uD83D\uDCCB┇Bewertung"), team, true)
				.submit().thenAccept(channel -> team.setEvaluationChannelID(channel.getIdLong()));
	}

	/**
	 * Updates the welcome text which was sent to every channel of every team.
	 */
	public void updateAllWelcomeMessages(JDA jda) {
		Guild guild = jda.getGuildById(Constants.GUILD_ID);
		Objects.requireNonNull(jda);
		for (Team team : teams) {
			guild.getTextChannelById(team.getTextChannelID()).getHistoryFromBeginning(1).queue(messageHistory -> {
				if (messageHistory.getRetrievedHistory().isEmpty())
					return;
				messageHistory.getRetrievedHistory().get(0).editMessage(createWelcomeTeamText(team)).queue();
			});
		}
	}

	private static String createWelcomeTeamText(Team team) {
		return String.format(TEAM_WELCOME_TEXT, team.getName());
	}

	private <T extends GuildChannel> ChannelAction<T> setPermissions(ChannelAction<T> channelAction, Team team, boolean volunteerAccess) {
		ChannelAction<T> channel = setEveryonePermission(channelAction)
				.addRolePermissionOverride(team.getRoleID(), PERMISSIONS_VIEW_TEXT_AND_VOICE_CHANNEL, Collections.emptySet());
		if (volunteerAccess)
			channel = channel.addRolePermissionOverride(Constants.VOLUNTEER_ROLE_ID, PERMISSIONS_VIEW_TEXT_AND_VOICE_CHANNEL, Collections.emptySet());
		return channel;
	}

	private <T extends GuildChannel> ChannelAction<T> setEveryonePermission(ChannelAction<T> channelAction) {
		return channelAction.addRolePermissionOverride(channelAction.getGuild().getPublicRole().getIdLong(), Collections.emptySet(), PERMISSIONS_VIEW_TEXT_AND_VOICE_CHANNEL);
	}

	public void removeAllTeamareas(JDA jda) {
		Guild guild = jda.getGuildById(Constants.GUILD_ID);
		Objects.requireNonNull(guild);
		for (Team team : teams) {
			if (team.getCategoryID() <= 0)
				continue;
			guild.getCategoryById(team.getCategoryID()).delete().queue();
			guild.getTextChannelById(team.getTextChannelID()).delete().queue();
			guild.getVoiceChannelById(team.getVoiceChannelID()).delete().queue();
			guild.getVoiceChannelById(team.getEvaluationChannelID()).delete().queue();

			team.setCategoryID(-1);
			team.setTextChannelID(-1);
			team.setVoiceChannelID(-1);
			team.setEvaluationChannelID(-1);
		}
		save();
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
		if (teams.isEmpty()) //Falback: Read Teams from HoT-Export
			teams = new CsvTeamImporter("./myregions.csv").importTeams();
	}
}
