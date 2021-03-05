package de.fll.regiom.commands.riddleCommands;

import de.fll.regiom.controller.GameController;
import de.fll.regiom.model.game.Riddle;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class RiddleSolveCommand extends RiddleCommand {

	private static final String COMMAND = RIDDLE_PREFIX + "solve ";

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, String command) {
		GameController.GameProgressState state = stateFromMember(event);
		Riddle riddle = state.getActualRiddle();
		if (riddle == null) {
			event.getChannel().sendMessage("Es gibt keine Lösung für dieses \"Rätsel\"").queue();
			return true;
		}
		String solution = command.substring(COMMAND.length()).strip();
		boolean correct = riddle.checkSolution(solution);
		if (correct)
			state.makeProgress();
		String message = "Eure Lösung " + solution + " ist" + (correct ? " richtig!" : " leider falsch!");
		event.getChannel().sendMessage(message).queue();
		return true;
	}

	@Override
	public boolean isCalled(String command) {
		return command.startsWith(COMMAND);
	}

	@Override
	public String getInfo() {
		return "Befehl, um Lösungen des Rätsels auszuprobieren";
	}
}
