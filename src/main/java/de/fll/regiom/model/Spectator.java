package de.fll.regiom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class Spectator implements Roleable {

	@JsonIgnore
	@Override
	@NotNull
	public List<Long> getRoles() {
		return Collections.singletonList(Constants.SPECTATOR_ROLE_ID);
	}

	@Override
	public String toString() {
		return "Spectator";
	}
}
