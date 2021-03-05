package de.fll.regiom.listeners;

import de.fll.regiom.io.json.JsonExporter;
import de.fll.regiom.model.Constants;
import de.fll.regiom.model.Roleable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//TODO implement Storeable and use StorageManager
public class InviteManager extends ListenerAdapter {

	private static InviteManager INSTANCE;

	public static InviteManager getInstance() {
		return INSTANCE;
	}

	private final Map<String, Roleable> roles;
	private final Map<String, Integer> inviteUses;

	public InviteManager() {
		this(new HashMap<>());
	}

	public InviteManager(@NotNull Map<String, Roleable> roles) {
		if (INSTANCE != null)
			throw new IllegalStateException();
		this.roles = roles;
		this.inviteUses = new HashMap<>();
		INSTANCE = this;
	}

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

	@NotNull
	public Map<String, Roleable> getRoles() {
		return roles;
	}

	public void putInvite(Invite invite, Roleable roleable) {
		putInvite(invite.getCode(), roleable);
	}

	public void putInvite(String code, Roleable roleable) {
		roles.put(code, roleable);
		JsonExporter.getInstance().exportInvites(getRoles());
	}

	public void clear() {
		roles.clear();
		JsonExporter.getInstance().exportInvites(getRoles());
	}
}
