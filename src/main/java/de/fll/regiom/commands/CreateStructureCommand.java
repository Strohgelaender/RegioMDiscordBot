package de.fll.regiom.commands;

import de.fll.regiom.controller.TeamManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class CreateStructureCommand implements Command {

	private static final String COMMAND = "create ";

	private final Map<Set<String>, Consumer<JDA>> actions = Map.of(
			Set.of("roles"), TeamManager.INSTANCE::createAllTeamRoles,
			Set.of("channels", "teamareas"), TeamManager.INSTANCE::createAllTeamareas,
			Set.of("welcometext", "teammessage", "text", "message"), TeamManager.INSTANCE::updateAllWelcomeMessages
	);

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, String command) {
		String request = command.substring(COMMAND.length());
		var action = actions.entrySet().stream()
				.filter(e -> e.getKey().contains(request))
				.limit(1)
				.map(Map.Entry::getValue)
				.findAny();
		if (action.isPresent()) {
			action.get().accept(event.getJDA());
			return true;
		}
		return false;
	}

	@Override
	public boolean canBeCalledBy(Member member) {
		return RoleHelper.isAdmin(member);
	}

	@Override
	public boolean isCalled(String command) {
		return command.startsWith(COMMAND) && actions.keySet().stream().flatMap(Set::stream).anyMatch(command::endsWith);
	}

	@Override
	public String getInfo() {
		return "**create** roles|invites|channels\n\tErstellt die Bereiche, die für jedes Team notwendig sind. **Admin only**";
	}
}
