package de.fll.regiom.commands;

import de.fll.regiom.controller.InviteManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReadAllInviteInfosCommand implements Command {

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command) {
		EmbedBuilder builder = new EmbedBuilder();
		InviteManager.INSTANCE.getRoles().forEach((code, roleable) -> {
			builder.addField(code, roleable.toString(), true);
		});
		builder.setTitle("Invite-Codes");
		event.getChannel().sendMessage(builder.build()).queue();
		return true;
	}

	@Override
	public boolean canBeCalledBy(@NotNull Member member) {
		return RoleHelper.isAdmin(member);
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return command.startsWith("invites");
	}

	@Override
	public @Nullable String getInfo() {
		return "**invites**\n\tGibt alle Invites mit ihren zugeordneten Rollen aus. **Admin only**";
	}
}
