package de.fll.regiom.commands;

import de.fll.regiom.pizza.OrderManager;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class PizzaCommand implements Command {

	private static final long channelID = 798356509583081513L;
	private OrderManager manager;

	/**
	 * creates a PizzaOrder of the Pizza minigame and registers it to the system.
	 *
	 * @param command a String beginning with "order pizza" and containg the names of pizza ingredients, separated either by ';' or by ' '
	 */
	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, String command) {
		MessageChannel channel = event.getGuild().getTextChannelById(channelID);
		if (channel == null)
			return false;
		if (manager == null)
			manager = new OrderManager(channel);
		Set<Character> forbidden = Set.of('f', 'r', 'i', 'd', 'o', 'l', 'n');
		for (int i = "order pizza".length(); i < command.length(); i++) {
			String check = command.substring(i, i + 8);
			int sum = 0;
			for (Character c : forbidden) {
				if (check.contains(String.valueOf(c)))
					sum++;
			}
			if (sum >= 5)
				return false;
		}
		manager.registerOrder(event.getAuthor().getIdLong(), command.substring("order pizza".length()).trim());
		channel.sendMessage("Du hast eine Pizza bestellt!").queue();
		return true;
	}

	@Override
	public boolean isCalled(String command) {
		return command.startsWith("order pizza");
	}
}
