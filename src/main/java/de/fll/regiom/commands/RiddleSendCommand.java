package de.fll.regiom.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class RiddleSendCommand implements Command {

	//TODO: CONNECT TO GAME_CONTROLLER AND PRINT THE TEAMS ACTUAL RIDDLE

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, String command) {
		return false;
	}

	@Override
	public boolean isCalled(String command) {
		return false;
	}

	@Override
	public String getInfo() {
		return "Zeigt euch euer aktuelles Rätsel noch einmal an";
	}
}
