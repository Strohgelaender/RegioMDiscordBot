package de.fll.regiom.commands;

import de.fll.regiom.controller.EvaluationSheetMessageSender;
import de.fll.regiom.model.evaluation.CoreValueEvaluationSheet;
import de.fll.regiom.model.evaluation.ResearchEvaluationSheet;
import de.fll.regiom.model.evaluation.RobotDesignEvaluationSheet;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.stream.Stream;

public class EvaluationSheetMessageCommand implements Command {

	private static final Random random = new Random();
	private static final EvaluationSheetMessageSender sheetMessageSender = new EvaluationSheetMessageSender();

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command) {
		switch (command) {
			case "robotdesign" -> sheetMessageSender.sendSheet(event.getChannel(), new RobotDesignEvaluationSheet());
			case "research" -> sheetMessageSender.sendSheet(event.getChannel(), new ResearchEvaluationSheet());
			case "corevalues" -> sheetMessageSender.sendSheet(event.getChannel(), new CoreValueEvaluationSheet());
			default -> {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canBeCalledBy(@NotNull Member member) {
		return RoleHelper.isAdmin(member);
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return Stream.of("robotdesign", "research", "corevalues").anyMatch(command::startsWith);
	}

	@Override
	public String getInfo() {
		return "**robotdesign | research | corevalues**\n\tSendet einen Bewertungsbogen in den Channel.\n\tNutzt diesen, um eure Gedanken festzuhalten und um euch abzustimmen. **Judge only**";
	}
}
