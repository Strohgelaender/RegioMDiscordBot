package de.fll.regiom.commands;

import de.fll.regiom.controller.TeamRepository;
import de.fll.regiom.model.Constants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RoleHelper {

	private RoleHelper() {
	}

	@Nullable
	public static Member toMember(@NotNull User user) {
		return user.getJDA().getGuildById(Constants.GUILD_ID).retrieveMember(user).complete();
	}

	private static boolean hasRole(@NotNull Member member, long roleID) {
		return member.getRoles().stream().map(Role::getIdLong).anyMatch(id -> id == roleID);
	}

	public static boolean isAdmin(@NotNull Member member) {
		return hasRole(member, Constants.ADMIN_ROLE_ID);
	}

	public static boolean isTeam(@NotNull Member member) {
		return TeamRepository.INSTANCE.getTeamByMember(member).isPresent();
	}

	public static boolean isReferee(@NotNull Member member) {
		return hasRole(member, Constants.REFEREE_ROLE_ID);
	}

	public static boolean isVolunteer(@NotNull Member member) {
		return hasRole(member, Constants.VOLUNTEER_ROLE_ID);
	}

}
