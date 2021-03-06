package de.fll.regiom.commands;

import de.fll.regiom.controller.RobotGameTokenRepository;
import de.fll.regiom.model.RobotGameAttempt;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class SearchTokenCommand implements Command {

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command) {
		String[] parts = command.split(" ");
		if (parts.length < 2)
			return false;

		String token;
		if (parts[1].length() == RobotGameTokenRepository.CODE_LENGTH)
			token = parts[1].toUpperCase(Locale.ROOT);
		else {
			if (parts.length < 3)
				return false;
			token = parts[2].toUpperCase(Locale.ROOT);
		}

		RobotGameAttempt attempt = RobotGameTokenRepository.INSTANCE.findByToken(token);
		if (attempt == null)
			return false;

		event.getChannel().sendMessage(RobotGameTokenRepository.INSTANCE.formatMessage(attempt)).queue();
		return true;
	}

	@Override
	public boolean canBeCalledBy(@NotNull Member member) {
		return RoleHelper.isAdmin(member) || RoleHelper.isReferee(member);
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return command.matches(String.format("((search|find)? ?(token|game)) [A-Za-z]{%d}", RobotGameTokenRepository.CODE_LENGTH));
	}

	@Override
	public String getInfo() {
		return "**search token** token\n\tHiermit gibt es alle Informationen über das angegbene RobotGameToken **Referee only**";
	}
}
