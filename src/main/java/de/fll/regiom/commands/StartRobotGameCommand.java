package de.fll.regiom.commands;

import de.fll.regiom.controller.RobotGameTokenRepository;
import de.fll.regiom.model.RobotGameAttempt;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static de.fll.regiom.controller.RobotGameTokenRepository.FORMATTER;
import static de.fll.regiom.model.Constants.CODE_LOG_CHANNEL;

@Deprecated
public class StartRobotGameCommand implements Command {

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command) {
		RobotGameAttempt attempt = RobotGameTokenRepository.INSTANCE.startRobotGame(event.getMember());

		String now = attempt.getStartTime().format(FORMATTER);

		event.getChannel().sendMessage("Der Code lautet **" + attempt.getCode() + "**\nViel Erfolg beim Robot Game!\n\uD83D\uDD52 " + now).queue();
		event.getGuild().getTextChannelById(CODE_LOG_CHANNEL).sendMessage(RobotGameTokenRepository.INSTANCE.formatMessage(attempt)).queue();
		return true;
	}

	@Override
	public boolean canBeCalledBy(@NotNull Member member) {
		return RoleHelper.isTeam(member);
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return Stream.of("startgame", "startrobotgame", "robotgame").anyMatch(command::startsWith);
	}

	@Override
	public String getInfo() {
		return "**startgame**\n\tVerwendet diesen Command, um eine neue RobotGame-Runde zu registrieren.\n\tDie Erstellung des Tokens sowie das Token selbst müssen im Video zu sehen sein.";
	}
}
