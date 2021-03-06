package de.fll.regiom.commands;

import de.fll.regiom.controller.InviteManager;
import de.fll.regiom.controller.RobotGameTokenRepository;
import de.fll.regiom.controller.TeamRepository;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public class DeleteCommand implements Command {

	private static final String COMMAND = "delete ";

	private final Map<String, Consumer<MessageReceivedEvent>> actions = Map.of(
			"tokens", e -> RobotGameTokenRepository.INSTANCE.clear(),
			"channels", e -> TeamRepository.INSTANCE.removeAllTeamareas(e.getJDA()),
			"teamareas", e -> TeamRepository.INSTANCE.removeAllTeamareas(e.getJDA()),
			"invites", e -> InviteManager.INSTANCE.clear()
	);

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command) {
		command = command.substring(COMMAND.length());
		if (actions.containsKey(command)) {
			actions.get(command).accept(event);
			return true;
		}
		return false;
	}

	@Override
	public boolean canBeCalledBy(@NotNull Member member) {
		return RoleHelper.isAdmin(member);
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return command.startsWith(COMMAND) && actions.containsKey(command.substring(COMMAND.length()));
	}

	@Override
	public String getInfo() {
		return "**delete** tokens|invites|channels\n\tLöscht die abgegebenen Daten aus der Datenbank bzw. entfernt die Sturkturen aus dem Discord-Server.\nErleichtet das testen. **Admin only**";
	}
}
