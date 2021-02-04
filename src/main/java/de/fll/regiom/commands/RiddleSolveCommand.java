package de.fll.regiom.commands;

import de.fll.regiom.controller.GameController;
import de.fll.regiom.controller.TeamRepository;
import de.fll.regiom.game.Riddle;
import de.fll.regiom.model.Team;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class RiddleSolveCommand implements Command {

	private static final String COMMAND = "solveriddle ";

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, String command) {
		Member member = event.getMember();
		if (member == null)
			return false;
		Team team = TeamRepository.INSTANCE.getTeamByMember(member).orElse(null);
		if (team == null) {
			event.getChannel().sendMessage("Du bist kein Teammitglied und nimmst nicht am Rätselspiel teil").queue();
			return true;
		}
		GameController.GameProgressState state = GameController.INSTANCE.getStateOfTeam(team);
		Riddle riddle = state.getActualRiddle();
		if (riddle == null) {
			event.getChannel().sendMessage("Es gibt keine Lösung für dieses \"Rätsel\"").queue();
			return true;
		}
		String solution = command.substring(COMMAND.length());
		if (riddle.checkSolution(solution)) {
			state.makeProgress();
			event.getChannel().sendMessage("Eure Lösung " + solution + " ist richtig!").queue();
		} else {
			event.getChannel().sendMessage("Eure Lösung " + solution + " ist leider falsch!").queue();
		}
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
