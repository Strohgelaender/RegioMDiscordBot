package de.fll.regiom.commands.riddleCommands;

import de.fll.regiom.model.game.Riddle;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class RiddleSendCommand extends RiddleCommand {

	private static final String COMMAND = RIDDLE_PREFIX + "current";

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command) {
		Riddle riddle = stateFromMember(event).getActualRiddle();
		if (riddle == null) {
			event.getChannel().sendMessage("Glückwunsch! Ihr seid schon mit dem Rätsel fertig!").queue();
			return true;
		}
		event.getChannel().sendMessage(riddle.getMessage()).queue();
		return true;
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return command.startsWith(COMMAND);
	}

	@Override
	public String getInfo() {
		return "Zeigt euch euer aktuelles Rätsel noch einmal an";
	}
}
