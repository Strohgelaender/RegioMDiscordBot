package de.fll.regiom.controller;

import de.fll.regiom.io.JsonExporter;
import de.fll.regiom.io.JsonImporter;
import de.fll.regiom.model.RobotGameAttempt;
import de.fll.regiom.model.Storable;
import de.fll.regiom.model.Team;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class RobotGameStartHandler implements Storable {

	private static final long CODE_LOG_CHANNEL = 798316567242735637L;
	private static final int CODE_LENGTH = 6;
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	private static RobotGameStartHandler instance;

	public static RobotGameStartHandler getInstance() {
		if (instance == null)
			instance = new RobotGameStartHandler();
		return instance;
	}

	private RobotGameStartHandler() {
		StorageManager.getInstance().register(this);
		load();
	}

	private final Random random = new SecureRandom();
	private final Map<String, RobotGameAttempt> attempts = new HashMap<>();

	public boolean startRobotGame(Member member, MessageChannel answerChannel) {
		Optional<Team> team = TeamManager.getInstance().getTeamByMember(member);
		if (team.isEmpty()) {
			System.out.println("[StartRobotGame] No Team found for Member " + member.getEffectiveName());
			return false;
		}
		RobotGameAttempt attempt;
		do {
			attempt = new RobotGameAttempt(generateRandomCode(), team.get(), LocalDateTime.now());
		} while (attempts.containsKey(attempt.getCode()));

		attempts.put(attempt.getCode(), attempt);

		String now = attempt.getStartTime().format(FORMATTER);
		System.out.println("[StartRobotGame] Team " + team.get().getName() + " starts at " + now + " with Code " + attempt.getCode());

		answerChannel.sendMessage("Der Code lautet **" + attempt.getCode() + "**\nViel Erfolg beim Robot Game!\n\uD83D\uDD52 " + now).queue();
		member.getGuild().getTextChannelById(CODE_LOG_CHANNEL).sendMessage(String.format("Code: %s Team: %s \uD83D\uDD52 %s", attempt.getCode(), team.get().getName(), now)).queue();
		return true;
	}

	private String generateRandomCode() {
		return random.ints('A', 'Z' + 1)
				.limit(CODE_LENGTH)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
	}

	@Override
	public boolean save() {
		JsonExporter.getInstance().exportTokens(attempts);
		return true;
	}

	@Override
	public void load() {
		attempts.clear();
		attempts.putAll(JsonImporter.getInstance().loadTokens());
	}
}
