package de.fll.regiom.commands;

import de.fll.regiom.controller.TeamRepository;
import de.fll.regiom.listeners.ChatCommandListener;
import de.fll.regiom.model.members.Team;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public class SendToTeamCommand implements Command {

	private static final String COMMAND_NAME = "sendtoteam";

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command) {
		String hotID = command.substring(COMMAND_NAME.length()).strip().substring(0, 4);
		try {
			Team team = TeamRepository.INSTANCE.getTeamByHotId(Integer.parseInt(hotID)).orElseThrow();
			String message = event.getMessage().getContentRaw().strip()
					.substring(ChatCommandListener.getPREFIX().length() + COMMAND_NAME.length() + 5).strip();
			message = CommandUtils.replaceTeamTags(message, team, event.getJDA());
			event.getGuild().getTextChannelById(team.getTextChannelID()).sendMessage(message).queue();
			return true;
		} catch (NumberFormatException | NoSuchElementException | NullPointerException e) {
			event.getChannel().sendMessage("Could not find the text channel of the team with ID " + hotID).queue();
			return false;
		}
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return command.strip().startsWith(COMMAND_NAME);
	}

	@Override
	public boolean canBeCalledBy(@NotNull Member member) {
		return RoleHelper.isVolunteer(member);
	}

	@Override
	public @Nullable String getInfo() {
		return "sendtoteam <hotID> schickt die Nachricht nach dem Befehl an das Team.";
	}
}
