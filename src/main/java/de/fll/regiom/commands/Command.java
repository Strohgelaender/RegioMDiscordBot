package de.fll.regiom.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.CheckReturnValue;

public interface Command {

	/**
	 * Performs the main functionality of the command.
	 *
	 * @param event   the MessageEvent which is calling the command
	 * @param command the command String without the global command prefix
	 * @return true, if the command was executed successfully, otherwise false
	 **/
	boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command);

	/**
	 * Checks if the command should be performed.
	 * By default, it checks if the combination of permission and correct command syntax is given.
	 *
	 * @param command the command String without the global command prefix
	 * @param member  the guild member who is trying to access the command
	 * @return true, if the command should be executed, otherwise false
	 **/
	@CheckReturnValue
	default boolean shouldExecute(@NotNull Member member, @NotNull String command) {
		return canBeCalledBy(member) && isCalled(command);
	}

	/**
	 * Checks if the given user is allowed to use this command.
	 * The default implementation always returns true.
	 *
	 * @param member The guild member who is trying to access the command
	 * @return true, if the user is allowed to perform the command, otherwise false
	 **/
	@CheckReturnValue
	default boolean canBeCalledBy(@NotNull Member member) {
		return true;
	}

	/**
	 * Checks if the command string matches the syntax of this command
	 *
	 * @param command the command String without the global command prefix
	 * @return true, if the given String matches the command syntax, otherwise false
	 **/
	@CheckReturnValue
	boolean isCalled(@NotNull String command);

	/**
	 * Method adding support for global help-issuing commands.
	 *
	 * @return A formatted String which contains helpful information about this command
	 */
	@CheckReturnValue
	@Nullable
	String getInfo();

}
