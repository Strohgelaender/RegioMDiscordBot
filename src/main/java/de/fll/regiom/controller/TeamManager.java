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
			Liebes Team *%s*

			dieser Channel ist privat und nur für euch!
			Keine Juroren können lesen, was ihr hier schreibt oder hören, was ihr in eurem Sprachkanal besprecht.
			Nutzt diesen Channel für alles, was gerade anfällt. Ihr könnt Notizen schreiben, Linklisten führen oder einfach Emotes spammen.
						
			Im anderen Sprachkanal "Bewertung" findet eure Jury-Session statt. Dort wird die Jury euch besuchen kommen.
			Sobald ihr bereit seid, tretet einfach diesem Sprachkanal bei oder nutzt den Bot-Command '!ready', um euch verschieben zu lassen, falls ihr im Team-Sprachkanal seid.
						
			Viel Spaß und Erfolg wünscht euch das Team der FLL München!""";

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

		teams.stream().sorted(Comparator.comparingInt(Team::getHotID)).forEachOrdered(team -> {
			Category category = creatTeamCategory(guild, teamarea, team);
			team.setCategoryID(category.getIdLong());

			createTeamTextChannel(category, team);
			createTeamPrivateVoiceChannel(category, team);
			createTeamEvaluationVoiceChannel(category, team);
		});
	}

	private Category creatTeamCategory(Guild guild, Category teamarea, Team team) {
		return setEveryonePermission(guild.createCategory(team.toString()))
				.addRolePermissionOverride(team.getRoleID(), PERMISSIONS_VIEW_TEXT_AND_VOICE_CHANNEL, Collections.emptySet())
				.setPosition(teamarea.getPosition() + 1)
				.complete();
	}

	private void createTeamTextChannel(Category teamCategory, Team team) {
		setPermissions(teamCategory.createTextChannel("\uD83D\uDCAC┇team-textchat"), team, false)
				.setTopic(TEAM_CHANNEL_TOPIC_TEXT)
				.queue(channel -> {
					team.setTextChannelID(channel.getIdLong());
					channel.sendMessage(String.format(TEAM_WELCOME_TEXT, team.getName())).queue();
				});
	}

	private void createTeamPrivateVoiceChannel(Category teamCategory, Team team) {
		setPermissions(teamCategory.createVoiceChannel("\uD83D\uDD0A┇Team Voicechat"), team, false)
				.queue(channel -> team.setVoiceChannelID(channel.getIdLong()));
	}

	private void createTeamEvaluationVoiceChannel(Category teamCategory, Team team) {
		setPermissions(teamCategory.createVoiceChannel("\uD83D\uDCCB┇Bewertung"), team, true)
				.queue(channel -> team.setEvaluationChannelID(channel.getIdLong()));
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
		}
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
