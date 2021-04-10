package de.fll.regiom.commands;

import de.fll.regiom.model.members.Team;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class CommandUtils {

	private CommandUtils() {
	}

	public static CompletableFuture<?> reactWithCheckbox(Message message) {
		return message.addReaction("✔").submit();
	}

	public static String replaceTeamTags(String message, Team targetTeam, JDA jda) {
		Role role = jda.getRoleById(targetTeam.getRoleID());
		String roleMention = role == null ? targetTeam.getName() : role.getAsMention();
		return message.replaceAll("<TeamName>", roleMention);
	}

	public static String getTeamTag(Team team) {
		return "[" + team.getHotID() + "]";
	}

	public static String getTaggedTeam(Team team) {
		return getTeamTag(team) + " **" + team.getName() + "**";
	}

}


