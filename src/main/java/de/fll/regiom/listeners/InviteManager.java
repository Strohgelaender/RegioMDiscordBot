package de.fll.regiom.listeners;

import de.fll.regiom.io.JSONExporter;
import de.fll.regiom.model.Constants;
import de.fll.regiom.model.Roleable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InviteManager extends ListenerAdapter {

	private final Map<String, Roleable> roles;
	private final Map<String, Integer> inviteUses;

	public InviteManager() {
		this(new HashMap<>());
	}

	public InviteManager(Map<String, Roleable> roles) {
		this.roles = roles;
		this.inviteUses = new HashMap<>();
		;
	}

	public void setup(JDA jda) {
		inviteUses.clear();
		jda.getGuildById(Constants.GUILD_ID).retrieveInvites().queue(invites -> {
			for (Invite invite : invites) {
				inviteUses.put(invite.getCode(), invite.getUses());
			}
		});
	}

	@Override
	public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
		event.getGuild().retrieveInvites().queue(invites -> {
			for (Invite invite : invites) {
				String code = invite.getCode();
				int uses = invite.getUses();
				if (!inviteUses.containsKey(code)) {
					inviteUses.put(code, uses);
					continue;
				} else if (uses == inviteUses.get(code)) {
					continue;
				}
				inviteUses.replace(code, uses);
				Roleable team = roles.getOrDefault(code, null);
				System.out.printf("[InviteManager] User %s joined using invite %s, mapped to %s.\n", event.getMember().getEffectiveName(), code, team == null ? "no Team" : team);
				if (team == null)
					continue;
				Role[] roles = team.getRoles().stream()
						.map(roleID -> event.getGuild().getRoleById(roleID)).filter(Objects::nonNull).toArray(Role[]::new);
				event.getGuild().modifyMemberRoles(event.getMember(), roles).reason("Automatische Rollenzuweisung durch Bot").queue();
			}
		});
	}

	public Map<String, Roleable> getRoles() {
		return roles;
	}

	public void putInvite(Invite invite, Roleable roleable) {
		putInvite(invite.getCode(), roleable);
	}

	public void putInvite(String code, Roleable roleable) {
		roles.put(code, roleable);
		JSONExporter.getInstance().exportInvites(getRoles());
	}
}
