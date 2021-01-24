package de.fll.regiom.commands;

import de.fll.regiom.controller.EvaluationSheetMessageSender;
import de.fll.regiom.controller.TeamManager;
import de.fll.regiom.model.Team;
import de.fll.regiom.model.evaluation.CoreValueEvaluationSheet;
import de.fll.regiom.model.evaluation.ResearchEvaluationSheet;
import de.fll.regiom.model.evaluation.RobotDesignEvaluationSheet;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class EvaluationSheetMessageCommand implements Command {

	private static final Random random = new Random();
	private static final EvaluationSheetMessageSender sheetMessageSender = new EvaluationSheetMessageSender();

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, String command) {
		Team randomTeam = getRandomTeam();
		switch (command) {
			case "robotdesign" -> sheetMessageSender.sendSheet(event.getChannel(), new RobotDesignEvaluationSheet(randomTeam));
			case "research" -> sheetMessageSender.sendSheet(event.getChannel(), new ResearchEvaluationSheet(randomTeam));
			case "corevalues" -> sheetMessageSender.sendSheet(event.getChannel(), new CoreValueEvaluationSheet(randomTeam));
			default -> {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canBeCalledBy(Member member) {
		return RoleHelper.isAdmin(member);
	}

	@Override
	public boolean isCalled(String command) {
		return Set.of("robotdesign", "research", "corevalues").stream().anyMatch(command::startsWith);
	}

	@Override
	public String getInfo() {
		return "**robotdesign | research | corevalues**\n\tSendet einen Bewertungsbogen in den Channel.\n\tNutzt diesen, um eure Gedanken festzuhalten und um euch abzustimmen. **Judge only**";
	}

	Team getRandomTeam() {
		List<Team> teams = TeamManager.INSTANCE.getTeams();
		return teams.get(random.nextInt(teams.size()));
	}
}
