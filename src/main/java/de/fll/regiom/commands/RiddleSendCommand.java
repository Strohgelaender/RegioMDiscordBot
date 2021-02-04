package de.fll.regiom.commands;

import de.fll.regiom.controller.GameController;
import de.fll.regiom.controller.TeamRepository;
import de.fll.regiom.game.Riddle;
import de.fll.regiom.model.Team;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RiddleSendCommand implements Command {

	private static final String COMMAND = "currentriddle";

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, String command) {
		Member member = RoleHelper.toMember(event.getAuthor());
		if (member == null)
			return false;
		Optional<Team> team = TeamRepository.INSTANCE.getTeamByMember(member);
		if (team.isEmpty()) {
			event.getChannel().sendMessage("Du bist kein Teammitglied und nimmst nicht am Rätselspiel teil").queue();
			return true;
		}
		Riddle riddle = GameController.INSTANCE.getRiddleOfTeam(team.get());
		if (riddle == null) {
			event.getChannel().sendMessage("Glückwunsch! Ihr seid schon mit dem Rätsel fertig!").queue();
			return true;
		}
		event.getChannel().sendMessage(riddle.getMessage()).queue();
		return true;
	}

	@Override
	public boolean isCalled(String command) {
		return command.startsWith(COMMAND);
	}

	@Override
	public String getInfo() {
		return "Zeigt euch euer aktuelles Rätsel noch einmal an";
	}
}
