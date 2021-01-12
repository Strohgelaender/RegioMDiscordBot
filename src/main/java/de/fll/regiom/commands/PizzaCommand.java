package de.fll.regiom.commands;

import de.fll.regiom.pizza.OrderManager;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class PizzaCommand implements Command {

	private static final long channelID = 798356509583081513L;
	private OrderManager manager;

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, String command) {
		MessageChannel channel = event.getGuild().getTextChannelById(channelID);
		if (channel == null)
			return false;
		if (manager == null)
			manager = new OrderManager(channel);
		manager.registerOrder(event.getAuthor().getIdLong(), command.substring("order pizza".length()).trim());
		channel.sendMessage("Du hast eine Pizza bestellt!").queue();
		return true;
	}

	@Override
	public boolean isCalled(String command) {
		return command.startsWith("order pizza");
	}
}
