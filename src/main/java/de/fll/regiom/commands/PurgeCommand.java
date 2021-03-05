package de.fll.regiom.commands;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class PurgeCommand implements Command {

	private static final String COMMAND = "purge";

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command) {
		if (!event.isFromType(ChannelType.TEXT))
			return false;

		int numMessages = Integer.parseInt(command.substring(COMMAND.length() + 1)); //+1 for space
		if (numMessages <= 0)
			return false;
		else if (numMessages > 100)
			numMessages = 100;

		TextChannel channel = event.getTextChannel();
		String successText = "Removed " + numMessages + " messages.";

		channel.getHistory().retrievePast(numMessages).queue(messages ->
				channel.deleteMessages(messages).queue(v ->
						channel.sendMessage(successText).queue()));
		return true;
	}

	@Override
	public boolean canBeCalledBy(@NotNull Member member) {
		return RoleHelper.isAdmin(member);
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return command.matches(COMMAND + " \\d+");
	}

	@Override
	public String getInfo() {
		return "**" + COMMAND + "** amount\n\tLöscht die letzten n Nachrichten in diesem Channel. **Admin only**";
	}
}
