package de.fll.regiom.commands;

import de.fll.regiom.pizza.OrderManager;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PizzaCommand implements Command {

	private static final long channelID = 798356509583081513L;
	private static OrderManager manager;

	/**
	 * creates a PizzaOrder of the Pizza minigame and registers it to the system.
	 *
	 * @param command a String beginning with "order pizza" and containg the names of pizza ingredients, separated either by ';' or by ' '
	 */
	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, String command) {
		MessageChannel channel = event.getGuild().getTextChannelById(channelID);
		if (channel == null) {
			sendFailureMessage(event.getChannel(), 2);
			return false;
		}
		if (manager == null)
			manager = new OrderManager(channel);

		if (!command.matches("[A-Za-zäüöß.,:;()& ]+")) {
			sendFailureMessage(channel, 3);
			return false;
		}
		if (command.matches(".*f.*r.*i.*d.*o.*l.*i.*n.*")) {
			sendFailureMessage(channel, 1);
			return false;
		}

		int prefixLength = "order pizza".length();
		manager.registerOrder(event.getAuthor().getIdLong(), command.substring(prefixLength).trim());
		channel.sendMessage("Du hast eine Pizza bestellt!").queue();
		return true;
	}

	private void sendFailureMessage(MessageChannel channel, int reasonCode) {
		String reason = switch (reasonCode) {
			case 1 -> "Is there anything left to say?";
			case 2 -> "Chosen channel doesn't exist! Please let this be fixed by your developers";
			case 3 -> "Illegal characters! Allowed are: [A-Z] , [a-z] , [ß.,:;()& öäü]";
			default -> "Invalid order message!";
		};
		channel.sendMessage("Failed to make order: " + reason).queue();
	}

	@Override
	public boolean isCalled(String command) {
		return command.startsWith("order pizza");
	}

	@Override
	public String getInfo() {
		return "**order pizza** ingredients\n\tIn unser eigenen Pizzaria Discordio wird dir eine leckere Pizza zubereitet und von unserem Lieferdienst ausgeliefert. Guten Appetit!";
	}

	private static Set<Character> generateAllowed() {
		Set<Character> allowed = new HashSet<>();
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String special = "ß.,:;()& öäü";
		List<String> allowedStrings = List.of(alphabet, alphabet.toLowerCase(), special);
		for (String s : allowedStrings) {
			char[] chars = s.toCharArray();
			for (char c : chars)
				allowed.add(c);
		}
		return allowed;
	}
}
