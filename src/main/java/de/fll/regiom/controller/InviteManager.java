package de.fll.regiom.controller;

import de.fll.regiom.io.json.JsonExporter;
import de.fll.regiom.io.json.JsonImporter;
import de.fll.regiom.model.Constants;
import de.fll.regiom.model.Storable;
import de.fll.regiom.model.members.Roleable;
import de.fll.regiom.model.members.Spectator;
import de.fll.regiom.model.members.Team;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public enum InviteManager implements Storable {
	INSTANCE;

	private final Map<String, Roleable> roles = new HashMap<>();

	InviteManager() {
		StorageManager.INSTANCE.register(this);
		load();
	}

	public void onMemberJoinWithInvite(@NotNull String code, @NotNull Member member) {
		Roleable team = roles.getOrDefault(code, null);
		System.out.printf("[InviteManager] User %s joined using invite %s, mapped to %s.%n", member.getEffectiveName(), code, team == null ? "no Team" : team);
		if (team == null)
			return;
		Role[] roles = team.getRoles().stream()
				.map(roleID -> member.getGuild().getRoleById(roleID))
				.filter(Objects::nonNull)
				.toArray(Role[]::new);
		member.getGuild().modifyMemberRoles(member, roles).reason("Automatische Rollenzuweisung via Invite-Mapping").queue();
	}

	@NotNull
	public CompletableFuture<?> createAllInvites(@NotNull JDA jda, @NotNull List<Team> teams) {
		TextChannel channel = jda.getTextChannelById(Constants.WELCOME_CHANNEL);
		Objects.requireNonNull(channel);
		return CompletableFuture.allOf(
				Stream.concat(teams.stream()
								.map(team -> createInvite(channel, team)),
						Stream.of(createInvite(channel, new Spectator()))
				).toArray(CompletableFuture[]::new))
				.thenAccept(v -> save());

	}

	@NotNull
	private CompletableFuture<?> createInvite(@NotNull TextChannel channel, @NotNull Roleable roleable) {
		return channel.createInvite().setUnique(true).submit()
				.thenAccept(invite -> roles.put(invite.getCode(), roleable));
	}

	@NotNull
	public CompletableFuture<?> removeAllInvites(@NotNull JDA jda) {
		var channel = Objects.requireNonNull(jda.getTextChannelById(Constants.WELCOME_CHANNEL));
		return channel.retrieveInvites().submit()
				.thenCompose(invites ->
						CompletableFuture.allOf(invites.stream()
								.filter(invite -> roles.containsKey(invite.getCode()))
								.peek(invite -> roles.remove(invite.getCode()))
								.map(invite -> invite.delete().submit())
								.toArray(CompletableFuture[]::new))
				);
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
		save();
	}

	@Override
	public boolean save() {
		JsonExporter.getInstance().exportInvites(roles);
		return true;
	}

	@Override
	public void load() {
		roles.putAll(JsonImporter.getInstance().importInvites());
	}
}
