package de.fll.regiom.listeners;

import de.fll.regiom.model.Constants;
import de.fll.regiom.model.RoleHolding;
import de.fll.regiom.model.Team;
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

	//TODO how to do persistent team mapping?
	private final Map<String, RoleHolding> teams = new HashMap<>();
	private final Map<String, Integer> inviteUses = new HashMap<>();

	public void setup(JDA jda) {
		teams.clear();
		inviteUses.clear();
		jda.getGuildById(Constants.GUILD_ID).retrieveInvites().queue(invites -> {
			for (Invite invite : invites) {
				inviteUses.put(invite.getCode(), invite.getUses());
			}
		});
		//Test code
		teams.put("ZNhSNQaP7f", new Team("Testteam", 795768188527706143L, 1234));
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
				RoleHolding team = teams.getOrDefault(code, null);
				System.out.printf("[InviteManager] User %s joined using invite %s, mapped to %s.\n", event.getMember().getEffectiveName(), code, team == null ? "no Team" : team);
				if (team == null)
					continue;
				Role[] roles = team.getRoles().stream()
						.map(roleID -> event.getGuild().getRoleById(roleID)).filter(Objects::nonNull).toArray(Role[]::new);
				event.getGuild().modifyMemberRoles(event.getMember(), roles).reason("Automatische Rollenzuweisung durch Bot").queue();
			}
		});
	}
}
