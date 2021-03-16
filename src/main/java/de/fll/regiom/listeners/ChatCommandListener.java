package de.fll.regiom.listeners;

import de.fll.regiom.commands.Command;
import de.fll.regiom.commands.HelpCommand;
import de.fll.regiom.commands.RoleHelper;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatCommandListener extends ListenerAdapter {

	private static final String PREFIX = "!";

	private final Set<Command> commands = setupCommands();

	@NotNull
	private static Set<Command> setupCommands() {
		Reflections reflections = new Reflections("de.fll.regiom.commands");
		return reflections.getSubTypesOf(Command.class).stream()
				//remove abstract classes
				.filter(command -> !Modifier.isAbstract(command.getModifiers()) && !Modifier.isInterface(command.getModifiers()))
				//make sure the default constructor exists (to remove classes like HelpCommand)
				.filter(command -> Stream.of(command.getDeclaredConstructors()).anyMatch(constructor -> constructor.getParameterCount() == 0))
				//create a new instance using this constructor
				.map(commandClass -> {
					try {
						return commandClass.getDeclaredConstructor().newInstance();
					} catch (ReflectiveOperationException e) {
						e.printStackTrace();
						return null;
					}
				})
				//everything should have worked fine.
				//If not, we should fix this bug! But to continue here we just remove this null-Object.
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	ChatCommandListener() {
		commands.add(new HelpCommand(commands));
	}

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (event.isFromType(ChannelType.PRIVATE)) {
			System.out.printf("[PM] %s: %s%n", event.getAuthor().getName(),
					event.getMessage().getContentDisplay());
		} else {
			System.out.printf("[%s][%s] %s: %s%n", event.getGuild().getName(),
					event.getTextChannel().getName(), event.getMember().getEffectiveName(),
					event.getMessage().getContentDisplay());
		}

		if (event.getAuthor().isBot())
			return;

		String msg = event.getMessage().getContentRaw().toLowerCase(Locale.ROOT).strip();

		if (!msg.startsWith(PREFIX))
			return;
		msg = msg.substring(PREFIX.length());

		Member member = event.getMember();
		if (member == null) {
			//DM -> retrieve Member-Information from Server
			member = RoleHelper.toMember(event.getAuthor());
			if (member == null)
				//DM from User who is not on the Server
				//ignore this (this would lead to errors in some modules otherwise)
				return;
		}

		for (Command command : commands) {
			if (command.shouldExecute(member, msg)) {
				command.execute(event, msg);
			}
		}
	}
}
