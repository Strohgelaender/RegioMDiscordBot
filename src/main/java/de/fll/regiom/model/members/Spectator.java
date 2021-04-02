package de.fll.regiom.model.members;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.fll.regiom.model.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
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
