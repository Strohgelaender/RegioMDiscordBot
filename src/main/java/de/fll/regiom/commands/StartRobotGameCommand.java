package de.fll.regiom.commands;

import de.fll.regiom.controller.RobotGameStartHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class StartRobotGameCommand implements Command {

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, String command) {
		return RobotGameStartHandler.getInstance().startRobotGame(event.getMember(), event.getChannel());
	}

	@Override
	public boolean canBeCalledBy(Member member) {
		return RoleHelper.isTeam(member);
	}

	@Override
	public boolean isCalled(String command) {
		return Set.of("startgame", "startrobotgame").stream().anyMatch(command::startsWith);
	}
}
