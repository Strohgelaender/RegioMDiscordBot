package de.fll.regiom.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;

public interface Command {

	boolean execute(@NotNull MessageReceivedEvent event, String command);

	@CheckReturnValue
	default boolean shouldExecute(Member member, String command) {
		return canBeCalledBy(member) && isCalled(command);
	}

	@CheckReturnValue
	default boolean canBeCalledBy(Member member) {
		return true;
	}

	@CheckReturnValue
	boolean isCalled(String command);

}
