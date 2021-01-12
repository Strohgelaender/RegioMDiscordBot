package de.fll.regiom.commands;

import de.fll.regiom.controller.TeamManager;
import de.fll.regiom.model.Constants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

final class RoleHelper {

	private RoleHelper() {
	}

	static boolean isAdmin(@NotNull Member member) {
		return member.getRoles().stream().map(Role::getIdLong).anyMatch(id -> id == Constants.ADMIN_ROLE_ID);
	}

	static boolean isTeam(@NotNull Member member) {
		return TeamManager.getInstance().getTeamByMember(member).isPresent();
	}
}
