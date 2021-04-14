package de.fll.regiom.commands;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Stream;

public class HelpCommand implements Command {

	private final Set<Command> commands;

	public HelpCommand(Set<Command> commands) {
		this.commands = commands;
	}

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String msg) {
		MessageBuilder builder = new MessageBuilder();
		Member member = RoleHelper.toMember(event.getAuthor());

		builder.append("Der Regio-Bot verfügt über folgende Befehle:\n\n");
		for (Command command : commands) {
			if (command.canBeCalledBy(member)) {
				String info = command.getInfo();
				if (info != null) {
					builder.append(command.getInfo()).append("\n");
				}
			}
		}
		builder.append("\nIrgendwas klappt nicht? Du hast einen Verbesserungsvorschlag oder eine neue Idee? Schreib einfach einen der Admins an.");
		event.getAuthor().openPrivateChannel().queue(channel -> {
			channel.sendMessage(builder.build()).queue();
		});
		return true;
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return Stream.of("help", "hilfe").anyMatch(command::startsWith);
	}

	@Override
	public String getInfo() {
		return "**help**\n\tDiese Nachricht";
	}
}
