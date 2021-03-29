package de.fll.regiom.controller;

import de.fll.regiom.model.Constants;
import de.fll.regiom.model.Team;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public enum DiscordTeamStructureManager {
	INSTANCE;

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

	public void createAllTeamRoles(JDA jda, List<Team> teams) {
		Guild guid = jda.getGuildById(Constants.GUILD_ID);
		Objects.requireNonNull(guid);
		Role teamBaseRole = guid.getRoleById(Constants.TEAM_ROLE_ID);
		Objects.requireNonNull(teamBaseRole);
		for (Team team : teams) {
			createTeamRole(guid, team, teamBaseRole);
		}
	}

	@NotNull
	public CompletableFuture<?> createAllTeamareas(@NotNull JDA jda, @NotNull List<Team> teams) {
		Guild guild = jda.getGuildById(Constants.GUILD_ID);
		Category teamarea = jda.getCategoryById(Constants.TEAMAREA_CATEGORY_ID);
		Objects.requireNonNull(guild);
		Objects.requireNonNull(teamarea);

		int teamsSize = teams.size();
		var allFutures = new CompletableFuture[teamsSize];

		teams.sort(Comparator.comparingInt(Team::getHotID));
		for (int i = 0; i < teamsSize; i++) {
			allFutures[i] = createTeamArea(guild, teamarea, teams.get(i), i);
		}

		return CompletableFuture.allOf(allFutures)
				.thenComposeAsync(v -> guild.getTextChannelById(Constants.LOG_CHANNEL_ID)
						.sendMessage("All Teamareas created successfully.").submit());
	}

	/**
	 * Updates the welcome text which was sent to every channel of every team.
	 */
	public void updateAllWelcomeMessages(@NotNull JDA jda, @NotNull List<Team> teams) {
		Guild guild = jda.getGuildById(Constants.GUILD_ID);
		Objects.requireNonNull(guild);
		for (Team team : teams) {
			if (team.getTextChannelID() <= 0)
				continue;

			var channel = guild.getTextChannelById(team.getTextChannelID());
			if (channel == null)
				continue;

			channel.getHistoryFromBeginning(1).queue(messageHistory -> {
				if (messageHistory.getRetrievedHistory().isEmpty())
					return;
				messageHistory.getRetrievedHistory().get(0).editMessage(createWelcomeTeamText(team, channel)).queue();
			});
		}
	}

	public void removeAllTeamareas(@NotNull JDA jda, @NotNull List<Team> teams) {
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
	}

	public void createTeamRole(@NotNull Guild guild, @NotNull Team team, @NotNull Role teamBaseRole) {
		guild.createCopyOfRole(teamBaseRole)
				.setName(team.getName())
				.setColor((Integer) null)
				.queue(role -> team.setRoleID(role.getIdLong()));
	}

	@NotNull
	public CompletableFuture<?> createTeamArea(@NotNull Guild guild, @NotNull Category teamarea, @NotNull Team team, int i) {
		return creatTeamCategory(guild, teamarea, team, i).thenComposeAsync(category -> {
			team.setCategoryID(category.getIdLong());
			return CompletableFuture.allOf(createTeamTextChannel(category, team),
					createTeamPrivateVoiceChannel(category, team),
					createTeamEvaluationVoiceChannel(category, team));

		});
	}

	@NotNull
	private CompletableFuture<Category> creatTeamCategory(@NotNull Guild guild, @NotNull Category teamarea, @NotNull Team team, int offset) {
		return setEveryonePermission(guild.createCategory(team.toString()))
				.addRolePermissionOverride(team.getRoleID(), PERMISSIONS_VIEW_TEXT_AND_VOICE_CHANNEL, Collections.emptySet())
				.setPosition(teamarea.getPosition() + offset + 1) //+1 for Game-Category (may change later)
				.submit();
	}

	@NotNull
	private CompletableFuture<?> createTeamTextChannel(@NotNull Category teamCategory, @NotNull Team team) {
		return setPermissions(teamCategory.createTextChannel("\uD83D\uDCAC┇team-textchat"), team, false)
				.setTopic(TEAM_CHANNEL_TOPIC_TEXT)
				.submit().thenComposeAsync(channel -> {
					team.setTextChannelID(channel.getIdLong());
					return channel.sendMessage(createWelcomeTeamText(team, channel)).submit();
				});
	}

	@NotNull
	private CompletableFuture<?> createTeamPrivateVoiceChannel(@NotNull Category teamCategory, @NotNull Team team) {
		return setPermissions(teamCategory.createVoiceChannel("\uD83D\uDD0A┇Team Voicechat"), team, false)
				.submit().thenAccept(channel -> team.setVoiceChannelID(channel.getIdLong()));
	}

	@NotNull
	private CompletableFuture<?> createTeamEvaluationVoiceChannel(@NotNull Category teamCategory, @NotNull Team team) {
		return setPermissions(teamCategory.createVoiceChannel("\uD83D\uDCCB┇Bewertung"), team, true)
				.submit().thenAccept(channel -> team.setEvaluationChannelID(channel.getIdLong()));
	}

	@NotNull
	private String createWelcomeTeamText(@NotNull Team team, @NotNull TextChannel channel) {
		Role role = channel.getJDA().getRoleById(team.getRoleID());
		String roleMention = role == null ? team.getName() : role.getAsMention();
		return String.format(TEAM_WELCOME_TEXT, roleMention);
	}

	@NotNull
	private <T extends GuildChannel> ChannelAction<T> setPermissions(@NotNull ChannelAction<T> channelAction, @NotNull Team team, boolean volunteerAccess) {
		ChannelAction<T> channel = setEveryonePermission(channelAction)
				.addRolePermissionOverride(team.getRoleID(), PERMISSIONS_VIEW_TEXT_AND_VOICE_CHANNEL, Collections.emptySet());
		if (volunteerAccess)
			channel = channel.addRolePermissionOverride(Constants.VOLUNTEER_ROLE_ID, PERMISSIONS_VIEW_TEXT_AND_VOICE_CHANNEL, Collections.emptySet());
		return channel;
	}

	@NotNull
	private <T extends GuildChannel> ChannelAction<T> setEveryonePermission(@NotNull ChannelAction<T> channelAction) {
		return channelAction.addRolePermissionOverride(channelAction.getGuild().getPublicRole().getIdLong(), Collections.emptySet(), PERMISSIONS_VIEW_TEXT_AND_VOICE_CHANNEL);
	}
}
