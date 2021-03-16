package de.fll.regiom.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class RobotGameAttempt {

	private final String code;
	private final Team team;
	private final LocalDateTime startTime;

	@JsonCreator
	public RobotGameAttempt(@JsonProperty("code") @NotNull String code, @JsonProperty("team") @NotNull Team team, @JsonProperty("startTime") @NotNull LocalDateTime startTime) {
		Objects.requireNonNull(code);
		Objects.requireNonNull(team);
		Objects.requireNonNull(startTime);
		this.code = code;
		this.team = team;
		this.startTime = startTime;
	}

	@NotNull
	public String getCode() {
		return code;
	}

	@NotNull
	public Team getTeam() {
		return team;
	}

	@NotNull
	public LocalDateTime getStartTime() {
		return startTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RobotGameAttempt attempt = (RobotGameAttempt) o;

		return code.equals(attempt.code);
	}

	@Override
	public int hashCode() {
		return code.hashCode();
	}
}
