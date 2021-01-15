package de.fll.regiom.controller;

import de.fll.regiom.io.JsonExporter;
import de.fll.regiom.io.JsonImporter;
import de.fll.regiom.model.RobotGameAttempt;
import de.fll.regiom.model.Storable;
import de.fll.regiom.model.Team;
import net.dv8tion.jda.api.entities.Member;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class RobotGameTokenManager implements Storable {

	public static final int CODE_LENGTH = 6;
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	private static RobotGameTokenManager instance;

	public static RobotGameTokenManager getInstance() {
		if (instance == null)
			instance = new RobotGameTokenManager();
		return instance;
	}

	private RobotGameTokenManager() {
		StorageManager.getInstance().register(this);
		load();
	}

	private final Random random = new SecureRandom();
	private final Map<String, RobotGameAttempt> attempts = new HashMap<>();

	public RobotGameAttempt startRobotGame(Member member) {
		Optional<Team> team = TeamManager.getInstance().getTeamByMember(member);
		if (team.isEmpty()) {
			System.out.println("[StartRobotGame] No Team found for Member " + member.getEffectiveName());
			return null;
		}
		RobotGameAttempt attempt;
		do {
			attempt = new RobotGameAttempt(generateRandomCode(), team.get(), LocalDateTime.now());
		} while (attempts.containsKey(attempt.getCode()));

		attempts.put(attempt.getCode(), attempt);
		System.out.println("[StartRobotGame] Team " + team.get().getName() + " starts at " + attempt.getStartTime().format(FORMATTER) + " with Code " + attempt.getCode());

		return attempt;
	}

	public RobotGameAttempt findByToken(String token) {
		return attempts.getOrDefault(token, null);
	}

	private String generateRandomCode() {
		return random.ints('A', 'Z' + 1)
				.limit(CODE_LENGTH)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
	}

	public String formatMessage(RobotGameAttempt attempt) {
		return String.format("Code: %s Team: %s \uD83D\uDD52 %s", attempt.getCode(), attempt.getTeam().getName(), attempt.getStartTime().format(FORMATTER));
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
