package de.fll.regiom.listeners;

import de.fll.regiom.controller.InviteManager;
import de.fll.regiom.model.Constants;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemberJoinListener extends ListenerAdapter {

	private final Map<String, Integer> inviteUses = new HashMap<>();

	public void setup(@NotNull JDA jda) {
		inviteUses.clear();
		Guild guild = jda.getGuildById(Constants.GUILD_ID);
		Objects.requireNonNull(guild);
		guild.retrieveInvites().queue(invites -> {
			for (Invite invite : invites) {
				inviteUses.put(invite.getCode(), invite.getUses());
			}
		});
	}

	@Override
	public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
		event.getGuild().retrieveInvites().queue(invites -> invites.stream()
				.filter(invite -> inviteUses.containsKey(invite.getCode()))
				.filter(invite -> invite.getUses() != inviteUses.get(invite.getCode()))
				.findAny()
				.ifPresent(invite -> {
					var code = invite.getCode();
					inviteUses.replace(code, invite.getUses());
					InviteManager.INSTANCE.onMemberJoinWithInvite(code, event.getMember());
				}));
	}

	@Override
	public void onGuildInviteCreate(@NotNull GuildInviteCreateEvent event) {
		inviteUses.put(event.getCode(), 0);
	}

	@Override
	public void onGuildInviteDelete(@NotNull GuildInviteDeleteEvent event) {
		inviteUses.remove(event.getCode());
	}
}
