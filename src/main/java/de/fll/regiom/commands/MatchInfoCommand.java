package de.fll.regiom.commands;

import de.fll.regiom.controller.TeamRepository;
import de.fll.regiom.model.members.Team;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public class MatchInfoCommand implements Command {

	private static final String COMMAND_NAME = "matchinfo";
	private static final long CHANNEL_ID = 795645461494628383L;

	@Override
	public boolean execute(@NotNull MessageReceivedEvent event, @NotNull String command) {
		try {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Aktuelles Robot-Game");
			command = command.substring(COMMAND_NAME.length());
			Team team1 = byString(command.strip().substring(0, 4));
			Team team2 = byString(command.strip().substring(5, 9));
			builder.setDescription(CommandUtils.getTaggedTeam(team1) + " - " + CommandUtils.getTaggedTeam(team2));
			event.getJDA().getTextChannelById(CHANNEL_ID).sendMessage(builder.build()).queue();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean canBeCalledBy(@NotNull Member member) {
		return RoleHelper.isReferee(member) || RoleHelper.isAdmin(member);
	}

	@Override
	public boolean isCalled(@NotNull String command) {
		return command.strip().startsWith(COMMAND_NAME);
	}

	private Team byString(String raw) throws NumberFormatException, NoSuchElementException {
		return TeamRepository.INSTANCE.getTeamByHotId(Integer.parseInt(raw)).orElseThrow();
	}

	@Override
	public @Nullable String getInfo() {
		return "matchinfo hotID1 hotID2 - Sendet eine Indo, dass das RobotGame vom Team mit id1 gegen das Team mit id2 läuft";
	}
}
