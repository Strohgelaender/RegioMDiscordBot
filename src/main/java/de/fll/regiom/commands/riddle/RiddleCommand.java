package de.fll.regiom.commands.riddle;

import de.fll.regiom.commands.Command;
import de.fll.regiom.commands.RoleHelper;
import de.fll.regiom.controller.GameController;
import de.fll.regiom.controller.TeamRepository;
import de.fll.regiom.model.members.Team;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class RiddleCommand implements Command {

	protected static final String RIDDLE_PREFIX = "riddle ";

	@Override
	public boolean canBeCalledBy(@NotNull Member member) {
		Optional<Team> team = TeamRepository.INSTANCE.getTeamByMember(member);
		return team.isPresent();
	}

	protected GameController.GameProgressState stateFromMember(@NotNull MessageReceivedEvent event) {
		Member member = RoleHelper.toMember(event.getAuthor());
		Optional<Team> team = TeamRepository.INSTANCE.getTeamByMember(member);
		assert team.isPresent();
		return GameController.INSTANCE.getStateOfTeam(team.get());
	}
}
