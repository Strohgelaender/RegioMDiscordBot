package de.fll.regiom.commands;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FeedbackCommand implements Command {

	private static final long FEEDBACK_CHANNEL = 831863646736220200L;

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command) {
		if (!event.isFromType(ChannelType.PRIVATE))
			return false; //This currently cannot be done by isCalled(). TODO change?

		var feedbackChannel = event.getJDA().getTextChannelById(FEEDBACK_CHANNEL);
		if (feedbackChannel == null)
			return false;

		CommandUtils.sendDuplicatedMessageWithFiles(event.getMessage(), feedbackChannel);
		return true;
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return true;
	}

	@Override
	public @Nullable String getInfo() {
		return null;
	}
}
