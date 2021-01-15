package de.fll.regiom.listeners;

import de.fll.regiom.commands.Command;
import de.fll.regiom.commands.EvaluationSheetMessageCommand;
import de.fll.regiom.commands.PizzaCommand;
import de.fll.regiom.commands.PurgeCommand;
import de.fll.regiom.commands.RebootCommand;
import de.fll.regiom.commands.SaveCommand;
import de.fll.regiom.commands.StartRobotGameCommand;
import de.fll.regiom.model.Constants;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Set;

public class ChatCommandListener extends ListenerAdapter {

	private static final String PREFIX = "!";

	//TODO find better way for this!
	private final Set<Command> commands = Set.of(
			//new CreateStructureCommand(),
			new StartRobotGameCommand(),
			new EvaluationSheetMessageCommand(),
			new PurgeCommand(),
			new SaveCommand(),
			new RebootCommand(),
			new PizzaCommand()
	);

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (event.isFromType(ChannelType.PRIVATE)) {
			System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
					event.getMessage().getContentDisplay());
		} else {
			System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
					event.getTextChannel().getName(), event.getMember().getEffectiveName(),
					event.getMessage().getContentDisplay());
		}

		String msg = event.getMessage().getContentRaw().toLowerCase(Locale.ROOT).trim();

		if (!msg.startsWith(PREFIX))
			return;
		msg = msg.substring(PREFIX.length());

		Member member = event.getMember();
		if (member == null) {
			member = event.getJDA().getGuildById(Constants.GUILD_ID).getMember(event.getAuthor());
		}

		for (Command command : commands) {
			if (command.shouldExecute(member, msg)) {
				command.execute(event, msg);
			}
		}
		//TODO !join teamid
	}
}
