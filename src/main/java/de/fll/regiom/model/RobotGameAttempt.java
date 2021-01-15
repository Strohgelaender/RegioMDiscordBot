package de.fll.regiom.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class RobotGameAttempt {

	private final String code;
	private final Team team;
	private final LocalDateTime startTime;

	@JsonCreator
	public RobotGameAttempt(@JsonProperty("code") String code, @JsonProperty("team") Team team, @JsonProperty("startTime") LocalDateTime startTime) {
		this.code = code;
		this.team = team;
		this.startTime = startTime;
	}

	public String getCode() {
		return code;
	}

	public Team getTeam() {
		return team;
	}

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