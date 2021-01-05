package de.fll.regiom.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class Team implements Roleable {

	private final String name;
	private final int hotID;
	private long roleID;
	private long textChannelID;
	private long voiceChannelID;
	private long evaluationChannelID;

	public Team(String name, int hotID) {
		this.name = name;
		this.hotID = hotID;
	}

	@JsonCreator
	public Team(@JsonProperty("name") String name, @JsonProperty("roleID") long roleID, @JsonProperty("hotID") int hotID) {
		this(name, hotID);
		this.roleID = roleID;
	}

	public String getName() {
		return name;
	}

	public long getRoleID() {
		return roleID;
	}

	public int getHotID() {
		return hotID;
	}

	public void setRoleID(long roleID) {
		this.roleID = roleID;
	}

	public long getTextChannelID() {
		return textChannelID;
	}

	public long getVoiceChannelID() {
		return voiceChannelID;
	}

	public long getEvaluationChannelID() {
		return evaluationChannelID;
	}

	public void setTextChannelID(long textChannelID) {
		this.textChannelID = textChannelID;
	}

	public void setVoiceChannelID(long voiceChannelID) {
		this.voiceChannelID = voiceChannelID;
	}

	public void setEvaluationChannelID(long evaluationChannelID) {
		this.evaluationChannelID = evaluationChannelID;
	}

	@JsonIgnore
	@Override
	public List<Long> getRoles() {
		return List.of(roleID, Constants.TEAM_ROLE_ID);
	}

	@Override
	public String toString() {
		return "[" + hotID + "] " + name;
	}
}
