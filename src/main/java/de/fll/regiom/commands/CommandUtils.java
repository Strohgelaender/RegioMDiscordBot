package de.fll.regiom.commands;

import net.dv8tion.jda.api.entities.Message;

import java.util.concurrent.CompletableFuture;

public final class CommandUtils {

	private CommandUtils() {
	}

	public static CompletableFuture<?> reactWithCheckbox(Message message) {
		return message.addReaction("✔").submit();
	}

}
