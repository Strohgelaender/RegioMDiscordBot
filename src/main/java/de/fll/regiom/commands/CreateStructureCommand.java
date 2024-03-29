package de.fll.regiom.commands;

import de.fll.regiom.controller.TeamRepository;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class CreateStructureCommand implements Command {

	private static final String COMMAND = "create ";

	private final Map<Set<String>, Function<JDA, CompletableFuture<?>>> actions = Map.of(
			Set.of("roles"), TeamRepository.INSTANCE::createAllTeamRoles,
			Set.of("channels", "teamareas"), TeamRepository.INSTANCE::createAllTeamareas,
			Set.of("welcometext", "teammessage", "text", "message"), TeamRepository.INSTANCE::updateAllWelcomeMessages,
			Set.of("invites", "codes"), TeamRepository.INSTANCE::createAllInvites
	);

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command) {
		String request = command.substring(COMMAND.length());
		var optional = actions.entrySet().stream()
				.filter(e -> e.getKey().contains(request))
				.limit(1)
				.map(Map.Entry::getValue)
				.findAny();
		optional.ifPresent(action -> {
			event.getMessage().addReaction("\uD83D\uDEA7").queue();
			action.apply(event.getJDA())
					.thenCompose(v -> event.getMessage().removeReaction("\uD83D\uDEA7").submit())
					.thenCompose(v -> CommandUtils.reactWithCheckbox(event.getMessage()));
		});
		return optional.isPresent();
	}

	@Override
	public boolean canBeCalledBy(@NotNull Member member) {
		return RoleHelper.isAdmin(member);
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return command.startsWith(COMMAND) && actions.keySet().stream().flatMap(Set::stream).anyMatch(command::endsWith);
	}

	@Override
	public String getInfo() {
		return "**create** roles|invites|channels|text\n\tErstellt die Bereiche, die für jedes Team notwendig sind. **Admin only**";
	}
}
