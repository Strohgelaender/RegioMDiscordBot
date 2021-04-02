package de.fll.regiom.commands;

import de.fll.regiom.controller.InviteManager;
import de.fll.regiom.model.members.GenericRoleHolder;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

public class RegisterInviteMappingCommand implements Command {

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command) {
		var mentionedRoles = event.getMessage().getMentionedRoles();
		if (mentionedRoles.isEmpty())
			return false;

		//this uses the content of getMessage because invites are case sensitive
		//but the command string is in lower case.
		String[] parts = event.getMessage().getContentDisplay().split(" ");
		if (parts.length < 4)
			return false;
		String code = parts[2];

		GenericRoleHolder holder = new GenericRoleHolder(mentionedRoles.stream().map(ISnowflake::getIdLong).collect(Collectors.toList()));
		InviteManager.INSTANCE.putInvite(code, holder);

		CommandUtils.reactWithCheckbox(event.getMessage());
		return true;
	}

	@Override
	public boolean canBeCalledBy(@NotNull Member member) {
		return RoleHelper.isAdmin(member);
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return command.matches("invite (add|put) [A-Za-z0-9]+( <@&\\d+>)+");
	}

	@Override
	public @Nullable String getInfo() {
		return "**invite add**\n\tRegistriert ein neues Invite-Mapping für den Bot. **Admin only**";
	}
}
