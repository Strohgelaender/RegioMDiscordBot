package de.fll.regiom.commands;

import de.fll.regiom.model.members.Team;
import de.fll.regiom.util.CompletableFutureUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class CommandUtils {

	private CommandUtils() {
	}

	@NotNull
	public static CompletableFuture<?> reactWithCheckbox(@NotNull Message message) {
		return message.addReaction("✔").submit();
	}

	@NotNull
	public static String replaceTeamTags(@NotNull String message, @NotNull Team targetTeam, @NotNull JDA jda) {
		Role role = jda.getRoleById(targetTeam.getRoleID());
		String roleMention = role == null ? targetTeam.getName() : role.getAsMention();
		return message.replaceAll("<TeamName>", roleMention);
	}

	@NotNull
	public static String getTeamTag(Team team) {
		return "[" + team.getHotID() + "]";
	}

	@NotNull
	public static String getTaggedTeam(Team team) {
		return getTeamTag(team) + " **" + team.getName() + "**";
	}

	@NotNull
	public static CompletableFuture<?> sendDuplicatedMessageWithFiles(@NotNull Message originalMessage, @NotNull String content, @NotNull TextChannel textChannel) {
		String message = content.isEmpty() ? "Fileupload:" : content;
		MessageAction action = textChannel.sendMessage(message);

		var attachments = originalMessage.getAttachments();
		return CompletableFutureUtil.combineAllFutures(attachments, attachment ->
				attachment.retrieveInputStream()
						.thenAccept(in -> action.addFile(in, attachment.getFileName()))
		).thenCompose(v -> action.submit());
	}

	@NotNull
	public static CompletableFuture<?> sendDuplicatedMessageWithFiles(@NotNull Message originalMessage, @NotNull TextChannel textChannel) {
		return sendDuplicatedMessageWithFiles(originalMessage, originalMessage.getContentDisplay(), textChannel);
	}

}


