package de.fll.regiom.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RebootCommand implements Command {

	private static final File BOT_FILE = new File("RegioMDiscordBot-1.0-jar-with-dependencies.jar");

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, String command) {
		if (!BOT_FILE.exists())
			return false;

		if (new SaveCommand().execute(event, command)) {
			try {
				new ProcessBuilder(getStartCommand()).inheritIO().start();
				System.exit(0);
			} catch (IOException e) {
				event.getChannel().sendMessage("Failed to restart: " + e.getClass().getSimpleName()).queue();
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	private List<String> getStartCommand() {
		return List.of("java", "-jar", BOT_FILE.getName());
	}

	@Override
	public boolean canBeCalledBy(Member member) {
		return member.getIdLong() == 138584634710687745L;
	}

	@Override
	public boolean isCalled(String command) {
		return command.equals("restart") || command.equals("reboot");
	}
}
