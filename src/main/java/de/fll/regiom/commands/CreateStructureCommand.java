package de.fll.regiom.commands;

import de.fll.regiom.controller.TeamManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public class CreateStructureCommand implements Command {

	private static final String COMMAND = "create ";

	private final Map<String, Consumer<MessageReceivedEvent>> actions = Map.of(
			"roles", e -> TeamManager.INSTANCE.createAllTeamRoles(e.getJDA()),
			"channels", e -> TeamManager.INSTANCE.createAllTeamareas(e.getJDA()),
			"teamareas", e -> TeamManager.INSTANCE.createAllTeamareas(e.getJDA())
	);

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, String command) {
		command = command.substring(COMMAND.length());
		if (actions.containsKey(command)) {
			actions.get(command).accept(event);
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
		return command.startsWith(COMMAND) && actions.keySet().stream().anyMatch(command::endsWith);
	}

	@Override
	public String getInfo() {
		return "**create** roles|invites|channels\n\tErstellt die Bereiche, die für jedes Team notwendig sind. **Admin only**";
	}
}
