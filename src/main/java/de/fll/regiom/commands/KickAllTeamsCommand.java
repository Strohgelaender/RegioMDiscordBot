package de.fll.regiom.commands;

import de.fll.regiom.model.Constants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KickAllTeamsCommand implements Command {

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command) {
		var teamRole = event.getGuild().getRoleById(Constants.TEAM_ROLE_ID);
		event.getGuild().retrieveMembers()
						.thenAccept(v -> event.getGuild().getMembersWithRoles(teamRole).stream()
								.filter(member -> member.getRoles().size() == 2) //only team + special role
								.forEach(member -> event.getGuild().kick(member).queue()));
		return true;
	}

	@Override
	public boolean canBeCalledBy(@NotNull Member member) {
		return RoleHelper.isAdmin(member);
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return command.startsWith("kickteam");
	}

	@Override
	public @Nullable String getInfo() {
		return "!kickteam - kicks all team members to allow a clean discord";
	}
}
